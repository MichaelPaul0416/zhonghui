package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    List<Account> queryAccounts(Map<String,Object> param);

    List<Map<String,Object>> orderByScore(Map<String,Object> param);

    Account queryAccountByOpenid(String openid);
}
