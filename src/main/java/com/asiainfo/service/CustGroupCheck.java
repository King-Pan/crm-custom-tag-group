package com.asiainfo.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface CustGroupCheck {

    /**
     * 检查用户是否在某个客户群组内
     *
     * @param jsonParam 查询参数
     * @return
     */
    @WebMethod
    String checkExistsNo(@WebParam(name = "jsonParam") String jsonParam);

    /**
     * 获取用户所有的客户群组
     *
     * @param jsonParam 查询参数
     * @return
     */
    @WebMethod
    String getCustGroupList(@WebParam(name = "jsonParam") String jsonParam);

}
