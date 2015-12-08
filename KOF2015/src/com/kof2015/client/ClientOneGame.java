package com.kof2015.client;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.common.Message;


public class ClientOneGame implements Runnable{
	
	String serverIp,nickName,challenger;
	int what_i;
	

	
	volatile int canSel;
	volatile String givePanelMe,givePanelOpp;
	volatile ChooseFighter[] acf;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	volatile Vector<Message> msg_list;
	
	JFrame jf;
	
	SelectPanel sp;
	
	int round;	//add
	
	public ClientOneGame(JFrame jf,String serverIp,String nickName){
		this.jf=jf;
		this.serverIp=serverIp;
		this.nickName=nickName;
		

		canSel=0;
		msg_list=new Vector<Message>();
		
		round=0;
	}
	
	class Show_OptionPane implements Runnable{
		String con,cap;
		public Show_OptionPane(String content,String cap){
			this.con=content;
			this.cap=cap;
		}
		@Override
		public void run() {
			JOptionPane.showMessageDialog(jf, con, cap, JOptionPane.INFORMATION_MESSAGE);
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
							
							one_choose_me();//add
							
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
					System.out.println("收到一个了:"+msg.type);
					
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void other_has_conn(){
		System.out.println("conn other!");
		new Thread(new Show_OptionPane("开始选牌", "已与对手["+challenger+"]连接")).start();		
	}
	
	public void setup_choose_finish(){
		JFrame jf=new JFrame();
		//jf.setLocationRelativeTo(null);
		jf.setSize(1200, 800);
		jf.setResizable(false);
		jf.setTitle(nickName+":选人阶段,首先双方各选1人,然后双方轮流各选2人,直到结束");
		
		sp=new SelectPanel(what_i,this,acf);
		sp.addTexttoMe("准备开始格斗家挑选\n"+givePanelMe);
		
		jf.add(sp);
		jf.setVisible(true);
	}
	
	public void one_choose_me(){
		sp.addTexttoOpp(givePanelOpp);
		sp.addTexttoMe(givePanelMe);
		sp.refreshAccordingToCF(acf);
		sp.repaint();
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
			
			new Thread(new Show_OptionPane("正在等待挑战者", "已连接战网")).start();
			
			
						
			//following should be the 布阵
			
			
					
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void verify_confirm(int []sel,int count){
		if(count!=canSel){
			if (count<canSel){
				JOptionPane.showMessageDialog(jf, "选择的格斗家过少!", "提示", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(jf, "选择的格斗家过多!", "提示", JOptionPane.INFORMATION_MESSAGE);	
			}
			return;
		}
		String toadd="你选择了:["+acf[sel[0]].fi.name+"]";
		if (count==2) toadd+=" and ["+acf[sel[1]].fi.name+"]\n";
		else toadd+="\n";
		sp.addTexttoMe(toadd);
		sp.CardBelongsToMe(sel[0]);
		if (count==2) sp.CardBelongsToMe(sel[1]);
		
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
	
	

}
