package com.zjw.swing.index;

import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.userManager.PathEditJFrame;
import com.zjw.swing.userManager.PathListFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/24 21:55
 */
@Component
public class HeadPathEditFrame extends JFrame {

    //JList传入
    public JTextField path;

    @Autowired
    private PathListFrame pathListFrame;

    @Autowired
    private HeadEditFrame headEditFrame;

    @Autowired
    private EmployIndexFrame employIndexFrame;

    @Autowired
    private CustomerIndexFrame customerIndexFrame;

    @Autowired
    private EmployService employService;

    @Autowired
    private CustomerService customerService;

    public void init() {
        this.setTitle("头像选择");
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void run() {
        this.init();

        JPanel panel = new JPanel(null);
        this.setContentPane(panel);

        //目录
        path = new JTextField();
        path.setBounds(10, 10, 300, 30);
        panel.add(path);

        JButton pathButton = new JButton("目录");
        pathButton.setBounds(320, 10, 60, 30);
        this.add(pathButton);

        JButton okButton = new JButton("确认");
        okButton.setBounds(320, 60, 60, 30);
        this.add(okButton);

        this.setVisible(true);

        pathButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(() -> pathListFrame.run(IndexConstant.PATH_MODE_IMAGE));
        });

        okButton.addActionListener(e -> {
            //diy路径
            String baseUrl = "./images/head";
            File file = new File(baseUrl);
            int index = Objects.requireNonNull(file.listFiles()).length;
            try {
                //获得图片目录
                InputStream fis = new FileInputStream(new File(path.getText()));
                byte[] bytes = new byte[fis.available()];
                //读取data到bytes
                fis.read(bytes);
                String url = baseUrl + "/t" + index + ".jpg";
                File f1 = new File(url);
                if (!f1.exists()) f1.createNewFile();
                OutputStream fos1 = new FileOutputStream(f1);
                //创建文件
                fos1.write(bytes);

                //更新头像
                if (StaticConfiguration.getEmploy() != null) {
                    employIndexFrame.changeImages(f1);
                    StaticConfiguration.getEmploy().setImagesPath(url);
                    employService.update(StaticConfiguration.getEmploy());
                } else {
                    customerIndexFrame.changeImages(f1);
                    StaticConfiguration.getCustomer().setImagesPath(url);
                    customerService.update(StaticConfiguration.getCustomer());
                }

                fis.close();
                fos1.close();
                MessageShows.ShowMessageText(this, null, "上传成功");
                this.setVisible(false);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
