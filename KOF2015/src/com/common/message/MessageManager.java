package com.common.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.common.Constants;
import com.common.UserInfo;

public class MessageManager {
	
	private static HashMap<UserInfo, MessageManager> mapGlobalConnections = 
			new HashMap<UserInfo, MessageManager>();
	
	private Socket connection;
	private MessageSender sender;
	private MessageReceiver receiver;
	private MessageHandler handler;
	
	private MessageManager()
	{}
	
	public static MessageManager getManager( UserInfo userInfo )
	{
		return mapGlobalConnections.get(userInfo);
	}
	
	public static MessageManager createManager( UserInfo userInfo, Socket socket )
	{
		MessageManager manager = new MessageManager();
		
		manager.connection = socket;
		// TODO �������ֽ���������������Щ�߳�
		
		mapGlobalConnections.put(userInfo, manager);
		
		return manager;
	}
	
	public static void rekeyManager( UserInfo oldkey, UserInfo newkey )
	{
		MessageManager manager = getManager(oldkey);
		
		mapGlobalConnections.remove(oldkey);
		mapGlobalConnections.put(newkey, manager);
	}
	
	public static boolean closeManager( UserInfo userInfo )
	{
		return false;
	}
	
	/**
	 * 
	 * @param strIpAddress IP��ַ���ַ������������ַ�������ȷ�ԡ�
	 * @return �����Ƿ�ɹ���
	 */
	public boolean connect( String strIpAddress )
	{
		try {
			disconnect();
			connection = new Socket(strIpAddress, Constants.PORT_NUMBER);
		} catch (UnknownHostException e) {
			// TODO 
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean disconnect()
	{
		if( connection == null )
			return true;
		
		try {
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			
			connection.getInputStream().close();
			connection.close();
			
			return true;
		} catch (IOException e) {
			// TODO ��δ����쳣����
		}
		
		return false;
	}
	
	class MessageSender implements Runnable {
		
		private volatile LinkedList<MessageOrigin> buffer;
		private ObjectOutputStream out;
		
		private boolean exit;
		
		public MessageSender(OutputStream out ) throws IOException {
			buffer = new LinkedList<MessageOrigin>();
			this.out = new ObjectOutputStream(out);
			
			exit = false;
		}
		
		public void send(MessageOrigin msg)
		{
			buffer.add(msg);
			buffer.notify();
		}
		
		public void close()
		{
			exit = true;
		}
		
		@Override
		public void run() {
			
			while( !exit ) {
				if( buffer.isEmpty() )
				{
					try {
						buffer.wait();
					} catch (InterruptedException e) {
						// TODO ��Ҳ��֪����ʲô= =
						e.printStackTrace();
					}
				}
				else
				{
					MessageOrigin msg = buffer.removeFirst();
					try {
						out.writeObject(msg);
						out.flush();
					} catch (IOException e) {
						// TODO ��Ҳ��֪����ʲô= =
						e.printStackTrace();
					}
				}
			}
			
			try {
				out.close();
			} catch (IOException e) {
				// TODO ������ʱ�򣬷����쳣
				e.printStackTrace();
			}
		}
	}
	
	class MessageReceiver implements Runnable {
		
		private MessageHandler handler;
		private ObjectInputStream in;
		
		public MessageReceiver(InputStream in) throws IOException {
			handler = new MessageHandler();
			this.in = new ObjectInputStream(in);
			
			new Thread(handler).run();
		}
		
		public MessageHandler getMessageHandler() {
			return handler;
		}

		@Override
		public void run() {
			try {
				Message msg = (Message) in.readObject();
				handler.addMessageToBuffer(msg);
				
			} catch (ClassNotFoundException e) {
				// TODO ��Ҳ��֪����ʲô= =
				e.printStackTrace();
			} catch (IOException e) {
				// TODO ��Ҳ��֪����ʲô= =
				e.printStackTrace();
			}
		}
	}
	
	class MessageHandler implements Runnable {
		
		public MessageHandler() {
			listenerList = new ArrayList<IMessageListener>();
		}
		
		private volatile LinkedList<Message> messageBuffer;
		
		public void addMessageToBuffer( Message msg )
		{
			messageBuffer.add(msg);
			messageBuffer.notify();
		}
		
		private ArrayList<IMessageListener> listenerList;
		
		public void addMessageListener( IMessageListener listener )
		{
			listenerList.add(listener);
		}
		
		public void removeMessageListener( IMessageListener listener )
		{
			listenerList.remove(listener);
		}

		@Override
		public void run() {
			while( true )
			{
				if( messageBuffer.isEmpty() )
				{
					try {
						messageBuffer.wait();
					} catch (InterruptedException e) {
						// TODO ��Ҳ��֪����ʲô= =
						e.printStackTrace();
					}
				} else {
					Message msg = messageBuffer.removeFirst();
					for( IMessageListener listener : listenerList )
					{
						listener.onReceviedMessage(msg);
					}
				}
			}
		}
	}
}
