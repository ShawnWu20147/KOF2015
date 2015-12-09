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
	
	public void showIt(){
		setVisible(true);
	}
	
	public KOF2015_Client(){
		setSize(400,100);
		setTitle("拳皇2015---作者:武翔宇 && 席圣渠");
		setResizable(false);
		setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent e) {
			      /*
				  int option=JOptionPane.showConfirmDialog(KOF2015_Client.this, "确定退出游戏?", "提示 ",JOptionPane.YES_NO_OPTION); 
			      if(option==JOptionPane.YES_OPTION){
			     // if(e.getWindow()   ==   MainFrame.this){   
			          System.exit(0); 
			      } 
			      else{
			    	  KOF2015_Client.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			      }*/ 
				  System.exit(0);
			   }
			  });

		JLabel jl_server=new JLabel("服务器地址");
		JLabel jl_nickname=new JLabel("用户昵称");
		final JTextField jtf_server=new JTextField("114.212.82.39");
		final JTextField jtf_nickname=new JTextField(generateNickname());
		
		
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
					
					ClientOneGame og=new ClientOneGame(KOF2015_Client.this,servers,nickname);
					Thread t=new Thread(og);
					t.start();		
					KOF2015_Client.this.setVisible(false);	
					
					
					//t.join();
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "遇到一个问题", "错误", JOptionPane.INFORMATION_MESSAGE);
				} 
			}
		});
		
		
		setVisible(true);
		
		
		
	}
	
	private String generateNickname()
	{
		final String[] prefix = {
				"昂戳的", "不懂规矩的", "突破天际的", "倒霉的", "暗爽的", "强拆的", "穿越的",
				"至高的", "忠厚的", "开朗的", "大方的", "成熟的", "善良的", "开放的", "好吃的",
				"婆婆妈妈的", "抑郁的", "贪小便宜的", "害羞的", "热心的", "阴险的", "幼稚的"
		};
		final String[] postfix = {
				"草稚京", "二阶堂红丸", "大门五郎",
				"特瑞", "安迪", "东丈",
				"坂崎良", "罗伯特", "坂崎由莉",
				"莉安娜", "拉尔夫", "克拉克",
				"麻宫雅典娜", "椎拳崇", "镇元斋",
				"金", "不知火舞", "神乐千鹤",
				"金家藩", "陈国汗", "蔡保健",
				"七伽社", "夏尔米", "克里斯",
				"山崎龙二", "玛丽", "比利",
				"八神庵", "大蛇", "矢吹真吾",
				"大师"
		};
		
		String nickname;
		int r1 = (int)(Math.random() * prefix.length);
		int r2 = (int)(Math.random() * postfix.length);
		nickname = prefix[ r1 ] + postfix[ r2 ];
		
		return nickname;
	}
	
	public static void main(String[] args){
		new KOF2015_Client();
		
	}
}
