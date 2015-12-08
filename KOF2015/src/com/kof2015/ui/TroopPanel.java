package com.kof2015.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.common.FighterInstance;

public class TroopPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309023000694113795L;
	
	BattlerPanel []bp;
	
	int[] me_sx={4,1,5,2,6,3};
	int[] opp_sx={1,4,2,5,3,6};
	
	
	public BattlerPanel[] getBattlerPanel(){
		return bp;
	}
	
	public void update(FighterInstance[] one,boolean self){
		//change bp[]
		
		//call to updateHpandRage
		
		repaint();
	}
	
	
	public TroopPanel(FighterInstance[] one,boolean self) {
		setLayout(new BorderLayout(10, 10));
		
		JProgressBar hpProgress = new JProgressBar();
		hpProgress.setPreferredSize(new Dimension(280, 42));
		hpProgress.setStringPainted(true);
		hpProgress.setValue(35);
		hpProgress.setString("17000/29999");
		hpProgress.setForeground(Color.RED);
		// add(hpProgress, BorderLayout.NORTH);
		
		JPanel teamPanel = new JPanel();
		teamPanel.setLayout(new GridLayout(3, 2, 10, 10));
		
		bp=new BattlerPanel[7];
		for (int i=1;i<=6;i++){
			bp[i]=new BattlerPanel(one[i-1],self);
		}
		

		
		
		
		teamPanel.setBorder(BorderFactory.createEtchedBorder());
		
		if (self){
			for (int i=1;i<=6;i++){
				teamPanel.add(bp[ me_sx[i-1]]);
			}
		}
		else{
			for (int i=1;i<=6;i++){
				teamPanel.add(bp[ opp_sx[i-1]]);
			}
		}
		
		
		
		add(teamPanel, BorderLayout.CENTER);
	}
	
	
	
}
