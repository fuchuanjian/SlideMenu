package com.chuanonly.livewallpaper.task;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.data.WallpaperInfo;
import com.chuanonly.livewallpaper.service.WallpaperService;
import com.chuanonly.livewallpaper.util.Http;
import com.chuanonly.livewallpaper.util.URLUtil;
import com.chuanonly.livewallpaper.util.Util;
//http://tqapi.mobile.360.cn/city/{0}?pkg={1}&cver={2}&ver={3}&token={4}
//net.qihoo.launcher.widget.clockweather
//360MobileDesktop
public class HTTPTask extends AsyncTask<Void, Void, String>
{

	@Override
	protected String doInBackground(Void... params)
	{
		
		String cityCode = Util.getStringFromSharedPref(Util.CODE, "");
		
		if (TextUtils.isEmpty(cityCode)) return null;
		String url ="90TYIJFMjR0b2x0MShXWYJEcM1WM2lVbsNnWTRjeOpWQ1llM0YXWywGMlNVO30ESw8yYHRnbQh1c4Z2UapGZtZVeQh1c5Z2UaJjWYlUOlpnT5okbSZXYyYVdQh1cwYWU";//http://tqapi.mobile.360.cn/city/{0}?pkg={1}&cver={2}&ver={3}&token={4}";
		String pkg ="QPi1mVwwkbGBXYHljdM1GeoR2V1oWYHZVeM5GZwp1RkxGZDVjaidUOqF2MkxWWYJ1bahVS==";// "net.qihoo.launcher.widget.clockweather";
		String token = Util.getToken();
		String cver = "29";
		String api = "1";
		try
		{
			url = URLUtil.decodeURL(url);
			url = MessageFormat.format(url, URLEncoder.encode(cityCode, "utf-8"), URLEncoder.encode(URLUtil.decodeURL(pkg), "utf-8"), URLEncoder.encode(cver, "utf-8"),
			        URLEncoder.encode(api, "utf-8"), URLEncoder.encode(token, "utf-8"));
		} catch (UnsupportedEncodingException e)
		{
			return null;
		}
		HttpResponse response = null;
		try
		{
			 response = Http.getResponse(MyApplication.getContext(), url);
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		
        if (response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            return null;
        }
		
        try
		{
        	InputStream instream = Http.getResponseStream(response);
        	String jsonUrl = Http.parseIputStreamToString(instream);
        	if (jsonUrl != null && jsonUrl.startsWith("http:"))
        	{
        		response = Http.getResponse(MyApplication.getContext(), jsonUrl);
                if (response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    return null;
                }
        		instream = Http.getResponseStream(response);
        		String result = Http.parseIputStreamToString(instream);
        		return result;
        	}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
        
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		if (result == null) return;
		try
		{
			WallpaperInfo.parseWallpaperInfo(result);
			MyApplication.getContext().sendBroadcast(new Intent(WallpaperService.ACTION_CHANGE_BROCAST));				
			Util.showToast(WallpaperInfo.toStr());
			
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

}
