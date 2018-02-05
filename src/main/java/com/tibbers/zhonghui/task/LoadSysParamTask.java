package com.tibbers.zhonghui.task;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.dao.ISysParamDao;
import com.tibbers.zhonghui.model.SysParam;
import com.tibbers.zhonghui.utils.CacheUtil;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Paul
 * @time:2018/1/20 11:27
 * @description:
 */
@Component
public class LoadSysParamTask implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ISysParamDao iSysParamDao;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null) {
            logger.info("开始从数据库中加载系统配置信息到缓存中");
            List<String> captions = iSysParamDao.queryAllType();
            if(captions != null && captions.size() > 0){
                String targetKey = null;
                for(String caption : captions){
                    String categoryId = null;
                    List<SysParam> itemList = iSysParamDao.queryConfigByType(caption);
                    CacheUtil.set(itemList.get(0).getCategoryid(),itemList);
                    logger.info(String.format("读取配置[%s]到缓存中,key为[%s]",itemList,itemList.get(0).getCategoryid()));
                    for(SysParam sysParam : itemList){
//                        String key = sysParam.getCategoryid() + sysParam.getItemid();
//                        CacheUtil.set(key,sysParam);

                        categoryId = sysParam.getCategoryid();
                        if(StringUtil.isEmpty(targetKey)){//第一次进来
                            targetKey = sysParam.getCategoryid();
                        }else {
                            if (targetKey.compareTo(categoryId) < 0) {
                                targetKey = categoryId;
                            }
                        }
                    }
                    logger.info(String.format("添加capition[%s]与categoryid[%s]的映射关系到缓存中",caption,categoryId));
                    CacheUtil.set(caption,categoryId);
                }
                CacheUtil.set(AppConstants.MAX_CATEGORYID,targetKey);
            }else{
                throw new APIException("系统参数表tsysparameter中没有配置大类");
            }
            logger.info("从数据库加载系统配置信息结束");
        }
    }
}
