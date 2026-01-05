package com.kk.mybatis.builder.xml;

import com.kk.mybatis.builder.BaseBuilder;
import com.kk.mybatis.io.Resources;
import com.kk.mybatis.mapping.MappedStatement;
import com.kk.mybatis.mapping.SqlCommandType;
import com.kk.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * XML配置构建器
 *
 * @author limoukun
 * @since 2025/11/3
 */
public class XMLConfigBuilder extends BaseBuilder {
    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());
        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException de) {
            de.printStackTrace();
        }
    }

    /**
     * 解析XML配置文件
     * 主要任务：
     * 1. 解析<mappers>节点
     * 2. 注册所有Mapper接口
     *
     * @return 解析完成后的Configuration对象
     */
    public Configuration parse() {
        // 解析mappers节点
        parseMappersElement(root.element("mappers"));
        return configuration;
    }

    /**
     * 解析mappers节点
     * 支持以下格式：
     * <mappers>
     *   <package name="com.kk.mybatis.binding.dao"/>
     *   <mapper resource="mapper/UserMapper.xml"/>
     * </mappers>
     *
     * @param mappersElement mappers节点元素
     */
    private void parseMappersElement(Element mappersElement) {
        if (mappersElement == null) {
            return;
        }

        // 遍历mappers下的所有子元素
        for (Object elementObj : mappersElement.elements()) {
            Element element = (Element) elementObj;
            
            // 处理<package>元素
            if ("package".equals(element.getName())) {
                // 获取name属性值，即包名
                String packageName = element.attributeValue("name");
                if (packageName != null && !packageName.isEmpty()) {
                    // 通过包名注册所有Mapper接口
                    configuration.addMappers(packageName);
                }
            }
            // 处理<mapper>元素
            else if ("mapper".equals(element.getName())) {
                // 获取resource属性值，即Mapper XML文件路径
                String resource = element.attributeValue("resource");
                if (resource != null && !resource.isEmpty()) {
                    // 解析Mapper XML文件
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
     * 解析Mapper XML文件
     *
     * @param resource Mapper XML文件路径
     */
    private void parseMapperXml(String resource) throws DocumentException, IOException, ClassNotFoundException {
        // 加载Mapper XML文件
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
        if (inputStream == null) {
            throw new RuntimeException("Cannot find mapper resource: " + resource);
        }

        // 解析XML
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();

        // 获取namespace，通常是Mapper接口的全限定名
        String namespace = rootElement.attributeValue("namespace");
        if (namespace == null || namespace.isEmpty()) {
            throw new RuntimeException("Mapper XML must have a namespace: " + resource);
        }

        // 遍历所有SQL语句节点
        for (Object elementObj : rootElement.elements()) {
            Element element = (Element) elementObj;

            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");

            // 支持常见的SQL标签
            SqlCommandType sqlCommandType = getSqlCommandType(element.getName());
            if (sqlCommandType != SqlCommandType.UNKNOWN) {
                // 获取SQL语句ID
                String id = element.attributeValue("id");
                if (id == null || id.isEmpty()) {
                    continue;
                }

                // 组合完整的statementId (namespace + "." + id)
                String statementId = namespace + "." + id;
                
                // 获取SQL语句内容
                String sql = element.getTextTrim();
                
                // 创建MappedStatement并注册到Configuration中
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, statementId, sqlCommandType, parameterType, resultType, sql).build();
                configuration.addMappedStatement(mappedStatement);
            }
        }

        // 注册Mapper
        configuration.addMapper(Resources.classForName(namespace));
    }

    /**
     * 根据标签名称获取SQL命令类型
     *
     * @param tagName 标签名称
     * @return SQL命令类型
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