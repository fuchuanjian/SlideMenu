package com.chuanonly.livewallpaper.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WallpaperInfo
{
	public String city;
	public String info;
	public String img;
	public String temperature;
	public String high;
	public String low;
	public String time;
	public WallpaperInfo(String json)
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
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String toString()
	{
		return "WallpaperInfo [city=" + city + ", info=" + info + ", img="
				+ img + ", temperature=" + temperature + ", high=" + high
				+ ", low=" + low + ", time=" + time + "]";
	}
	
	
}
