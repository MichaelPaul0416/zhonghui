package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.SysParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 9:37
 * @description:
 */
@Repository
public interface ISysParamDao {

    List<SysParam> loadSysConfig2Cache();//查询所有缓存

    List<String> queryAllType();//查询所有的系统配置项类

    List<SysParam> queryConfigByType(String caption);//根据caption查询配置项

    void createSysCategory(SysParam sysParam);

    void createItem4Category(List<SysParam> list);//新建一个大类下面的明细

    List<SysParam> queryConfigByCategoryId(String categoryid);

    boolean deleteCategoryItem(SysParam sysParam);//删除某一个类别明细

    boolean updateCategoryItem(SysParam sysParam);//更新某一个条目

    List<Map<String,String>> queryCategoryCaption();
}
