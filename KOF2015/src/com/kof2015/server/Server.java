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
		
		// 注册程序结束的响应方法
		Runtime.getRuntime().addShutdownHook(new Thread(new onExit()));
		
		// 注册监听端口
		try {
			ssListener = new ServerSocket( Constants.PORT_NUMBER );
		} catch (IOException e) {
			// XXX  
			System.out.println("注册监听端口发生异常");
			System.exit(-1);
		}
		
		// 持续监听客户端请求
		while( true ) {
			try {
				Socket socket = ssListener.accept();
				new ClientHandler(socket);
			} catch (IOException e) {
				// XXX 不知道在什么情况会触发
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
				// 创建失败，则没有创建消息管理器，也没有将该消息管理器储存在哈希表中
				// 客户端断开连接，不需要再创建该处理器
				// 不需要做额外处理，等待程序自然结束
			} catch ( MessageManagerClosedException e ) {
				// 由连接断开引发的异常
				// FIXME 没有发送任何数据就断开了，难道是网络攻击？
			}
		}
		
		class onLoginMessage implements IMessageListener {

			@Override
			public void onReceviedMessage( Message msg ) {
				
				if( msg.iType == Message.TYPE_LOGIN ) {
					UserInfo info = new UserInfo( msg.strMsg );
					System.out.println( "试图登录的昵称：" + info.getIdentifier() );
					try {
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
					} catch (MessageManagerClosedException e) {
						// 由连接断开引发的异常，该消息管理器再不能使用
						// FIXME 没有断线重连的情况下，不需要额外处理
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
					// XXX 不知道为什么会失败
					System.err.println("ServerSocket退出时，发生异常");
				}
			}
		}
		
	}
}
