package com.kof2015.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.common.Constants;
import com.common.UserInfo;
import com.common.message.IMessageListener;
import com.common.message.Message;
import com.common.message.MessageManager;
import com.common.message.MessageManagerClosedException;

public class Server {
	
	public static void main(String[] args) {
		new Server();
	}
	
	private ServerSocket ssListener = null;
	
	public Server() {
		System.out.println("server start.");
		
		// ע������������Ӧ����
		Runtime.getRuntime().addShutdownHook(new Thread(new onExit()));
		
		// ע������˿�
		try {
			ssListener = new ServerSocket( Constants.PORT_NUMBER );
		} catch (IOException e) {
			// XXX  
			System.out.println("ע������˿ڷ����쳣");
			System.exit(-1);
		}
		
		// ���������ͻ�������
		while( true ) {
			try {
				Socket socket = ssListener.accept();
				new ClientHandler(socket);
			} catch (IOException e) {
				// XXX ��֪����ʲô����ᴥ��
				System.err.println("�����ͻ�������ʱ�������쳣��");
			}
		}
	}
	
	class ClientHandler {
		
		private MessageManager mManager;
		
		public ClientHandler( Socket socket ) {
			try {
				mManager = MessageManager.createManager( null, socket );
				mManager.addHandler(new onLoginMessage());
			} catch( IOException e ) {
				// ����ʧ�ܣ���û�д�����Ϣ��������Ҳû�н�����Ϣ�����������ڹ�ϣ����
				// �ͻ��˶Ͽ����ӣ�����Ҫ�ٴ����ô�����
				// ����Ҫ�����⴦���ȴ�������Ȼ����
			} catch ( MessageManagerClosedException e ) {
				// �����ӶϿ��������쳣
				// FIXME û�з����κ����ݾͶϿ��ˣ��ѵ������繥����
			}
		}
		
		class onLoginMessage implements IMessageListener {

			@Override
			public void onReceviedMessage( Message msg ) {
				
				if( msg.iType == Message.TYPE_LOGIN ) {
					UserInfo info = new UserInfo( msg.strMsg );
					System.out.println( "��ͼ��¼���ǳƣ�" + info.getIdentifier() );
					try {
						// ����û����Ѿ�����
						if( UserManager.contains( info ) ) {
							mManager.send(Message.generateLoginFeedback( Message.TYPE_LOGIN_EXIST ));
						}
						// ��¼�ɹ�
						else {
							UserManager.add(info);
							mManager.send(Message.generateLoginFeedback( Message.TYPE_LOGIN_SUCCESS ));
							mManager.removeHandler( this );
							MessageManager.addManager( info, mManager );
							// TODO ����µĴ�����
						}
					} catch (MessageManagerClosedException e) {
						// �����ӶϿ��������쳣������Ϣ�������ٲ���ʹ��
						// FIXME û�ж�������������£�����Ҫ���⴦��
					}
				}
			}
		}
	}
	
	class onExit implements Runnable {

		@Override
		public void run() {
			// TODO �����˳����ӣ��ر�ͬ�ͻ������ӣ����ͷ���Դ
			System.out.println("exiting.");
			
			if( ssListener != null ) {
				try {
					ssListener.close();
				} catch (IOException e) {
					// XXX ��֪��Ϊʲô��ʧ��
					System.err.println("ServerSocket�˳�ʱ�������쳣");
				}
			}
		}
		
	}
}
