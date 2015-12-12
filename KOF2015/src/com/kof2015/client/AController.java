package com.kof2015.client;

import javax.swing.JPanel;

public abstract class AController {
	
	protected JPanel controlledPanel;
	
	public AController() {
	}
	
	abstract public void setCallbacksOnPanel( JPanel panel );
	
	protected ICompleteListener completeListener;
	
	public void addCompleteListener(ICompleteListener listener)
	{
		completeListener = listener;
	}
	
	public void complete()
	{
		completeListener.onComplete();
	}
}
