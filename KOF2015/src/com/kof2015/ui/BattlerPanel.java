package com.kof2015.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BattlerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7725867065586415009L;
	
	private JButton faceButton = null;
	
	public BattlerPanel() {
		setLayout(new BorderLayout(5, 5));
		
		JPanel statusPanel = new JPanel();
		{
			statusPanel.setLayout(new GridLayout(0, 1));
			for (int i = 0; i < 4; i++)
			{
				JLabel stateLabel = new JLabel();
				stateLabel.setPreferredSize(new Dimension(24, 24));
				
				if ( i < 3) {
					ImageIcon image = new ImageIcon("img/state0" + ( 2 + i ) +".png");
					stateLabel.setIcon(image);
				}
				statusPanel.add(stateLabel);
			}
		}
		add(statusPanel, BorderLayout.EAST);
		
		JPanel charPanel = new JPanel();
		charPanel.setLayout(new BorderLayout(5, 5));
		{
			faceButton = new JButton();
			faceButton.setIcon(new ImageIcon("img/battler/" + ((int)(Math.random() * 34)) + ".jpg"));
			faceButton.setPreferredSize(new Dimension(100, 120));
			faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
			charPanel.add(faceButton, BorderLayout.CENTER);
			
			faceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("button clicked: " + faceButton.hashCode());
					faceButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
				}
			});
			
			JPanel progressPanel = new JPanel();
			progressPanel.setLayout(new GridLayout(0, 1, 2, 2));
			{
				JProgressBar rageProgress = new JProgressBar();
				rageProgress.setForeground(Color.YELLOW);
				rageProgress.setValue((int) (Math.random() * 100));
				
				JProgressBar hpProgress = new JProgressBar();
				hpProgress.setForeground(Color.RED);
				hpProgress.setValue((int) (Math.random() * 100));
				
				progressPanel.add(hpProgress);
				progressPanel.add(rageProgress);
			}
			charPanel.add(progressPanel, BorderLayout.SOUTH);
		}
		add(charPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		{
			JButton infoButton = new JButton();
			infoButton.setText("¼ò½é");
			
			JButton attackButton = new JButton();
			attackButton.setText("¹¥»÷");
			
			JButton skillButton = new JButton();
			skillButton.setText("´óÕÐ");
			
			controlPanel.add(infoButton);
			controlPanel.add(attackButton);
			controlPanel.add(skillButton);
		}
		add(controlPanel, BorderLayout.SOUTH);
	}
	
	public void select()
	{
		faceButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
	}
	
	public void unselect()
	{
		faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
	}
}
