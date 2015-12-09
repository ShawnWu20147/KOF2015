package com.kof2015.battle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

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
	
	int[] me_sx={3,0,4,1,5,2};
	int[] opp_sx={0,3,1,4,2,5};
	
	
	public BattlerPanel[] getBattlerPanel(){
		return bp;
	}
	
	public void update(FighterInstance[] one,boolean self){
		//change bp[]
		
		//call to updateHpandRage
		
		for (int i=0;i<6;i++){
			if (self)
				bp[i].updateHpandRage(one[i]);
			else
				bp[i].updateHpandRage(one[i]);
		}
		
		
		repaint();
	}
	
	public void disableAll(){
		for (int i=0;i<6;i++)
			bp[i].disableAll();
	}
	
	
	
	public TroopPanel(FighterInstance[] one,boolean self,ActionListener attackListener,ActionListener skillListener) {
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
		
		bp=new BattlerPanel[6];
		for (int i=0;i<6;i++){
			bp[i]=new BattlerPanel(one[i],self);
			bp[i].addAttackButtonListener(attackListener);
			bp[i].addSkillButtonListener(skillListener);
			
		}
		
		// bp[0] bp[1]
		// bp[2] bp[3]
		// bp[4] bp[5]
		
		//one[3]  one[0]
		//one[4]  one[1]
		//one[5]  one[2]
		
		// one[0] one[3]
		// one[1] one[4]
		// one[2] one[5]
		
		
		teamPanel.setBorder(BorderFactory.createEtchedBorder());
		
		if (self){
			for (int i=0;i<6;i++){
				teamPanel.add(bp[ me_sx[i]]);
			}
		}
		else{
			for (int i=0;i<6;i++){
				teamPanel.add(bp[ opp_sx[i]]);
			}
		}
		
		
		
		add(teamPanel, BorderLayout.CENTER);
	}

	public void enableMe() {
		for (int i=0;i<6;i++)
			bp[i].enableMe();
		
	}

	public void disableMyPower() {
		for (int i=0;i<6;i++)
			bp[i].disableSkill();
		
	}

	public void disableMyNormal(int i_info2) {
		bp[i_info2].disableAttack();
		
	}
	
	
	
}
