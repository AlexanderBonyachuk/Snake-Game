package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;    // сколько в пикселях занимает одна секция змейки и яблочка
    private final int ALL_DOTS = 400;
    private Image dot;                  // картинка секция змеи
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];    // массив для позиции Х змейки
    private int[] y = new int[ALL_DOTS];    // массив для позиции Y змейки
    private int dots;                       // текущий размер змейки
    private Timer timer = new Timer(250,this);  // частота 250, this ссылка на текущий класс GameField
    private boolean left = false;           // необязательно инициализировать переменные, т.к. boolean при объявлении по умолчанию присваивается false
    private boolean right = true;           // изначально змейка движется направо
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.BLACK);     // черное игровое поле
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());     // добавляем свой обработчик нажатия клавиш
        setFocusable(true);
    }

    public void initGame() {
        dots = 3;                       // стартовый размер змеи
        for (int i =0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;   // стартовое значение для Х позиции
            y[i] = 48;
        }
        timer.start();
        createApple();
    }

    public void createApple() {
        ThreadLocalRandom random = ThreadLocalRandom.current();     // поток для рандомных чисел (java 11 и выше)
        appleX = random.nextInt(20) * DOT_SIZE;          // получение случайного числа 0-19 квадратиков 20*16=320 -SIZE
        appleY = random.nextInt(20) * DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();         // присваиваем переменной яблоко картинку
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();          // присваиваем переменной клетка змеи картинку
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);                   // основную механику по перерисовке берет класс родитель
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);   // перерисовка яблока
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);    // перерисовка змейки
            }
        } else {
            String str = "GAME OVER!";
            Font f = new Font("Impact", 2, 50);
            g.setColor(Color.GREEN);
            g.setFont(f);
            g.drawString(str, 50, SIZE/2);
        }
    }

    public void move() {
        // перемещение тела змейки:
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // перемещение головы змейки:
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void chekApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;             // змейка растет при поедании яблока
            createApple();      // создаем новое яблоко
        }
    }

    public void chekCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;         // если змейка столкнулась сама с собой конец игры
            }
        }
        if (x[0] > SIZE || x[0] < 0 || y[0] > SIZE || y[0] < 0) {
            inGame = false;         // если змейка столкнулась с краем поля конец игры
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            chekApple();         // проверка на поедание яблока
            chekCollisions();    // проверка на столкновение с границами
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            // меняем направление движения на лево:
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
            // рестарт с Enter-а
            if (key == KeyEvent.VK_ENTER) {
                inGame = true;
                right = true;
                left = false;
                up = false;
                down = false;
                initGame();
            }
        }
    }
}
