package com.kof2015.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class DBHelper {  
    public static final String url = "jdbc:mysql://127.0.0.1/KOF2015";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "1234";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public DBHelper() {  
        try {  
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("conn succ");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
    
    public ResultSet executeOneQuery(String sql){
    	try {
			pst=conn.prepareStatement(sql);
			return pst.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    public int Login(String username,String userpwd){
    	return 0;
    }
    
    
    public int Register(String username,String userpwd){
    	try {
    		
    		String dup="select * from PlayerLogin where username='"+username+"'";
    		ResultSet trs=executeOneQuery(dup);
    		if (trs.next()){
    			return -2;	//duplicate
    		}
    		
    		String ac="select userid from PlayerLogin";
    		ResultSet tt=executeOneQuery(ac);
    		HashSet<Integer> hs=new HashSet<Integer>();
    		while (tt.next()){
    			int c=Integer.valueOf(tt.getString(1));
    			hs.add(c);
    		}
    		
    		int myid=(username+"."+userpwd).hashCode();
    		while (hs.contains(myid)){
    			myid=myid+(int)(Math.random()*41);
    		}
    		
    		
    		
			pst=conn.prepareStatement("insert into PlayerLogin(username,userpasswd,userid) values (?,?,?)" );
			pst.setString(1, username);
			pst.setString(2, userpwd);
			pst.setString(3, String.valueOf(myid));
			int i = pst.executeUpdate();
	        return i;
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return -1;
    	
    }
    
    
    
  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}  