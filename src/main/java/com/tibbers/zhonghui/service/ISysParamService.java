package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.SysParam;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 9:48
 * @description:
 */
public interface ISysParamService {
    List<SysParam> loadSysConfig();

    boolean createSysCategory(SysParam sysParam);

    void createItem4Category(List<SysParam> list);//新建一个大类下面的明细

    List<SysParam> queryConfigByCategoryId(String categoryId);

    List<SysParam> loadSysConfig2Cache();

    boolean deleteCategoryItem(SysParam sysParam);

    boolean updateCategoryItem(SysParam sysParam);

    List<Map<String, String>> queryAllType();
}
