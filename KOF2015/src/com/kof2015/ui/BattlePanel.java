package com.kof2015.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BattlePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8546653500036463777L;
	
	public BattlePanel() {
		JLabel timeLabel = new JLabel();
		TroopPanel troopLeft = new TroopPanel();
		TroopPanel troopRight = new TroopPanel();
		
		timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
		timeLabel.setText("99");
		timeLabel.setVerticalAlignment(SwingConstants.TOP);
		timeLabel.setBorder(BorderFactory.createEtchedBorder(Color.LIGHT_GRAY, Color.DARK_GRAY));
		
		add(troopLeft);
		add(timeLabel);
		add(troopRight);
		
//		GridBagLayout layout = new GridBagLayout();
//		setLayout(layout);
//		add(troopLeft);
//		add(timeLabel);
//		add(troopRight);
//		
//		GridBagConstraints s= new GridBagConstraints();
//		s.fill = GridBagConstraints.BOTH;
//		s.gridwidth = 5;
//		s.weightx = 0;
//		s.weighty = 0;
//		layout.setConstraints(troopLeft, s);
//		layout.setConstraints(troopRight, s);
//		
//		s.gridwidth = 6;
//		layout.setConstraints(timeLabel, s);
	}
}
