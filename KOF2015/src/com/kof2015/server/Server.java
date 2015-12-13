package com.kof2015.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.common.Constants;
import com.common.UserInfo;
import com.common.message.IMessageListener;
import com.common.message.Message;
import com.common.message.MessageManager;

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
			// TODO 
			System.out.println("ע������˿ڷ����쳣");
			System.exit(-1);
		}
		
		// ���������ͻ�������
		while( true ) {
			try {
				Socket socket = ssListener.accept();
				new ClientHandler(socket);
			} catch (IOException e) {
				// TODO ��֪����ʲô����ᴥ��
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
				// TODO 
			}
		}
		
		class onLoginMessage implements IMessageListener {

			@Override
			public void onReceviedMessage( Message msg ) {
				if( msg.iType == Message.TYPE_LOGIN ) {
					UserInfo info = new UserInfo( msg.strMsg );
					System.out.println( "��ͼ��¼���ǳƣ�" + info.getIdentifier() );
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
					// TODO ��֪��Ϊʲô��ʧ��
					System.err.println("ServerSocket�˳�ʱ�������쳣");
				}
			}
		}
		
	}
}
