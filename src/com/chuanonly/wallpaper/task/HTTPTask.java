package com.chuanonly.wallpaper.task;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.w3c.dom.Document;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.chuanonly.wallpaper.MyApplication;
import com.chuanonly.wallpaper.data.WallpaperInfo;
import com.chuanonly.wallpaper.model.WOEIDUtils;
import com.chuanonly.wallpaper.model.WeatherInfo;
import com.chuanonly.wallpaper.service.WallpaperService;
import com.chuanonly.wallpaper.util.Http;
import com.chuanonly.wallpaper.util.URLUtil;
import com.chuanonly.wallpaper.util.Util;

public class HTTPTask extends AsyncTask<Void, Void, String>
{

	@Override
	protected String doInBackground(Void... params)
	{
		try
		{
			int isYahoo = Util.getIntFromSharedPref(Util.ISYAHOO, 0);
			if (isYahoo == 0)
			{
				String url = "90TYIJFMjR0b2x0MShXWYJEcM1WM2lVbsNnWTRjeOpWQ1llM0YXWywGMlNVO30ESw8yYHRnbQh1c4Z2UapGZtZVeQh1c5Z2UaJjWYlUOlpnT5okbSZXYyYVdQh1cwYWU";// http://tqapi.mobile.360.cn/city/{0}?pkg={1}&cver={2}&ver={3}&token={4}";
				String pkg = "QPi1mVwwkbGBXYHljdM1GeoR2V1oWYHZVeM5GZwp1RkxGZDVjaidUOqF2MkxWWYJ1bahVS==";// "net.qihoo.launcher.widget.clockweather";
				String token = Util.getToken();
				String cver = "29";
				String api = "1";
				String cityCode = Util.getStringFromSharedPref(Util.CODE, "");

				if (TextUtils.isEmpty(cityCode))
					return null;
				url = URLUtil.decodeURL(url);
				url = MessageFormat.format(url,
						URLEncoder.encode(cityCode, "utf-8"),
						URLEncoder.encode(URLUtil.decodeURL(pkg), "utf-8"),
						URLEncoder.encode(cver, "utf-8"),
						URLEncoder.encode(api, "utf-8"),
						URLEncoder.encode(token, "utf-8"));
				HttpResponse response = null;
				response = Http.getResponse(MyApplication.getContext(), url);
				if (response == null || response.getStatusLine() == null
						|| response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				{
					return null;
				}
				InputStream instream = Http.getResponseStream(response);
				String jsonUrl = Http.parseIputStreamToString(instream);
				if (jsonUrl != null && jsonUrl.startsWith("http:"))
				{
					response = Http.getResponse(MyApplication.getContext(), jsonUrl);
					if (response == null
							|| response.getStatusLine() == null
							|| response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
					{
						return null;
					}
					instream = Http.getResponseStream(response);
					String result = Http.parseIputStreamToString(instream);
					return result;
				}
			}else {
				//yahoo == 1;
				String yahooCityCode = Util.getStringFromSharedPref(Util.CODE, "");
				String weatherString = Util.getWeatherString(MyApplication.getContext(), yahooCityCode);
				WOEIDUtils woeidUtils = WOEIDUtils.getInstance();
				Document weatherDoc = Util.convertStringToDocument(MyApplication.getContext(), weatherString);
				WeatherInfo weatherInfo = Util.parseWeatherInfo(MyApplication.getContext(), weatherDoc, woeidUtils.getWoeidInfo());
				if (weatherInfo != null)
				{					
					weatherInfo.WoeidNumber = yahooCityCode;
					Util.setStringToSharedPref(Util.SCENE_INFO, weatherInfo.getCurrentText());
					Util.setIntToSharedPref(Util.REAL_TYPE, Util.changeYahooType(weatherInfo.getCurrentCode()));
					Util.setIntToSharedPref(Util.TYPE, Util.changeYahooType(weatherInfo.getCurrentCode()));
					Util.setStringToSharedPref(Util.YAHOO_DESC, weatherInfo.getCurrentText());
					Util.setStringToSharedPref(Util.SCENE_TEMPERATUR, String.valueOf(weatherInfo.getCurrentTempC()));
					Util.setLongToSharedPref(Util.LAST_UPDATETIME, System.currentTimeMillis());
					Util.setLongToSharedPref(Util.LAST_PICK_TIME, 0);
					
					int mode = Util.getIntFromSharedPref(Util.MODE, -1);
					if (mode ==-1)
					{
						Util.setIntToSharedPref(Util.MODE, 2);
					}
				}
			}
			
		} catch (Exception e)
		{
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result)
	{
		if (result == null)
			return;
		try
		{
			WallpaperInfo.parseWallpaperInfo(result);

		} catch (Exception e)
		{
			// TODO: handle exception
		}
		try {			
			MyApplication.getContext().sendBroadcast(
					new Intent(WallpaperService.ACTION_CHANGE_BROCAST));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
