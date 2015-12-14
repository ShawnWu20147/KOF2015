package com.kof2015.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.common.message.Message;
import com.kof2015.client.login.LoginController;
import com.kof2015.client.login.LoginPanel;

/**
 * TODO 也许可以用个状态机模式。
 * @author cellzero
 *
 */
public class Client {
	
	public static void main(String[] args) {
		System.out.println("Hello World!");
//		new Client();
		
		Message msg = Message.generateLoginMessage("cellzero");
		try {
		 ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File("E:/Person.txt")));
				          oo.writeObject(msg);
				          System.out.println("Person对象序列化成功！");
				          oo.close();
		} catch (IOException e ){
			
		}
	}
	
	public Client() {
		// 开启窗体
		jfGameFrame = new JFrame();
		jfGameFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO 退出。目前测试，关闭窗口、强制退出（Alt + F4），或是结束进程，都会调用这个方法。
				// 正常还是异常退出，看程序当前的状态
				// 没有登录：随便退出
				// 已经登录，没有对战：关闭同服务器的连接
				// 正在对战：异常退出，若没有断线重连，则判负
				exitEmergency();
			}
		});
		jfGameFrame.setVisible(true);
		
		openLoginStage();
	}
	
	/**
	 * TODO 处理异常退出的情况 
	 */
	public void exitEmergency()
	{
		System.exit( 0 );
	}
	
	private JFrame jfGameFrame;
	private JPanel jpCurrentPanel;
	
	private void openNewStage( JPanel newPanel, AController newController, ICompleteListener listener )
	{
		if(jpCurrentPanel != null)
			jfGameFrame.remove(jpCurrentPanel);
		
		newController.addCompleteListener(listener);
		newController.setCallbacksOnPanel(newPanel);
		
		if(newPanel != null)
		{
			jfGameFrame.add(newPanel);
			jfGameFrame.pack();
		}
		jpCurrentPanel = newPanel;
	}
	
	private void openLoginStage()
	{
		openNewStage(new LoginPanel(), new LoginController(), new ICompleteListener() {
			@Override
			public void onComplete() {
				// TODO 切换到下一场景
				System.out.println(" 登录成功 ");
				openMatchStage();
			}
		});
	}
	
	private void openMatchStage()
	{}
	
	private void openChooseStage()
	{}
	
	private void openFormationStage()
	{}
	
	private void openBattleStage()
	{}
}
