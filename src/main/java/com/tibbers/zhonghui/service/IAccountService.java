package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.common.Pager;
import org.springframework.web.multipart.MultipartFile;

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

    void uploadAccountImage(MultipartFile file, Account account);

    void updatePersonalInfo(String person);

    Account queryByAccountid(String accountid);

    Map<String, List<Map<String, Object>>> queryAccountTrades(String accountid, Pager pager);
}
