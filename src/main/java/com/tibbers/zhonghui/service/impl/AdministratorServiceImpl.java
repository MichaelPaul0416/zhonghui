package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.dao.IAccountServiceDao;
import com.tibbers.zhonghui.dao.IAdministratorDao;
import com.tibbers.zhonghui.dao.IOrdersDao;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.Administrator;
import com.tibbers.zhonghui.model.Orders;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IAdministratorService;
import com.tibbers.zhonghui.utils.DateUtil;
import com.tibbers.zhonghui.utils.MD5Utils;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/2/26 22:45
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdministratorServiceImpl implements IAdministratorService {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IAdministratorDao administratorDao;

    @Autowired
    private IAccountServiceDao accountServiceDao;

    @Autowired
    private IOrdersDao ordersDao;


    @Override
    public String createSingleAdmin(String adminInfo) {
        try {
            Administrator administrator = JSONObject.parseObject(adminInfo, Administrator.class);
            String password = MD5Utils.string2MD5(administrator.getAdminpassword());
            administrator.setAdminpassword(password);
            logger.info(String.format("插入的管理员信息[%s]",administrator));
            administratorDao.createSingleAdmin(administrator);
            return administrator.getAdminid();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new APIException(e);
        }
    }

    @Override
    public void deleteAdmin(String adminid) {
        Administrator administrator = new Administrator();
        administrator.setAdminid(adminid);
        administrator.setIsvalid("0");
        logger.warn(String.format("即将将管理员[%s]逻辑删除",administrator));
        administratorDao.deleteAdmin(administrator);
    }

    @Override
    public List<Administrator> queryAdmins() {
        return administratorDao.queryAllAdmin();
    }

    @Override
    public void updateInfoAdmin(String updateAdmin) {
        try{
            Administrator administrator = JSONObject.parseObject(updateAdmin,Administrator.class);
            logger.info(String.format("更新管理员信息[%s]",administrator));
            administratorDao.updateInfoAdmin(administrator);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new APIException(e);
        }
    }

    @Override
    public boolean loginAdmin(Administrator administrator) {
        boolean login;
        try{
            String psd = administrator.getAdminpassword();
            Administrator query = administratorDao.queryAdmin(administrator);
            if(query != null && !StringUtil.isEmpty(query.getAdminname())) {
                logger.info(String.format("查询adminid[%s]的信息为[%s]", administrator.getAdminid(), query));
                String store = MD5Utils.string2MD5(administrator.getAdminpassword());
                login = query.getAdminpassword().equals(store);
            }else {
                throw new APIException(String.format("管理员[%s]不存在",administrator));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new APIException("登录异常，异常信息["+e.getCause().getMessage()+"]");
        }
        return login;
    }

    @Override
    public Administrator queryAdmin(Administrator administrator) {
        try{
            administrator = administratorDao.queryAdmin(administrator);
            return administrator;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new APIException(e);
        }
    }

    @Override
    public Map<String, List<Map<String, String>>> queryAccountTradeDetails(String accountid, Pager pager) {
        logger.info(String.format("开始查询[%s]的基本用户信息",accountid));
        Account account = accountServiceDao.queryByAccountid(accountid);
        if(account != null && !StringUtil.isEmpty(account.getAccountname())){
            Map<String,List<Map<String,String>>> accountTrades = new HashMap<>();
            Map<String,Object> param = new HashMap<>();
            if("1".equals(account.getIsvip())){//vip用户
                param.put("account",param);
                param.put("pager",pager);
                List<Map<String,String>> saleRecords = administratorDao.queryVIPSaleRecords(param);

                logger.info(String.format("查询到[%s]的产品出售明细",saleRecords));
                accountTrades.put("saleRecords",saleRecords);
            }else {
                accountTrades.put("saleRecords",null);
            }

            logger.info(String.format("查询[%s]的购买明细",accountid));
            param.clear();
            Orders orders = new Orders();
            orders.setAccountid(accountid);
            orders.setIsvalid("1");
            param.put("orders",orders);
            param.put("pager",pager);
            List<Map<String,String>> buyDetails = ordersDao.queryOrdersByPager(param);
            logger.info(String.format("查询[%s]的购买记录",buyDetails));
            accountTrades.put("buyRecords",buyDetails);
            return accountTrades;
        }else {
            throw new APIException(String.format("账户体系中不存在accountid为[%s]的账户",accountid));
        }

    }

    @Override
    public List<Map<String, Object>> hotProductsLastDays(String offsetDate, Pager pager) {
        logger.info(String.format("开始查询最近[%s]天最热销的产品",offsetDate));
        int offset = Integer.parseInt(offsetDate);
        if(offset > 0){
            throw new APIException("日期偏移量必须为负数");
        }

        String before = DateUtil.caculateDate(offset);
        String current = StringUtil.currentDateTime();

        Map<String,Object> param = new HashMap<>();
        param.put("start",before);
        param.put("end",current);
        param.put("pager",pager);
        List<Map<String,Object>> result = administratorDao.hotProductsLastDays(param);

        logger.info(String.format("分页查询最近[%s]天的热销产品[%s]",offset,result));


        return result;
    }

    @Override
    public List<Map<String, Object>> accountCustomRecordInFewMonths(String offsetMonth, Pager pager) {
        logger.info(String.format("开始查询最近[%s]月的消费记录",offsetMonth));
        int month = Integer.parseInt(offsetMonth);
        if(month > 0){
            throw new APIException("月份偏移量必须小于零");
        }

        String current = StringUtil.currentDateTime();
        String before = DateUtil.caculateDate(month * 30);

        Map<String,Object> param = new HashMap<>();
        param.put("start",before);
        param.put("end",current);
        param.put("pager",pager);

        List<Map<String,Object>> result = administratorDao.accountCustomRecordInFewMonths(param);
        logger.info(String.format("查询到最近[%s]个月的账户消费记录[%s]",offsetMonth,result));
        return result;
    }

    @Override
    public List<Map<String, Object>> sumTotalRecommander(String offsetMonth, Pager pager) {
        logger.info(String.format("开始查询最近[%s]月VIP用户的推荐情况",offsetMonth));
        int month = Integer.parseInt(offsetMonth);

        if(month > 0){
            throw new APIException("月份偏移量必须小于零");
        }
        String current = StringUtil.currentDateTime().substring(0,8);
        String before = DateUtil.caculateDate(month * 30).substring(0,8);
        Map<String,Object> param = new HashMap<>();
        param.put("start",before);
        param.put("end",current);
        param.put("pager",pager);

        List<Map<String,Object>> result = administratorDao.sumTotalRecommander(param);
        logger.info(String.format("查询到最近[%s]个月VIP用户的推荐情况[%s]",offsetMonth,result));
        return result;
    }
}
