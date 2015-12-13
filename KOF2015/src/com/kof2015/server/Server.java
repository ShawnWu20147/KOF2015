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
		
		// 注册程序结束的响应方法
		Runtime.getRuntime().addShutdownHook(new Thread(new onExit()));
		
		// 注册监听端口
		try {
			ssListener = new ServerSocket( Constants.PORT_NUMBER );
		} catch (IOException e) {
			// TODO 
			System.out.println("注册监听端口发生异常");
			System.exit(-1);
		}
		
		// 持续监听客户端请求
		while( true ) {
			try {
				Socket socket = ssListener.accept();
				new ClientHandler(socket);
			} catch (IOException e) {
				// TODO 不知道在什么情况会触发
				System.err.println("监听客户端请求时，发生异常。");
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
					System.out.println( "试图登录的昵称：" + info.getIdentifier() );
					// 如果用户名已经有了
					if( UserManager.contains( info ) ) {
						mManager.send(Message.generateLoginFeedback( Message.TYPE_LOGIN_EXIST ));
					}
					// 登录成功
					else {
						UserManager.add(info);
						mManager.send(Message.generateLoginFeedback( Message.TYPE_LOGIN_SUCCESS ));
						mManager.removeHandler( this );
						MessageManager.addManager( info, mManager );
						// TODO 添加新的处理器
					}
				}
			}
		}
	}
	
	class onExit implements Runnable {

		@Override
		public void run() {
			// TODO 程序退出钩子，关闭同客户端连接，并释放资源
			System.out.println("exiting.");
			
			if( ssListener != null ) {
				try {
					ssListener.close();
				} catch (IOException e) {
					// TODO 不知道为什么会失败
					System.err.println("ServerSocket退出时，发生异常");
				}
			}
		}
		
	}
}
