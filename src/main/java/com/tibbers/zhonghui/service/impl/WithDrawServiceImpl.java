package com.tibbers.zhonghui.service.impl;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.dao.IAccountServiceDao;
import com.tibbers.zhonghui.dao.IWithDrawDao;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.WithDraw;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IWithDrawService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/20
 * @Description:
 * @Resource:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WithDrawServiceImpl implements IWithDrawService{

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IWithDrawDao withDrawDao;

    @Autowired
    private IAccountServiceDao accountServiceDao;
    @Override
    public List<WithDraw> applyWithDrawOrBatch(List<WithDraw> withDraws) {
        logger.info(String.format("开始批量插入提现申请"));
        try {
            for(WithDraw withDraw : withDraws){
                withDraw.setSerialid(StringUtil.generateUUID());
                String accountid = withDraw.getAccountid();
                Account account = accountServiceDao.queryByAccountid(accountid);
                if(account != null && !StringUtil.isEmpty(account.getPersonid())){
                    if(account.getAccobalance().compareTo(withDraw.getAmount()) < 0){
                        throw new APIException(String.format("账户余额[%s]小于提现金额[%s]",account.getAccobalance(),withDraw.getAmount()));
                    }else {
                        Account update = new Account();
                        String balance = account.getAccobalance();
                        double current = StringUtil.formatStr2Dobule(balance);
                        current = current - StringUtil.formatStr2Dobule(withDraw.getAmount());
                        update.setAccountid(accountid);
                        update.setAccobalance(String.valueOf(current));
                        logger.info(String.format("开始更新账户[%s]的账户余额",account.getAccountid()));
                        accountServiceDao.updateAccountInfo(account);
                        logger.info(String.format("冻结账户[%s]余额[%s]成功",account.getAccountid(),withDraw.getAmount()));
                    }
                }else {
                    throw new APIException(String.format("账户[%s]不存在",accountid));
                }
                withDraw.setApplydatetime(StringUtil.currentDateTime());
            }
            withDrawDao.applyWithDrawOrBatch(withDraws);
            logger.info(String.format("批量插入提现申请结束"));
            return withDraws;
        }catch (Exception e){
            throw new APIException(e.getCause().getMessage(),e);
        }
    }

    @Override
    public void auditWithDraw(WithDraw withDraw) {
        logger.info(String.format("管理员[%s]审核[%s]的提现申请",withDraw.getAdminid(),withDraw.getAccountid()));
        try {

            if("1".equals(withDraw.getApplystate())){
                logger.info(String.format("管理员同意[%s]的提现申请，即将更新账户余额",withDraw.getAccountid()));
//                Account account = new Account();
                Account query = accountServiceDao.queryByAccountid(withDraw.getAccountid());
                if(query != null && !StringUtil.isEmpty(query.getPersonid())){
                    withDrawDao.auditWithDraw(withDraw);
                    logger.info(String.format("账户[%s]提现申请更新成功",withDraw.getSerialid()));
                }else {
                    throw new APIException(String.format("账户[%s]不存在或者尚未绑定微信账号",withDraw.getAccountid()));
                }
            }else {
                logger.info(String.format("管理员拒绝[%s]的提现申请",withDraw.getAccountid()));
                Account query = accountServiceDao.queryByAccountid(withDraw.getAccountid());
                if(query != null && !StringUtil.isEmpty(query.getPersonid())){
                    withDrawDao.auditWithDraw(withDraw);
                    logger.info(String.format("更新提现申请[%s]成功",withDraw.getSerialid()));
                }else {
                    throw new APIException(String.format("账户[%s]不存在或者尚未绑定微信账号",withDraw.getAccountid()));
                }
            }
        }catch (Exception e){
            throw new APIException(e.getMessage(),e);
        }
        logger.info(String.format("审核成功，已更新提现申请"));
    }

    @Override
    public List<WithDraw> queryWithDraws(WithDraw withDraw, Pager pager) {
        logger.info(String.format("查询提现申请,请求参数[%s]",withDraw));
        try{
            Map<String,Object> param = new HashMap<>();
            param.put("withdraw",withDraw);
            param.put("pager",pager);
            return  withDrawDao.queryWithDraws(param);
        }catch (Exception e){
            throw new APIException(e.getMessage(),e);
        }

    }

    @Override
    public List<Map<String, String>> queryTotalWithdraw(String accountid) {
        logger.info(String.format("开始查询[%s]的提现总金额",accountid));
        return withDrawDao.queryTotalWithdraw(accountid);
    }
}
