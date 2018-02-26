package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Administrator;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Paul
 * @time:2018/2/26 22:12
 * @description:
 */
@Repository
public interface IAdministratorDao {

    void createSingleAdmin(Administrator administrator);

    void deleteAdmin(Administrator administrator);

    Administrator queryAdmin(Administrator administrator);

    List<Administrator> queryAllAdmin();

    void updateInfoAdmin(Administrator administrator);
}
