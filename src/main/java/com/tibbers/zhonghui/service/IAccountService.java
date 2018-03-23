package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.common.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 13:09
 * @description:
 */
public interface IAccountService {

    Map<String,String> registerNewAccount(Map<String,String> map);

    void updateAccountInfo(String account);

    List<String> uploadAccountImage(HttpServletRequest request);

    void updatePersonalInfo(String person);

    Account queryByAccountid(String accountid);

    Map<String, List<Map<String, Object>>> queryAccountTrades(String accountid, Pager pager);

    List<Account> queryAccounts(String isvip, String termid, Pager pager);

    List<Map<String,Object>> orderByScore(Map<String,Object> param);

    Account queryAccountByOpenid(String openid);
}
