package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Administrator;

import java.util.List;

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
}
