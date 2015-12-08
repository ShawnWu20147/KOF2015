package com.kof2015.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.common.FighterInfo;
import com.common.FighterInstance;
import com.kof2015.server.DBHelper;

public class BattlePanel extends JPanel {
	
	
	private static final long serialVersionUID = -8546653500036463777L;
	
	
	TroopPanel troopLeft,troopRight;
	
	JTextArea infoArea;
	
	public BattlePanel(FighterInstance []self,FighterInstance[] opp) {
		setLayout(new BorderLayout(5, 10));
		JPanel container = new JPanel();
		JLabel timeLabel = new JLabel();
		troopLeft = new TroopPanel(self,true);
		troopRight = new TroopPanel(opp,false);
		
		timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
		timeLabel.setText("99");
		timeLabel.setVerticalAlignment(SwingConstants.TOP);
		timeLabel.setBorder(BorderFactory.createEtchedBorder(Color.LIGHT_GRAY, Color.DARK_GRAY));	
		container.add(troopLeft);
		container.add(timeLabel);
		container.add(troopRight);
		add(container, BorderLayout.CENTER);
		
		infoArea = new JTextArea();
		infoArea.setEditable(false);
		infoArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		infoArea.setPreferredSize(new Dimension(0, 150));
		add(infoArea, BorderLayout.SOUTH);
		
		
	
	}
	
	public void addLog(String a){
		infoArea.append(a);
	}
	
	public void update(FighterInstance []self,FighterInstance[] opp){
		troopLeft.update(self, true);
		troopRight.update(opp, false);
		repaint();
	}
	
	
	
	
	public static void main(String[] args) {
		
		
		DBHelper db1;
		ResultSet ret;
		ArrayList<FighterInfo> all_fighters=new ArrayList<FighterInfo>();
		
		try{
			
			db1 = new DBHelper();
			String query="select * from fighter";
			ret=db1.executeOneQuery(query);
		
				while (ret.next()){
					int id=ret.getInt(1);
					String name=ret.getString(2);
					int ability=ret.getInt(3);
					int fighter_type=ret.getInt(4);
					int base_hp=ret.getInt(5);
					int base_attack=ret.getInt(6);
					int base_defence=ret.getInt(7);
					int base_hit=ret.getInt(8);
					int base_block=ret.getInt(9);
					int base_attack_anger=ret.getInt(10);
					int base_attacked_anger=ret.getInt(11);
					int base_power_anger=ret.getInt(12);
					int base_powered_anger=ret.getInt(13);
					String description=ret.getString(14);
					String skill_name=ret.getString(15);
					String skill_description=ret.getString(16);
					int skill_type=ret.getInt(17);
					double skill_ratio=ret.getDouble(18);
					
					FighterInfo fi=new FighterInfo(id,name,ability,fighter_type,base_hp,base_attack,base_defence,base_hit,base_block
							,base_attack_anger,base_attacked_anger,base_power_anger,base_powered_anger,description,skill_name,skill_description
							,skill_type,skill_ratio);
					
					//if (id==1) System.out.println(fi.toStringHtml());
					all_fighters.add(fi);
					
				}
			
		
		
		
	}catch(Exception e){
		e.printStackTrace();
	}
	FighterInstance []fi_me=new FighterInstance[6];
	
	FighterInstance []fi_opp=new FighterInstance[6];
	
	for (int i=0;i<6;i++){
		fi_me[i]=new FighterInstance(all_fighters.get(i), 0);
		fi_opp[i]=new FighterInstance(all_fighters.get(i+6), 0);
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new BattlePanel(fi_me,fi_opp));
		frame.pack();
		frame.setResizable(false);
		
		frame.setVisible(true);
	}
}
