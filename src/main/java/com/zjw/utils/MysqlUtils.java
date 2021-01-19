package com.zjw.utils;

import com.zjw.swing.message.MessageShows;

import java.awt.*;
import java.io.File;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/19 14:48
 */
public class MysqlUtils {

    //备份
    public static void dump(String host, String port, String username, String password, String dataBaseName, String path, String sqlName, Component component)
            throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File datafile = new File(path + File.separator + sqlName + ".sql");
        if (datafile.exists()) {
            MessageShows.ShowMessageText(component, null, "文件已存在，请检查");
            return;
        }
        //拼接cmd命令
        Process exec = Runtime.getRuntime().exec("cmd /c mysqldump --column-statistics=0 -h" + host +
                " -P" + port + " -u" + username + " -p" + password + " " + dataBaseName + " > " + datafile);

        if (exec.waitFor() == 0) {
            MessageShows.ShowMessageText(component, null, "数据库备份成功，备份路径为：" + datafile.getPath());
        } else {
            MessageShows.ShowMessageText(component, null, "备份失败");
        }
    }


    //还原
    public static void backup(String host, String port, String username, String password, String dataBaseName, String path, Component component)
            throws Exception {
        File datafile = new File(path);
        if (!datafile.exists()) {
            MessageShows.ShowMessageText(component, null, "文件不已存在，请检查");
            return;
        }
        //拼接cmd命令
        Process exec = Runtime.getRuntime().exec("cmd /c mysql -h" + host +
                " -P" + port + " -u" + username + " -p" + password + " " + dataBaseName + " < " + datafile);
        if (exec.waitFor() == 0) {
            MessageShows.ShowMessageText(component, null, "数据库还原成功，还原的文件为：" + datafile);
        }
    }
}
