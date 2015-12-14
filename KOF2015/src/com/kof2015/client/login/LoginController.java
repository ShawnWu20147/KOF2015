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
 * <p>控制整个登陆过程，直到成功同服务器建立连接，并完成登陆信息交互。</p>
 * <p>输入数据：</p>
 * <p>输出数据：登陆者信息、对手信息。</p>
 * @author cellzero
 */
public class LoginController extends AController {
	
	public LoginController() {
	}
	
	public void setCallbacksOnPanel( JPanel panel ) {
		// 注册界面上的回调函数
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
			// 获取当前输入框中的信息
			String strServer = jpLogin.getServerString();
			String strNickname = jpLogin.getNicknameString();
			
			// TODO 校验输入正确性：
			//	IP地址 格式正确性
			//	昵称 是否有什么限制
			
			// 开启新线程
			new Thread(new waitingSocket( strServer, strNickname )).start();
			
			// 将按钮设置为不可按状态
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
				// 检查是否已经有连接
				UserInfo oldInfo = DataManager.getManager().getUserInfo(), newInfo = new UserInfo(strNickname);;
				MessageManager manager = MessageManager.getManager(oldInfo);
				
				// 尝试同服务器建立连接（Socket）
				if( manager == null )
				{
					Socket socket = null;
					boolean success = true;
					String strError = "";
					try {
						socket = new Socket(strServer, Constants.PORT_NUMBER);
					} catch (UnknownHostException e1) {
						// TODO 在界面提示：检查IP地址的正确性
						strError = "检查IP地址的正确性";
						success = false;
					} catch (IOException e1) {
						// TODO 在界面提示：检查网络是否可用
						strError = "检查网络是否可用";
						success = false;
					}
					
					// 连接失败
					if( !success )
					{
						// 用户提示：确认服务器地址之类的
						System.err.println( strError );
						jpLogin.setConnectButtonState(LoginPanel.WAITING_INPUT);
					}
					// 连接成功
					else
					{
						// 检查是否有用户名
						DataManager.getManager().setUserInfo(newInfo);
						// 在消息管理者（MessageManager）中注册
						try {
							manager = MessageManager.createManager(newInfo, socket);
						} catch (IOException e) {
							// TODO 管理器创建失败，输入、输出流出现问题
							System.err.println("创建消息管理器失败...");
							return;
						}
					}
				} else {
					DataManager.getManager().setUserInfo(newInfo);
					MessageManager.rekeyManager(oldInfo, newInfo);
				}
				
				try {
					// 注册监听器：登陆操作是否成功
					manager.addHandler( new loginMessageListener() );
					// 发送登录请求
					manager.send(Message.generateLoginMessage( newInfo.getIdentifier() ));
				} catch (MessageManagerClosedException e) {
					// XXX 给出提示信息
					jpLogin.setConnectButtonState(LoginPanel.WAITING_INPUT);
				}
			}
		}
		
		class loginMessageListener implements IMessageListener
		{
			@Override
			public void onReceviedMessage( Message msg ) {
				
				switch( msg.iType ) {
				// TODO 登录失败
				// 处理1：将提示信息反馈给用户
				//	情况1：用户名不存在
				//	情况2：重复的昵称
				//	情况3：服务器正在维护？
				case Message.TYPE_LOGIN_EXIST:
					System.out.println( "重复的昵称" );
					break;
				case Message.TYPE_LOGIN_NONUSER:
					System.out.println( "用户名不存在" );
					break;
				// 登录成功
				// 调用结束方法（complete）
				case Message.TYPE_LOGIN_SUCCESS:
					complete();
					break;
				}
				
				// 更新按钮为可按状态
				if( msg.iType == Message.TYPE_LOGIN_EXIST || msg.iType == Message.TYPE_LOGIN_NONUSER || msg.iType == Message.TYPE_LOGIN_SUCCESS ) {
					try {
						MessageManager.getManager(DataManager.getManager().getUserInfo()).removeHandler( this );
					} catch (MessageManagerClosedException e) {
						// XXX 好像什么都不用做
					}
					jpLogin.setConnectButtonState(LoginPanel.WAITING_INPUT);
				}
			}
			
		}
	}
}
