package com.kof2015.server;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import com.common.FighterInfo;
import com.common.Message;




public class KOF2015 {
	DBHelper db1 = null;
	ResultSet ret = null;
	
	ArrayList<FighterInfo> all_fighters;
	
	boolean fromDB=true;
	
	public KOF2015() throws IOException, InterruptedException, ClassNotFoundException{
		db1 = new DBHelper();//create DBHelper
		all_fighters=new ArrayList<FighterInfo>();
		
		load_all_fighters(fromDB);
		
		//putFighterDatatoFile();
		
		System.out.println("---load all fighters succ, start working");
		
		ServerSocket ss=new ServerSocket(9798);
		while (true){
			Socket player1=ss.accept();
			System.out.println("--conn from:"+player1.getInetAddress()+"----"+player1.getPort());
			InputStream is=player1.getInputStream();
			ObjectInputStream ois1=new ObjectInputStream(is);
			
			OutputStream os=player1.getOutputStream();
			ObjectOutputStream oos1=new ObjectOutputStream(os);
			
			Message p1=(Message)ois1.readObject();

			
			Socket player2=ss.accept();
			System.out.println("--conn from:"+player2.getInetAddress()+"----"+player2.getPort());
			InputStream is2=player2.getInputStream();
			ObjectInputStream ois2=new ObjectInputStream(is2);
			OutputStream os2=player2.getOutputStream();
			ObjectOutputStream oos2=new ObjectOutputStream(os2);
			
			Message p2=(Message)ois2.readObject();

			
			
			Message msg1=new Message(0);
			msg1.s_info1=p1.msg;
			msg1.s_info2=p2.msg;
			msg1.i_info1=1;
			
			oos1.writeUnshared(msg1);
			
			Message msg2=new Message(0);
			msg2.s_info1=p1.msg;
			msg2.s_info2=p2.msg;
			msg2.i_info1=2;
			
			oos2.writeUnshared(msg2);
			
			ServerHandleOne ho=new ServerHandleOne(all_fighters,ois1, ois2, oos1, oos2,p1.msg,p2.msg);
			new Thread(ho).start();

			
			
			
			
			
		}
		
		
	}
	
	
	private void load_all_fighters(boolean fromDB) {
		if (!fromDB){
			File f=new File("all_fighters");
			if (!f.exists())
				f=new File("../all_fighters");
			try {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
				all_fighters=(ArrayList<FighterInfo>) ois.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("get from files over");
			return;
			
		}
		String query="select * from fighter";
		ret=db1.executeOneQuery(query);
		try {
			while (ret.next()){
				int id=ret.getInt(1);
				String name=ret.getString(2);
				int ability=ret.getInt(3);
				int fighter_type=ret.getInt(4);
				int base_hp=ret.getInt(5);
				int base_attack=ret.getInt(6);
				int base_defence=ret.getInt(7);
				int base_hit=ret.getInt(8);
				int base_block=ret.getInt(9);
				int base_attack_anger=ret.getInt(10);
				int base_attacked_anger=ret.getInt(11);
				int base_power_anger=ret.getInt(12);
				int base_powered_anger=ret.getInt(13);
				String description=ret.getString(14);
				String skill_name=ret.getString(15);
				String skill_description=ret.getString(16);
				int skill_type=ret.getInt(17);
				double skill_ratio=ret.getDouble(18);
				String skill_state_description=ret.getString(19);
				int skill_state_type=ret.getInt(20);
				int skill_state_ratio=ret.getInt(21);
				
				
				FighterInfo fi=new FighterInfo(id,name,ability,fighter_type,base_hp,base_attack,base_defence,base_hit,base_block
						,base_attack_anger,base_attacked_anger,base_power_anger,base_powered_anger,description,skill_name,skill_description
						,skill_type,skill_ratio,skill_state_description,skill_state_type,skill_state_ratio);
				
				//if (id==1) System.out.println(fi.toStringHtml());
				all_fighters.add(fi);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void putFighterDatatoFile(){
		File f=new File("all_fighters");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(all_fighters);
			
			oos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public static void main(String[] args) throws Exception{    

		new KOF2015();
	}  
}
