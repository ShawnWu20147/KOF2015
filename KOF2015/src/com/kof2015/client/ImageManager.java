package com.kof2015.client;

import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class ImageManager {
	
	private static ImageManager manager;
	private static HashMap<String, ImageIcon> cache;
	
	private ImageManager() {
		cache = new HashMap<String, ImageIcon>();
	}
	
	public static ImageManager getManager() {
		if( manager == null )
		{
			manager = new ImageManager();
		}
		return manager;
	}
	
	private static final String[] IMG_FOLDER_LIST = {
		"../img",
		"../../img"
	};
	
	public ImageIcon getImage(String filename)
	{
		ImageIcon image = null;
		
		if( cache.containsKey(filename) )
			image = cache.get(filename);
		else
		{
			image = loadImage(filename);
			if( image != null )
				cache.put(filename, image);
		}
		
		return image;
	}
	
	private ImageIcon loadImage(String filename)
	{
		ImageIcon image = null;
		
		for( String foldername : IMG_FOLDER_LIST ) {
			String picPath = foldername + "/" + filename;
			File f = new File(picPath);
			
			if( f.exists() )
			{
				image = new ImageIcon(picPath);
				break;
			}
		}
		
		return image;
	}
}
