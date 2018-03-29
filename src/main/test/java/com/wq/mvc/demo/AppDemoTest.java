package com.wq.mvc.demo;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.model.*;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.utils.DateUtil;
import com.tibbers.zhonghui.utils.MD5Utils;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
//        account.setScore("");
        account.setImagepath("");
        System.out.println(JSONObject.toJSON(account));
    }

    @Test
    public void newShopCar(){
        ShoppingCar shoppingCar = new ShoppingCar();
//        shoppingCar.setSerialid(StringUtil.generateUUID());
        shoppingCar.setAccountid("oe9PL4qR5KW4gKxN0csK2n3LgdCE");
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
        receiveAddress.setAccountid("210e512c6849734756");
        receiveAddress.setReceivename("王23强");
        receiveAddress.setContactphone("18369874355");
        receiveAddress.setPostcode("310018");
        receiveAddress.setProvince("浙江省");
        receiveAddress.setCity("杭州市");
        receiveAddress.setArea("滨江区");
        receiveAddress.setStreet("白杨街道");
        receiveAddress.setAddressmore("哦松岛枫凭dstg借后卫hi");

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
        orderItems.setDiscount(0);
        orderItems.setNote("备注123");
        orderItems.setReverse1("");
        list.add(orderItems);

        System.out.println(JSONObject.toJSON(list));
    }

    @Test
    public void refund(){
        Refund refund = new Refund();
        refund.setAccountid("hi373b450480v834");
        refund.setOrderid("20180221141610c5f0f0ce86962386");
        refund.setProductid("7c25e01bdb896763");
        refund.setAmount("1");
        refund.setItemid("002003");
        refund.setDetail("我要退款");

        System.out.println(JSONObject.toJSON(refund));
    }

    @Test
    public void subString(){
        String str = "nonce_str=ad4f9ded44dc4d96a5493e57db4063c5&refund_desc=我要退款&out_trade_no=20180221141610c5f0f0ce86962386&out_refund_no=20180225182619488978a1cd732831&appid=wxb4b01bcd56f57b44&total_fee=1&refund_fee=100&mch_id=1498864162&sign_type=MD5&key=2ab9071b06b9f739b950ddb41db2690d&";
        StringBuilder builder = new StringBuilder(str);
        System.out.println(builder.substring(0,str.length() - 1 ));
    }

    @Test
    public void addAdmin(){
        Administrator administrator = new Administrator();
        administrator.setAdminid(StringUtil.generateUUID());
        administrator.setAdminname("Paul");
        String password = "123456";
        String encrypt = MD5Utils.string2MD5(password);
        administrator.setAdminpassword(encrypt);
        administrator.setCreatedatetime(StringUtil.currentDateTime());
        administrator.setSerialid(StringUtil.serialId());
        administrator.setIdentitytype("0");
        administrator.setIsvalid("1");
        administrator.setReverse1("");
        administrator.setReverse2("");

        System.out.println(JSONObject.toJSON(administrator));
    }

    @Test
    public void queryProduct(){
        Product product = new Product();
        product.setProductname("西瓜");

        System.out.println(JSONObject.toJSONString(product));
    }

    @Test
    public void recommandInfo(){
//        System.out.println(StringUtil.serialId());
        for (int i=0;i<100;i++){
            System.out.println(Math.abs("210e512c68497189".hashCode()));
        }
    }

    @Test
    public void date(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetime = simpleDateFormat.format(calendar.getTime());

        System.out.println(DateUtil.caculateDate(10));
    }

    @Test
    public void money(){
//        double money = StringUtil.formatStr2Dobule("36.16");
//        BigDecimal bigDecimal = new BigDecimal("36.16");
//        double money = bigDecimal.multiply(new BigDecimal("100")).doubleValue();
//        System.out.println(money);

        BigDecimal bigDecimal = new BigDecimal("12");
        double value = bigDecimal.divide(new BigDecimal("100")).doubleValue();
        System.out.println(String.valueOf(value));

    }
}

