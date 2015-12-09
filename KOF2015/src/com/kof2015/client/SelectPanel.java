package com.kof2015.client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.common.FighterInfo;
import com.kof2015.server.DBHelper;

public class SelectPanel extends JPanel{
	TextArea ta_me;
	TextArea ta_opp;
	
	JButton confirm;
	
	SelectAllPeople sa;
	
	SelectOnePeople []sp_one;
	
	ClientOneGame cog_this;	//this pointer
	
	
	
	public void disableAll(){
		sa.disableAll();
	}
	
	public SelectPanel(int what_i,ClientOneGame cog,ChooseFighter[] cf){
		this.cog_this=cog;
		sa=new SelectAllPeople(what_i,cf);
		add(sa);
		
		sp_one=sa.getAllPeople();
		
		ta_me=new TextArea();
		ta_opp=new TextArea();	
		ta_me.setEditable(false);
		ta_opp.setEditable(false);
		
		JPanel jp=new JPanel();
		jp.add(ta_me);	
		JPanel jp2=new JPanel();
		jp2.setLayout(new GridLayout(5,1));	
		confirm=new JButton("确认选择");
		jp2.add(confirm);	
		jp.add(jp2);
		jp.add(ta_opp);
		add(jp);	
		
		confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int []has_selected=new int[20];
				int count=0;
				for (int i=0;i<sp_one.length;i++){
					if (sp_one[i].selected){
						has_selected[count++]=i;
					}
				}
				cog_this.verify_confirm(has_selected,count);
				
			}
		});
	}

	
	public void CardBelongsToMe(int index){
		sp_one[index].jl_pic.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
	}
	
	public static void main(String[] args){
		JFrame jf=new JFrame();
		//jf.setLocationRelativeTo(null);
		jf.setSize(1200, 800);
		jf.setResizable(false);
		jf.setTitle("选人阶段,首先双方各选1人,然后双方轮流各选2人,直到结束");
		
		
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
		
		ChooseFighter []cf=new ChooseFighter[16];
		for (int i=0;i<16;i++){
			double q=Math.random();
			double p=Math.random();
			boolean selected;
			int who;
			if (q<0.5) selected=true;
			else selected=false;
			if (p<0.5) who=1;
			else who=2;
			cf[i]=new ChooseFighter(all_fighters.get((int)(Math.random()*30)), selected, who);
		}
			
		SelectPanel sp=new SelectPanel(1,null,cf);
		
		jf.add(sp);
		
		jf.setVisible(true);
		
	}
	
	public void refreshAccordingToCF(ChooseFighter []cf){
		for (int i=0;i<cf.length;i++){
			sp_one[i].refresh(cf[i]);
			
		}
	}
	
	public void addTexttoMe(String a){
		ta_me.append(a);
	}
	
	public void addTexttoOpp(String a){
		ta_opp.append(a);
	}

}
