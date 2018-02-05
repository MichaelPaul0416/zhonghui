package com.tibbers.zhonghui.service.impl;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.dao.ISysParamDao;
import com.tibbers.zhonghui.model.SysParam;
import com.tibbers.zhonghui.service.ISysParamService;
import com.tibbers.zhonghui.utils.CacheUtil;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Paul
 * @time:2018/1/20 9:49
 * @description:
 */
@Transactional
@Service
public class SysParamServiceImpl implements ISysParamService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ISysParamDao iSysParamDao;

    @Override
    public List<SysParam> loadSysConfig() {
        return iSysParamDao.loadSysConfig2Cache();
    }

    @Override
    public boolean createSysCategory(SysParam sysParam) {
        Object categoryId = CacheUtil.get(AppConstants.MAX_CATEGORYID);
        int number;
        if(categoryId != null){
            String current = (String) categoryId;
            number = Integer.parseInt(current.substring(0,3)) + 1;
        }else {
            number = 1;
        }
        try {
            sysParam.setCategoryid(StringUtil.formatNumber(number, 6, "0"));
            iSysParamDao.createSysCategory(sysParam);
            //更新缓存
            CacheUtil.set(AppConstants.MAX_CATEGORYID,sysParam.getCategoryid());
            logger.info(String.format("更新缓存参数[%s]为[%s]",AppConstants.MAX_CATEGORYID,sysParam.getCategoryid()));
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
        return true;
    }

    @Override
    public void createItem4Category(List<SysParam> list) {
        //itemId需要自己生成
        for(SysParam sysParam : list){
            String categoryId = sysParam.getCategoryid();
            String itemId = categoryId.substring(0,3) + (int)(Math.random() * 1000000);
            sysParam.setItemid(itemId);
        }
        try{
            iSysParamDao.createItem4Category(list);
            List<SysParam> cacheList = (List<SysParam>) CacheUtil.get(list.get(0).getCategoryid());
            if(cacheList == null){
                cacheList = new ArrayList<>();
            }
            for(SysParam sysParam : list){
                cacheList.add(sysParam);
            }
            String key = cacheList.get(0).getCategoryid();
            logger.info(String.format("更新配置[%s]到缓存中,key为[%s]",cacheList,key));
            CacheUtil.set(key,cacheList);
        }catch (Exception e){
            throw  new APIException(e.getCause());
        }
    }

    @Override
    public List<SysParam> queryConfigByCategoryId(String categoryId) {
        List<SysParam> list = (List<SysParam>) CacheUtil.get(categoryId);
        if(list == null){
            try {
                list = iSysParamDao.queryConfigByCategoryId(categoryId);
                CacheUtil.set(list.get(0).getCategoryid(),list);
            }catch (Exception e){
                throw new APIException(e.getCause());
            }
        }
        return list;
    }

    @Override
    public List<SysParam> loadSysConfig2Cache() {
        return iSysParamDao.loadSysConfig2Cache();
    }

    @Override
    public boolean deleteCategoryItem(SysParam sysParam) {
        boolean flag ;
        try{
            List<SysParam> list = (List<SysParam>) CacheUtil.get(sysParam.getCategoryid());
            SysParam index = null;
            iSysParamDao.deleteCategoryItem(sysParam);
            for(SysParam sysP : list){
                if(sysP.getItemid().equals(sysParam.getItemid())){
                    index = sysP;
                    break;
                }
            }
            list.remove(index);
            CacheUtil.set(sysParam.getCategoryid(),list);
            flag = true;
            return flag;
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public boolean updateCategoryItem(SysParam sysParam) {
        boolean flag ;
        try {
            List<SysParam> list = (List<SysParam>) CacheUtil.get(sysParam.getCategoryid());
            iSysParamDao.updateCategoryItem(sysParam);
            for(SysParam param : list){
                if(sysParam.getItemid().equals(param.getItemid())){
                    param.setItemname(sysParam.getItemname());
                    param.setDescription(sysParam.getDescription());
                }
            }
            CacheUtil.set(sysParam.getCategoryid(),list);
            flag = true;
            return flag;
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }
}
