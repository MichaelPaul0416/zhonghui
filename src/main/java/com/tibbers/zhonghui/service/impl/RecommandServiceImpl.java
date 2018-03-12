package com.tibbers.zhonghui.service.impl;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.dao.IRecommandDao;
import com.tibbers.zhonghui.model.Recommand;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IRecommandService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 0:41
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecommandServiceImpl implements IRecommandService {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IRecommandDao recommandDao;

    @Override
    public void insertRecommand(String accountid,String recommander) {
        try{
            Recommand recommand = new Recommand();
            recommand.setSerialid(StringUtil.generateUUID());
            recommand.setAccountid(accountid);
            recommand.setRecommander(recommander);
            recommand.setRecommanddate(StringUtil.currentDate());
            recommand.setRecommandtime(StringUtil.currentTime());
            logger.info(String.format("新增推荐人与被推荐人记录[%s]",recommand));
            recommandDao.insertRecommand(recommand);
        }catch (Exception e){
            throw new APIException(e);
        }
    }

    @Override
    public List<Recommand> queryRecommandByPager(Pager pager) {
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("pager",pager);
            List<Recommand> list = recommandDao.queryRecommandByPager(map);
            logger.info(String.format("分页查询结果[%s]",list));
            return list;
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public List<Map<String, String>> queryMyRecommandAccounts(String accountid, Pager pager) {
        logger.info(String.format("开始查询被账户[%s]的用户信息",accountid));
        Map<String,Object> param = new HashMap<>();
        Recommand recommand = new Recommand();
        recommand.setAccountid(accountid);
        param.put("recommand",recommand);
        param.put("pager",pager);
        List<Map<String,String>> list = recommandDao.queryMyRecommandAccounts(param);

        logger.info(String.format("查询到账户[%s]所推荐的客户信息列表[%s]",accountid,list));
        return list;
    }
}
