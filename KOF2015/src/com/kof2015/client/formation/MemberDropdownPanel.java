package com.kof2015.client.formation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MemberDropdownPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3336785410696520028L;
	
	private JButton faceButton;
	private JComboBox<String> selectBox;
	
	public MemberDropdownPanel() {
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
		setLayout(new BorderLayout(5, 5));
		
		faceButton = new JButton();
		faceButton.setPreferredSize(new Dimension(120, 120));
		faceButton.setBackground(getBackground());
		faceButton.setBorder(BorderFactory.createLoweredBevelBorder());
		add(faceButton, BorderLayout.CENTER);
		
		selectBox = new JComboBox<String>();
		add(selectBox, BorderLayout.SOUTH);
	}
	
	public void setOptionalFighters( String[] namelist )
	{
		selectBox.removeAllItems();
		for( String name : namelist )
		{
			selectBox.addItem(name);
		}
		selectBox.setSelectedIndex(-1);
	}
	
	public void addItemChangedListener( ItemListener listener )
	{
		selectBox.addItemListener(listener);
	}
	
	public void addActionListener( ActionListener listener )
	{
		selectBox.addActionListener(listener);
	}
	
	public void addImageClickListener( ActionListener listener )
	{
		faceButton.addActionListener(listener);
	}
	
	public int getSelectedIndex()
	{
		return selectBox.getSelectedIndex();
	}
	
	public void setSelectedBattler( int selected, int imgid )
	{
		if( selected >= 0 )
			selectBox.setSelectedIndex( selected );
		else
			selectBox.setSelectedIndex( -1 );
		
		String pic_path="img/battler/" + imgid + ".jpg";
		File f=new File(pic_path);
		if (!f.exists()){
			pic_path="../img/battler/" + imgid + ".jpg";
		}
		
		if( imgid >= 0 )
			faceButton.setIcon(new ImageIcon(pic_path));
		else
			faceButton.setIcon(null);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MemberDropdownPanel());
		frame.setTitle("MemberDropdownPanel Test");
		frame.pack();
		frame.setVisible(true);
	}
}
