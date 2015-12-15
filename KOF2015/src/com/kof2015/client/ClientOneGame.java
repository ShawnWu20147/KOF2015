package com.kof2015.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.common.FighterInfo;
import com.common.FighterInstance;
import com.common.Message;
import com.kof2015.battle.BattlePanel;
import com.kof2015.battle.BattlerPanel;
import com.kof2015.battle.TroopPanel;


public class ClientOneGame implements Runnable{
	
	String serverIp,nickName,challenger;
	int what_i;
	

	
	volatile static int canSel;
	volatile String givePanelMe,givePanelOpp;
	volatile ChooseFighter[] acf;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	volatile Vector<Message> msg_list;
	
	JFrame jf_login;
	
	
	JFrame jf_select;
	
	JFrame jf_formation;
	
	JFrame frame_battle;
	
	SelectPanel sp;
	
	
	
	
	
	
	
	BattlePanel bp_total;
	FighterInstance []p1_f;
	FighterInstance []p2_f;
	
	volatile String battle_show;
	
	JTextArea extra_info;
	
	public ClientOneGame(JFrame jf,String serverIp,String nickName){
		this.jf_login=jf;
		this.serverIp=serverIp;
		this.nickName=nickName;
		

		canSel=0;
		msg_list=new Vector<Message>();
		
		
	}
	
	class Show_OptionPane implements Runnable{
		String con,cap;
		public Show_OptionPane(String content,String cap){
			this.con=content;
			this.cap=cap;
		}
		@Override
		public void run() {
			JOptionPane.showMessageDialog(jf_login, con, cap, JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	public void getP1P2fromMessage(FighterInstance[] f){
		if (p1_f==null)
			p1_f=new FighterInstance[6];
		if (p2_f==null)
			p2_f=new FighterInstance[6];
		for (int i=0;i<6;i++){
			p1_f[i]=f[i];
			p2_f[i]=f[i+6];
		}
	}
	
	class Handle_Msg_Thread implements Runnable{

		@Override
		public void run() {
			System.out.println("ready to handle msg");
			while (true){
				try {
					Thread.currentThread().sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (msg_list.size()>0){
					Message msg=msg_list.get(0);
					msg_list.remove(0);
					
					System.out.println("one with type:"+msg.type);
					switch(msg.type){
					case -1:
						//just debug msg
						System.out.println(msg.msg);
						break;
					case 0:
						//login
						what_i=msg.i_info1;
						System.out.println("ITS LOGIN! I AM "+what_i);
						challenger=(msg.i_info1==1)?msg.s_info2:msg.s_info1;
						
						other_has_conn();	//added
						break;
					case 2:
						//select cards
						System.out.println("haha: the type is:"+msg.i_info1);
						if (msg.i_info1==-1){
							//start
							givePanelMe=msg.s_info1;
							givePanelOpp=msg.s_info2;
							acf=msg.cf;
							
							setup_choose_finish();	//add
						}
						else if (msg.i_info1==1){
							//end
							//do nothing since it is end
							acf=msg.cf;
							canSel=0;
							givePanelMe=msg.s_info1;
							givePanelOpp=msg.s_info2;
							//even it is over, we should tell the player
							
							one_choose_over();//add
							
						}
						else{
							//must be 0, one selection
							if (msg.i_info2!=what_i){
								System.out.println("wrong! should give to me!");
								System.exit(-1);
							}
							canSel=msg.i_info3;
							givePanelMe=msg.s_info1;
							givePanelOpp=msg.s_info2;
							if (givePanelMe==null) givePanelMe="";
							if (givePanelOpp==null) givePanelOpp="";
							
							
							acf=msg.cf;
							
							for (ChooseFighter ccc:acf){
								System.out.println("in handlemsg:"+ccc.fi.name+"\t"+ccc.selected);
							}
							
							
							one_choose_me();//add
							
						}
						
						break;
					case 3:
						FighterInfo []fi=msg.fi;
						begin_formation(fi);
						break;
					case 4:
						//the battle!!!!
						System.out.println(msg.i_info1);
						
						
						
						switch(msg.i_info1){
						case -2:
							//the battle starts!
							getP1P2fromMessage(msg.fi_b);
							battle_show=msg.s_info1;
							Battle_Method();
							break;
						case -1:
							//turn starts
							int whos_turn=msg.i_info2;
							battle_show=msg.s_info1;
							getP1P2fromMessage(msg.fi_b);
							if (whos_turn==what_i){
								MyTurnStart();
							}
							else{
								OtherTurnStart();
							}
							break;
						case 0:
							// power
							whos_turn=msg.i_info2;
							battle_show=msg.s_info1;
							getP1P2fromMessage(msg.fi_b);
							if (whos_turn==what_i){
								MyTurnProcessWithPower(msg);
							}
							else{
								OtherTurnProcessWithPower(msg);
							}
							break;
						case 1:
							// normal
							whos_turn=msg.i_info2;
							battle_show=msg.s_info1;
							getP1P2fromMessage(msg.fi_b);
							
							
							for (FighterInstance fi1:msg.fi_b){
								System.out.println("��һ��:"+fi1.name+" "+fi1.hp+" "+fi1.max_hp);
							}
							
							
							
							
							
							if (whos_turn==what_i){
								MyTurnProcessWithNormalAttack(msg);
							}
							else{
								OtherTurnProcessWithNormalAttack(msg);
							}
							
							break;
						case 2:
							//result
							battle_show=msg.s_info1;
							ShowResult();
							break;
						}
						
							
					}
				}
			}
			
		}

		
	}
	

	class Recv_Thread implements Runnable{

		@Override
		public void run() {
			try{
				while (true){
					
					try {
						Thread.currentThread().sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Message msg=(Message)ois.readUnshared();
					msg_list.add(msg);
					System.out.println("�յ�һ����:"+msg.type);
					
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void other_has_conn(){
		System.out.println("conn other!");
		new Thread(new Show_OptionPane("��ʼѡ��", "�������["+challenger+"]����")).start();		
	}
	
	public void ShowResult() {
		
		JOptionPane.showMessageDialog(jf_login, battle_show, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		
		
		extra_info.append(battle_show);
		bp_total.addLog(battle_show);
		bp_total.repaint();
		
	}

	public void OtherTurnProcessWithNormalAttack(Message foruse) {
		
		int who_atk_index=foruse.i_info3;
		int who_atk=foruse.i_info2;
		
		
		System.out.println("INOTHERTURN: ready to add log:"+battle_show);
		extra_info.append(battle_show);
		bp_total.addLog(battle_show);
		
		if (what_i==1)
			bp_total.update(p1_f, p2_f,true,who_atk_index,who_atk);
		else
			bp_total.update(p2_f, p1_f,true,who_atk_index,who_atk);
		bp_total.repaint();
		
	}

	public void MyTurnProcessWithNormalAttack(Message foruse) {
		
		extra_info.append(battle_show);
		bp_total.addLog(battle_show);
		
		
		
		BattlerPanel []all_my_bp=bp_total.my_p;
		boolean []res=new boolean[6];
		for (int i=0;i<6;i++){
			all_my_bp[i].disableSkill();	//of course, since it conducts normal attack
			res[i]=all_my_bp[i].attackButton.isEnabled();
			all_my_bp[i].attackButton.setEnabled(false);
		}
		
		
		int who_atk_index=foruse.i_info3;
		int who_atk=foruse.i_info2;
		
		
		if (what_i==1)
			bp_total.update(p1_f, p2_f,true,who_atk_index,who_atk);
		else
			bp_total.update(p2_f, p1_f,true,who_atk_index,who_atk);
		
		System.out.println(foruse.i_info3+"���ж�������");
		
		
		
		for (int i=0;i<6;i++){
			if (res[i])
				all_my_bp[i].attackButton.setEnabled(true);
		}
		
		
		bp_total.disableMyNormal(foruse.i_info3);	//using index
		
		bp_total.disableMyPower();
		bp_total.repaint();
		
	}

	public void OtherTurnProcessWithPower(Message foruse) {
		extra_info.append(battle_show);
		bp_total.addLog(battle_show);
		
		int who_atk_index=foruse.i_info3;
		int who_atk=foruse.i_info2;
		
		
		if (what_i==1)
			bp_total.update(p1_f, p2_f,true,who_atk_index,who_atk);
		else
			bp_total.update(p2_f, p1_f,true,who_atk_index,who_atk);
		
		bp_total.repaint();
		
	}

	public void MyTurnProcessWithPower(Message foruse) {
		
		extra_info.append(battle_show);
		bp_total.addLog(battle_show);
		
		int who_atk_index=foruse.i_info3;
		int who_atk=foruse.i_info2;

		BattlerPanel []all_my_bp=bp_total.my_p;
		boolean []res_atk=new boolean[6];
		boolean []res_pw=new boolean[6];
		for (int i=0;i<6;i++){
			
			res_atk[i]=all_my_bp[i].attackButton.isEnabled();	//�޸��ˣ�ԭ�����Թ�����,���ڻ����Թ���
			
			all_my_bp[i].disableAttack();
			
			
			
			res_pw[i]=all_my_bp[i].skillButton.isEnabled();	//����,ԭ�����ԷŴ��е�,���ڿ϶����ǿ��Ե�
			
			all_my_bp[i].disableSkill();
			

		}
		
		
		
		
		if (what_i==1)
			bp_total.update(p1_f, p2_f,true,who_atk_index,who_atk);
		else
			bp_total.update(p2_f, p1_f,true,who_atk_index,who_atk);
		
		
		for (int i=0;i<6;i++){
			if (all_my_bp[i].myfi.hp>0 && res_atk[i])
				all_my_bp[i].attackButton.setEnabled(true);
			if (res_pw[i]){
				System.out.println("�л���ָ�:"+i);
				//ԭ�����ԷŴ��е� ���л���ָ�
				all_my_bp[i].restoreRageEnabled();
			}
		}
		
		
		
		bp_total.repaint();
		
	}

	public void OtherTurnStart() {
		//it's easy to see we should disable all elements(���˼��) in the battlefield
		extra_info.append(battle_show);
		bp_total.disableAll();
		bp_total.addLog(battle_show);
		
		
		
		if (what_i==1)
			bp_total.update(p1_f, p2_f,false,-1,-1);
		else
			bp_total.update(p2_f, p1_f,false,-1,-1);
		bp_total.repaint();
		
	}

	public void MyTurnStart() {
		//������start ���Կ���enableMe Ȼ��update����һЩ����enable�� �غ��в�������Щ����
		
		extra_info.append(battle_show);
		
		bp_total.unSelectAll();
		
		bp_total.enableMe();
		bp_total.addLog(battle_show);
		
		if (what_i==1)
			bp_total.updateMyTurn(p1_f, p2_f);
		else
			bp_total.updateMyTurn(p2_f, p1_f);
		bp_total.repaint();
	}

	public void begin_formation(FighterInfo[] fi) {
		jf_formation = new JFrame();
		FormationPanel testObject = new FormationPanel(this,challenger);
		

		jf_formation.setTitle("��"+nickName+"��" +"����");
		jf_formation.add(testObject);
		jf_formation.pack();
		jf_formation.setVisible(true);
		
		testObject.passingCandidates( fi );
		
	}

	public void setup_choose_finish(){
		jf_select=new JFrame();
		//jf.setLocationRelativeTo(null);
		jf_select.setSize(1200, 800);
		jf_select.setResizable(false);
		jf_select.setTitle(nickName+":ѡ�˽׶�,����˫����ѡ1��,Ȼ��˫��������ѡ2��,ֱ������");
		
		sp=new SelectPanel(what_i,this,acf);
		sp.addTexttoMe("׼����ʼ�񶷼���ѡ\n"+givePanelMe);
		
		jf_select.add(sp);
		jf_select.setVisible(true);
	}
	
	public void one_choose_me(){
		
		//��Ҫ enable ��ѡ
		//д��!!!  ����refreshAccordingtoCF������
		
		sp.enableConfrim();
		
		sp.addTexttoOpp(givePanelOpp);
		sp.addTexttoMe(givePanelMe);
		sp.refreshAccordingToCF(acf);
		sp.repaint();
	}
	
	public void one_choose_over(){
		sp.addTexttoOpp(givePanelOpp);
		sp.addTexttoMe(givePanelMe);
		sp.refreshAccordingToCF(acf);
		sp.repaint();
		sp.addTexttoMe("����ʱ0.5����Զ��رմ�ҳ��!!!!");
		
		Timer tm=new Timer();
		TimerTask tt=new TimerTask() { 
			@Override
            public void run() {
				jf_select.setVisible(false);
				jf_select.dispose();
            }
		};
		tm.schedule(tt, 500);
		

	}	
	
	
	
	@Override
	public void run() {
		try{
			
			Socket ss=new Socket(serverIp,9798);
			OutputStream os=ss.getOutputStream();
			oos=new ObjectOutputStream(os);
			InputStream is=ss.getInputStream();
			ois=new ObjectInputStream(is);
			
			Message msg=new Message(0,nickName);
			oos.writeUnshared(msg);
			oos.reset();
			
			
			Recv_Thread rt=new Recv_Thread();
			Thread t1=new Thread(rt);
			t1.start();
			
			Handle_Msg_Thread hmt=new Handle_Msg_Thread();
			Thread t2=new Thread(hmt);
			t2.start();
			
			new Thread(new Show_OptionPane("���ڵȴ���ս��", "������ս��")).start();
			
			
						
			//following should be the ����
			
			
					
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void verify_confirm(int []sel,int count){
		if(count!=canSel){
			if (count<canSel){
				JOptionPane.showMessageDialog(jf_login, "ѡ��ĸ񶷼ҹ���!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(jf_login, "ѡ��ĸ񶷼ҹ���!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);	
			}
			return;
		}
		
		sp.disableConfirm();
		String toadd="��ѡ����:["+acf[sel[0]].fi.name+"]";
		if (count==2) toadd+=" and ["+acf[sel[1]].fi.name+"]\n";
		else toadd+="\n";
		sp.addTexttoMe(toadd);
		sp.CardBelongsToMe(sel[0]);
		if (count==2) sp.CardBelongsToMe(sel[1]);
		
		sp.disableAll();
		
		
		sp.repaint();
		
		
		
		
		
		//now send the result to the server
		Message msg_tell=new Message(2);
		msg_tell.identifier=what_i;
		msg_tell.i_info1=count;
		msg_tell.i_info2=sel[0];
		if (count==2) msg_tell.i_info3=sel[1];
		try {
			oos.writeUnshared(msg_tell);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}

	public void formationover(FighterInfo []fi) {
		Message msg=new Message(3);
		msg.fi=fi;
		try {
			oos.writeUnshared(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		Timer tm=new Timer();
		TimerTask tt=new TimerTask() { 
			@Override
            public void run() {
				System.out.println("here!");
				jf_formation.setVisible(false);
				jf_formation.dispose();
            }
		};
		tm.schedule(tt, 500);
		*/
		
	}
	
	
	public void Battle_Method(){
		
		jf_formation.setVisible(false);
		jf_formation.dispose();
		
		extra_info=new JTextArea();
		extra_info.setEditable(false);
		JFrame jf_extra=new JFrame();
		jf_extra.setTitle("��ʾ��ϸ��ս����Ϣ");
		JScrollPane jp_s=new JScrollPane(extra_info);
		jp_s.setVerticalScrollBarPolicy( 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		jf_extra.add(jp_s);
		jf_extra.setSize(480, 320);
		jf_extra.setLocationRelativeTo(sp);
		jf_extra.setVisible(true);
		
		//��һ��ʱ�򱻵���
		
		frame_battle = new JFrame();
		frame_battle.setTitle("��"+nickName+"�� vs ��"+challenger+"��!");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		if (what_i==1)
			bp_total=new BattlePanel(this,p1_f,p2_f);
		else
			bp_total=new BattlePanel(this,p2_f,p1_f);
		
		bp_total.setWhatI(what_i);
		
		
		frame_battle.add(bp_total);
		frame_battle.pack();
		frame_battle.setResizable(false);
		
		frame_battle.setVisible(true);
	}
	
	public void verify_attack_actions(int atk_index,int atkd_index){
		//SEND IT TO THE SERVER
		Message msg=new Message(4);
		msg.from=what_i;
		msg.i_info1=1;	//normal attack
		msg.i_info2=atk_index;
		msg.i_info3=atkd_index;
		try{
			oos.writeUnshared(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void verify_skill_actions(int atk_index, int atkd_index) {
		//SEND IT TO THE SERVER
		Message msg=new Message(4);
		msg.from=what_i;
		msg.i_info1=0;	//bisha Power!
		msg.i_info2=atk_index;
		msg.i_info3=atkd_index;
		try{
			oos.writeUnshared(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	

	public void end_my_turn() {
		Message msg=new Message(4);
		msg.i_info1=2;
		msg.from=what_i;
		
		bp_total.unSelectAll();
		try{
			oos.writeUnshared(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}


	
	

}
