package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.dao.IAccountServiceDao;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.Person;
import com.tibbers.zhonghui.model.SysParam;
import com.tibbers.zhonghui.service.IAccountService;
import com.tibbers.zhonghui.utils.CacheUtil;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 13:10
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements IAccountService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IAccountServiceDao accountServiceDao;

    @Autowired
    private ServiceConfigBean serviceConfigBean;

    @Override
    public Map<String,String> registerNewAccount(Map<String, String> map) {
        String personInfo = map.get("personInfo");
        String accountInfo = map.get("accountInfo");
        Map<String,String> infoMap = new HashMap<>();
        if(!StringUtils.isEmpty(personInfo) && !StringUtil.isEmpty(accountInfo)){
            try {
                Person person = JSONObject.parseObject(personInfo, Person.class);
                Account account = JSONObject.parseObject(accountInfo, Account.class);
                String personId = StringUtil.generateUUID();
                person.setPersonid(personId);
                logger.info(String.format("即将注册个人信息[%s]", person));
                accountServiceDao.registerPersonInfo(person);
                String accountId = StringUtil.generateUUID();
                account.setAccountid(accountId);
                account.setPersonid(personId);

                //需要校验下custtype是否存在
                String categoryid = (String) CacheUtil.get(AppConstants.CUSTTYPE);
                List<SysParam> sysParamList = (List<SysParam>) CacheUtil.get(categoryid);
                boolean flag = false;
                for (SysParam sysParam : sysParamList) {
                    if (sysParam.getItemid().equals(account.getCusttype())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    logger.info(String.format("即将注册系统账户信息[%s]", account));
                    accountServiceDao.registerAccountInfo(account);
                    logger.info("信息注册成功");

                    infoMap.put("personid", personId);
                    infoMap.put("accountid", accountId);
                } else {
                    throw new APIException("账户类型与现有系统中配置的账户类型不匹配，请联系管理员");
                }
            }catch (Exception e){
                throw new APIException(e.getCause());
            }
        }else{
            throw new APIException("输入的个人信息与账户信息为空，请检查后重新输入");
        }
        return infoMap;
    }

    @Override
    public void updateAccountInfo(String account) {
        try{
            Account acco = JSONObject.parseObject(account,Account.class);
            logger.info(String.format("开始修改账户[%s]信息",acco.getAccountid()));
            accountServiceDao.updateAccountInfo(acco);
            logger.info(String.format("账户[%s]信息修改成功",acco.getAccountid()));
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void uploadAccountImage(MultipartFile file, Account account) {
        String localPath = serviceConfigBean.getAbsoluteAccountPathPrefix();
        File dir = new File(localPath);
        if(!dir.exists()){
            logger.info(String.format("目录[%s]不存在，创建文件夹",localPath));
        }
        String storePath = localPath + "\\" + account.getAccountid();
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(storePath,file.getOriginalFilename()));
            logger.info(String.format("文件[%s]落地完成",storePath));
            account.setImagepath(storePath + "\\" + file.getOriginalFilename());
            accountServiceDao.updateAccountInfo(account);
            logger.info(String.format("产品[%s]更新图片目录成功",account.getAccountid()));
        } catch (Exception e) {
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void updatePersonalInfo(String person) {
        try{
            Person personBean = JSONObject.parseObject(person,Person.class);
            if(StringUtils.isEmpty(personBean.getReverse3())){
                personBean.setReverse3(" ");
            }
            logger.info(String.format("更新个人信息[%s]",personBean));
            accountServiceDao.updatePersonalInfo(personBean);
            logger.info(String.format("个人信息[%s]更新成功",personBean.getPersonid()));
        }catch (Exception e){
            throw new APIException(e.getCause());
        }

    }

    @Override
    public Account queryByAccountid(String accountid) {
        try{
            return accountServiceDao.queryByAccountid(accountid);
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }


}
