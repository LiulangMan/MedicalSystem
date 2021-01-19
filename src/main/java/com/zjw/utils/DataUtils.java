package com.zjw.utils;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.*;
import com.zjw.domain.util.GoodsAndSuppliedId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 15:25
 */
public class DataUtils {

    public static SimpleDateFormat defaultDataFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static Object[][] GoodsListToObjectArray(List<Goods> list) {
        Object[][] objects = new Object[list.size()][6];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).getGoodId();
            objects[i][1] = list.get(i).getGoodName();
            objects[i][2] = list.get(i).getGoodText();
            objects[i][3] = list.get(i).getGoodStock();
            objects[i][4] = list.get(i).getGoodMoney();
            objects[i][5] = list.get(i).getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方" : "非处方";
        }

        return objects;
    }

    public static Object[][] SelectGoodsListToObjectArray(List<Goods> list) {
        Object[][] objects = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).getGoodId();
            objects[i][1] = list.get(i).getGoodName();
            objects[i][2] = list.get(i).getGoodMoney();
            objects[i][3] = list.get(i).getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方" : "非处方";
        }

        return objects;
    }

    public static Object[][] OrderGoodsListToObjectArray(List<Goods> list) {
        //{"ID", "药名", "类型", "单价", "数量", "总计"};
        Object[][] objects = new Object[list.size()][6];

        for (int i = 0; i < list.size(); i++) {
            Double goodMoney = list.get(i).getGoodMoney();
            int cnt = Math.min(1, list.get(i).getGoodStock());
            objects[i][0] = list.get(i).getGoodId();
            objects[i][1] = list.get(i).getGoodName();
            objects[i][2] = list.get(i).getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方" : "非处方";
            objects[i][3] = goodMoney;
            objects[i][4] = cnt;
            objects[i][5] = cnt * goodMoney;
        }

        return objects;
    }

    public static Object[][] OrderStockGoodsListToObjectArray(List<Goods> list) {
        //{"ID", "药名", "类型", "单价", "数量", "总计"};
        Object[][] objects = new Object[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            Double goodMoney = list.get(i).getGoodMoney();
            int cnt = 1;
            objects[i][0] = list.get(i).getGoodId();
            objects[i][1] = list.get(i).getGoodName();
            objects[i][2] = list.get(i).getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方" : "非处方";
            objects[i][3] = goodMoney;
            objects[i][4] = cnt;
            objects[i][5] = cnt * goodMoney;
        }

        return objects;
    }


    public static Object[][] OrderSimpleInformationToArray(List<Order> list) {
        Object[][] objects = new Object[list.size()][7];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).getOrderId();
            objects[i][1] = list.get(i).getCustomerId();
            objects[i][2] = list.get(i).getCustomerName();
            objects[i][3] = list.get(i).getCustomerPhone();
            objects[i][4] = list.get(i).getSaleEmployName();
            objects[i][5] = list.get(i).getOrderMoney();
            objects[i][6] = defaultDataFormat.format(list.get(i).getOrderTime());
        }

        return objects;
    }

    public static Object[][] StockGoodsToArray(List<Goods> list) {
        Object[][] objects = new Object[list.size()][7];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).getGoodId();
            objects[i][1] = list.get(i).getSupplierId();
            objects[i][2] = list.get(i).getGoodName();
            objects[i][3] = list.get(i).getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方" : "非处方";
            objects[i][4] = list.get(i).getGoodText();
            objects[i][5] = list.get(i).getGoodMoney();
            objects[i][6] = list.get(i).getGoodStock();
        }

        return objects;
    }

    public static List<Goods> queryForGoodsNameInCache(Map<Integer, Goods> cache, String name) {
        List<Goods> result = new ArrayList<>();
        for (Goods goods : cache.values()) {
            if (goods.getGoodName().contains(name)) {
                result.add(goods);
            }
        }
        return result;
    }

    public static List<Goods> queryForGoodsDescriptionInCache(Map<Integer, Goods> cache, String description) {
        List<Goods> result = new ArrayList<>();
        for (Goods goods : cache.values()) {
            if (goods.getGoodText().contains(description)) {
                result.add(goods);
            }
        }
        return result;
    }

    public static List<Goods> queryForGoodsLeftPriceInCache(Map<Integer, Goods> cache, double price) {
        List<Goods> result = new ArrayList<>();
        for (Goods goods : cache.values()) {
            if (price >= goods.getGoodMoney()) {
                result.add(goods);
            }
        }
        return result;
    }

    public static Goods queryForGoodsIdInCache(Map<Integer, Goods> cache, int id) {
        return cache.get(id);
    }

    public static Object[][] StockGoodsOrderToArray(List<StockOrder> list) {
        Object[][] objects = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).getStockId();
            objects[i][1] = list.get(i).getStockEmploy();
            objects[i][2] = list.get(i).getStockMoney();
            objects[i][3] = defaultDataFormat.format(list.get(i).getStockTime());
        }

        return objects;
    }

    public static Object[][] LoginInfoToArray(List<InfoLogin> loginList) {
        Object[][] objects = new Object[loginList.size()][3];

        for (int i = 0; i < loginList.size(); i++) {
            objects[i][0] = loginList.get(i).getUsername();
            objects[i][1] = loginList.get(i).getLoginType() == IndexConstant.LOGIN_TYPE_ADMIN ? "超级管理员" :
                    loginList.get(i).getLoginType() == IndexConstant.LOGIN_TYPE_EMPLOY ? "员工" : "顾客";
            objects[i][2] = defaultDataFormat.format(loginList.get(i).getLoginTime());
        }
        return objects;
    }

    public static Object[][] AnnouncementToArray(List<Announcement> announcementList) {
        Object[][] objects = new Object[announcementList.size()][2];

        for (int i = 0; i < announcementList.size(); i++) {
            objects[i][0] = announcementList.get(i).getTitle();
            objects[i][1] = defaultDataFormat.format(announcementList.get(i).getWriteTime());
        }
        return objects;
    }

    public static Object[][] SupplierToArray(List<Supplier> list) {
        Object[][] objects = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).getSupplierId();
            objects[i][1] = list.get(i).getName();
            objects[i][2] = list.get(i).getPhone();
            objects[i][3] = list.get(i).getAddress();
        }
        return objects;
    }

    public static Object[][] UserToArray(List<Object> list) {
        Object[][] objects = new Object[list.size()][7];

        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof Employ) {
                Employ employ = (Employ) o;
                objects[i][0] = employ.getEmployId();
                objects[i][1] = employ.getLoginName();
                objects[i][2] = employ.getName();
                objects[i][3] = employ.getSex() == IndexConstant.BOY_TYPE ? "男" : "女";
                objects[i][4] = employ.getAddress();
                objects[i][5] = employ.getPhone();
                objects[i][6] = employ.getType();
            }

            if (o instanceof Customer) {
                Customer customer = (Customer) o;
                objects[i][0] = customer.getCustomerId();
                objects[i][1] = customer.getLoginName();
                objects[i][2] = customer.getName();
                objects[i][3] = customer.getSex() == IndexConstant.BOY_TYPE ? "男" : "女";
                objects[i][4] = customer.getAddress();
                objects[i][5] = customer.getPhone();
                objects[i][6] = IndexConstant.LOGIN_TYPE_CUSTOMER;
            }
        }
        return objects;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class GoodsForSaleAndStock {
        private Integer id;
        private String name;
        private Integer sale;
        private Integer stock;
    }

    public static Object[][] SaleStockToArray(List<Goods> goodsList, List<Goods> stockGoods) {

        List<GoodsForSaleAndStock> list = new ArrayList<>();

        //由于返回的均是根据Id升序排序的，因此，采用双指针法移动
        int p1 = 0;
        int p2 = 0;

        while (p1 <= goodsList.size() - 1 && p2 <= stockGoods.size() - 1) {
            Goods goods = goodsList.get(p1);
            Goods stock = stockGoods.get(p2);
            //情况1
            if (goods.getGoodId().equals(stock.getGoodId())) {
                list.add(new GoodsForSaleAndStock(goods.getGoodId(), goods.getGoodName(), goods.getGoodStock(), stock.getGoodStock()));
                p1++;
                p2++;
            }
            //情况2
            else if (goods.getGoodId().compareTo(stock.getGoodId()) < 0) {
                list.add(new GoodsForSaleAndStock(goods.getGoodId(), goods.getGoodName(), goods.getGoodStock(), 0));
                p1++;
            }
            //情况3
            else if (goods.getGoodId().compareTo(stock.getGoodId()) > 0) {
                list.add(new GoodsForSaleAndStock(stock.getGoodId(), stock.getGoodName(), 0, stock.getGoodStock()));
                p2++;
            }
        }

        while (p1 <= goodsList.size() - 1) {
            Goods goods = goodsList.get(p1);
            list.add(new GoodsForSaleAndStock(goods.getGoodId(), goods.getGoodName(), goods.getGoodStock(), 0));
            p1++;
        }

        while (p2 <= stockGoods.size() - 1) {
            Goods stock = stockGoods.get(p2);
            list.add(new GoodsForSaleAndStock(stock.getGoodId(), stock.getGoodName(), 0, stock.getGoodStock()));
            p2++;
        }


        Object[][] objects = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            objects[i][0] = list.get(i).id;
            objects[i][1] = list.get(i).name;
            objects[i][2] = list.get(i).sale;
            objects[i][3] = list.get(i).stock;
        }

        return objects;
    }
}
