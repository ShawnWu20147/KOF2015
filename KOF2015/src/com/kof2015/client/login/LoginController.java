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
 * <p>控制整个登陆过程，直到成功同服务器建立连接，并完成登陆信息交互。</p>
 * <p>输入数据：</p>
 * <p>输出数据：登陆者信息、对手信息。</p>
 * @author cellzero
 */
public class LoginController extends AController {
	
	public LoginController() {
	}
	
	public void setCallbacksOnPanel( JPanel panel ) {
		// TODO 注册界面上的回调函数
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
			
			// 将按钮设置为不可按状态
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
				// 尝试同服务器建立连接（Socket）
				// TODO 开启新线程，尝试Socket连接
				
				Socket socket = null;
				boolean success = true;
				try {
					socket = new Socket(strServer, Constants.PORT_NUMBER);
				} catch (UnknownHostException e1) {
					// TODO 提示：检查IP地址的正确性
					System.err.println("检查IP地址的正确性");
					success = false;
				} catch (IOException e1) {
					// TODO 提示：检查网络是否可用
					System.err.println("检查网络是否可用");
					success = false;
				}
				
				// 连接失败
				if( !success )
				{
					// 用户提示：确认服务器地址之类的
				}
				// 连接成功
				else
				{
//					// 检查是否有用户名
//					UserInfo info = new UserInfo(strNickname);
//					DataManager.getManager().setUserInfo(info);
//					// 在消息管理者（MessageManager）中注册
//					MessageManager manager = MessageManager.createManager(info, socket);
					// TODO 发送登录请求
					//manager.send();
					// TODO 注册监听器：登陆操作是否成功
					//manager.addHandler();
				}
			}
		}
		
		class loginMessageListener implements IMessageListener
		{
			@Override
			public void onReceviedMessage( Message msg ) {
				// TODO 更新按钮为可按状态
				
				// TODO 登录成功
				// 调用结束方法（complete）
				complete();
				
				// TODO 登录失败
				// 处理1：将提示信息反馈给用户
				//	情况1：用户名不存在
				//	情况2：重复的昵称
				// 处理2：断开当前连接
				MessageManager.closeManager(DataManager.getManager().getUserInfo());
			}
			
		}
	}
}
