package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Administrator;

import java.util.List;

/**
 * @author: Paul
 * @time:2018/2/26 22:43
 * @description:
 */
public interface IAdministratorService {
    void createSingleAdmin(String adminInfo);

    void deleteAdmin(String adminid);

    List<Administrator> queryAdmins();

    void updateInfoAdmin(String updateAdmin);

    boolean loginAdmin(String loginJson);
}
