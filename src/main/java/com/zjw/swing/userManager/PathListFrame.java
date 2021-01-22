package com.zjw.swing.userManager;

import com.zjw.swing.utils.PathJButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.Vector;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/19 19:35
 */
@Component
public class PathListFrame extends JFrame {

    private JList<PathJButton> list;

    private JTextField path;

    private PathJButton currentPath;

    private JComboBox<String> listType;

    @Autowired
    private PathEditJFrame pathEditJFrame;

    private void init() {
        this.setSize(550, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void run() {

        init();

        JPanel panel = new JPanel(null);
        this.setContentPane(panel);

        //目录列表
        list = new JList<>();
        list.setFont(new Font(null, Font.PLAIN, 15));
        JScrollPane comp = new JScrollPane(list);
        comp.setBounds(20, 20, 400, 400);
        this.add(comp);


        //路径显示
        path = new JTextField();
        path.setEditable(false);
        path.setBounds(20, 430, 400, 30);
        this.add(path);

        //根目录
        JButton rootPathButton = new JButton("根目录");
        rootPathButton.setBounds(440, 20, 80, 30);
        this.add(rootPathButton);


        //上一级
        JButton lastPathButton = new JButton("上一级");
        lastPathButton.setBounds(440, 70, 80, 30);
        this.add(lastPathButton);

        //文件选择
        listType = new JComboBox<>(new String[]{"Directory", ".*", ".sql", ".xml", ".doc"});
        listType.setBounds(440, 350, 80, 30);
        this.add(listType);

        //确认
        JButton okButton = new JButton("确认");
        okButton.setBounds(440, 400, 80, 30);
        this.add(okButton);

        //初始化根目录
        this.refreshRoots();


        this.setVisible(true);

        /*监听*/
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                PathJButton value = list.getSelectedValue();
                refreshList(value);
            }
        });

        rootPathButton.addActionListener(e -> {
            refreshRoots();
        });

        lastPathButton.addActionListener(e -> {
            PathJButton parentButton = currentPath.parentButton;
            if (parentButton == null) {
                //上一级是根目录
                refreshRoots();
                return;
            }
            refreshList(parentButton);
        });

        okButton.addActionListener(e -> {
            String result = path.getText() + (list.getSelectedValue() == null ? "" : File.separator + list.getSelectedValue().file.getName());
            pathEditJFrame.path.setText(result);
            this.setVisible(false);
            this.dispose();
        });
    }


    private void refreshList(PathJButton pathJButton) {

        if (pathJButton == null) return;

        //记录当前路径
        this.currentPath = pathJButton;

        File[] files = pathJButton.file.listFiles();

        if (files == null) {
            return;
        }

        Vector<PathJButton> vector = new Vector<>();

        String fileType = (String) listType.getSelectedItem();
        Objects.requireNonNull(fileType);
        if (fileType.equals(".*")) {
            fileType = "";
        }

        for (File file : files) {
            if (file.isDirectory() || file.getName().endsWith(fileType)) {
                PathJButton button = new PathJButton(file.getName());
                button.file = file;
                button.parentButton = pathJButton;
                vector.add(button);
            }
        }

        //刷新
        list.setListData(vector);
        path.setText(pathJButton.file.getPath());
    }

    private void refreshRoots() {
        //添加根目录
        Vector<PathJButton> roots = new Vector<>();
        for (File file : File.listRoots()) {
            PathJButton button = new PathJButton(file.getName());
            button.file = file;
            button.parentButton = null;
            button.addActionListener(e -> {
                refreshList(button);
            });
            roots.add(button);
        }

        list.setListData(roots);
        path.setText("");
    }

    public static void main(String[] args) {
        new PathListFrame().run();
    }
}
