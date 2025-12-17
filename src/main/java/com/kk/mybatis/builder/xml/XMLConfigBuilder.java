package com.kk.mybatis.builder.xml;

import java.io.Reader;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.kk.mybatis.builder.BaseBuilder;
import com.kk.mybatis.session.Configuration;

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
            // 后续可以添加对<mapper>元素的支持，例如：
            // <mapper resource="xx/xxMapper.xml"/>
            // <mapper class="xx.xxMapper"/>
        }
    }
}