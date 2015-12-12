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
import com.common.message.MessageOrigin;
import com.common.message.MessageManager;
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
		// TODO ע������ϵĻص�����
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
			
			// ����ť����Ϊ���ɰ�״̬
		}
		
		class waitingSocket implements Runnable
		{
			private String strServer;
			private String strNickname;
			
			public waitingSocket() {
				// TODO Auto-generated constructor stub
			}
			
			@Override
			public void run() {
				// ����ͬ�������������ӣ�Socket��
				// TODO �������̣߳�����Socket����
				
				Socket socket = null;
				boolean success = true;
				try {
					socket = new Socket(strServer, Constants.PORT_NUMBER);
				} catch (UnknownHostException e1) {
					// TODO ��ʾ�����IP��ַ����ȷ��
					System.err.println("���IP��ַ����ȷ��");
					success = false;
				} catch (IOException e1) {
					// TODO ��ʾ����������Ƿ����
					System.err.println("��������Ƿ����");
					success = false;
				}
				
				// ����ʧ��
				if( !success )
				{
					// �û���ʾ��ȷ�Ϸ�������ַ֮���
				}
				// ���ӳɹ�
				else
				{
//					// ����Ƿ����û���
//					UserInfo info = new UserInfo(strNickname);
//					DataManager.getManager().setUserInfo(info);
//					// ����Ϣ�����ߣ�MessageManager����ע��
//					MessageManager manager = MessageManager.createManager(info, socket);
					// TODO ���͵�¼����
					//manager.send();
					// TODO ע�����������½�����Ƿ�ɹ�
					//manager.addHandler();
				}
			}
		}
		
		class loginMessageListener implements IMessageListener
		{
			@Override
			public void onReceviedMessage( Message msg ) {
				// TODO ���°�ťΪ�ɰ�״̬
				
				// TODO ��¼�ɹ�
				// ���ý���������complete��
				complete();
				
				// TODO ��¼ʧ��
				// ����1������ʾ��Ϣ�������û�
				//	���1���û���������
				//	���2���ظ����ǳ�
				// ����2���Ͽ���ǰ����
				MessageManager.closeManager(DataManager.getManager().getUserInfo());
			}
			
		}
	}
}
