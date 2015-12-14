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
			manager.closeAll();
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
			// XXX �ر�Socket�쳣������ʲô������������������Ҫ����= =
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
				// XXX �����쳣
				System.err.println(getClass().getName() + "����ʧ�ܣ������ߣ�" + connection.getRemoteSocketAddress());
				closeAll();
			}
		}
		
		public void close()
		{
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// XXX �ر�ʧ��
				System.err.println(getClass().getName() + "�ر�ʧ�ܣ������ߣ�" + connection.getRemoteSocketAddress());
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
				// XXX �ر�ʧ��
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
						// XXX �쳣�˳�������Ϣ���ݸ��ϲ�
						System.err.println(getClass().getName() + "�쳣�˳�");
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
					// XXX �����ܱ���ϰ�
					assert false: getClass().getName() + "�Ļ�����У��ڵȴ�ʱ�����";
				}
			}
		}
	}
}
