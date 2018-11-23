package com.asiainfo.tag.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/10/10
 * Time: 上午10:57
 * Description: No Description
 */
@Slf4j
public class Dom4jUtil {
    /**
     * 通过xml字符获取document文档
     *
     * @param xmlstr 要序列化的xml字符
     * @return 返回文档对象
     * @throws DocumentException
     */
    public static Document getXMLByString(String xmlstr) throws DocumentException {
        if (StringUtils.isBlank(xmlstr)) {
            return null;
        }
        Document document = DocumentHelper.parseText(xmlstr);
        return document;
    }

    /**
     * 解析XML为Document对象
     *
     * @param xml 被解析的XMl
     * @return
     */
    public final static Element parseXml(String xml) {
        StringReader sr = new StringReader(xml);
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(sr);
        } catch (DocumentException e) {
            log.error("解析xml字符串失败 xml=" + xml, e);
        }
        Element rootElement = document != null ? document.getRootElement() : null;
        return rootElement;
    }


}
