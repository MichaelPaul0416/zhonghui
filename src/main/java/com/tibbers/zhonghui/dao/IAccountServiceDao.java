package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.Person;
import org.springframework.stereotype.Repository;

/**
 * @author: Paul
 * @time:2018/1/21 13:09
 * @description:
 */
@Repository
public interface IAccountServiceDao {

    void registerPersonInfo(Person person);

    void registerAccountInfo(Account account);

    void updateAccountInfo(Account account);

    void updatePersonalInfo(Person person);

    Account queryByAccountid(String accountid);
}
