package com.kof2015.ui;
import java.awt.*;
import javax.swing.*;

public class GridBagDemo extends JFrame {
    public static void main(String args[]) {
        GridBagDemo demo = new GridBagDemo();
    }

    public GridBagDemo() {
        init();
        this.setSize(600,600);
        this.setVisible(true);
    }
    public void init() {
        j1 = new JButton("��");
        j2 = new JButton("����");
        j3 = new JButton("���Ϊ");
        j4 = new JPanel();
        String[] str = { "java�ʼ�", "C#�ʼ�", "HTML5�ʼ�" };
        j5 = new JComboBox(str);
        j6 = new JTextField();
        j7 = new JButton("���");
        j8 = new JList(str);
        j9 = new JTextArea();
        j9.setBackground(Color.PINK);//Ϊ�˿���Ч������������ɫ
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.add(j1);//�������ӽ�jframe
        this.add(j2);
        this.add(j3);
        this.add(j4);
        this.add(j5);
        this.add(j6);
        this.add(j7);
        this.add(j8);
        this.add(j9);
        GridBagConstraints s= new GridBagConstraints();//����һ��GridBagConstraints��
        //������������ӽ����������ʾλ��
        s.fill = GridBagConstraints.BOTH;
        //�÷�����Ϊ���������������ڵ�������������Ҫ��ʱ����ʾ���
        //NONE�������������С��
        //HORIZONTAL���ӿ������ʹ����ˮƽ��������������ʾ���򣬵��ǲ��ı�߶ȡ�
        //VERTICAL���Ӹ������ʹ���ڴ�ֱ��������������ʾ���򣬵��ǲ��ı��ȡ�
        //BOTH��ʹ�����ȫ��������ʾ����
        s.gridwidth=1;//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
        s.weightx = 0;//�÷����������ˮƽ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
        s.weighty=0;//�÷������������ֱ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
        layout.setConstraints(j1, s);//�������
        s.gridwidth=1;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(j2, s);
        s.gridwidth=1;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(j3, s);
        s.gridwidth=0;//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
        s.weightx = 0;//����Ϊ1��j4��ռ��4���񣬲��ҿ��Ժ������죬
        //�������Ϊ1�������е��еĸ�Ҳ���������,����j7���ڵ���Ҳ��������
        //����Ӧ���Ǹ���j6��������
        s.weighty=0;
        layout.setConstraints(j4, s)
        ;s.gridwidth=2;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(j5, s);
        ;s.gridwidth=4;
        s.weightx = 1;
        s.weighty=0;
        layout.setConstraints(j6, s);
        ;s.gridwidth=0;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(j7, s);
        ;s.gridwidth=2;
        s.weightx = 0;
        s.weighty=1;
        layout.setConstraints(j8, s);
        ;s.gridwidth=5;
        s.weightx = 0;
        s.weighty=1;
        layout.setConstraints(j9, s);
    }
    JButton j1;
    JButton j2;
    JButton j3;
    JPanel j4;
    JComboBox j5;
    JTextField j6;
    JButton j7;
    JList j8;
    JTextArea j9;
}