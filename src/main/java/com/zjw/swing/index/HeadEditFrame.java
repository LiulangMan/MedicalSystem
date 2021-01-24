package com.zjw.swing.index;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Customer;
import com.zjw.domain.Employ;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/24 13:42
 */
@Component
public class HeadEditFrame extends JFrame {

    @Autowired
    private EmployService employService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployIndexFrame employIndexFrame;

    @Autowired
    private CustomerIndexFrame customerIndexFrame;

    @Autowired
    private HeadPathEditFrame headPathEditFrame;

    private JPanel panel;

    private int current = 0;

    private int total;

    private java.util.List<java.awt.Component> componentList = new ArrayList<java.awt.Component>();

    public void init() {
        this.setTitle("头像选择");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        total = Objects.requireNonNull(new File(".\\src\\main\\resources\\images\\head").listFiles()).length;
    }

    public void run() {

        this.init();

        panel = new JPanel(null);
        this.setContentPane(panel);

        JButton lastButton = new JButton("上一页");
        lastButton.setBounds(100, 610, 100, 30);
        this.add(lastButton);

        JButton nextButton = new JButton("下一页");
        nextButton.setBounds(250, 610, 100, 30);
        this.add(nextButton);

        JButton diyButton = new JButton("上传头像");
        diyButton.setBounds(400, 610, 100, 30);
        this.add(diyButton);

        //加载图片
        this.refreshData(9, 3, current);

        this.setVisible(true);

        nextButton.addActionListener(e -> {
            if (this.current + 9 < total) {
                this.current += 9;
            }
            refreshData(9, 3, this.current);
        });

        lastButton.addActionListener(e -> {
            this.current = Math.max(this.current - 9, 0);
            refreshData(9, 3, this.current);
        });

        diyButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(() -> headPathEditFrame.run());
        });

    }

    public void refreshData(int pageTotal, int len, int next) {

        if (next >= total) return;

        //刷新
        panel.setVisible(false);

        for (java.awt.Component component : componentList) {
            panel.remove(component);
        }
        componentList.clear();
        for (int i = 0; i < pageTotal && (i + next) < total; i++) {
            ImageJPanel m = new ImageJPanel(null, "/images/head/t" + (i + next) + ".jpg");
            int x = i % len;
            int y = i / len;
            m.setBounds(x * 200, y * 200, 200, 200);
            panel.add(m);
            componentList.add(m);

            JFrame temp = this;
            /*监听*/
            m.addMouseListener(new DefaultMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String imagePath = m.getImagePath();
                    if (StaticConfiguration.getEmploy() != null) {
                        employIndexFrame.changeImages(imagePath);
                        Employ employ = StaticConfiguration.getEmploy();
                        employ.setImagesPath(imagePath);
                        employService.update(employ);
                    } else {
                        customerIndexFrame.changeImages(imagePath);
                        Customer customer = StaticConfiguration.getCustomer();
                        customer.setImagesPath(imagePath);
                        customerService.update(customer);
                    }
                    temp.setVisible(false);
                    temp.dispose();
                }
            });
        }

        panel.setVisible(true);
    }

    public static void main(String[] args) {
        new HeadEditFrame().run();
    }

}
