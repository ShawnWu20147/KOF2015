package com.common;

public class FighterInstance {
	
	public int max_hp;
	
	public FighterInfo base;
	
	/**
	 * ��ǰ������ֵ��
	 */
	public int hp;
	
	public boolean isDead;
	
	/**
	 * ��ǰ��ŭ��ֵ��
	 */
	public int anger;
	
	// TODO ��� MaxHp��MaxAnger
	// TODO ��ӻ�������
	// TODO ����쳣״̬��
	
	/**
	 * �����񶷼ҵ�����ʵ�壬������ǰ����ֵ����ʱ���ɱ䣩���ԣ�����ս���ȡ�
	 * @param base �񶷼ҵĻ������ݡ�
	 * @param ratio �����Ǽ������ı��ʡ�
	 */
	public FighterInstance(FighterInfo base, double ratio) {
		this.base=base;
		
		max_hp=base.base_hp;
		
		hp=base.base_hp;
		
		anger=0;
		
		isDead=false;
	}
	
	/**
	 * ռλ�á������������쳣״̬�����Եĸı䡣
	 * ���������ǣ��ı���������ͣ��ı����ֵ��
	 */
	public void bouns(  )
	{}
}
