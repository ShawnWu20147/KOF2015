package com.kof2015.battle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.common.Constants;
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
	
	public JButton attackButton;
	
	public JButton skillButton;
	
	boolean isme;
	
	boolean canAction;
	
	public FighterInstance myfi;
	

	
	public void disableAll(){
		disableAttack();
		disableSkill();
		//disableFace();
	}
	
	public void disableAttack(){
		attackButton.setEnabled(false);
	}
	
	public void disableSkill(){
		skillButton.setEnabled(false);
	}
	
	public void enableAttack(){
		attackButton.setEnabled(true);
	}
	
	public void enableSkill(){
		skillButton.setEnabled(true);
	}
	
	public void disableFace(){
		faceButton.setEnabled(false);
	}
	
	public void enableFace(){
		faceButton.setEnabled(true);
	}
	
	public void addAttackButtonListener(ActionListener l){
		attackButton.addActionListener(l);
	}
	
	public void addSkillButtonListener(ActionListener l){
		skillButton.addActionListener(l);
	}
	
	
	public void updateRage(FighterInstance fi){
		if (fi.anger>=1000){
			skillButton.setEnabled(true);
		}
	}
	
	public void updateHpandRagewithAnimation(FighterInstance fi){
		
		
		
		FighterInstance old=myfi;
		int old_hp=old.hp;
		if (old_hp<=0) return;
		
		
		
		int new_hp=fi.hp;
		
		if (new_hp!=old_hp){
			Color cc=isme?Color.RED:Color.BLUE;
			faceButton.setBorder(BorderFactory.createEtchedBorder(cc, cc));
		}
		
		if (new_hp<=0){
			new_hp=0;
			int gap=new_hp-old_hp;
			int change=gap/25;
			//  gap=100 change =4
			
			int cur=old_hp;
			for (int i=0;i<25;i++){
				cur+=change;
				hpProgress.setValue(cur);
				try {
					Thread.currentThread().sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			hpProgress.setValue(0);
			
			
			isClicked=false;
			faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
			
			String pic_path="img/battler/dead.jpg";
			File f=new File(pic_path);
			if (!f.exists()){
				pic_path="../img/battler/dead.jpg";
			}
			faceButton.setIcon(new ImageIcon(pic_path));
			rageProgress.setValue(0);
			disableAttack();
			disableSkill();
			disableFace();
			myfi=fi;
		}
		
		else{
			int old_anger=old.anger;
			int new_anger=fi.anger;
			if (old_anger>=Constants.MAX_ANGER) old_anger=Constants.MAX_ANGER;
			if (new_anger>=Constants.MAX_ANGER) new_anger=Constants.MAX_ANGER;
			
			
			int gap_hp=new_hp-old_hp;
			int gap_anger=new_anger-old_anger;
			
			int change_hp=gap_hp/25;
			int change_anger=gap_anger/25;
			
			int cur_hp=old_hp;
			int cur_anger=old_anger;
			for (int i=0;i<25;i++){
				cur_hp+=change_hp;
				cur_anger+=change_anger;
				hpProgress.setValue(cur_hp);
				rageProgress.setValue(cur_anger);
				try {
					Thread.currentThread().sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			hpProgress.setValue(new_hp);
			rageProgress.setValue(new_anger);
			myfi=fi;
			
			if (rageProgress.getValue()<rageProgress.getMaximum()){
				skillButton.setEnabled(false);
			}
			
		}
		
		if (new_hp!=old_hp && !isClicked){
			faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
		}
		
		
		
	}
	
	
	public void updateHpandRage(FighterInstance fi){
		this.myfi=fi;
		int hp=fi.hp;
		int rage=fi.anger;
		
		
		
		hpProgress.setValue(hp);
		rageProgress.setValue(rage);
		
		if (rageProgress.getValue()<rageProgress.getMaximum()){
			skillButton.setEnabled(false);
		}
		
		
		if (hp<=0){
			isClicked=false;
			faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
			
			String pic_path="img/battler/dead.jpg";
			File f=new File(pic_path);
			if (!f.exists()){
				pic_path="../img/battler/dead.jpg";
			}
			
			
			faceButton.setIcon(new ImageIcon(pic_path));
			rageProgress.setValue(0);
			
			disableAttack();
			disableSkill();
			disableFace();
		}
		
		repaint();
	}
	
	
	public BattlerPanel(FighterInstance fi,boolean me) {
		this.myfi=fi;
		
		canAction=true;
		
		this.isme=me;
		setLayout(new BorderLayout(5, 5));
		
		JPanel statusPanel = new JPanel();
		{
			statusPanel.setLayout(new GridLayout(0, 1));
			for (int i = 0; i < 4; i++)
			{
				final JLabel stateLabel = new JLabel();
				stateLabel.setPreferredSize(new Dimension(24, 24));
				
				/*
				if ( i < 3) {
					ImageIcon image = new ImageIcon("img/state0" + ( 2 + i ) +".png");
					stateLabel.setIcon(image);
				}
				*/
				
				
				
				if (i==0){
					
					String st_path="img/state00.png";
					File f=new File(st_path);
					if (!f.exists())
						st_path="../img/state00.png";
					
					ImageIcon image = new ImageIcon(st_path);
					
					stateLabel.setIcon(image);
					statusPanel.add(stateLabel);
					
					stateLabel.addMouseListener(new MouseListener() {
						
					
					
					
						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
	
						@Override
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub
							JFrame jf=new JFrame();
							jf.setTitle("¸ñ¶·¼Ò¶îÍâ×´Ì¬");
							jf.setLocationRelativeTo(stateLabel);
							JTextArea jt=new JTextArea();
							jt.setEditable(false);
							jt.setText(myfi.getStateInfo());
							jf.add(jt);
							jf.setSize(400, 300);
							jf.setVisible(true);
						}
					});
				}
				
			}
		}
		add(statusPanel, BorderLayout.EAST);
		
		
		String pic_path="img/battler/" + fi.id + ".jpg";
		File f=new File(pic_path);
		if (!f.exists()){
			pic_path="../img/battler/" + fi.id + ".jpg";
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
				
				hpProgress = new JProgressBar();
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
			infoButton.setText("×´Ì¬");
			
			
			
			
			
			attackButton = new JButton();
			attackButton.setText("¹¥»÷");
			if (!isme){
				attackButton.setEnabled(false);
			}
			
			
			skillButton = new JButton();
			skillButton.setText("´óÕÐ");
			skillButton.setEnabled(false);
			
			controlPanel.add(infoButton);
			controlPanel.add(attackButton);
			controlPanel.add(skillButton);
		}
		add(controlPanel, BorderLayout.SOUTH);
		
		if (!isme)
			infoButton.setEnabled(false);
		
		infoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame jf=new JFrame();
				jf.setTitle("¸ñ¶·¼Ò×´Ì¬");
				JTextArea jt=new JTextArea();
				jt.setEditable(false);
				jt.setText(myfi.getStatus());
				jf.add(jt);
				jf.setLocationRelativeTo(infoButton);
				jf.pack();
				jf.setVisible(true);
				
			}
		});
		
		
		
	
		
		
		
	}
	
	
	
	
	
	public void select()
	{
		faceButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
	}
	
	public void unselect()
	{
		faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
	}

	
	

	public void enableMe() {
		enableAttack();
		enableFace();
		enableSkill();
		
	}

	public void restoreRageEnabled() {
		if (myfi.anger>=Constants.MAX_ANGER)
			enableSkill();
		
	}

}
