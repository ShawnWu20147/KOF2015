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
		setTitle("ȭ��2015---����:������ && ϯʥ��");
		setResizable(false);
		setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent e) {
			      /*
				  int option=JOptionPane.showConfirmDialog(KOF2015_Client.this, "ȷ���˳���Ϸ?", "��ʾ ",JOptionPane.YES_NO_OPTION); 
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

		JLabel jl_server=new JLabel("��������ַ");
		JLabel jl_nickname=new JLabel("�û��ǳ�");
		final JTextField jtf_server=new JTextField("114.212.82.39");
		final JTextField jtf_nickname=new JTextField("abc");
		
		
		JPanel jt=new JPanel();
		jt.setLayout(new GridLayout(2,2));
		jt.add(jl_server);
		jt.add(jtf_server);
		jt.add(jl_nickname);
		jt.add(jtf_nickname);
		
		add(jt,BorderLayout.CENTER);
		
		
		
		
		JButton jb_conn=new JButton("��½��Ѱ��һ������");
		
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
					JOptionPane.showMessageDialog(null, "����һ������", "����", JOptionPane.INFORMATION_MESSAGE);
				} 
			}
		});
		
		
		setVisible(true);
		
		
		
	}
	
	public static void main(String[] args){
		new KOF2015_Client();
		
	}
}
