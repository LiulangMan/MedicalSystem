package com.zjw.swing.userManager;

import com.zjw.swing.message.MessageShows;
import com.zjw.utils.MysqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/19 15:38
 */
@Component
public class PathEditJFrame extends JFrame {

    @Autowired
    private PathListFrame pathListFrame;

    //JList传入
    JTextField path;

    private void init() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("数据备份目录");
    }

    public void run(boolean isDump) {

        init();

        JPanel panel = new JPanel(null);
        this.setContentPane(panel);

        //目录
        path = new JTextField();
        path.setBounds(10, 10, 300, 30);
        panel.add(path);

        //文件
        JTextField file = new JTextField();
        file.setBounds(10, 60, 300, 30);
        if (isDump) panel.add(file);


        JButton pathButton = new JButton("目录");
        pathButton.setBounds(320, 10, 60, 30);
        this.add(pathButton);

        JButton okButton = new JButton("确认");
        okButton.setBounds(320, 60, 60, 30);
        this.add(okButton);

        this.setVisible(true);

        /*监听*/
        pathButton.addActionListener(e -> {
            pathListFrame.run();
        });

        okButton.addActionListener(e -> {
            String pathText = path.getText();
            String fileText = file.getText();
            try {
                //备份
                if (isDump) {
                    boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认备份？");
                    if (!b) return;
                    MysqlUtils.dump("zjw.life", "3310", "root", "root", "MedicalSystem",
                            pathText, fileText, this);
                } else {
                    boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认恢复？");
                    if (!b) return;
                    MysqlUtils.backup("zjw.life", "3310", "root", "root", "MedicalSystem",
                            pathText, this);
                }
                this.setVisible(false);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        new PathEditJFrame().run(true);
    }
}
