package com.kk.mybatis.builder.xml;

import com.kk.mybatis.builder.BaseBuilder;
import com.kk.mybatis.io.Resources;
import com.kk.mybatis.mapping.Environment;
import com.kk.mybatis.mapping.MappedStatement;
import com.kk.mybatis.mapping.SqlCommandType;
import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.session.datasource.DataSourceFactory;
import com.kk.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

/**
 * XML 配置构建器
 *
 * @author limoukun
 * @since 2025/11/3
 */
public class XMLConfigBuilder extends BaseBuilder {
    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化 Configuration 和 TypeAliasRegistry
        super(new Configuration());
        // 2. 使用 dom4j 读取主配置 XML
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException de) {
            de.printStackTrace();
        }
    }

    /**
     * 解析 mybatis 主配置文件
     * 解析顺序：
     * 1. 先解析 environments，构建事务管理器和数据源
     * 2. 再解析 mappers，注册 Mapper 接口和 SQL 语句
     *
     * @return 解析完成后的 Configuration
     */
    public Configuration parse() {
        try {
            // `<environments>` 节点中包含多个 `<environment>`
            parseEnvironmentElement(root.element("environments"));
            // 解析 mapper 映射配置
            parseMappersElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    /**
     * 解析 `<environments>` 节点，并把每个 `<environment>` 转换为 Environment 对象
     *
     * 解析规则：
     * 1. 遍历 environment 列表，读取每个环境的 id
     * 2. 根据 transactionManager.type（别名）构建 TransactionFactory
     * 3. 根据 dataSource.type（别名）构建 DataSourceFactory
     * 4. 读取 dataSource 下所有 property，注入 DataSourceFactory
     * 5. 构建 Environment，保存到 Configuration
     * 6. 根据 `<environments default="...">` 选择当前生效环境
     *
     * @param environmentsElement `<environments>` 节点
     */
    private void parseEnvironmentElement(Element environmentsElement) {
        if (environmentsElement == null) {
            return;
        }

        // 读取默认环境 ID：<environments default="development">
        String defaultEnvironmentId = environmentsElement.attributeValue("default");

        // 遍历 `<environment>` 列表
        List<Element> environmentElements = environmentsElement.elements("environment");
        for (Element environmentElement : environmentElements) {
            // 1) 获取环境 id
            String id = environmentElement.attributeValue("id");
            if (id == null || id.trim().isEmpty()) {
                throw new RuntimeException("Environment id is required.");
            }

            // 2) 构建事务管理器
            Element transactionManagerElement = environmentElement.element("transactionManager");
            if (transactionManagerElement == null) {
                throw new RuntimeException("transactionManager is required. environment id: " + id);
            }
            String transactionType = transactionManagerElement.attributeValue("type");
            TransactionFactory transactionFactory = createByAlias(transactionType, TransactionFactory.class, "transactionManager");

            // 3) 构建数据源（DataSourceFactory -> DataSource）
            Element dataSourceElement = environmentElement.element("dataSource");
            if (dataSourceElement == null) {
                throw new RuntimeException("dataSource is required. environment id: " + id);
            }
            String dataSourceType = dataSourceElement.attributeValue("type");
            DataSourceFactory dataSourceFactory = createByAlias(dataSourceType, DataSourceFactory.class, "dataSource");

            // 4) 读取 dataSource 下 property 列表并注入到工厂
            Properties properties = parseProperties(dataSourceElement.elements("property"));
            dataSourceFactory.setProperties(properties);

            // 5) 构建 Environment，并存入 Configuration
            Environment environment = new Environment.Builder(id)
                    .transactionFactory(transactionFactory)
                    .dataSource(dataSourceFactory.getDataSource())
                    .build();

            configuration.addEnvironment(environment);

            // 6) 选择当前生效环境
            // 若配置了 default，则选择和 default 匹配的环境；
            // 若未配置 default，则默认选择第一个解析到的环境。
            if (defaultEnvironmentId == null || defaultEnvironmentId.trim().isEmpty()) {
                if (configuration.getEnvironment() == null) {
                    configuration.setEnvironment(environment);
                }
            } else if (defaultEnvironmentId.equals(id)) {
                configuration.setEnvironment(environment);
            }
        } // for-end

        if (configuration.getEnvironment() == null) {
            throw new RuntimeException("No environment selected. Check `<environments default=\"...\">` configuration.");
        }
    }

    /**
     * 通过类型别名创建实例，并校验实例类型是否符合预期。
     * 例如：
     * - "JDBC" -> JdbcTransactionFactory
     * - "DRUID" -> DruidDataSourceFactory
     *
     * @param alias        配置中的 type 值（别名）
     * @param requiredType 期望类型（接口/父类）
     * @param nodeName     当前解析节点名称（仅用于异常提示）
     * @param <T>          目标类型
     * @return 创建出的实例
     */
    private <T> T createByAlias(String alias, Class<T> requiredType, String nodeName) {
        if (alias == null || alias.trim().isEmpty()) {
            throw new RuntimeException("`type` is required for node: " + nodeName);
        }

        Class<?> aliasClass = typeAliasRegistry.resolveAlias(alias);
        if (aliasClass == null) {
            throw new RuntimeException("Cannot resolve alias `" + alias + "` for node: " + nodeName);
        }
        if (!requiredType.isAssignableFrom(aliasClass)) {
            throw new RuntimeException("Alias `" + alias + "` does not implement " + requiredType.getSimpleName());
        }

        try {
            return requiredType.cast(aliasClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate alias `" + alias + "`", e);
        }
    }

    /**
     * 解析 `<property name="..." value="..."/>` 列表为 Properties
     *
     * @param propertyElements property 节点列表
     * @return 解析后的 Properties
     */
    private Properties parseProperties(List<Element> propertyElements) {
        Properties properties = new Properties();
        for (Element propertyElement : propertyElements) {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            if (name != null && !name.trim().isEmpty() && value != null) {
                properties.setProperty(name, value);
            }
        }
        return properties;
    }

    /**
     * 解析 mappers 节点
     * 支持以下格式：
     * <mappers>
     *   <package name="com.kk.mybatis.binding.dao"/>
     *   <mapper resource="mapper/UserMapper.xml"/>
     * </mappers>
     *
     * @param mappersElement mappers 节点元素
     */
    private void parseMappersElement(Element mappersElement) {
        if (mappersElement == null) {
            return;
        }

        // 遍历 mappers 下的所有子元素
        for (Object elementObj : mappersElement.elements()) {
            Element element = (Element) elementObj;

            // 处理 <package> 元素
            if ("package".equals(element.getName())) {
                // 获取 name 属性值，即包名
                String packageName = element.attributeValue("name");
                if (packageName != null && !packageName.isEmpty()) {
                    // 通过包名注册所有 Mapper 接口
                    configuration.addMappers(packageName);
                }
            }
            // 处理 <mapper> 元素
            else if ("mapper".equals(element.getName())) {
                // 获取 resource 属性值，即 mapper XML 文件路径
                String resource = element.attributeValue("resource");
                if (resource != null && !resource.isEmpty()) {
                    // 解析 mapper XML 文件
                    try {
                        parseMapperXml(resource);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 解析 mapper XML 文件
     *
     * @param resource mapper XML 文件路径
     */
    private void parseMapperXml(String resource) throws DocumentException, IOException, ClassNotFoundException {
        // 加载 mapper XML 文件
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new RuntimeException("Cannot find mapper resource: " + resource);
            }

            // 解析 XML
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();

            // 获取 namespace，通常是 Mapper 接口的全限定名
            String namespace = rootElement.attributeValue("namespace");
            if (namespace == null || namespace.isEmpty()) {
                throw new RuntimeException("Mapper XML must have a namespace: " + resource);
            }

            // 遍历所有 SQL 语句节点
            for (Object elementObj : rootElement.elements()) {
                Element element = (Element) elementObj;

                String parameterType = element.attributeValue("parameterType");
                String resultType = element.attributeValue("resultType");

                // 支持常见的 SQL 标签
                SqlCommandType sqlCommandType = getSqlCommandType(element.getName());
                if (sqlCommandType != SqlCommandType.UNKNOWN) {
                    // 获取 SQL 语句 ID
                    String id = element.attributeValue("id");
                    if (id == null || id.isEmpty()) {
                        continue;
                    }

                    // 组合完整 statementId (namespace + "." + id)
                    String statementId = namespace + "." + id;

                    // 获取 SQL 语句内容
                    String sql = element.getTextTrim();

                    // 创建 MappedStatement 并注册到 Configuration
                    MappedStatement mappedStatement = new MappedStatement.Builder(
                            configuration,
                            statementId,
                            sqlCommandType,
                            parameterType,
                            resultType,
                            sql
                    ).build();
                    configuration.addMappedStatement(mappedStatement);
                }
            }

            // 注册 Mapper 接口
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

    /**
     * 根据标签名称获取 SQL 命令类型
     *
     * @param tagName 标签名称
     * @return SQL 命令类型
     */
    private SqlCommandType getSqlCommandType(String tagName) {
        switch (tagName.toLowerCase()) {
            case "select":
                return SqlCommandType.SELECT;
            case "insert":
                return SqlCommandType.INSERT;
            case "update":
                return SqlCommandType.UPDATE;
            case "delete":
                return SqlCommandType.DELETE;
            default:
                return SqlCommandType.UNKNOWN;
        }
    }
}
