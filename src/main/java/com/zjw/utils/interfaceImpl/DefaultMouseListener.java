package com.zjw.utils.interfaceImpl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 16:08
 */
public interface DefaultMouseListener extends MouseListener {
    @Override
    default void mouseClicked(MouseEvent e){}

    @Override
    default void mousePressed(MouseEvent e){}

    @Override
    default void mouseReleased(MouseEvent e){}

    @Override
    default void mouseEntered(MouseEvent e){}

    @Override
    default void mouseExited(MouseEvent e){}
}
