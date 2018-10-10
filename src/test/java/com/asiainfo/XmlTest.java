package com.asiainfo;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.model.GroupInfo;
import com.asiainfo.tag.model.IdInfo;
import com.asiainfo.tag.utils.Dom4jUtil;
import com.asiainfo.tag.utils.FastJsonUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.xpath.DefaultXPath;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/10/10
 * Time: 上午11:25
 * Description: No Description
 */
public class XmlTest {


    @Test
    public void xml(){
        String xml = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soap:Body>\n" +
                "        <ns2:checkExistsNoResponse xmlns:ns2=\"http://service.asiainfo.com/\">\n" +
                "            <return>{\"errmsg\":\"Hbase查询出错：查询表是否存在出错: [COC_CUSTOMER_GROUP_20181009]\",\"retcode\":\"-3\"}</return>\n" +
                "        </ns2:checkExistsNoResponse>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        try {
            Document document = Dom4jUtil.getXMLByString(xml);
            DefaultXPath xpath = new DefaultXPath("//soap:Envelope/soap:Body/ns2:checkExistsNoResponse/return");
            Map<String,String> map = new HashMap<>();
            map.put("soap","http://schemas.xmlsoap.org/soap/envelope/");
            map.put("ns2","http://service.asiainfo.com/");

            xpath.setNamespaceURIs(map);
            Node n = xpath.selectSingleNode(document);
            System.out.println(n.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void list(){
        String xml = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soap:Body>\n" +
                "        <ns2:getCustGroupListResponse xmlns:ns2=\"http://service.asiainfo.com/\">\n" +
                "            <return>{\"errmsg\":\"\",\"retcode\":\"0\",\"custgrouplist\":[{\"id\":\"HQD123\"},{\"id\":\"HQD345\"},{\"id\":\"HQD3455532\"},{\"id\":\"HQD112453\"}]}</return>\n" +
                "        </ns2:getCustGroupListResponse>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        try {
            Document document = Dom4jUtil.getXMLByString(xml);
            DefaultXPath xpath = new DefaultXPath("//soap:Envelope/soap:Body/ns2:getCustGroupListResponse/return");
            Map<String, String> map = new HashMap<>(10);
            map.put("soap", "http://schemas.xmlsoap.org/soap/envelope/");
            map.put("ns2", "http://service.asiainfo.com/");
            xpath.setNamespaceURIs(map);
            Node n = xpath.selectSingleNode(document);
            JSONObject jsonObject = FastJsonUtils.getJsonObject(n.getStringValue());
            System.out.println(jsonObject.getString("custgrouplist"));
            System.out.println(jsonObject.getString("custgrouplist"));
            List<IdInfo> list = JSONObject.parseArray(jsonObject.getString("custgrouplist"), IdInfo.class);
            List<GroupInfo> groupInfoList = new ArrayList<>(40);
            GroupInfo groupInfo;
            int index = 0;
            for (IdInfo info : list) {
                groupInfo = new GroupInfo(++index, "18100000000", info.getId());
                groupInfoList.add(groupInfo);
            }
            System.out.println(groupInfoList);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
