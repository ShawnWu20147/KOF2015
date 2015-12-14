package com.kof2015.client.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JPanel;

import com.common.Constants;
import com.common.UserInfo;
import com.common.message.IMessageListener;
import com.common.message.Message;
import com.common.message.MessageManager;
import com.common.message.MessageManagerClosedException;
import com.kof2015.client.AController;
import com.kof2015.client.DataManager;

/**
 * <p>����������½���̣�ֱ���ɹ�ͬ�������������ӣ�����ɵ�½��Ϣ������</p>
 * <p>�������ݣ�</p>
 * <p>������ݣ���½����Ϣ��������Ϣ��</p>
 * @author cellzero
 */
public class LoginController extends AController {
	
	public LoginController() {
	}
	
	public void setCallbacksOnPanel( JPanel panel ) {
		// ע������ϵĻص�����
		LoginPanel jpLogin = ( LoginPanel ) ( panel );
		
		jpLogin.addConnectionListener(new onConnectionButtonClicked(jpLogin));
	}
	
	class onConnectionButtonClicked implements ActionListener {
		
		private LoginPanel jpLogin;
		
		public onConnectionButtonClicked( LoginPanel jpLogin ) {
			this.jpLogin = jpLogin;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// ��ȡ��ǰ������е���Ϣ
			String strServer = jpLogin.getServerString();
			String strNickname = jpLogin.getNicknameString();
			
			// TODO У��������ȷ�ԣ�
			//	IP��ַ ��ʽ��ȷ��
			//	�ǳ� �Ƿ���ʲô����
			
			// �������߳�
			new Thread(new waitingSocket( strServer, strNickname )).start();
			
			// ����ť����Ϊ���ɰ�״̬
			jpLogin.setConnectButtonState(LoginPanel.CONNECTING_SERVER);
		}
		
		class waitingSocket implements Runnable
		{
			private String strServer;
			private String strNickname;
			
			public waitingSocket( String strServer, String strNickname ) {
				this.strServer = strServer;
				this.strNickname = strNickname;
			}
			
			@Override
			public void run() {
				// ����Ƿ��Ѿ�������
				UserInfo oldInfo = DataManager.getManager().getUserInfo(), newInfo = new UserInfo(strNickname);;
				MessageManager manager = MessageManager.getManager(oldInfo);
				
				// ����ͬ�������������ӣ�Socket��
				if( manager == null )
				{
					Socket socket = null;
					boolean success = true;
					String strError = "";
					try {
						socket = new Socket(strServer, Constants.PORT_NUMBER);
					} catch (UnknownHostException e1) {
						// TODO �ڽ�����ʾ�����IP��ַ����ȷ��
						strError = "���IP��ַ����ȷ��";
						success = false;
					} catch (IOException e1) {
						// TODO �ڽ�����ʾ����������Ƿ����
						strError = "��������Ƿ����";
						success = false;
					}
					
					// ����ʧ��
					if( !success )
					{
						// �û���ʾ��ȷ�Ϸ�������ַ֮���
						System.err.println( strError );
						jpLogin.setConnectButtonState(LoginPanel.WAITING_INPUT);
					}
					// ���ӳɹ�
					else
					{
						// ����Ƿ����û���
						DataManager.getManager().setUserInfo(newInfo);
						// ����Ϣ�����ߣ�MessageManager����ע��
						try {
							manager = MessageManager.createManager(newInfo, socket);
						} catch (IOException e) {
							// TODO ����������ʧ�ܣ����롢�������������
							System.err.println("������Ϣ������ʧ��...");
							return;
						}
					}
				} else {
					DataManager.getManager().setUserInfo(newInfo);
					MessageManager.rekeyManager(oldInfo, newInfo);
				}
				
				try {
					// ע�����������½�����Ƿ�ɹ�
					manager.addHandler( new loginMessageListener() );
					// ���͵�¼����
					manager.send(Message.generateLoginMessage( newInfo.getIdentifier() ));
				} catch (MessageManagerClosedException e) {
					// XXX ������ʾ��Ϣ
					jpLogin.setConnectButtonState(LoginPanel.WAITING_INPUT);
				}
			}
		}
		
		class loginMessageListener implements IMessageListener
		{
			@Override
			public void onReceviedMessage( Message msg ) {
				
				switch( msg.iType ) {
				// TODO ��¼ʧ��
				// ����1������ʾ��Ϣ�������û�
				//	���1���û���������
				//	���2���ظ����ǳ�
				//	���3������������ά����
				case Message.TYPE_LOGIN_EXIST:
					System.out.println( "�ظ����ǳ�" );
					break;
				case Message.TYPE_LOGIN_NONUSER:
					System.out.println( "�û���������" );
					break;
				// ��¼�ɹ�
				// ���ý���������complete��
				case Message.TYPE_LOGIN_SUCCESS:
					complete();
					break;
				}
				
				// ���°�ťΪ�ɰ�״̬
				if( msg.iType == Message.TYPE_LOGIN_EXIST || msg.iType == Message.TYPE_LOGIN_NONUSER || msg.iType == Message.TYPE_LOGIN_SUCCESS ) {
					try {
						MessageManager.getManager(DataManager.getManager().getUserInfo()).removeHandler( this );
					} catch (MessageManagerClosedException e) {
						// XXX ����ʲô��������
					}
					jpLogin.setConnectButtonState(LoginPanel.WAITING_INPUT);
				}
			}
			
		}
	}
}
