package com.wq.mvc.demo;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.model.*;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.Order;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * @author: Paul
 * @time:2018/1/16 0:14
 * @description:
 */
public class AppDemoTest {
    SqlSession sqlSession;

    @Before
    public void init() throws IOException {
        //读取配置文件
        Reader reader = Resources.getResourceAsReader("Configuration.xml");
        //获取工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //获取会话
        sqlSession = sqlSessionFactory.openSession();
    }

    @After
    public void destroy(){
        this.sqlSession.close();
    }

    @Test
    public void queryAllType(){
        List<String> list = sqlSession.selectList("queryAllType");
        System.out.println(list);
    }

    @Test
    public void queryConfigByType(){
        List<SysParam> list = sqlSession.selectList("queryConfigByType","refund");
        System.out.println(list);
    }

    @Test
    public void transfer2Json(){
        List<SysParam> list = new ArrayList<>();
        for(int i=0;i<3;i++){
            SysParam sysParam = new SysParam();
            sysParam .setCategoryid("003000");
            String itemid = ("003" + StringUtil.formatNumber((int)(Math.random() * 1000000),6,"0"));
            sysParam.setItemname("sjedf" + (int)(Math.random() * 1000));
            sysParam.setCaption("你好");
            sysParam.setDescription("eh" + UUID.randomUUID().toString().replaceAll("-","").substring(0,8));
            sysParam.setReverse("");
            list.add(sysParam);
        }
        System.out.println(String.valueOf(JSONObject.toJSON(list)));
    }


    @Test
    public void insertSingleProduct(){
        Product product = new Product();
        product.setProductid("100001");
        product.setUnitprice("123.1");
        product.setProductname("西瓜");
        product.setItemid("001001");
        product.setImagepath("E:\\privatework\\Zhonghui\\src\\main\\test\\java\\com\\wq\\mvc\\demo\\AppDemoTest.java");
        product.setColor("红色");
        product.setValuescore(12);
        product.setProductgrade("甜");

        System.out.println(JSONObject.toJSON(product));
//        sqlSession.insert("insertSingleProduct",product);
//        sqlSession.commit();
    }

    @Test
    public void  properties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/sysproconfig.properties"));
        System.out.println(properties.get("absoluteProductPathPrefix"));
    }


    @Test
    public void registerAccount(){
        Person person = new Person();
        person.setPersonid("");
        person.setAddress("sdfnoh");
        person.setAge(19);
        person.setBindQQ("3754934875");
        person.setPersonname("iedofno剥");
        person.setPhone("34759701244");
        person.setSex("男");
        person.setReverse1("");
        person.setReverse2("");
        person.setReverse3("");

        System.out.println(JSONObject.toJSON(person));

        Account account = new Account();
        account.setAccountid("");
        account.setPersonid("");
        account.setAccobalance("340");
        account.setCusttype("00430320");
        account.setBindphone("37453475034");
        account.setIsvip("0");
        account.setScore("");
        account.setImagepath("");
        System.out.println(JSONObject.toJSON(account));
    }

    @Test
    public void newShopCar(){
        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.setSerialid(StringUtil.generateUUID());
        shoppingCar.setAccountid("210e512c68497189");
        shoppingCar.setAdddatetime(StringUtil.currentDateTime());
        shoppingCar.setDeleteflag("1");
        shoppingCar.setNumber(10);
        shoppingCar.setProductid("7c25e01bdb896763");
        shoppingCar.setModifydatetime("");
        shoppingCar.setReverse1("");
        shoppingCar.setReverse2("");

        System.out.println(JSONObject.toJSON(shoppingCar));
    }

    @Test
    public void queryListByPager(){
        Map<String,Object> map = new HashMap<>();
        map.put("accountid","210e512c68497189");
        map.put("salestate","3");
        Pager pager = new Pager(0,2);
        map.put("pager",pager);
        List<Map<String,String>> list = sqlSession.selectList("queryListByPager",map);
        System.out.println(list);
    }

    @Test
    public void addReceiveAddress(){
        ReceiveAddress receiveAddress = new ReceiveAddress();
        receiveAddress.setSerialid(StringUtil.generateUUID());
        receiveAddress.setAccountid("210e512c68497189");
        receiveAddress.setReceivename("王强");
        receiveAddress.setContactphone("18369872045");
        receiveAddress.setPostcode("310018");
        receiveAddress.setProvince("浙江省");
        receiveAddress.setCity("杭州市");
        receiveAddress.setArea("滨江区");
        receiveAddress.setStreet("白杨街道");
        receiveAddress.setAddressmore("哦松岛枫凭借后卫hi");

        System.out.println(JSONObject.toJSON(receiveAddress));
    }

    @Test
    public void createSingleOrder(){
        Orders orders = new Orders();
        orders.setAccountid("210e512c68497189");
        orders.setFreetransport("0");
        orders.setTransportfee("12.2");
        orders.setNote("备注");
        orders.setAmount("122.02");
        orders.setReverse1("");
        orders.setReverse2("");
        System.out.println(JSONObject.toJSON(orders));

        List<OrderItems> list = new ArrayList<>();
        OrderItems orderItems = new OrderItems();
        orderItems.setProductid("7c25e01bdb896763");
        orderItems.setPronumber(1);
        orderItems.setDicount(0);
        orderItems.setNote("备注123");
        orderItems.setReverse1("");
        list.add(orderItems);

        System.out.println(JSONObject.toJSON(list));
    }
}

