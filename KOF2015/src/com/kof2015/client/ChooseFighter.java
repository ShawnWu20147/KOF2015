package com.kof2015.client;

import com.kof2015.server.FighterInfo;

public class ChooseFighter {
	FighterInfo fi;
	boolean selected;
	int who_select;
	public ChooseFighter(FighterInfo fi,boolean selected,int who_select){
		this.fi=fi;
		this.selected=selected;
		this.who_select=who_select;
	}
}
