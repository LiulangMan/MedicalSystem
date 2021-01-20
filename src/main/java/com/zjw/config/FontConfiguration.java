package com.zjw.config;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/20 11:38
 */
public class FontConfiguration {
    //字体大小从10~40
    private static java.util.List<Font> defaultFontList = new ArrayList<>(30);
    private static java.util.List<Font> songFontList = new ArrayList<>(30);
    private static java.util.List<Font> kaiFontList = new ArrayList<>(30);
    private static Map<String, List<Font>> fontMap = new HashMap<>();

    static {
        for (int i = 0; i < 30; i++) {
            defaultFontList.add(new Font(null, Font.PLAIN, 10 + i));
            songFontList.add(new Font("宋体", Font.PLAIN, 10 + i));
            kaiFontList.add(new Font("楷体", Font.PLAIN, 10 + i));
        }

        fontMap.put("default", defaultFontList);
        fontMap.put("宋体", songFontList);
        fontMap.put("楷体", kaiFontList);
    }


    public static void initGlobalFont(String name, int size) {
        List<Font> fonts = fontMap.get(name);
        if (fonts == null) {
            fonts = defaultFontList;
        }
        if (size < 10 || size > 39) {
            throw new ArrayIndexOutOfBoundsException("size<10 || size >39");
        }

        Font f = fonts.get(size - 10);
        FontUIResource fontRes = new FontUIResource(f);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }

    }

    public static Font getFont(String name, int size) {
        List<Font> fonts = fontMap.get(name);
        if (fonts == null) {
            fonts = defaultFontList;
        }
        if (size < 10 || size > 39) {
            throw new ArrayIndexOutOfBoundsException("size<10 || size >39");
        }

        return fonts.get(size - 10);
    }
}
