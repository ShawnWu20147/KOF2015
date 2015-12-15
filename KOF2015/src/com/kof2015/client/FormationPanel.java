package com.kof2015.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.common.FighterInfo;

public class FormationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1688137239730609797L;
	
	/**
	 * 入口方法。
	 * 传递参数，获取候选列表。
	 * @param candidates 因为相同的格斗家，可能属性不同，所以传递的不是ID数组，而是包含了格斗家属性的FighterInfo数组。
	 */
	public void passingCandidates( FighterInfo[] candidates )
	{
		String[] namelist = new String[candidates.length];
		for( int i = 0; i < candidates.length; i++ )
			namelist[ i ] = candidates[ i ].name;
		
		for( final MemberDropdownPanel tile : tiles )
		{
			tile.setOptionalFighters(namelist);
			tile.addItemChangedListener( new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					int state = e.getStateChange();
					if ( state == ItemEvent.SELECTED )
					{
						selected(tile, tile.getSelectedIndex());
					}
					else if ( state == ItemEvent.DESELECTED )
					{
						// TODO 提示信息？
					}
				}
			} );
		}
		this.candidates = candidates;
		
		formation = new int[ tiles.length ];
		for( int i = 0; i < tiles.length; i++ )
			formation[ i ] = -1;
		for( int i = 0; i < candidates.length && i < tiles.length; i++ )
		{
			tiles[ i ].setSelectedBattler( i , candidates[i].id );
			formation[ i ] = i;
		}
		infoLabel.setText("请选择阵型");
	}
	
	/**
	 * 出口方法。
	 * 标志布阵结束，并且完成了正确性检查。
	 * @param fighters 格斗家列表，按照阵型前排（1，2，3），后排（4，5，6）的顺序排列。
	 */
	public void readyToFight( FighterInfo[] fighters )
	{
		String info = "";
		
		int index=0;
		
		for( FighterInfo fighter : fighters ){
			info += String.valueOf(++index)+"号位:【"+fighter.name+"】\n";
		}
		infoLabel.setText(info);
		infoLabel.append("阵型部署完毕,准备与【"+oppoName+"】决一死战!");
		
		try {
			cog.formationover(fighters);
		} catch( NullPointerException e ) {
			System.out.println("单元测试中...");
		}
		
	}
	
	private MemberDropdownPanel[] tiles;
	private JTextArea infoLabel;
	
	private FighterInfo[] candidates;
	/**
	 * 布阵信息，按照队伍位置【0 ~ 6】编号，内容为候选者编号。
	 */
	private int[] formation;
	
	private ClientOneGame cog;
	
	private String oppoName;
	
	/**
	 * 布置好界面，主要描述好位置。
	 * 因为还没有数据，所以数据的初始化要滞后。
	 */
	public FormationPanel(ClientOneGame cog,String oppoName)
	{
		
		this.cog=cog;
		this.oppoName=oppoName;
		
		final int[] convert = { 3, 0, 4, 1, 5, 2 };
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		{
			JPanel container = new JPanel();
			container.setLayout(new BorderLayout(10, 10));
			{
				JPanel contentPanel = new JPanel();
				contentPanel.setLayout(new GridLayout(3, 2, 20, 20));
				{
					tiles = new MemberDropdownPanel[ 6 ];
					for ( int i = 0; i < 6; i++ )
						tiles[ i ] = new MemberDropdownPanel();
					for ( int i = 0; i < 6; i++ )
						contentPanel.add( tiles[ convert[ i ] ] );
				}
				container.add(contentPanel, BorderLayout.CENTER);
				
				JPanel infoPanel = new JPanel();
				infoPanel.setLayout(new BorderLayout(5, 5));
				{
					infoLabel = new JTextArea();
					infoLabel.setEditable(false);
					
					infoLabel.setLineWrap(true);
					infoLabel.setPreferredSize(new Dimension(200, 0));
					infoLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
					infoPanel.add(infoLabel, BorderLayout.CENTER);
					
					JButton readyButton = new JButton("布阵完成");
					readyButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if( isFormationReady() )
							{
								FighterInfo[] fighters = new FighterInfo[ 6 ];
								for( int i = 0; i < fighters.length; i++ )
									fighters[ i ] = candidates[ formation[ i ] ];
								readyToFight(fighters);
							}
							else
							{
								// TODO 提示信息
								infoLabel.setText("阵型没布置好，还不能开始哦，亲~");
							}
						}
					});
					infoPanel.add(readyButton, BorderLayout.SOUTH);
				}
				container.add(infoPanel, BorderLayout.EAST);
			}
			add(container);
		}
	}
	
	private void selected( MemberDropdownPanel tile, int selected )
	{
		int oldSelected = -1;
		int conflict = -1;
		for( int i = 0; i < tiles.length; i++ )
		{
			if( tiles[ i ] == tile )
			{
				if( formation[ i ] == selected )
					break;
				
				oldSelected = formation[ i ];
				formation[ i ] = selected;
				tile.setSelectedBattler( selected, candidates[selected].id );
				infoLabel.setText(candidates[selected].toString());
			}
			else if( tiles[ i ].getSelectedIndex() == selected )
			{
				conflict = i;
			}
		}
		
		if( conflict >= 0 )
		{
			formation[ conflict ] = oldSelected;
			tiles[ conflict ].setSelectedBattler(oldSelected, candidates[ oldSelected ].id );
		}
	}
	
	private boolean isFormationReady()
	{
		for( int i = 0; i < tiles.length; i++ )
		{
			int id = formation[ i ];
			if( id < 0 )
				return false;
			// TODO 检查没有超过上界
			for( int j = i + 1; j < tiles.length; j++ )
				if( id == tiles[ j ].getSelectedIndex() )
					return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		FormationPanel testObject = new FormationPanel(null,"圣嘿");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("测试：" + testObject.getClass().getName());
		frame.add(testObject);
		frame.pack();
		frame.setVisible(true);
		
		/*
		FighterInfo[] fighters = new FighterInfo[ 8 ];
		for( int i = 0; i < fighters.length; i++ )
		{
			fighters[ i ] = new FighterInfo(
					(int)(Math.random() * 30),
					"测试者" + i, 
					(int)(Math.random() * 30),
					(int)(Math.random() * 3), 
					(int)(500 + Math.random() * 1000),
					(int)(50 + Math.random() * 100),
					(int)(25 + Math.random() * 50),
					(int)(Math.random() * 100),
					(int)(Math.random() * 100),
					(int)(Math.random() * 50),
					(int)(Math.random() * 40),
					(int)(Math.random() * 100),
					(int)(Math.random() * 80),
					"随机生成的角色，没有什么特点= =",
					"必杀技" + i,
					"犀利魔力多！",
					(int)(Math.random() * 8),
					(double)((int)(Math.random() * 10) + (int)(Math.random() * 10) * 0.1 )
					);
		}
		
		testObject.passingCandidates( fighters );
		*/
	}
}
