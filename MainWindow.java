package com.company;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(370, 390);
        setLocation(1500,400);
        setResizable(false);
        add(new GameField());       // добавляем игровое поле
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
    }

}
