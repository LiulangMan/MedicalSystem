package com.zjw.config;

import com.zjw.domain.*;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 22:40
 * @Text: 静态配置类
 */
public class StaticConfiguration {

    //静态信息
    private static Employ employ;
    private static Customer customer;
    private static Integer loginType;
    //销售管理：订单药品
    private static List<Goods> orderGoods = new ArrayList<>(20);
    //销售管理：选择的药品
    private static List<Goods> selectGoods = new ArrayList<>(20);
    //全局线程池
    private static ExecutorService pool = Executors.newCachedThreadPool();
    //采购管理：选择的药品
    private static List<Goods> selectStockGoods = new ArrayList<>(20);
    //采购管理，订单
    private static Map<Integer, Goods> stockOrderGoods = new HashMap<>(20);

    //本地缓存，减少数据库压力
    private static Map<Integer, Goods> goodsCache = new HashMap<>(100);
    private static Map<String, Order> orderCache = new HashMap<>(100);

    //采购管理所用的缓存
    private static Map<Integer, Goods> stockGoodsCache = new HashMap<>(100);
    private static Map<String, StockOrder> stockGoodsOrderCache = new HashMap<>(100);

    //字体
    private static Font font = new Font(null, Font.PLAIN, 25);

    //初始化Config
    public static void initConfigUtil(Employ employ, Customer customer, List<Goods> goodsList, List<Order> orderList,
                                      List<Goods> stockGoodsList, List<StockOrder> stockOrderList) {
        StaticConfiguration.employ = employ;
        StaticConfiguration.customer = customer;
        refreshStockGoodsOrderCache(stockOrderList);
        refreshStockGoodsCache(stockGoodsList);
        refreshGoodsCache(goodsList);
        refreshOrderCache(orderList);
    }

    public static void addStockGoodsOrderCache(StockOrder order) {
        stockGoodsOrderCache.put(order.getStockId(), order);
    }

    public static void refreshStockGoodsOrderCache(List<StockOrder> list) {
        stockGoodsOrderCache.clear();
        for (StockOrder stockOrder : list) {
            stockGoodsOrderCache.put(stockOrder.getStockId(), stockOrder);
        }
    }

    public static StockOrder getStockGoodsOrderInCache(String id) {
        return stockGoodsOrderCache.get(id);
    }

    public static void refreshStockGoodsCache(List<Goods> list) {
        stockGoodsCache.clear();
        for (Goods goods : list) {
            stockGoodsCache.put(goods.getGoodId(), goods);
        }
    }

    public static void removeStockGoodsCache(Goods goods) {
        if (containSelectStockGoods(goods)) {
            selectStockGoods.remove(goods);
        }
    }

    public static Goods getStockGoodsInCache(Integer id) {
        return stockGoodsCache.get(id);
    }

    public static void addSelectStockGoods(Goods goods) {
        if (!containSelectStockGoods(goods)) {
            selectStockGoods.add(goods);
        }
    }

    public static void clearSelectStockGoods() {
        selectStockGoods.clear();
    }

    public static void removeSelectStockGoods(Goods goods) {
        if (containSelectStockGoods(goods)) {
            selectStockGoods.remove(goods);
        }
    }

    public static Order getOrderInCache(String id) {
        return orderCache.get(id);
    }

    public static void addOrderInCache(Order order) {
        orderCache.put(order.getOrderId(), order);
    }

    public static void refreshOrderCache(List<Order> list) {
        orderCache.clear();
        for (Order order : list) {
            orderCache.put(order.getOrderId(), order);
        }
    }

    public static Goods getGoodsInCache(Integer id) {
        return goodsCache.get(id);
    }

    public static void refreshGoodsCache(List<Goods> list) {
        goodsCache.clear();
        for (Goods goods : list) {
            goodsCache.put(goods.getGoodId(), goods);
        }
    }

    public static void addThreadPoolTask(Runnable task) {
        //pool.submit(task);
        //调用事件调度线程创建UI
        SwingUtilities.invokeLater(task);
    }


    public static void clearOrderGoods() {
        orderGoods.clear();
    }

    public static void transToOrderGoods() {
        clearOrderGoods();
        orderGoods.addAll(selectGoods);
        clearSelectGoods();
    }

    public static void transToStockOrderGoods() {
        stockOrderGoods.clear();
//        stockOrderGoods.addAll(selectStockGoods);
        for (Goods goods : selectStockGoods) {
            stockOrderGoods.put(goods.getGoodId(), goods);
        }
        selectStockGoods.clear();
    }

    public static void addSelectGoods(Goods goods) {
        if (containSelectGoods(goods)) {
            return;
        }

        selectGoods.add(goods);
    }

    public static void removeSelectGoods(Goods goods) {
        if (!containSelectGoods(goods)) {
            return;
        }
        selectGoods.remove(goods);
    }

    public static void clearSelectGoods() {
        StaticConfiguration.selectGoods.clear();
    }

    private static boolean containSelectGoods(Goods goods) {
        for (Goods selectGood : selectGoods) {
            if (goods.getGoodId().equals(selectGood.getGoodId())) {
                return true;
            }
        }
        return false;
    }

    private static boolean containSelectStockGoods(Goods goods) {
        for (Goods selectGood : selectStockGoods) {
            if (goods.getGoodId().equals(selectGood.getGoodId())) {
                return true;
            }
        }
        return false;
    }


    //一系列get set方法


    public static void setLoginType(Integer loginType) {
        StaticConfiguration.loginType = loginType;
    }

    public static Integer getLoginType() {
        return loginType;
    }

    public static Map<String, StockOrder> getStockGoodsOrderCache() {
        return stockGoodsOrderCache;
    }

    public static void setStockGoodsOrderCache(Map<String, StockOrder> stockGoodsOrderCache) {
        StaticConfiguration.stockGoodsOrderCache = stockGoodsOrderCache;
    }

    public static Map<Integer, Goods> getStockOrderGoods() {
        return stockOrderGoods;
    }

    public static void setStockOrderGoods(Map<Integer, Goods> stockOrderGoods) {
        StaticConfiguration.stockOrderGoods = stockOrderGoods;
    }

    public static ExecutorService getPool() {
        return pool;
    }

    public static void setPool(ExecutorService pool) {
        StaticConfiguration.pool = pool;
    }

    public static Map<Integer, Goods> getStockGoodsCache() {
        return stockGoodsCache;
    }

    public static void setStockGoodsCache(Map<Integer, Goods> stockGoodsCache) {
        StaticConfiguration.stockGoodsCache = stockGoodsCache;
    }

    public static List<Goods> getSelectStockGoods() {
        return selectStockGoods;
    }

    public static void setSelectStockGoods(List<Goods> selectStockGoods) {
        StaticConfiguration.selectStockGoods = selectStockGoods;
    }

    public static Map<String, Order> getOrderCache() {
        return orderCache;
    }

    public static void setOrderCache(Map<String, Order> orderCache) {
        StaticConfiguration.orderCache = orderCache;
    }

    public static Map<Integer, Goods> getGoodsCache() {
        return goodsCache;
    }

    public static void setGoodsCache(Map<Integer, Goods> goodsCache) {
        StaticConfiguration.goodsCache = goodsCache;
    }

    public static List<Goods> getSelectGoods() {
        return selectGoods;
    }

    public static void setSelectGoods(List<Goods> selectGoods) {
        StaticConfiguration.selectGoods = selectGoods;
    }

    public static void setCustomer(Customer customer) {
        StaticConfiguration.customer = customer;
    }

    public static Customer getCustomer() {
        return customer;
    }

    public static void setEmploy(Employ employ) {
        StaticConfiguration.employ = employ;
    }

    public static Employ getEmploy() {
        return employ;
    }

    public static void setOrderGoods(List<Goods> orderGoods) {
        StaticConfiguration.orderGoods = orderGoods;
    }

    public static List<Goods> getOrderGoods() {
        return orderGoods;
    }

    //空配置
    public static final StaticConfiguration EMPTY_CONFIG = null;

    public static Font getFont() {
        return font;
    }

    public static void setFont(Font font) {
        StaticConfiguration.font = font;
    }
}
