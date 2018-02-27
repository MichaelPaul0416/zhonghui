package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.dao.IAdministratorDao;
import com.tibbers.zhonghui.model.Administrator;
import com.tibbers.zhonghui.service.IAdministratorService;
import com.tibbers.zhonghui.utils.MD5Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            logger.info(String.format("查询adminid[%s]的信息为[%s]",administrator.getAdminid(),query));
            String store = MD5Utils.convertFromMD5(query.getAdminpassword());
            login = psd.equals(store);
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
}
