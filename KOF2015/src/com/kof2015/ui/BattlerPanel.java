package com.kof2015.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.common.FighterInstance;

public class BattlerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7725867065586415009L;
	
	JButton faceButton = null;
	
	
	boolean isClicked;
	
	
	JProgressBar rageProgress,hpProgress;
	
	JPanel progressPanel;
	
	JPanel charPanel;
	
	JPanel controlPanel;
	
	JButton infoButton;
	
	JButton attackButton;
	
	JButton skillButton;
	
	boolean isme;
	
	public void updateHpandRage(int hp,int rage){
		hpProgress.setValue(hp);
		rageProgress.setValue(rage);
		
		
		
		
		if (hp<=0){
			
			String pic_path="img/battler/dead.jpg";
			File f=new File(pic_path);
			if (!f.exists()){
				pic_path="../img/battler/dead.jpg";
			}
			
			
			faceButton.setIcon(new ImageIcon(pic_path));
			rageProgress.setValue(0);
		}
		
		repaint();
	}
	
	
	public BattlerPanel(FighterInstance fi,boolean me) {
		this.isme=me;
		setLayout(new BorderLayout(5, 5));
		
		JPanel statusPanel = new JPanel();
		{
			statusPanel.setLayout(new GridLayout(0, 1));
			for (int i = 0; i < 4; i++)
			{
				JLabel stateLabel = new JLabel();
				stateLabel.setPreferredSize(new Dimension(24, 24));
				
				/*
				if ( i < 3) {
					ImageIcon image = new ImageIcon("img/state0" + ( 2 + i ) +".png");
					stateLabel.setIcon(image);
				}
				*/
				statusPanel.add(stateLabel);
			}
		}
		add(statusPanel, BorderLayout.EAST);
		
		
		String pic_path="img/battler/" + fi.base.id + ".jpg";
		File f=new File(pic_path);
		if (!f.exists()){
			pic_path="../img/battler/" + fi.base.id + ".jpg";
		}
		
		
		charPanel = new JPanel();
		charPanel.setLayout(new BorderLayout(5, 5));
		
		isClicked=false;
		
		
		
		{
			faceButton = new JButton();
			faceButton.setIcon(new ImageIcon(pic_path));
			faceButton.setPreferredSize(new Dimension(100, 120));
			faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
			charPanel.add(faceButton, BorderLayout.CENTER);
			
			faceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//System.out.println("button clicked: " + faceButton.hashCode());
					if (!isClicked){
						isClicked=true;
						Color cc=isme?Color.RED:Color.BLUE;
						faceButton.setBorder(BorderFactory.createEtchedBorder(cc, cc));
					}
					else{
						isClicked=false;
						faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
					}
				}
			});
			
			progressPanel = new JPanel();
			progressPanel.setLayout(new GridLayout(0, 1, 2, 2));
			{
				rageProgress = new JProgressBar();
				rageProgress.setForeground(Color.YELLOW);
				rageProgress.setMaximum(1000);
				rageProgress.setValue(fi.anger);
				
				JProgressBar hpProgress = new JProgressBar();
				hpProgress.setForeground(Color.RED);
				hpProgress.setMaximum(fi.max_hp);
				hpProgress.setValue(fi.hp);
				
				progressPanel.add(hpProgress);
				progressPanel.add(rageProgress);
			}
			charPanel.add(progressPanel, BorderLayout.SOUTH);
		}
		add(charPanel, BorderLayout.CENTER);
		
		controlPanel = new JPanel();
		{
			infoButton = new JButton();
			infoButton.setText("¼ò½é");
			
			attackButton = new JButton();
			attackButton.setText("¹¥»÷");
			
			skillButton = new JButton();
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
