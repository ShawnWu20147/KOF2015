package com.kof2015.battle;

import com.common.FighterInstance;

public class RefreshHPAnimation implements Runnable {
	
	BattlerPanel mbp;
	FighterInstance mfi;
	public RefreshHPAnimation(BattlerPanel bp,FighterInstance fi) {
		this.mbp=bp;
		this.mfi=fi;
		
		
	}
	
	@Override
	public void run() {
		mbp.updateHpandRagewithAnimation(mfi);

	}

}
