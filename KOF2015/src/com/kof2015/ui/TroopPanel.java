package com.kof2015.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class TroopPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309023000694113795L;
	
	public TroopPanel() {
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
		teamPanel.setBorder(BorderFactory.createEtchedBorder());
		{
			teamPanel.add(new BattlerPanel());
			teamPanel.add(new BattlerPanel());
			teamPanel.add(new BattlerPanel());
			teamPanel.add(new BattlerPanel());
			teamPanel.add(new BattlerPanel());
			teamPanel.add(new BattlerPanel());
		}
		add(teamPanel, BorderLayout.CENTER);
	}
}
