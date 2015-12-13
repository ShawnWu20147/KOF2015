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
	// �෽��
	// ========================================
	/**
	 * ���ع���������������ڸù��������򷵻�null��
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
	 * @param userInfo <p>���Ϊ�գ��򲻽��ù�������ӽ���ϣ��<br>�����ϣ�����Ѿ��������û���Ϣ���᷵���Ѿ�����Ĺ�������</p>
	 * @param socket ֻ��Ϊ��������״̬���׽��ִ��������������򷵻�null��
	 * @return
	 * @throws IOException
	 */
	public static MessageManager createManager( UserInfo userInfo, Socket socket ) throws IOException
	{
		MessageManager manager = null;
		
		synchronized (mapGlobalConnections) {
			// ����Ƿ��Ѿ�����
			manager = getManager( userInfo );
			if( manager == null )
			{
				// ����׽���
				if( socket != null && socket.isConnected() && !socket.isClosed() ) {
					manager = new MessageManager( socket );
				}
				// ���ͻ���Ϣ
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
			manager.close();
	}
	
	/**
	 * ����������Ϊnull����ʲô������= =
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
	// ʵ������
	// ========================================
	private MessageManager( Socket socket ) throws IOException
	{
		connection = socket;
		// �������ֽ���������������Щ�߳�
		sender = new MessageSender(socket.getOutputStream());
		receiver = new MessageReceiver(socket.getInputStream());
		handler = receiver.getMessageHandler();
		
		new Thread(sender).start();
		new Thread(receiver).start();
	}
	
	public void close()
	{
		bExit = true;
		
		sender.close();
		receiver.recvclose();
		handler.close();
		
		try {
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			
			connection.getInputStream().close();
			connection.close();
		} catch (IOException e) {
			// TODO �ر�Socket�쳣
		}
	}
	
	public void send( Message msg )
	{
		sender.send(msg);
	}
	
	public void addHandler( IMessageListener listener )
	{
		handler.addMessageListener(listener);
	}
	
	public void removeHandler( IMessageListener listener )
	{
		handler.removeMessageListener(listener);
	}
	
	class MessageSender implements Runnable {
		
		private volatile LinkedList<Message> buffer;
		private ObjectOutputStream out;
		
		public MessageSender(OutputStream out ) throws IOException {
			buffer = new LinkedList<Message>();
			this.out = new ObjectOutputStream(out);
		}
		
		public void send(Message msg)
		{
			synchronized( buffer ) {
				buffer.add(msg);
				buffer.notify();
			}
		}
		
		public void flush()
		{
			synchronized( buffer ) {
				while( !buffer.isEmpty() ) {
					Message msg = buffer.removeFirst();
					
					try {
						out.writeObject(msg);
						out.flush();
					} catch (IOException e) {
						// TODO ����ʧ��
						System.err.println(getClass().getName() + "����ʧ�ܣ������ߣ�" + connection.getRemoteSocketAddress());
					}
				}
			}
		}
		
		public void close()
		{
			flush();
			
			try {
				out.close();
			} catch (IOException e) {
				// TODO �ر�ʧ��
				System.err.println(getClass().getName() + "�ر�ʧ�ܣ������ߣ�" + connection.getRemoteSocketAddress());
			}
			synchronized( buffer ) {
				buffer.notify();
			}
		}
		
		@Override
		public void run() {
			
			while( !bExit ) {
				flush();
				
				try {
					synchronized( buffer ) {
						buffer.wait();
					}
				} catch (InterruptedException e) {
					// TODO �����ܱ���ϰ�
					assert false: getClass().getName() + "�Ļ�����У��ڵȴ�ʱ�����";
				}
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
		
		public void recvclose()
		{
			try {
				in.close();
			} catch (IOException e) {
				// TODO �ر�ʧ��
				System.err.println(getClass().getName() + "�ر�ʧ�ܣ������ߣ�" + connection.getRemoteSocketAddress());
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
						// TODO �쳣�˳�������Ϣ���ݸ��ϲ�
						System.err.println(getClass().getName() + "�쳣�˳�");
						close();
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
				// ���
				synchronized (increaseListeners) {
					Iterator<IMessageListener> iterator = increaseListeners.iterator();
					while( iterator.hasNext() ) {
						listenerList.add( iterator.next() );
						iterator.remove();
					}
				}
				
				// ����
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
			
			listenerList.removeAll(new LinkedList<IMessageListener>());
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
					// TODO �����ܱ���ϰ�
					assert false: getClass().getName() + "�Ļ�����У��ڵȴ�ʱ�����";
				}
			}
		}
	}
}
