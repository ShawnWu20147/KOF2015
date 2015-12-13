package com.kof2015.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.kof2015.client.login.LoginController;
import com.kof2015.client.login.LoginPanel;

/**
 * TODO Ҳ������ø�״̬��ģʽ��
 * @author cellzero
 *
 */
public class Client {
	
	public static void main(String[] args) {
		System.out.println("Hello World!");
		new Client();
	}
	
	public Client() {
		// ��������
		jfGameFrame = new JFrame();
		jfGameFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO �˳���Ŀǰ���ԣ��رմ��ڡ�ǿ���˳���Alt + F4�������ǽ������̣�����������������
				// ���������쳣�˳���������ǰ��״̬
				// û�е�¼������˳�
				// �Ѿ���¼��û�ж�ս���ر�ͬ������������
				// ���ڶ�ս���쳣�˳�����û�ж������������и�
				exitEmergency();
			}
		});
		jfGameFrame.setVisible(true);
		
		openLoginStage();
	}
	
	/**
	 * TODO �����쳣�˳������ 
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
				// TODO �л�����һ����
				System.out.println(" ��¼�ɹ� ");
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
