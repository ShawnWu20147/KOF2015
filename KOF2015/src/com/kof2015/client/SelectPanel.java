package com.kof2015.client;

import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.common.Message;
import com.kof2015.ui.BattlePanel;
import com.kof2015.ui.BattlerPanel;

public class SelectPanel extends JPanel {
	ObjectInputStream	ois;
	ObjectOutputStream	oos;
	
	Vector<Message> msg_list;
	
	Thread t1;
	Thread t2;
	
	public volatile int over;
	
	class Recv_Msg implements Runnable{

		@Override
		public void run() {
			try {
				while (true){
					Message msg=(Message)ois.readObject();
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
					case -1:
						
						break;
					case 0:
						System.out.println("Impossible!");
						System.exit(-1);
					case 1:
						int who=toh.from;
						JOptionPane.showMessageDialog(null, "对方不懂规矩,强退!", "无法开始", JOptionPane.INFORMATION_MESSAGE);
						t1.stop();
						t2.stop();
						over=-1;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					}
				}
			}
			
		}
		
	}
	
	
	public SelectPanel(ObjectInputStream ois,ObjectOutputStream oos){
		
		over=0;
		
		this.ois=ois;
		this.oos=oos;
		msg_list=new Vector<Message>();
		
		t1=new Thread(new Recv_Msg());
		t2=new Thread(new Handle_Msg());
		t1.start();
		t2.start();
		
		setLayout(new GridLayout(3, 8));
		
		JLabel []jl=new JLabel[16];
		for (int i=0;i<16;i++){
			jl[i]=new JLabel(String.valueOf(i));
			add(jl[i]);
		}
		
		JTextArea jta=new JTextArea();
		add(jta);
		
		
		
		
	}
}
