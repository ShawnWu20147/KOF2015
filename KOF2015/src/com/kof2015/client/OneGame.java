package com.kof2015.client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.common.Message;


public class OneGame implements Runnable{
	
	String serverIp,nickName,challenger;
	int what_i;
	
	volatile boolean other_conn;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	volatile Vector<Message> msg_list;
	
	JFrame jf;
	
	public OneGame(JFrame jf,String serverIp,String nickName){
		this.jf=jf;
		this.serverIp=serverIp;
		this.nickName=nickName;
		
		other_conn=false;
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
						System.out.println(msg.msg+"\t"+msg.s_info1);
						break;
					case 0:
						//login
						what_i=msg.i_info1;
						challenger=(msg.i_info1==1)?msg.s_info2:msg.s_info1;
						other_conn=true;
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

					Message msg=(Message)ois.readObject();
					msg_list.add(msg);
					
					
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
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
			oos.writeObject(msg);
			
			
			
			Recv_Thread rt=new Recv_Thread();
			Thread t1=new Thread(rt);
			t1.start();
			
			Handle_Msg_Thread hmt=new Handle_Msg_Thread();
			Thread t2=new Thread(hmt);
			t2.start();
			
			new Thread(new Show_OptionPane("正在等待挑战者", "已连接战网")).start();
			
			
			
			
			while (!other_conn);
			
			System.out.println("conn other!");
			
			new Thread(new Show_OptionPane("准备开始选牌", "已与对手["+challenger+"]连接")).start();
			
			//jframe.show
			
			
			
			
			
			
			
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
