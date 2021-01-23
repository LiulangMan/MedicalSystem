package com.zjw.utils;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/23 18:22
 */
public class DefaultSwingWork<T, K> extends SwingWorker<T, K> {

    private List<Method> methods = new ArrayList<>();

    private List<Object> objs = new ArrayList<>();

    private List<Object[]> args = new ArrayList<>();

    @Override
    protected void done() {
        super.done();
    }

    @Override
    protected T doInBackground() throws Exception {
        for (int i = 0; i < methods.size(); i++) {
            methods.get(i).invoke(objs.get(i), args.get(i));
        }
        return null;
    }

    public void addTask(Object obj, Method method, String... arg) {
        methods.add(method);
        objs.add(obj);
        args.add(arg);
    }
}
