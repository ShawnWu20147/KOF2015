package com.kof2015.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.common.FighterInfo;

public class FormationPanelv2 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062288459368019966L;
	
	/**
	 * ��ڷ�����
	 * ���ݲ�������ȡ��ѡ�б�
	 * @param candidates ��Ϊ��ͬ�ĸ񶷼ң��������Բ�ͬ�����Դ��ݵĲ���ID���飬���ǰ����˸񶷼����Ե�FighterInfo���顣
	 */
	public void passingCandidates( FighterInfo[] candidates )
	{
		
	}
	
	public FormationPanelv2() {
		
		setLayout(new BorderLayout(10, 10));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout(10, 10));
		{
			JPanel p1 = new JPanel();
			p1.setLayout(new GridLayout(3, 2, 10, 10));
			{
				for( int i = 0; i < 6; i++ ) {
					JLabel faceLabel = new JLabel();
					faceLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
					faceLabel.setPreferredSize(new Dimension(120, 120));
					p1.add(faceLabel);
				}
			}
			leftPanel.add(p1, BorderLayout.CENTER);
			
			leftPanel.add(new JButton("�������"), BorderLayout.SOUTH);
		}
		add(leftPanel, BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(10, 10));
		{
			
			JList<String> optionList = new JList<String>();
			optionList.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			// optionList.setPreferredSize(new Dimension(200, 100));
			JScrollPane sp = new JScrollPane(optionList);
			sp.setPreferredSize(new Dimension(200, 100));
			rightPanel.add(sp, BorderLayout.NORTH);
			
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			listModel.addElement("Debbie Scott");
			listModel.addElement("Scott Hommel");
			listModel.addElement("Shanron Zakhour");
			listModel.addElement("Debbie Scott");
			listModel.addElement("Scott Hommel");
			listModel.addElement("Shanron Zakhour");
			optionList.setModel(listModel);
			
			optionList.setSelectedIndex(2);
			optionList.setSelectedIndices(new int[0]);
			
			JTextArea infoArea = new JTextArea();
			infoArea.setBorder(BorderFactory.createLoweredBevelBorder());
			rightPanel.add(infoArea, BorderLayout.CENTER);
		}
		add(rightPanel, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		FormationPanelv2 testObject = new FormationPanelv2();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("���ԣ�" + testObject.getClass().getName());
		frame.add(testObject);
		frame.pack();
		frame.setVisible(true);
		
		FighterInfo[] fighters = new FighterInfo[ 8 ];
		for( int i = 0; i < fighters.length; i++ )
		{
			fighters[ i ] = new FighterInfo(
					(int)(Math.random() * 30),
					"������" + i, 
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
					"������ɵĽ�ɫ��û��ʲô�ص�= =",
					"��ɱ��" + i,
					"Ϭ��ħ���࣡",
					(int)(Math.random() * 8),
					(double)((int)(Math.random() * 10) + (int)(Math.random() * 10) * 0.1 )
					);
		}
		testObject.passingCandidates( fighters );
	}
}
