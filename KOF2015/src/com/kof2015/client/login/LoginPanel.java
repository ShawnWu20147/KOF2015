package com.kof2015.client.login;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.common.UserInfo;
import com.kof2015.client.ImageManager;

public class LoginPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6307155633151534741L;
	
	private JTextField jtfServer;
	private JTextField jtfNickname;
	private JButton jbConnection;

	public LoginPanel() {
		
		setLayout(new BorderLayout(10, 5));
		
		JLabel logoLabel = new JLabel(ImageManager.getManager().getImage("logo_small.jpg"));
		add(logoLabel, BorderLayout.CENTER);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BorderLayout(5, 5));
		{
			JPanel jt=new JPanel();
			jt.setLayout(new GridLayout(2, 2, 5, 5));
			{
				JLabel jl_server=new JLabel("服务器地址");
				JLabel jl_nickname=new JLabel("用户昵称");
//				jtfServer=new JTextField("114.212.82.39");
				jtfServer=new JTextField("127.0.0.1");
				jtfNickname=new JTextField(UserInfo.generateNickname());
				
				jt.add(jl_server);
				jt.add(jtfServer);
				jt.add(jl_nickname);
				jt.add(jtfNickname);
			}
			loginPanel.add(jt,BorderLayout.CENTER);
			
			jbConnection=new JButton("登陆并寻找一场比赛");
			loginPanel.add(jbConnection,BorderLayout.SOUTH);
		}
		add(loginPanel, BorderLayout.SOUTH);
	}
	
	public void addConnectionListener( ActionListener listener )
	{
		jbConnection.addActionListener(listener);
	}
	
	public String getServerString()
	{
		return jtfServer.getText();
	}
	
	public String getNicknameString()
	{
		return jtfNickname.getText();
	}
}
