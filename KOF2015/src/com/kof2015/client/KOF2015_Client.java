package com.kof2015.client;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.common.Message;
import com.kof2015.ui.BattlePanel;

public class KOF2015_Client extends JFrame{
	
	int what_i;
	
	
	class Wait_for_Over implements Runnable{
		SelectPanel sp;
		JFrame jf;
		public Wait_for_Over(SelectPanel sp,JFrame jf){
			this.sp=sp;
			this.jf=jf;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (sp.over==0){
				
			};
			if (sp.over==-1){
				jf.setVisible(false);
				jf.dispose();
				
				KOF2015_Client.this.setVisible(true);
			}
			else{
				// go to 安排窗口
			}
		}
	}
	
	public KOF2015_Client(){
		setSize(400,100);
		setTitle("拳皇2015---作者:武翔宇 && 席胜渠");
		setResizable(false);
		setLocationRelativeTo(null);
		
		/*
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent e) {
			      int option=JOptionPane.showConfirmDialog(KOF2015_Client.this, "确定退出游戏?", "提示 ",JOptionPane.YES_NO_OPTION); 
			      if(option==JOptionPane.YES_OPTION){
			     // if(e.getWindow()   ==   MainFrame.this){   
			          System.exit(0); 
			      } 
			      else{
			    	  KOF2015_Client.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			      }
			   }
			  });
			  
			  */
		
		
		JLabel jl_server=new JLabel("服务器地址");
		JLabel jl_nickname=new JLabel("用户昵称");
		final JTextField jtf_server=new JTextField("114.212.82.39");
		final JTextField jtf_nickname=new JTextField("昂戳");
		
		
		JPanel jt=new JPanel();
		jt.setLayout(new GridLayout(2,2));
		jt.add(jl_server);
		jt.add(jtf_server);
		jt.add(jl_nickname);
		jt.add(jtf_nickname);
		
		add(jt,BorderLayout.CENTER);
		
		
		
		
		JButton jb_conn=new JButton("登陆并寻找一场比赛");
		
		add(jb_conn,BorderLayout.SOUTH);
		
		jb_conn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String servers=jtf_server.getText();
				String nickname=jtf_nickname.getText();
				
				try {
					
					Socket ss=new Socket(servers,9798);
					OutputStream os=ss.getOutputStream();
					final ObjectOutputStream oos=new ObjectOutputStream(os);
					
					InputStream is=ss.getInputStream();
					final ObjectInputStream ois=new ObjectInputStream(is);
					
					Message msg=new Message(0);
					msg.msg=nickname;
					oos.writeObject(msg);
					oos.flush();
					
					
					JOptionPane.showMessageDialog(null, "正在等待别的玩家", "已连接战网", JOptionPane.INFORMATION_MESSAGE);
					Message m=(Message) ois.readObject();
					what_i=m.i_info1;
					String others=(m.i_info1==1)?m.info2:m.info1;
					JOptionPane.showMessageDialog(null, "准备开始选牌", "已连接上:"+others, JOptionPane.INFORMATION_MESSAGE);
					
					
					setVisible(false);
					
					final JFrame frame = new JFrame();
					frame.setSize(900, 600);
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					
					SelectPanel sp=new SelectPanel(ois, oos);
					frame.add(sp);
					//frame.pack();
					frame.setVisible(true);
					
					new Thread(new Wait_for_Over(sp,frame)).start();
					
					
					
					
					
					
					
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "遇到一个问题", "错误", JOptionPane.INFORMATION_MESSAGE);
					
					
				} 
				
				
				
			}
		});
		
		
		
		
		setVisible(true);
	}
	
	public static void main(String[] args){
		new KOF2015_Client();
		
	}
}
