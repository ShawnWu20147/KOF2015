package com.kof2015.battle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.common.FighterInfo;
import com.common.FighterInstance;
import com.kof2015.client.ClientOneGame;
import com.kof2015.server.DBHelper;

public class BattlePanel extends JPanel {
	
	
	private static final long serialVersionUID = -8546653500036463777L;
	
	
	TroopPanel troopLeft,troopRight;
	
	JTextArea infoArea;
	
	JButton end_my_turn;
	
	public BattlerPanel []my_p;
	BattlerPanel []opp_p;
	

	ClientOneGame tcog;
	
	int what_i;
	
	JLabel timeLabel;
	
	public void setWhatI(int what){
		this.what_i=what;
	}
	
	
	public void disableAll(){
		troopLeft.disableAll();
		troopRight.disableAll();
		end_my_turn.setEnabled(false);
	}
	
	
	
	public BattlePanel(ClientOneGame cog,FighterInstance []self,FighterInstance[] other) {
		this.tcog=cog;
		setLayout(new BorderLayout(5, 10));
		JPanel container = new JPanel();
		timeLabel = new JLabel();
		
		ActionListener attack_l=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JButton jb=(JButton) arg0.getSource();
				int who_attack=0;
				for (int i=0;i<6;i++)
					if (my_p[i].attackButton==jb){
						who_attack=i;
						break;
					}
				int count=0;
				int who_attacked=0;
				for (int i=0;i<6;i++){
					if (opp_p[i].isClicked){
						count++;
						who_attacked=i;
					}
				}
				if (count!=1){
					JOptionPane.showMessageDialog(null, "攻击且只1人!", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				//very who_attacked correct
				
				
				if (who_attacked>=3){
					if (opp_p[0].myfi.hp>0 || opp_p[1].myfi.hp>0 || opp_p[2].myfi.hp>0){
						JOptionPane.showMessageDialog(null, "必须先攻击前排角色!", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
				
				for (int i=0;i<6;i++)
					my_p[i].disableSkill();
				
				// who_attack -> who->attacked
				tcog.verify_attack_actions(who_attack,who_attacked);
				
				
			
				
			}
		};
		
		ActionListener skill_l=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JButton jb=(JButton) arg0.getSource();
				int who_attack=0;
				for (int i=0;i<6;i++){
					if (my_p[i].skillButton==jb){
						who_attack=i;
						break;
					}
				}
				
				System.out.println(who_attack+" "+my_p[who_attack].myfi.name+"放大招了");
				
				int count=0;
				int who_attacked=0;
				for (int i=0;i<6;i++){
					if (opp_p[i].isClicked){
						count++;
						who_attacked=i;
					}
				}
				FighterInstance fi=my_p[who_attack].myfi;
				assert(fi.anger>=1000);
				int skill_type=fi.skill_type_i;
				switch(skill_type){
				case 0:
					//列杀
					if (count>=3){
						JOptionPane.showMessageDialog(null, "请选最多2个横排角色!", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (count==0){
						JOptionPane.showMessageDialog(null, "请至少选择1个角色决定一列!", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (count==1){
						if (who_attacked>2 && opp_p[who_attacked-3].myfi.hp<=0){
							boolean valid=true;
							for (int i=0;i<3;i++){
								if (opp_p[i].myfi.hp>0) valid=false;
							}
							if (!valid){
								JOptionPane.showMessageDialog(null, "此格斗家前面人已挂了,但是前排仍然有人，无法进行列杀!", "提示", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
					}
					if (count==2){
						if (who_attacked<=2){
							if ( opp_p[who_attacked+3].isClicked!=true){
								JOptionPane.showMessageDialog(null, "必选选择2个同列格斗家!", "提示", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
						else{
							if ( opp_p[who_attacked-3].isClicked!=true){
								JOptionPane.showMessageDialog(null, "必选选择2个同列格斗家!", "提示", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
					}
					break;
				case 1:
					//单体
					if (count!=1){
						JOptionPane.showMessageDialog(null, "请选择一个前排角色!", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (who_attacked>2){
						if (opp_p[0].myfi.hp>0 || opp_p[1].myfi.hp>0 || opp_p[2].myfi.hp>0){
							JOptionPane.showMessageDialog(null, "前排角色没有死绝,无法选择一个后排角色!", "提示", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
					break;
				case 2:
					//of course succ
					//AOE
					break;
				case 3:
					//of course succ
					//全体加血
					break;
				case 4:
					//of course succ
					//前排全杀
					break;
				case 5:
					//of course succ
					//后排全杀
					break;
				case 6:
					//杀伤 后排单人
					if (count!=1){
						JOptionPane.showMessageDialog(null, "请选择一个后排角色!", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (who_attacked<=2 && (opp_p[3].myfi.hp>0 || opp_p[4].myfi.hp>0 || opp_p[5].myfi.hp>0)){
						JOptionPane.showMessageDialog(null, "后排未死光,请选择一个后排角色!", "提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					break;
				case 7:
					//of course succ
					//随机三人杀(连续判定3次)
					break;
				}
				
				
				
				tcog.verify_skill_actions(who_attack,who_attacked);
				
				
			}
		};		
		
		
		troopLeft = new TroopPanel(self,true,attack_l,skill_l);
		troopRight = new TroopPanel(other,false,attack_l,skill_l);
		
		
		
		timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
		timeLabel.setText("99");
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setVerticalAlignment(SwingConstants.TOP);
		timeLabel.setBorder(BorderFactory.createEtchedBorder(Color.LIGHT_GRAY, Color.DARK_GRAY));	
		container.add(troopLeft);
		
		JPanel jp_tmp=new JPanel();
		jp_tmp.setLayout(new GridLayout(2, 1));
		jp_tmp.add(timeLabel);
		
		end_my_turn=new JButton("结束回合");
		jp_tmp.add(end_my_turn);
		
		container.add(jp_tmp);
		container.add(troopRight);
		add(container, BorderLayout.CENTER);
		
		
		
		infoArea = new JTextArea();
		infoArea.setEditable(false);
		infoArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		infoArea.setPreferredSize(new Dimension(0, 150));
		JScrollPane jp=new JScrollPane(infoArea);
		jp.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		add(jp, BorderLayout.SOUTH);
		
		
		end_my_turn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tcog.end_my_turn();
				
			}
		});
		
		
		my_p=troopLeft.getBattlerPanel();
		opp_p=troopRight.getBattlerPanel();
	
	}
	
	public void addLog(String a){
		
		infoArea.append(a);
	}
	
	public void update(FighterInstance []self,FighterInstance[] opp,boolean some_damage,int atk_index,int who_atk){
		if (!some_damage){
			troopLeft.update(self, true);
			troopRight.update(opp, false);
			repaint();
		}
		else{
			//hehe
			if (who_atk==what_i){
				my_p[atk_index].faceButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
			}
			else{
				opp_p[atk_index].faceButton.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
			}
			
			
			Thread []t=new Thread[12];
			for (int i=0;i<6;i++){
				RefreshHPAnimation rha_self=new RefreshHPAnimation(my_p[i], self[i]);
				RefreshHPAnimation rha_other=new RefreshHPAnimation(opp_p[i], opp[i]);
				t[i]=new Thread(rha_self);
				t[i+6]=new Thread(rha_other);
			}
			for (int i=0;i<12;i++) t[i].start();
			for (int i=0;i<12;i++)
				try {
					t[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			if (who_atk==what_i){
				my_p[atk_index].faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
			}
			else{
				opp_p[atk_index].faceButton.setBorder(BorderFactory.createRaisedBevelBorder());
			}
			
			//over
		}
	}
	
	public void updateMyTurn(FighterInstance []self,FighterInstance[] opp){
		troopLeft.updateMe(self, true);
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
					
					
					String skill_state_description=ret.getString(19);
					int skill_state_type=ret.getInt(20);
					int skill_state_ratio=ret.getInt(21);
					
					FighterInfo fi=new FighterInfo(id,name,ability,fighter_type,base_hp,base_attack,base_defence,base_hit,base_block
							,base_attack_anger,base_attacked_anger,base_power_anger,base_powered_anger,description,skill_name,skill_description
							,skill_type,skill_ratio,skill_state_description,skill_state_type,skill_state_ratio);
					
					//if (id==1) System.out.println(fi.toStringHtml());
					all_fighters.add(fi);
					
				}
			
		
		
		
	}catch(Exception e){
		e.printStackTrace();
	}
	FighterInstance []fi_me=new FighterInstance[6];
	
	FighterInstance []fi_opp=new FighterInstance[6];
	
	/*
	for (int i=0;i<6;i++){
		fi_me[i]=new FighterInstance(all_fighters.get(i), 0);
		fi_opp[i]=new FighterInstance(all_fighters.get(i+6), 0);
	}
	*/
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new BattlePanel(null,fi_me,fi_opp));
		frame.pack();
		frame.setResizable(false);
		
		frame.setVisible(true);
	}


	public void enableMe() {
		troopLeft.enableMe();
		end_my_turn.setEnabled(true);
	}



	public void disableMyPower() {
		troopLeft.disableMyPower();
		
	}



	public void disableMyNormal(int i_info2) {
		troopLeft.disableMyNormal(i_info2);
		
	}



	public void unSelectAll() {
		troopLeft.unselectAll();
		troopRight.unselectAll();
		
		
	}
}
