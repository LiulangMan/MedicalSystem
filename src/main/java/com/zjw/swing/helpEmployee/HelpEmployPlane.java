package com.zjw.swing.helpEmployee;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Question;
import com.zjw.service.QuestionService;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:09
 */
@Component
public class HelpEmployPlane extends ImageJPanel {

    private DefaultJTable questionTable;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HelpQuestionEditFrame helpQuestionEditFrame;

    public HelpEmployPlane() {
        super(null, "/images/index/t7.jpg");
    }

    public void init() {

        //问题表
        questionTable = new DefaultJTable(new Object[]{"ID", "问题", "提问时间", "回答", "回答时间", "状态"}, new DefaultTableModel());
        questionTable.getJScrollPane().setBounds(0, 0, 1200, 600);

        this.add(questionTable.getJScrollPane());

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem answerButton = new JMenuItem("回答问题");
        JMenuItem deleteButton = new JMenuItem("删除问题");
        jPopupMenu.add(answerButton);

        if (StaticConfiguration.getLoginType() == IndexConstant.LOGIN_TYPE_ADMIN) {
            jPopupMenu.add(deleteButton);
        }

        //搜索栏
        JLabel searchF = new JLabel("问题");
        searchF.setSize(80, 30);
        searchF.setLocation(10, 600);
        this.add(searchF);

        JTextField searchField = new JTextField();
        searchField.setSize(300, 30);
        searchField.setLocation(100, 600);
        this.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.setSize(100, 30);
        searchButton.setLocation(400, 600);
        this.add(searchButton);

        JTextField baiduText = new JTextField();
        baiduText.setSize(200, 30);
        baiduText.setLocation(780, 700);
        this.add(baiduText);

        JButton urlButton = new JButton("百度一下");
        urlButton.setSize(100, 30);
        urlButton.setLocation(1000, 700);
        this.add(urlButton);


        /*监听*/
        questionTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown()) {
                    jPopupMenu.show(questionTable, e.getX(), e.getY());
                } else if (e.getClickCount() == 2) {
                    int row = questionTable.getSelectedRow();
                    String questionText = (String) questionTable.getValueAt(row, 1);
                    String answerText = (String) questionTable.getValueAt(row, 3);
                    MessageShowByText.show(questionText, answerText, FontConfiguration.getFont("宋体", 25));
                }
            }
        });

        answerButton.addActionListener(e -> {
            Integer id = (Integer) questionTable.getValueAt(questionTable.getSelectedRow(), 0);
            Question question = questionService.queryOneById(id);
            StaticConfiguration.addThreadPoolTask(() -> helpQuestionEditFrame.run(question));
        });

        deleteButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认删除选中问题？");
            if (!b) return;

            int[] rows = questionTable.getSelectedRows();
            for (int row : rows) {
                questionService.deleteQuestionById((Integer) questionTable.getValueAt(row, 0));
            }
            MessageShows.ShowMessageText(this, null, "删除成功");
            this.refreshData();
        });

        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            if (text.equals("")) {
                this.refreshData();
                return;
            }
            List<Question> questions = questionService.queryAllByQuestion(text);
            questionTable.refreshData(DataUtils.QuestionToArray(questions));
        });

        urlButton.addActionListener(e -> {
            try {
                URI uri = URI.create("https://www.baidu.com/s?wd=" + baiduText.getText());
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                } else {
                    MessageShows.ShowMessageText(this, "", "不支持浏览器");
                }

            } catch (Exception e1) {
                MessageShows.ShowMessageText(this, "", e1.toString());
            }
        });
    }

    public void refreshData() {
        List<Question> questions = questionService.queryAll();
        questionTable.refreshData(DataUtils.QuestionToArray(questions));
    }
}
