package com.common.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.common.UserInfo;

public class MessageManager {
	
	private static volatile HashMap<UserInfo, MessageManager> mapGlobalConnections = 
			new HashMap<UserInfo, MessageManager>();
	
	// ========================================
	// 类方法
	// ========================================
	/**
	 * 返回管理器，如果不存在该管理器，则返回null。
	 * @param userInfo
	 * @return
	 */
	public static MessageManager getManager( UserInfo userInfo )
	{
		MessageManager mManager = null;
		
		synchronized (mapGlobalConnections) {
			mManager = mapGlobalConnections.get(userInfo); 
		}
		return mManager;
	}
	
	/**
	 * @param userInfo <p>如果为空，则不将该管理器添加进哈希表。<br>如果哈希表中已经包含该用户信息，会返回已经保存的管理器。</p>
	 * @param socket 只会为处在连接状态的套接字创建管理器，否则返回null。
	 * @return
	 * @throws IOException
	 */
	public static MessageManager createManager( UserInfo userInfo, Socket socket ) throws IOException
	{
		MessageManager manager = null;
		
		synchronized (mapGlobalConnections) {
			// 检查是否已经存在
			manager = getManager( userInfo );
			if( manager == null )
			{
				// 检查套接字
				if( socket != null && socket.isConnected() && !socket.isClosed() ) {
					manager = new MessageManager( socket );
				}
				// 检查客户信息
				if( userInfo != null) {
					mapGlobalConnections.put(userInfo, manager);
				}
			}
		}
		
		return manager;
	}
	
	public static void closeManager( UserInfo userInfo )
	{
		MessageManager manager = getManager(userInfo);
		
		if( manager != null )
			manager.closeAll();
	}
	
	/**
	 * 如果任意参数为null，则什么都不做= =
	 * @param key
	 * @param manager
	 */
	public static void addManager( UserInfo key, MessageManager manager )
	{
		synchronized (mapGlobalConnections) {
			if( key != null && manager != null )
				mapGlobalConnections.put(key, manager);
		}
	}
	
	public static MessageManager removeManager( UserInfo key )
	{
		MessageManager mManager;
		synchronized (mapGlobalConnections) {
			mManager = mapGlobalConnections.remove(key);
		}
		return mManager;
	}
	
	public static void rekeyManager( UserInfo oldInfo, UserInfo newInfo )
	{
		MessageManager manager = removeManager( oldInfo );
		addManager( newInfo, manager );
	}
	
	private Socket connection;
	private MessageSender sender;
	private MessageReceiver receiver;
	private MessageHandler handler;
	
	private boolean bExit = false;
	
	// ========================================
	// 实例方法
	// ========================================
	private MessageManager( Socket socket ) throws IOException
	{
		connection = socket;
		// 创建各种接收器，并开启这些线程
		sender = new MessageSender(socket.getOutputStream());
		receiver = new MessageReceiver(socket.getInputStream());
		handler = receiver.getMessageHandler();
	}
	
	public void closeAll()
	{
		bExit = true;
		
		sender.close();
		receiver.close();
		handler.close();
		
		try {
			connection.close();
		} catch (IOException e) {
			// XXX 关闭Socket异常，好像什么都不用做啊，反正不要用了= =
		}
	}
	
	public boolean isClosed() {
		return bExit;
	}
	
	public void send( Message msg ) throws MessageManagerClosedException
	{
		if( isClosed() )
			throw new MessageManagerClosedException();
		sender.send(msg);
	}
	
	public void addHandler( IMessageListener listener ) throws MessageManagerClosedException
	{
		if( isClosed() )
			throw new MessageManagerClosedException();
		handler.addMessageListener(listener);
	}
	
	public void removeHandler( IMessageListener listener ) throws MessageManagerClosedException
	{
		if( isClosed() )
			throw new MessageManagerClosedException();
		handler.removeMessageListener(listener);
	}
	
	class MessageSender {
		private ObjectOutputStream out;
		
		public MessageSender(OutputStream out ) throws IOException {
			this.out = new ObjectOutputStream(out);
		}
		
		public void send(Message msg)
		{
			try {
				out.writeObject(msg);
				out.flush();
			} catch (IOException e) {
				// XXX 发送异常
				System.err.println(getClass().getName() + "发送失败，连接者：" + connection.getRemoteSocketAddress());
				closeAll();
			}
		}
		
		public void close()
		{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// XXX 关闭失败
				System.err.println(getClass().getName() + "关闭失败，连接者：" + connection.getRemoteSocketAddress());
			}
		}
	}
	
	class MessageReceiver implements Runnable {
		
		private MessageHandler handler;
		private ObjectInputStream in;
		
		public MessageReceiver(InputStream in) throws IOException {
			handler = new MessageHandler();
			this.in = new ObjectInputStream(in);
			
			new Thread(handler).start();
			new Thread(this).start();
		}
		
		public MessageHandler getMessageHandler() {
			return handler;
		}
		
		public void close()
		{
			try {
				in.close();
			} catch (IOException e) {
				// XXX 关闭失败
				System.err.println(getClass().getName() + "关闭失败，连接者：" + connection.getRemoteSocketAddress());
			}
		}

		@Override
		public void run() {
			while( true )
			{
				try {
					Message msg = (Message) in.readObject();
					handler.addMessageToBuffer(msg);
				} catch (ClassNotFoundException | IOException e) {
					if( !bExit ) {
						// XXX 异常退出，将消息传递给上层
						System.err.println(getClass().getName() + "异常退出");
						closeAll();
					}
				}
			}
		}
	}

	class MessageHandler implements Runnable {
		
		public MessageHandler() {
			messageBuffer = new LinkedList<Message>();
			listenerList = new ArrayList<IMessageListener>();
			increaseListeners = new ArrayList<IMessageListener>();
			decreaseListeners = new ArrayList<IMessageListener>();
		}
		
		private volatile LinkedList<Message> messageBuffer;
		
		public void addMessageToBuffer( Message msg )
		{
			synchronized( messageBuffer ) {
				messageBuffer.add(msg);
				messageBuffer.notify();
			}
		}
		
		private volatile ArrayList<IMessageListener> listenerList;
		private volatile ArrayList<IMessageListener> increaseListeners, decreaseListeners;
		
		public void addMessageListener( IMessageListener listener )
		{
			synchronized( increaseListeners ) {
				increaseListeners.add(listener);
			}
		}
		
		public void removeMessageListener( IMessageListener listener )
		{
			synchronized( decreaseListeners ) {
				decreaseListeners.add(listener);
			}
		}
		
		private void modifyListenerList() {
			synchronized (listenerList) {
				// 添加
				synchronized (increaseListeners) {
					Iterator<IMessageListener> iterator = increaseListeners.iterator();
					while( iterator.hasNext() ) {
						listenerList.add( iterator.next() );
						iterator.remove();
					}
				}
				
				// 减少
				synchronized (decreaseListeners) {
					Iterator<IMessageListener> iterator = decreaseListeners.iterator();
					while( iterator.hasNext() ) {
						listenerList.remove( iterator.next() );
						iterator.remove();
					}
				}
			}
		}
		
		public void flush()
		{
			synchronized (listenerList) {
				modifyListenerList();
				synchronized (messageBuffer) {
					while( !messageBuffer.isEmpty() ) {
						Message msg = messageBuffer.removeFirst();
						for( IMessageListener listener : listenerList )
						{
							listener.onReceviedMessage(msg);
						}
					}
				}
			}
		}
		
		public void close()
		{
			flush();
			
			synchronized ( listenerList ) {
				listenerList.removeAll(new LinkedList<IMessageListener>());
			}
			synchronized( messageBuffer ) {
				messageBuffer.notify();
			}
		}
		
		@Override
		public void run() {
			while( !bExit )
			{
				flush();
				
				try {
					synchronized( messageBuffer ) {
						messageBuffer.wait();
					}
				} catch (InterruptedException e) {
					// XXX 不可能被打断吧
					assert false: getClass().getName() + "的缓冲队列，在等待时被打断";
				}
			}
		}
	}
}
