package com.chuanonly.wallpaper2.util;

import java.util.HashMap;

import android.R.integer;


public class CacheTextrue
{
	public static HashMap<String, Param> map = new HashMap<String, Param>();

	public static int get(int resId, float scaleX, float ScaleY, int angle)
	{
		String str = resId +"-"+scaleX+"-"+ScaleY+"-"+angle;
		if (map.containsKey(str))
		{
			Param p = map.get(str);
			return p.id;
		}else {
			return -1;
		}
	}
	

	public static int getWidth(int resId, float scaleX, float ScaleY, int angle)
	{
		String str = resId +"-"+scaleX+"-"+ScaleY+"-"+angle;
		if (map.containsKey(str))
		{
			Param p = map.get(str);
			return p.width;
		}else {
			return 0;
		}
	}
	public static int getHeight(int resId, float scaleX, float ScaleY, int angle)
	{
		String str = resId +"-"+scaleX+"-"+ScaleY+"-"+angle;
		if (map.containsKey(str))
		{
			Param p = map.get(str);
			return p.height;
		}else {
			return 0;
		}
	}

	public static int get(String customPath)
	{
		String str = customPath;
		if (map.containsKey(str))
		{
			Param p = map.get(str);
			return p.id;
		}else {
			return -1;
		}
	}
	public static int getWidth(String customPath)
	{
		String str = customPath;
		if (map.containsKey(str))
		{
			Param p = map.get(str);
			return p.width;
		}else {
			return -1;
		}
	}
	public static int getHeight(String customPath)
	{
		String str = customPath;
		if (map.containsKey(str))
		{
			Param p = map.get(str);
			return p.height;
		}else {
			return -1;
		}
	}
	public static void put (int resId, float scaleX, float ScaleY, int angle, int value,int width, int height)
	{
		String str = resId +"-"+scaleX+"-"+ScaleY+"-"+angle;
		if (str == null || str.equals(""))
			return;
		Param p = new Param();
		p.id = value;
		p.width = width;
		p.height = height;
		map.put(str, p);
	}
	public static void put (String cumtomPath,int id,int width, int height)
	{
		String str = cumtomPath;
		if (str == null || str.equals(""))
			return;
		Param p = new Param();
		p.id = id;
		p.width = width;
		p.height = height;
		map.put(str, p);
	}
	
	public static void put (int resId, float scaleX, float ScaleY, int angle, int value)
	{
		put(resId, scaleX, ScaleY, angle, value, 0, 0);
	}
	
	
	
	public static void put (int resId, int value)
	{
		 put(resId, 1.0f, 1.0f, 0 , value, 0, 0);
	}
	
	public static int get(int resId)
	{
		return get(resId, 1.0f, 1.0f, 0);
	}
	public static  void clear()
	{			
		map.clear();
	}
	public static class Param
	{
		public int id;
		public int width;
		public int height;
	}
}
