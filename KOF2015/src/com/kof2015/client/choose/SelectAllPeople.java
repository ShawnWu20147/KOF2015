package com.kof2015.client.choose;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectAllPeople extends JPanel{
	SelectOnePeople []sp;
	
	public SelectAllPeople(int who_am_i,ChooseFighter []cf){
		setLayout(new GridLayout(2, 8,5,5));
		sp=new SelectOnePeople[16];
		for (int i=0;i<16;i++){
			sp[i]=new SelectOnePeople(who_am_i,i,cf[i]);
			add(sp[i]);
		}
	}
	
	public SelectOnePeople[] getAllPeople(){
		return sp;
	}

	public void disableAll(){
		for (int i=0;i<16;i++){
			sp[i].disableSelect();
		}
	}
	
}
