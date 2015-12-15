package com.kof2015.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.common.Constants;
import com.common.FighterInfo;
import com.common.FighterInstance;
import com.common.Message;
import com.common.SkillState;
import com.kof2015.client.ChooseFighter;

public class ServerHandleOne implements Runnable {
	ObjectInputStream ois1,ois2;
	ObjectOutputStream oos1,oos2;
	String name1,name2;
	
	Vector<Message> msg_list;
	
	Thread t1;
	Thread t2;
	Thread t3;
	
	ArrayList<FighterInfo> all_fighters;
	
	
	HashSet<Integer> all_cand;
	ArrayList<FighterInfo> can_fighters;
	
	volatile ChooseFighter []cf;
	
	volatile boolean p1_sel_over;
	volatile boolean p2_sel_over;
	
	volatile String extra_for_opp="";
	
	
	volatile FighterInstance []p1_fi;
	volatile FighterInstance []p2_fi;
	volatile boolean p1_format_over;
	volatile boolean p2_format_over;
	
	
	int first_attack;
	
	public ServerHandleOne(ArrayList<FighterInfo> all_fighters,ObjectInputStream ois1,ObjectInputStream ois2,ObjectOutputStream oos1,ObjectOutputStream oos2,String name1,String name2){
		this.all_fighters=all_fighters;
		this.ois1=ois1;
		this.oos1=oos1;
		this.ois2=ois2;
		this.oos2=oos2;
		this.name1=name1;
		this.name2=name2;
		msg_list=new Vector<Message>();
		
		all_cand=new HashSet<Integer>();
		can_fighters=new ArrayList<FighterInfo>();
		
		p1_sel_over=false;
		p2_sel_over=false;
		
		p1_format_over=false;
		p2_format_over=false;
		
	}
	
	class Recv_1 implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					Thread.currentThread().sleep(20);
					Message msg=(Message) ois1.readObject();
					msg.from=1;
					msg_list.add(msg);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	class Recv_2 implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					Message msg=(Message) ois2.readObject();
					msg.from=2;
					msg_list.add(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	class Handle_Msg implements Runnable{

		@Override
		public void run() {
			while (true){
				if (msg_list.size()>0){
					Message toh=msg_list.get(0);
					msg_list.remove(0);
					
					//following are the handle
					switch(toh.type){
					case 1:
						System.out.println("Impossible!");
						break;
					case 2:
						//considering selecting fighters
						
						int who=toh.identifier;
						
						int nums=toh.i_info1;
						String debug_pl=name1;
						if (who==2) debug_pl=name2;
						int first=toh.i_info2;
						cf[first].selected=true;
						
						cf[first].who_select=who;
						
						String debug_sel="【"+debug_pl+"】"+"选择了:["+cf[first].fi.name+"]";
						if (nums==2){
							int second=toh.i_info3;
							debug_sel+=" && ["+cf[second].fi.name+"]";
							cf[second].selected=true;
							cf[second].who_select=who;
						}
						System.out.println(debug_sel);
						
						extra_for_opp="对手"+debug_sel+"\n";
						
						for (ChooseFighter cc:cf){
							System.out.println(cc.fi.name+" "+cc.selected+" "+cc.who_select);
						}
						
						if (who==1){
							p1_sel_over=true;
						}
						else{
							p2_sel_over=true;
						}
						
						break;
					case 3:
						
						FighterInfo []fi=toh.fi;
						assert(fi.length==6);
						if (toh.from==1){
							p1_fi=new FighterInstance[6];
							//前排血量提高20%  后排攻击加50
							for (int i=0;i<6;i++){
								if (i<=2)
									p1_fi[i]=new FighterInstance(fi[i], 0.2,0);
								else
									p1_fi[i]=new FighterInstance(fi[i], 0,50);
							}
							System.out.println("【"+name1+"】布阵完毕!");
							p1_format_over=true;
							if (p2_format_over){
								GO_TO_BATTLE();
							}
						}
						else if (toh.from==2){
							p2_fi=new FighterInstance[6];
							for (int i=0;i<6;i++){
								if (i<=2)
									p2_fi[i]=new FighterInstance(fi[i], Constants.FRONT_HP_BONUS,0);
								else
									p2_fi[i]=new FighterInstance(fi[i], 0,Constants.BACK_ATTACK_BOUNS);
								
							}
							System.out.println("【"+name1+"】布阵完毕!");
							p2_format_over=true;
							if (p1_format_over){
								GO_TO_BATTLE();
							}
						}
						else{
							System.out.println("MEET A BUG!!!!!!!");
							System.exit(-1);
						}
						
						break;
					case 4:
						switch (toh.i_info1){
						case 0:
							//必杀攻击
							int from_who=toh.from;	//1 or 2
							int atk_id=toh.i_info2;
							int atkd_id=toh.i_info3;
							
							ConductOnePower(from_who,atk_id,atkd_id);
							
							break;
						case 1:
							//普通攻击
							from_who=toh.from;	//1 or 2
							atk_id=toh.i_info2;
							atkd_id=toh.i_info3;
							
							ConductOneAttak(from_who,atk_id,atkd_id);

							break;
						case 2:
							// some player's turn is over
							from_who=toh.from;
							ChangePlayer(from_who);
							
							break;
						}
						break;
					}
				}
			}
			
		}
		
	}
	
	
	@Override
	public void run() {
		t1=new Thread(new Recv_1());
		t2=new Thread(new Recv_2());
		t3=new Thread(new Handle_Msg());
		
		t1.start();
		t2.start();
		t3.start();
		
		
		
		
		Message msg=new Message(-1);
		//following are choose pai
		double d1=Math.random();
		
		
		
		if (d1<0.5){
			first_attack=1;
			System.out.println("player1["+name1+"]先手");
			msg.msg="player1["+name1+"]先手";
		}
		else{
			first_attack=2;
			System.out.println("player2["+name2+"]先手");
			msg.msg="player2["+name2+"]先手";			
		}
		try {
			oos1.writeUnshared(msg);
			oos1.reset();
			oos2.writeUnshared(msg);
			oos2.reset();
			//the up 2 are useless
			
			int MAX_SEL=all_fighters.size();
			for (int i=0;i<16;i++){
				int cur=(int)(Math.random()*(double)MAX_SEL);
				while (all_cand.contains(cur)){
					cur=(int)(Math.random()*(double)MAX_SEL);
				}
				all_cand.add(cur);
				can_fighters.add(all_fighters.get(cur));
			}
			
			// now all_cand && can_fighters are with size 16
			cf=new ChooseFighter[16];
			for (int i=0;i<16;i++){
				cf[i]=new ChooseFighter(can_fighters.get(i),false,-2);
			}
			
			Message msg_both=new Message(2);
			msg_both.i_info1=-1;
			msg_both.cf=cf;
			if (d1>=0.5)
				msg_both.s_info1="你【后手】所以先选择，选择1张\n";
			else
				msg_both.s_info1="你【先手】所以后选择\n";
			
			oos1.writeUnshared(msg_both);
			oos1.reset();
			
			if (d1<0.5)
				msg_both.s_info1="你【后手】所以先选择，选择1张\n";
			else
				msg_both.s_info1="你【先手】所以后选择\n";			
			
			oos2.writeUnshared(msg_both);
			oos2.reset();
			
			Thread.currentThread().sleep(1000);
			
			if (d1>0.5){	
				for (int i=0;i<5;i++){
					Message msg1=new Message(2);
					if (i==0 || i==4) msg1.i_info3=1;
					else msg1.i_info3=2;
					msg1.i_info1=0;msg1.i_info2=1;
					msg1.cf=cf;
					msg1.s_info1="轮到你选择了,选择"+String.valueOf(msg1.i_info3)+"张\n";
					
					if (i!=0)
						msg1.s_info2=extra_for_opp;	//显示在别人的框子里
					else
						msg1.s_info2="";
					
					oos1.writeUnshared(msg1);
					oos1.reset();
					while(!p1_sel_over) Thread.currentThread().sleep(20);
					p1_sel_over=false;
					
					Message msg2=new Message(2);
					if (i==0 || i==4) msg2.i_info3=1;
					else msg2.i_info3=2;
					msg2.i_info1=0;msg2.i_info2=2;
					msg2.cf=cf;
					
					msg2.s_info1="轮到你选择了,选择"+String.valueOf(msg2.i_info3)+"张\n";
					msg2.s_info2=extra_for_opp;	//显示在别人的框子里
					
					oos2.writeUnshared(msg2);
					oos2.reset();
					while (!p2_sel_over) Thread.currentThread().sleep(20);
					p2_sel_over=false;

				}
			}
			else{
				for (int i=0;i<5;i++){		
					Message msg2=new Message(2);
					if (i==0 || i==4) msg2.i_info3=1;
					else msg2.i_info3=2;
					msg2.i_info1=0;msg2.i_info2=2;
					msg2.cf=cf;	
					msg2.s_info1="轮到你选择了,选择"+String.valueOf(msg2.i_info3)+"张\n";		
					if (i!=0)
						msg2.s_info2=extra_for_opp;	//显示在别人的框子里
					else
						msg2.s_info2="";
					
					oos2.writeUnshared(msg2);
					oos2.reset();
					while(!p2_sel_over) Thread.currentThread().sleep(20);
					p2_sel_over=false;
					
					Message msg1=new Message(2);
					if (i==0 || i==4) msg1.i_info3=1;
					else msg1.i_info3=2;
					msg1.i_info1=0;msg1.i_info2=1;
					msg1.cf=cf;
					
					msg1.s_info1="轮到你选择了,选择"+String.valueOf(msg1.i_info3)+"张\n";
					msg1.s_info2=extra_for_opp;	//显示在别人的框子里
					
					
					oos1.writeUnshared(msg1);
					oos1.reset();
					while (!p1_sel_over) Thread.currentThread().sleep(20);
					p1_sel_over=false;
					

				}
			}
			
			//通知双方结束选择
			Message msg_over=new Message(2);
			msg_over.i_info1=1;
			msg_over.cf=cf;
			
			if (d1<0.5){
				//1先手,后选择,最后一个也是它选的，所以2要知道对面选了什么
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2=extra_for_opp;
				oos2.writeUnshared(msg_over);
				oos2.flush();
				
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2="";
				oos1.writeUnshared(msg_over);
				oos1.flush();
			}
			else{
				//2先手,后选择,最后一个也是它选的，所以1要知道对面选了什么
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2=extra_for_opp;
				oos1.writeUnshared(msg_over);
				oos1.flush();
				
				msg_over.s_info1="双方选择完毕,下一步布阵\n";
				msg_over.s_info2="";
				oos2.writeUnshared(msg_over);
				oos2.flush();
			}
			
			//now here means select over!!!!
			
			FighterInfo []p1f=new FighterInfo[8];
			FighterInfo []p2f=new FighterInfo[8];
			int index_p1=0,index_p2=0;
			for (int i=0;i<16;i++){
				if (cf[i].who_select==1){
					p1f[index_p1++]=cf[i].fi;
				}
				else{
					p2f[index_p2++]=cf[i].fi;
				}
			}
			Message msg_both_send=new Message(3);
			msg_both_send.fi=p1f;
			oos1.writeUnshared(msg_both_send);
			
			msg_both_send.fi=p2f;
			oos2.writeUnshared(msg_both_send);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}

	public void ChangePlayer(int from_who) {
		for (FighterInstance fi:p1_fi){
			ArrayList<SkillState> need_del=new ArrayList<SkillState>();
			for (SkillState ss:fi.all_ss){
				ss.decreaseRound();
				if (ss.getRoundsLeft()<=0) need_del.add(ss);
			}
			for (SkillState ss:need_del){
				System.out.println("delete state:"+ss.toString());
				fi.all_ss.remove(ss);
			}

		}
		for (FighterInstance fi:p2_fi){
			ArrayList<SkillState> need_del=new ArrayList<SkillState>();
			for (SkillState ss:fi.all_ss){
				ss.decreaseRound();
				if (ss.getRoundsLeft()<=0) need_del.add(ss);
			}
			for (SkillState ss:need_del){
				System.out.println("delete state:"+ss.toString());
				fi.all_ss.remove(ss);
			}
		}
		
		Message msg=new Message(4);
		msg.i_info1=-1;
		addFIBtoMsg(msg);
		
		for (int i=0;i<12;i++){
			System.out.println(msg.fi_b[i].name);
			
		}	
		try{
			switch(from_who){
			case 1:
				msg.i_info2=2;	//轮到p2行动
				msg.s_info1="你【"+name2+"】的回合开始\n";
				oos2.reset();
				oos2.writeUnshared(msg);
				
				msg.s_info1="对手【"+name2+"】的回合开始\n";
				oos1.reset();
				oos1.writeUnshared(msg);
				break;
			case 2:
				msg.i_info2=1;
				msg.s_info1="你【"+name1+"】的回合开始\n";
				oos1.reset();
				oos1.writeUnshared(msg);
				
				msg.s_info1="对手【"+name1+"】的回合开始\n";
				oos2.reset();
				oos2.writeUnshared(msg);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void ConductOneAttak(int from_who, int atk_id, int atkd_id) {
		System.out.println("一次攻击:"+from_who+" :"+atk_id+" "+atkd_id);
		switch(from_who){
		case 1:
			FighterInstance fi_a=p1_fi[atk_id];
			FighterInstance fi_d=p2_fi[atkd_id];			
			String condition="";		
			int hit_rt=fi_a.getActualHit();
			int block_rt=fi_d.getActualBlock();		
			int act_attack=fi_a.getActualAttack();
			int act_def=fi_d.getActualDefence();
			int dmg=(int) (act_attack*Constants.NORMAL_ATTACK_MODIFY-act_def*Constants.NORMAL_DEFENCE_MODIFY);		
			int first_block=(int) (Math.random()*100);
			if (first_block<=block_rt){
				condition+="\t格挡!";
				
				int mul=Constants.NORMAL_BLOCK_MAX-Constants.NORMAL_BLOCK_MIN;
				
				int how_much=(int) (Math.random()*mul)+Constants.NORMAL_BLOCK_MIN;
				if (block_rt>=100){
					how_much+=block_rt-100;
					if (how_much>=100) how_much=100;
				}
				condition+="格挡住"+how_much+"%的伤害\n";
				dmg=(int) (dmg*(1- how_much/100.0));
			}
			else{
				//判断暴击
				int second_hit=(int) (Math.random()*100);
				if (second_hit<=hit_rt){
					condition+="\t暴击!";
					
					int mul=Constants.NORMAL_HIT_MAX-Constants.NORMAL_HIT_MIN;
					
					int how_much=(int) (Math.random()*mul)+Constants.NORMAL_HIT_MIN;
					
					if (hit_rt>=100){
						how_much+=hit_rt-100;
					}
					
					condition+="暴击造成额外"+how_much+"%的伤害\n";
					dmg=(int) (dmg*(1+ how_much/100.0));
				}
			}
			
			String info1="【"+name1+"】的["+fi_a.name+"]对【"+name2+"】的["+fi_d.name+"]发动了普通攻击\n";
			String info3=info1+condition+"\t本次攻击造成了"+dmg+"的伤害\n";
			
			fi_d.hp-=dmg;
			
			
			fi_a.anger+=fi_a.true_atk_anger;
			fi_d.anger+=fi_d.true_atkd_anger;
			
			if (fi_a.fighter_type==1) fi_a.anger+=Constants.SKILL_ANGER_INCREASE;
			if (fi_d.fighter_type==2) fi_d.anger+=Constants.DEFENCE_ANGER_INCREASE;
			
			
			if (fi_d.hp<=0){
				fi_d.isDead=true;
				fi_d.anger=0;
				info3+="\t"+fi_d.name+"倒下了!\n";
				
				fi_a.anger+=Constants.KILL_BONUS_ANGER;
				
			}
			
			if (fi_a.anger>Constants.MAX_ANGER) fi_a.anger=Constants.MAX_ANGER;
			if (fi_d.anger>Constants.MAX_ANGER) fi_d.anger=Constants.MAX_ANGER;
			
			System.out.println(info1);
			
			Message msg_1=new Message(4);
			
			msg_1.i_info1=1;	//normal attack
			msg_1.i_info2=1;	//whose turn
			
			msg_1.i_info3=atk_id;	//attacker's id
			
			msg_1.s_info1=info3;	//show info
			
			addFIBtoMsg(msg_1);
			
			
			/*
			for (int i=0;i<12;i++){
				System.out.println("攻击后:"+msg_1.fi_b[i].base.name+" "+msg_1.fi_b[i].hp);
			}
			*/
			
			msg_1.s_info2="debug";
			
			try{
				oos1.reset();
				oos2.reset();
				
				oos1.writeUnshared(msg_1);
				oos2.writeUnshared(msg_1);
			}catch(Exception e){
				e.printStackTrace();
			}
			msg_1=null;
			
			boolean over=true;
			for (FighterInstance fp2:p2_fi){
				if (fp2.hp>0){
					over=false;
					break;
				}
			}
			if (over){
				// p1 win
				Message msg_over=new Message(4);
				msg_over.i_info1=2;
				msg_over.i_info2=1;
				msg_over.s_info1="恭喜你赢了!";
				try {
					oos1.writeUnshared(msg_over);
					
					msg_over.s_info1="你输了,傻叉!";
					oos2.writeUnshared(msg_over);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			
			
			
			
			break;
		case 2:
			
			fi_a=p2_fi[atk_id];
			fi_d=p1_fi[atkd_id];
			

			
			hit_rt=fi_a.getActualHit();
			block_rt=fi_d.getActualBlock();
			
			
			act_attack=fi_a.getActualAttack();
			act_def=fi_d.getActualDefence();
			
			
			dmg=(int) (act_attack*Constants.NORMAL_ATTACK_MODIFY-act_def*Constants.NORMAL_DEFENCE_MODIFY);
			
			condition="";
			
			first_block=(int) (Math.random()*100);
			if (first_block<=block_rt){
				condition+="\t格挡!";
				
				int mul=Constants.NORMAL_BLOCK_MAX-Constants.NORMAL_BLOCK_MIN;
				
				int how_much=(int) (Math.random()*mul)+Constants.NORMAL_BLOCK_MIN;
				
				if (block_rt>=100){
					how_much+=block_rt-100;
					if (how_much>=100) how_much=100;
				}
				
				condition+="格挡住"+how_much+"%的伤害\n";
				dmg=(int) (dmg*(1- how_much/100.0));
			}
			else{
				//判断暴击
				int second_hit=(int) (Math.random()*100);
				if (second_hit<=hit_rt){
					condition+="\t暴击!";
					
					int mul=Constants.NORMAL_HIT_MAX-Constants.NORMAL_HIT_MIN;
					
					int how_much=(int) (Math.random()*mul)+Constants.NORMAL_HIT_MIN;
					
					if (hit_rt>=100){
						how_much+=hit_rt-100;
					}
					
					condition+="暴击造成额外"+how_much+"%的伤害\n";
					dmg=(int) (dmg*(1+ how_much/100.0));
				}
			}
			

			info1="【"+name2+"】的["+fi_a.name+"]对【"+name1+"】的["+fi_d.name+"]发动了普通攻击\n";
			info3=info1+condition+"\t本次攻击造成了"+dmg+"的伤害\n";
			
			
			fi_d.hp-=dmg;
			

			
			fi_a.anger+=fi_a.true_atk_anger;
			fi_d.anger+=fi_d.true_atkd_anger;
			
			if (fi_a.fighter_type==1) fi_a.anger+=Constants.SKILL_ANGER_INCREASE;
			if (fi_d.fighter_type==2) fi_d.anger+=Constants.DEFENCE_ANGER_INCREASE;
			
			
			
			if (fi_d.hp<=0){
				fi_d.anger=0;
				fi_d.isDead=true;
				info3+="\t"+fi_d.name+"倒下了!\n";
				
				fi_a.anger+=Constants.KILL_BONUS_ANGER;
			}
			
			if (fi_a.anger>Constants.MAX_ANGER) fi_a.anger=Constants.MAX_ANGER;
			if (fi_d.anger>Constants.MAX_ANGER) fi_d.anger=Constants.MAX_ANGER;
			
			
			System.out.println(info1);
			

			
			Message msg_2=new Message(4);
			msg_2.i_info1=1;		//normal attack
			msg_2.i_info2=2;		//whose turn
			
			msg_2.s_info1=info3;
			
			msg_2.i_info3=atk_id;
			
			addFIBtoMsg(msg_2);
			
			for (int i=0;i<12;i++){
				System.out.println("攻击后:"+msg_2.fi_b[i].name+" "+msg_2.fi_b[i].hp);
			}
			
			msg_2.s_info2="debug";
			
			try{
				oos1.reset();
				oos2.reset();
				
				oos2.writeUnshared(msg_2);
				
				oos1.writeUnshared(msg_2);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			msg_2=null;
			
			
			over=true;
			for (FighterInstance fp1:p1_fi){
				if (fp1.hp>0){
					over=false;
					break;
				}
			}
			if (over){
				// p2 win
				Message msg_over=new Message(4);
				msg_over.i_info1=2;
				msg_over.i_info2=2;
				msg_over.s_info1="恭喜你赢了!";
				try {
					oos2.writeUnshared(msg_over);
					
					msg_over.s_info1="你输了,傻叉!";
					oos1.writeUnshared(msg_over);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			
			
			

			
			break;
		}
		
	}
	
	public String generateInfo(String attacker,FighterInstance wo,String defencer,FighterInstance ta){
		String name=wo.name;
		String skill_name=wo.skill_name;
		double rate=wo.true_skill_ratio;
		
		String des1="【"+attacker+"】的["+name+"]发动了必杀技------"+skill_name+"\n";
		
		int act_attack=wo.getActualAttack();
		int act_def=ta.getActualDefence();
		
		int  dmg=(int) (act_attack*rate*Constants.POWER_ATTACK_MODIFY-act_def*Constants.POWER_DEFENCE_MODIFY);
		
		
		int hit_rt=wo.getActualHit();
		int block_rt=ta.getActualBlock();
		
		
		
		String condition="";
		
		int first_block=(int) (Math.random()*100);
		if (first_block<=block_rt){
			condition+="\t大招被格挡!";
			
			int mul=Constants.POWER_BLOCK_MAX-Constants.POWER_BLOCK_MIN;
			
			int how_much=(int) (Math.random()*mul)+Constants.POWER_BLOCK_MIN;
			
			if (block_rt>=100){
				how_much+=block_rt-100;
				if (how_much>=100) how_much=100;
			}
			
			
			
			condition+="格挡住"+how_much+"%的伤害\n";
			dmg=(int) (dmg*(1- how_much/100.0));
		}
		else{
			//判断暴击
			int second_hit=(int) (Math.random()*100);
			if (second_hit<=hit_rt){
				condition+="\t大招暴击!";
				
				int mul=Constants.POWER_HIT_MAX-Constants.POWER_HIT_MIN;
				
				int how_much=(int) (Math.random()*mul)+Constants.POWER_HIT_MIN;
				
				if (hit_rt>=100){
					how_much+=hit_rt-100;
				}
				
				condition+="暴击造成额外"+how_much+"%的伤害\n";
				dmg=(int) (dmg*(1+ how_much/100.0));
			}
		}
		


		
		
		
		
		
		
		String kill="\t对【"+defencer+"】的["+ta.name+"造成了"+dmg+"的伤害\n";
		
		//wo.anger+=wo.true_pw_anger;
		ta.anger+=ta.true_pwd_anger;
		
		ta.hp-=dmg;
		if (ta.hp<=0){
			ta.isDead=true;
			ta.anger=0;
			kill+="\t"+ta.name+"倒下了\n";
			ta.anger=0;
			wo.anger+=Constants.KILL_BONUS_ANGER;
		}
		
		if (wo.anger>Constants.MAX_ANGER) wo.anger=Constants.MAX_ANGER;
		return des1+condition+kill;
		
	}
	
	public void ConductOnePower(int from_who, int atk_id, int atkd_id) {
		
		//攻击者id ---  atk_id
		//被攻击者id --- atkd_id 仅仅指单攻的情况
		
		HashSet<Integer> index_attacked=new HashSet<Integer>();
		
		FighterInstance []atks=null;
		FighterInstance []defs=null;
		String name_atk=null;
		String name_def=null;
		if (from_who==1){
			atks=p1_fi;
			defs=p2_fi;
			name_atk=name1;
			name_def=name2;
		}
		else{
			atks=p2_fi;
			defs=p1_fi;
			name_atk=name2;
			name_def=name1;			
		}
		
		
		

			FighterInstance wo=atks[atk_id];
			int skill_type=wo.skill_type_i;
			wo.anger=0;
			
			switch(skill_type){
			case 0:
				//排杀
				index_attacked.add(atkd_id);
				if (atkd_id<=2){
					FighterInstance fi1=defs[atkd_id];
					
					
					String log1=generateInfo(name_atk, wo, name_def, fi1);
					
					if (defs[atkd_id+3].hp>0){
						FighterInstance fi2=defs[atkd_id+3];
						
						index_attacked.add(atkd_id+3);
						
						log1+=generateInfo(name_atk, wo, name_def, fi2);
					}
					
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
					//log1 is sent
				}
				else{
					//一定是后排 可能前排已经挂了,可能没有
					FighterInstance fi1=defs[atkd_id];
					
					
					
					String log1=generateInfo(name_atk, wo, name_def, fi1);
					
					if (defs[atkd_id-3].hp>0){
						FighterInstance fi2=defs[atkd_id-3];
						
						index_attacked.add(atkd_id-3);
						
						log1+=generateInfo(name_atk, wo, name_def, fi2);
					}
					
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
					//log1 is sent
				}
				
				
				
				break;
			case 1:
				//单体
				{
				FighterInstance fi1=defs[atkd_id];
				
				index_attacked.add(atkd_id);
				
				String log1=generateInfo(name_atk, wo, name_def, fi1);
				
				CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
				
				//log1 is sent 
				
				}
				
				
				break;
			case 2:
				//AOE
				{
					String log1="";
					for (int i=0;i<6;i++){
						FighterInstance fi1=defs[i];
						if (fi1.hp>0){
							log1+=generateInfo(name_atk, wo, name_def, fi1);
							index_attacked.add(i);
						}
					}
					
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
					//log1 is used
				}
				break;
			case 3:
				//加血
				//直接在这里写
				{
					String log1="";
					for (int i=0;i<6;i++){
						FighterInstance fi1=atks[i];
						if (fi1.hp<=0) continue;
						int orig=fi1.hp;
						int restore= (int) (wo.getActualAttack()*wo.true_skill_ratio*1.5);

						int hit=wo.getActualHit();
						int restore_baoji=(int) (Math.random()*100);
						if (restore_baoji<=hit){
							
							int mul=Constants.POWER_HIT_MAX-Constants.POWER_HIT_MIN;
							
							int how_much=(int) (Math.random()*mul)+Constants.POWER_HIT_MIN;
							
							if (hit>=100){
								how_much+=hit-100;
							}
							
							restore=(int) (restore*(1+how_much/100.0));
						}
						
						fi1.hp+=restore;
						if (fi1.hp>fi1.max_hp) fi1.hp=fi1.max_hp;
						int true_res=fi1.hp-orig;
						log1+="\t【"+name_atk+"】的["+wo.name+"]对["+fi1.name+"]进行回血:"+true_res+"\n";
						
					}
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,null);
				}
				
				
				break;
			case 4:
				//前列全杀
				{
					String log1="";
					boolean ok=false;
					for (int i=0;i<3;i++){
						FighterInstance fi1=defs[i];
						if (fi1.hp>0){
							index_attacked.add(i);
							ok=true;
							log1+=generateInfo(name_atk, wo, name_def, fi1);
						}
					}
					if (!ok){
						for (int i=3;i<6;i++){
							FighterInstance fi1=defs[i];
							if (fi1.hp>0){
								index_attacked.add(i);
								log1+=generateInfo(name_atk, wo, name_def, fi1);
							}
						}
					}
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
					//log1 is sent
					
				}
				
				break;
			case 5:
				//后列全杀
				{
					String log1="";
					boolean ok=false;
					for (int i=3;i<6;i++){
						FighterInstance fi1=defs[i];
						if (fi1.hp>0){
							ok=true;
							index_attacked.add(i);
							log1+=generateInfo(name_atk, wo, name_def, fi1);
						}
					}
					if (!ok){
						for (int i=0;i<3;i++){
							FighterInstance fi1=defs[i];
							if (fi1.hp>0){
								index_attacked.add(i);
								log1+=generateInfo(name_atk, wo, name_def, fi1);
							}
						}
					}
					
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
					//log1 is sent
			
				}
				//后排全杀
				break;
			case 6:
				//后列单杀
				{
				FighterInstance fi1=defs[atkd_id];
				
				index_attacked.add(atkd_id);
				
				String log1=generateInfo(name_atk, wo, name_def, fi1);
				
				CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
				//log1 is sent 
				
				}
				
				//后排单杀
				break;
			case 7:
				{
					String log1="";
					for (int i=0;i<3;i++){
						ArrayList<Integer> all_live_index=new ArrayList<Integer>();
						ArrayList<FighterInstance> all_live=new ArrayList<FighterInstance>();
						for (int j=0;j<6;j++){
							if(defs[j].hp>0){
								all_live.add(defs[j]);
								all_live_index.add(j);
							}
						}
						if (all_live.size()==0){
							CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
							break;
						}
						int sz=all_live.size();
						int who_luck=(int)(Math.random()*sz);
						index_attacked.add(all_live_index.get(who_luck));
						FighterInstance fi1=all_live.get(who_luck);
						log1+=generateInfo(name_atk, wo, name_def, fi1);
					}
					
					CalSkillStateAndsendPowerResult(log1, from_who, atk_id,index_attacked);
					//log1 is sent
				}
				//随机3人杀
			}
			
	
	
	
		
	}
	
	public void giveNearbyState(FighterInstance []mine,int index,SkillState ss){
		FighterInstance fi_me=mine[index];
		fi_me.addState(ss);
		HashSet [] index_hs=new HashSet[6];
		for (int i=0;i<6;i++) index_hs[i]=new HashSet<Integer>();
		index_hs[0].add(1);index_hs[0].add(3);
		index_hs[1].add(0);index_hs[1].add(2);index_hs[1].add(4);
		index_hs[2].add(1);index_hs[2].add(5);
		index_hs[3].add(0);index_hs[3].add(4);
		index_hs[4].add(1);index_hs[4].add(3);index_hs[4].add(5);
		index_hs[5].add(2);index_hs[5].add(4);
		
		HashSet<Integer> a=index_hs[index];
		for (Integer ii:a){
			FighterInstance fi=mine[ii];
			if (fi.hp>0){
				SkillState ss_c=new SkillState(ss);
				fi.addState(ss_c);
			}
		}
		
	}
	
	public void CalSkillStateAndsendPowerResult(String logs,int whosturn,int attk,HashSet<Integer> atkd_id){
		FighterInstance attacker=null;
		FighterInstance []all_atks=null;
		FighterInstance []all_defs=null;
		
		if (whosturn==1){
			attacker=p1_fi[attk];
			all_atks=p1_fi;
			all_defs=p2_fi;
		}
		else{
			attacker=p2_fi[attk];
			all_atks=p2_fi;
			all_defs=p1_fi;
		}
		
		String skill_extra_des=attacker.original_base.skill_state_description;
		int skill_extra_tp=attacker.original_base.skill_state_type;
		int skill_extra_ratio=attacker.original_base.skill_state_ratio;
		
		SkillState ss=new SkillState(skill_extra_des,skill_extra_tp,skill_extra_ratio);
		
		switch(skill_extra_tp){
		case 0:
		case 1:
		case 8:
		case 14:
		case 15:
		case 19:
		case 20:
			SkillState ss_c=new SkillState(ss);
			attacker.addState(ss_c);
			break;
			
		case 2:
			for (FighterInstance fi:all_atks){
				if (fi.hp>0){
					SkillState ss_cc=new SkillState(ss);
					fi.addState(ss_cc);
				}
			}
			break;
		case 3:
			if (all_atks[0].hp>0 || all_atks[1].hp>0 || all_atks[2].hp>0){
				for (int i=0;i<3;i++){
					if (all_atks[i].hp>0){
						SkillState ss_cc=new SkillState(ss);
						all_atks[i].addState(ss_cc);
					}
				}
			}
			else{
				for (int i=3;i<6;i++){
					if (all_atks[i].hp>0){
						SkillState ss_cc=new SkillState(ss);
						all_atks[i].addState(ss_cc);
						
					}
				}
			}
			break;
			
		case 4:
		case 13:
			for (int u:atkd_id){
				FighterInstance fi=all_defs[u];
				if (fi.hp>0){
					SkillState ss_cc=new SkillState(ss);
					fi.addState(ss_cc);
				}
			}
			break;
			
		case 5:
			giveNearbyState(all_atks, attk, ss);
			break;
		case 6:
			//判定眩晕
		case 9:
			//判定沉默
			for (int u:atkd_id){
				FighterInstance fi=all_defs[u];
				if (fi.hp>0){
					double chance=Math.random()*100;
					if (chance<=skill_extra_ratio){
						SkillState ss_cc=new SkillState(ss);
						fi.addState(ss_cc);
					}
				}
			}
			break;
		case 7:
			//not support now
			break;
		case 10:
			//not support now
			break;
		case 11:
			//not support now
			break;
		case 12:
			//降怒
			for (int u:atkd_id){
				FighterInstance fi=all_defs[u];
				if (fi.hp>0){
					fi.anger-=skill_extra_ratio;
					if (fi.anger<0) fi.anger=0;
				}
			}
			break;


		case 16:
		case 17:
		case 18:
			for (FighterInstance fi:all_atks){
				if (Constants.ORCH_NAME.contains(fi.name)){
					SkillState ss_cc=new SkillState(ss);
					fi.addState(ss_cc);
				}
			}
			break;	
		}
		//over
		
		
		
		Message msg=new Message(4);
		msg.i_info1=0;
		msg.i_info2=whosturn;
		msg.i_info3=attk;
		msg.s_info1=logs;
		addFIBtoMsg(msg);
		
		try{
			oos1.reset();
			oos2.reset();
			oos1.writeUnshared(msg);
			oos2.writeUnshared(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if (whosturn==1){
			boolean over=true;
			for (int i=0;i<6;i++){
				if (p2_fi[i].hp>0){
					over=false;
					break;
				}
			}
			if (over){
				Message msg_over=new Message(4);
				msg_over.i_info1=2;
				msg_over.i_info2=2;
				msg_over.s_info1="恭喜你赢了!";
				try {
					oos1.writeUnshared(msg_over);
					
					msg_over.s_info1="你输了,傻叉!";
					oos2.writeUnshared(msg_over);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		else{
			boolean over=true;
			for (int i=0;i<6;i++){
				if (p1_fi[i].hp>0){
					over=false;
					break;
				}
			}
			if (over){
				Message msg_over=new Message(4);
				msg_over.i_info1=2;
				msg_over.i_info2=2;
				msg_over.s_info1="恭喜你赢了!";
				try {
					oos2.writeUnshared(msg_over);
					
					msg_over.s_info1="你输了,傻叉!";
					oos1.writeUnshared(msg_over);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	

	public void addFIBtoMsg(Message msg){
		msg.fi_b=new FighterInstance[12];
		for (int i=0;i<6;i++){
			msg.fi_b[i]=p1_fi[i];
			msg.fi_b[i+6]=p2_fi[i];
		}
	}

	public void GO_TO_BATTLE() {
		System.out.println("双方布阵完毕!准备战斗!");
		//using p1_fi[] and p2_fi[] which are FighterInstance
		// first send them to clients, with type=4 subtype=-1 means start
		
		Message msg=new Message(4);
		msg.i_info1=-2;
		msg.s_info1="战斗开始!\n";
		addFIBtoMsg(msg);
		
		try {
			oos1.writeUnshared(msg);
			oos2.writeUnshared(msg);
		
			System.out.println(first_attack);
		
			msg=null;
			
			//然后发送-1 给 第一回合的攻击者以及防御者
			Message msg_round1=new Message(4);
			msg_round1.i_info1=-1;
			msg_round1.i_info2=first_attack;
			addFIBtoMsg(msg_round1);	
			
			for (int i=0;i<12;i++) System.out.println(msg_round1.fi_b[i].name);
			
			if (first_attack==1){
				msg_round1.s_info1="你【"+name1+"】的回合开始\n";
				oos1.writeUnshared(msg_round1);
				msg_round1.s_info1="对手【"+name1+"】的回合开始\n";
				oos2.writeUnshared(msg_round1);
			}
			else{
				msg_round1.s_info1="你【"+name2+"】的回合开始\n";
				oos2.writeUnshared(msg_round1);
				msg_round1.s_info1="对手【"+name2+"】的回合开始\n";
				oos1.writeUnshared(msg_round1);
			}
		
		} catch (IOException e) {

		}
		
		
	}

}
