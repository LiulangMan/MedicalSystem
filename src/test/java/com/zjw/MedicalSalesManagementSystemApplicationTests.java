package com.zjw;

import com.zjw.domain.Customer;
import com.zjw.domain.Employ;
import com.zjw.domain.Goods;
import com.zjw.domain.Supplier;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.mapper.OrderMapper;
import com.zjw.mapper.SupplierMapper;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.service.GoodService;
import com.zjw.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;


@SpringBootTest
class MedicalSalesManagementSystemApplicationTests {

    @Autowired
    EmployService employService;

    @Autowired
    CustomerService customerService;

    @Autowired
    GoodService goodService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SupplierMapper supplierMapper;

    @Test
    void contextLoads() {
        Employ employ = new Employ(1, RandomUtils.randomIdentity(), "1", "1", "1", 1, "1", "1", 1);
        employService.insert(employ);
    }

    @Test
    void test1() {
        Customer customer = new Customer(1, RandomUtils.randomIdentity(), "test", "test", "test", 1, "test", "test");
        customerService.insert(customer);

    }

    @Test
    void test2() {
        for (int i = 0; i < 100; i++) {
            goodService.insertStockList(new Goods(i, "testName" + i, i + 100, "test", i + 10.0, i % 2));
        }
    }


    @Test
    void test3(){
        List<GoodsIdAndGoodsCntForOrder> a = orderMapper.selectAllForGoodsInformationByOrderId("FriJan0821:01:41CST2021399.6799018235231");
        System.out.println(a);
    }

    @Test
    void test4(){
        for (int i = 0; i < 10; i++) {
            supplierMapper.insert(new Supplier(i+1,"SupplierName"+i,"11111111","111111111"));
        }
    }


}
