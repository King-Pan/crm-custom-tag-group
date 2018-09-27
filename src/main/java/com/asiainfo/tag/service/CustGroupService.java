package com.asiainfo.tag.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/21
 * Time: 上午11:45
 * Description: No Description
 */
public interface CustGroupService {
    /**
     * 检查某个用户是否在这个客户群组内
     *
     * @param jsonParma
     * @return
     * @throws Exception
     */
    String checkTagInGroup(String jsonParma);

    /**
     * 查询用户的客户群组
     *
     * @param jsonParma
     * @return
     * @throws Exception
     */
    String getUserTagGroupList(String jsonParma);
}
