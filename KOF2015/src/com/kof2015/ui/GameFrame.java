package com.kof2015.ui;
import javax.swing.JFrame;

public class GameFrame {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new BattlePanel());
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
