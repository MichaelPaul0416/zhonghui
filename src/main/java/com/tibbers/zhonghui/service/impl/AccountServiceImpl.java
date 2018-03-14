package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.dao.*;
import com.tibbers.zhonghui.model.*;
import com.tibbers.zhonghui.model.common.Pager;
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
import java.util.ArrayList;
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

    @Autowired
    private IOrdersDao ordersDao;

    @Autowired
    private IRefundDao refundDao;

    @Autowired
    private IRecommandIncomeDao recommandIncomeDao;

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

    @Override
    public Map<String, List<Map<String, Object>>> queryAccountTrades(String accountid, Pager pager) {
        logger.info(String.format("开始查询账户[%s]的资金收支交易记录",accountid));
        Map<String,List<Map<String,Object>>> queryResults = new HashMap<>();

        logger.info(String.format("查询账户[%s]的资金支出记录",accountid));
        Map<String,Object> param = new HashMap<>();
        Orders orders = new Orders();
        orders.setAccountid(accountid);
        param.put("account",orders);
        param.put("pager",pager);
        //根据订单完成日期降序，查询订单表中有效的订单
        List<Map<String,String>> orderSerialList = ordersDao.queryOrderIdsForDetails(param);
        List<Map<String,Object>> capitalInSerials = new ArrayList<>();
        for(Map<String,String> orderSerial : orderSerialList) {

            Map<String, Object> orderResult = new HashMap<>();
            orderResult.put("emcapitalserialno", orderSerial.get("emcapitalserial"));//三方流水号
            orderResult.put("orderid",orderSerial.get("orderid"));
            orderResult.put("orderdonedatetime",orderSerial.get("donedatetime"));//订单完成时间
            orderResult.put("amount",orderSerial.get("amount"));//订单金额
            orderResult.put("thirdMsg",orderSerial.get("thirdpartmsg"));

            capitalInSerials.add(orderResult);
            logger.info(String.format("查询到资金支出流水[%s]",orderResult));
        }
        queryResults.put("capitalInSerials",capitalInSerials);

        //查询所有退款流水
        logger.info(String.format("查询账户[%s]的退款流水记录",accountid));
        param.clear();
        Refund refund = new Refund();
        refund.setAccountid(accountid);
        refund.setAgreestate("2");
        param.put("refund",refund);
        param.put("pager",pager);

        List<Refund> refundList = refundDao.queryRefundsByPager(param);
        List<Map<String,Object>> refundResults = new ArrayList<>();
        for(Refund singleRefund : refundList){
            Map<String,Object> refundMap = new HashMap<>();
            refundMap.put("emcapitalserialno",singleRefund.getRefundserialid());
            refundMap.put("orderid",singleRefund.getOrderid());
            refundMap.put("refunddatetime",singleRefund.getAgreedatetime());
            refundMap.put("amount",singleRefund.getAmount());
            refundMap.put("message",singleRefund.getDetail());

            refundResults.add(refundMap);
            logger.info(String.format("查询到退款流水[%s]",refundMap));
        }

        queryResults.put("refundSerials",refundResults);

        logger.info(String.format("查询账户[%s]作为推荐者的收益明细",accountid));
        param.clear();
        RecommandIncome recommandIncome = new RecommandIncome();
        recommandIncome.setAccountid(accountid);
        recommandIncome.setAlreadydone("1");
        param.put("income",recommandIncome);
        param.put("pager",pager);

        List<RecommandIncome> recommandIncomes = recommandIncomeDao.queryRecommandIncomeByPager(param);
        List<Map<String,Object>> incomeResults = new ArrayList<>();
        for(RecommandIncome singleIncome : recommandIncomes){
            Map<String,Object> income = new HashMap<>();
            income.put("incomeserialno",singleIncome.getIncomeserialno());
            income.put("comefrom",singleIncome.getComefrom());
            income.put("amount",singleIncome.getIncome());
            income.put("incomedatetime",singleIncome.getIncomedatetime());
            income.put("description",singleIncome.getDescription());

            incomeResults.add(income);
            logger.info(String.format("查询到推荐收益信息[%s]",income));
        }
        queryResults.put("incomeSerials",incomeResults);
        logger.info(String.format("查询账号[%s]的资金收支明细结束",accountid));
        return queryResults;
    }

    @Override
    public List<Account> queryAccounts(String isvip, String termid, Pager pager) {
        Map<String,Object> param = new HashMap<>();
        Account account = new Account();
        account.setIsvip(isvip);
        account.setCusttype(termid);
        param.put("account",account);
        param.put("pager",pager);

        List<Account> accounts = accountServiceDao.queryAccounts(param);
        logger.info(String.format("查询到账户信息[%s]",accounts));
        return accounts;
    }

    @Override
    public List<Map<String, Object>> orderByScore(Map<String, Object> param) {
        return accountServiceDao.orderByScore(param);
    }

    @Override
    public Account queryAccountByOpenid(String openid) {
        logger.info(String.format("根据微信openid[%s]查询绑定的账户信息",openid));
        Account account = accountServiceDao.queryAccountByOpenid(openid);
        logger.info(String.format("微信openid[%s]绑定的账户信息如下[%s]",openid,account));
        return account;
    }


}
