package com.kof2015.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.common.Message;
import com.kof2015.ui.BattlePanel;
import com.kof2015.ui.BattlerPanel;

public class SelectOnePeople extends JPanel {
	JButton select;
	JButton intro;
	
	JLabel jl_pic;
	
	int myid;
	int myindex;
	
	String info;
	
	boolean already;
	boolean selected;
	
	int who_sel;
	int who_am_i;
	
	public boolean isSelected(){
		return selected;
	}
	
	public SelectOnePeople(int who_am_i,int index,ChooseFighter cf){
		
		
		
		this.who_am_i=who_am_i;
		
		myindex=index;
		myid=cf.fi.id;
		
		select=new JButton("选择");
		intro=new JButton("简介");
		
		String pic_path="img/battler/"+myid+".jpg";
		File f=new File("img/battler/"+myid+".jpg");
		if (!f.exists())
			pic_path="../img/battler/"+myid+".jpg";
		
		
		ImageIcon image = new ImageIcon(pic_path); 
		
		jl_pic=new JLabel(image);
		
		
		already=cf.selected;
		if (already){
			who_sel=cf.who_select;
			int isme=1;
			if (who_sel!=who_am_i)
				isme=0;
			switch(isme){
			case 1:
				
				jl_pic.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
				break;
			case 0:
				jl_pic.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
				
				break;
			}
			
		}
		else{
			jl_pic.setBorder(BorderFactory.createEtchedBorder(new Color(1,2,3,0),new Color(1,2,3,0)));
		}
		info=cf.fi.toString();
		

		setLayout(new GridLayout(2, 1));
		add(jl_pic);	
		JPanel nj=new JPanel();
		nj.add(select);
		//nj.add(intro);
		add(nj);
		
		select.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected){
					//it means we have chosen it, but not want to cancel
					//jl_pic.setBorder(null);
					jl_pic.setBorder(BorderFactory.createEtchedBorder(new Color(1,2,3,0),new Color(1,2,3,0)));
					
					
					selected=false;
					select.setText("选择");
					return;
				}
				if (!already){
					jl_pic.setBorder(BorderFactory.createEtchedBorder(Color.GREEN, Color.GREEN));
					
					selected=true;
					select.setText("取消");
				}
				else{
					JOptionPane.showMessageDialog(null, "此格斗家已经被选了", "提示", JOptionPane.INFORMATION_MESSAGE);			
				}
				
			}
		});
		
		intro.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame jf=new JFrame();
				jf.setResizable(false);
				jf.setSize(300,500);
				JTextArea jt=new JTextArea(info);
				jt.setLineWrap(true);
				jt.setEditable(false);
				jf.add(jt);
				jf.setLocationRelativeTo(null);
				jf.setVisible(true);
				
			}
		});
		
		jl_pic.setToolTipText(cf.fi.toStringHtml());
		
	}
	
	public SelectOnePeople(int index,int id){
		myindex=index;
		myid=id;
		
		
		select=new JButton("选择");
		intro=new JButton("简介");
		
		ImageIcon image = new ImageIcon("img/battler/"+id+".jpg"); 
		jl_pic=new JLabel(image);
		
		setLayout(new GridLayout(2, 1));
		
		add(jl_pic);
		
		JPanel nj=new JPanel();
		nj.add(select);
		nj.add(intro);
		add(nj);
	}
	
	public void refresh(ChooseFighter cf){
		
		selected=false;
		
		select.setText("选择");
		
		already=cf.selected;	//是否已经被别人选了
		
		if (already){
			System.out.println("选择者是:"+cf.who_select+"   我是:"+who_am_i);
			who_sel=cf.who_select;
			
			int isme=1;
			if (who_sel!=who_am_i)
				isme=0;
			
			switch(isme){
			case 1:
				jl_pic.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
				break;
			case 0:
				jl_pic.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
				
				break;
			}
			
		}
		else{
			jl_pic.setBorder(BorderFactory.createEtchedBorder(new Color(1,2,3,0),new Color(1,2,3,0)));
		}
		
	}
	
	public static void main(String[] args){
		
		JFrame jf=new JFrame();
		jf.setSize(600, 300);
		jf.setResizable(false);
		
		
		SelectOnePeople sp=new SelectOnePeople(0,2);
		sp.setSize(80,80);
		
		jf.add(sp);
		jf.setVisible(true);
	}
}
