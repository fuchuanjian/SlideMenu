package com.chuanonly.wallpaper2.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chuanonly.wallpaper2.util.Util;

public class WallpaperInfo
{
	public static String city;
	public static String info;
	public static String img;
	public static String temperature;
	public static String high;
	public static String low;
	public static String time;
	public static void parseWallpaperInfo(String json)
	{
		if (json == null)
			return;
		try
		{
			JSONObject obj = new JSONObject(json);
			
			
			JSONObject realTimeObj = obj.optJSONObject("realtime");
			city = realTimeObj.optString("city_name");
			
			JSONObject infoObj = realTimeObj.optJSONObject("weather");
			temperature = infoObj.optString("temperature");
			info = infoObj.optString("info");
			img = infoObj.optString("img");
			
			JSONObject oneDayObj = obj.optJSONArray("weather").getJSONObject(0).optJSONObject("info");
			JSONArray dayInfoArray = oneDayObj.optJSONArray("day");
			if (dayInfoArray != null)
			{
				high = dayInfoArray.optString(2);
			}
			JSONArray nightInfoArray = oneDayObj.optJSONArray("night");
			if (nightInfoArray != null)
			{
				low = nightInfoArray.optString(2);
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			time = formatter.format(new Date(System.currentTimeMillis()));
			
			Util.setStringToSharedPref(Util.SCENE_INFO, info);
			Util.setStringToSharedPref(Util.SCENE_TEMPERATUR, temperature);
			Util.setIntToSharedPref(Util.TYPE, Integer.valueOf(img));
			Util.setIntToSharedPref(Util.REAL_TYPE, Integer.valueOf(img));
			Util.setLongToSharedPref(Util.LAST_UPDATETIME, System.currentTimeMillis());
			Util.setLongToSharedPref(Util.LAST_PICK_TIME, 0);
			int mode = Util.getIntFromSharedPref(Util.MODE, -1);
			if (mode ==-1)
			{
				 Util.setIntToSharedPref(Util.MODE, 2);
			}
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String toStr()
	{
		return "WallpaperInfo [city=" + city + ", info=" + info + ", img="
				+ img + ", temperature=" + temperature + ", high=" + high
				+ ", low=" + low + ", time=" + time + "]";
	}
	
	
}
