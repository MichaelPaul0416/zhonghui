package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Administrator;
import com.tibbers.zhonghui.model.common.Pager;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/2/26 22:43
 * @description:
 */
public interface IAdministratorService {
    String createSingleAdmin(String adminInfo);

    void deleteAdmin(String adminid);

    List<Administrator> queryAdmins();

    void updateInfoAdmin(String updateAdmin);

    boolean loginAdmin(Administrator admin);

    Administrator queryAdmin(Administrator administrator);

    Map<String, List<Map<String, String>>> queryAccountTradeDetails(String accountid, Pager pager);
}
