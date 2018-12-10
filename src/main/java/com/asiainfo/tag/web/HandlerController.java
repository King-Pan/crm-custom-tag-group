package com.asiainfo.tag.web;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.tag.model.GroupInfo;
import com.asiainfo.tag.model.IdInfo;
import com.asiainfo.tag.utils.Dom4jUtil;
import com.asiainfo.tag.utils.FastJsonUtils;
import com.asiainfo.tag.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.xpath.DefaultXPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/10/10
 * Time: 上午9:51
 * Description: No Description
 */
@Slf4j
@RestController
public class HandlerController {


    @Value("${cxf.url}")
    private String url;


    @RequestMapping("/checkNum")
    public String checkNum(@RequestParam("telnum") String telnum, @RequestParam("custgroupid") String custgroupid) {
        String result = "";
        if (StringUtils.isBlank(telnum) || StringUtils.isBlank(custgroupid)) {
            throw new RuntimeException("检查号码是否在客户群内查询参数: 号码和客户群不能为空");
        }

        Map<String, String> jsonParam = new HashMap<>(2);
        jsonParam.put("custgroupid", custgroupid);
        jsonParam.put("telnum", telnum);

        StringBuffer sb = new StringBuffer();
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.asiainfo.com/\">");
        sb.append(" <soapenv:Header/>");
        sb.append("<soapenv:Body>");
        sb.append("<ser:checkExistsNo>");
        sb.append(" <jsonParam>");
        sb.append(FastJsonUtils.getBeanToJson(jsonParam));
        sb.append("</jsonParam>");
        sb.append("</ser:checkExistsNo>");
        sb.append("</soapenv:Body>");
        sb.append("</soapenv:Envelope>");

        String response = HttpClientUtil.uniform(
                url,
                sb.toString(), "UTF-8");
        log.info("========> 手动测试cxf 检查号码 返回结果: " + response);

        try {
            Document document = Dom4jUtil.getXMLByString(response);
            DefaultXPath xpath = new DefaultXPath("//soap:Envelope/soap:Body/ns2:checkExistsNoResponse/return");
            Map<String, String> map = new HashMap<>();
            map.put("soap", "http://schemas.xmlsoap.org/soap/envelope/");
            map.put("ns2", "http://service.asiainfo.com/");
            xpath.setNamespaceURIs(map);
            Node n = xpath.selectSingleNode(document);
            JSONObject jsonObject = FastJsonUtils.getJsonObject(n.getStringValue());
            if ("0".equals(jsonObject.getString("telstatus"))) {
                result = "号码:" + telnum + "，在客户群：" + custgroupid + "内";
            } else {
                result = "号码:" + telnum + "，不在客户群：" + custgroupid + "内   返回信息：" + jsonObject.getString("errmsg");
            }
            log.info("========> 手动测试cxf {}", result);
        } catch (Throwable e) {
            log.error("checkExistsNo方法调用出错", e);
        }
        return result;
    }

    @RequestMapping("/list")
    public Object list(@RequestParam("telnum") String telnum) {
        Map<String, Object> result = new HashMap<>(40);
        if (StringUtils.isBlank(telnum)) {
            throw new RuntimeException("查询号码所有客户群参数: 号码不能为空");
        }

        Map<String, String> jsonParam = new HashMap<>(2);
        jsonParam.put("telnum", telnum);

        StringBuffer sb = new StringBuffer();
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.asiainfo.com/\">");
        sb.append(" <soapenv:Header/>");
        sb.append("<soapenv:Body>");
        sb.append("<ser:getCustGroupList>");
        sb.append(" <jsonParam>");
        sb.append(FastJsonUtils.getBeanToJson(jsonParam));
        sb.append("</jsonParam>");
        sb.append("</ser:getCustGroupList>");
        sb.append("</soapenv:Body>");
        sb.append("</soapenv:Envelope>");
        String response = HttpClientUtil.uniform(
                url,
                sb.toString(), "UTF-8");
        log.info("========> 手动测试cxf 查询号码:{}所有客户群 返回结果:{}", telnum, response);
        try {
            Document document = Dom4jUtil.getXMLByString(response);
            DefaultXPath xpath = new DefaultXPath("//soap:Envelope/soap:Body/ns2:getCustGroupListResponse/return");
            Map<String, String> map = new HashMap<>(10);
            map.put("soap", "http://schemas.xmlsoap.org/soap/envelope/");
            map.put("ns2", "http://service.asiainfo.com/");
            xpath.setNamespaceURIs(map);
            Node n = xpath.selectSingleNode(document);
            JSONObject jsonObject = FastJsonUtils.getJsonObject(n.getStringValue());
            if ("-2".equals(jsonObject.getString("retcode"))) {
                result.put("errmsg", jsonObject.getString("errmsg"));
                result.put("status", 500);
            } else {
                result.put("errmsg", jsonObject.getString("errmsg"));
                result.put("status", 200);
                List<IdInfo> list = JSONObject.parseArray(jsonObject.getString("custgrouplist"), IdInfo.class);
                List<GroupInfo> groupInfoList = new ArrayList<>(40);
                GroupInfo groupInfo;
                int index = 0;
                for (IdInfo info : list) {
                    groupInfo = new GroupInfo(++index, telnum, info.getId());
                    groupInfoList.add(groupInfo);
                }
                result.put("datas", groupInfoList);
            }
            log.info("========> 手动测试cxf {}", result);
        } catch (Throwable e) {
            log.error("getCustGroupList方法调用出错", e);
        }
        return result;
    }
}
