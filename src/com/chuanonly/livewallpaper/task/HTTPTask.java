package com.chuanonly.livewallpaper.task;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.os.AsyncTask;

import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.data.WallpaperInfo;
import com.chuanonly.livewallpaper.util.Http;
import com.chuanonly.livewallpaper.util.Trace;
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
		String url ="PC0tUWYwc1hQdVYyYXZSbko5TnplOUlYWjJaU2Z5c1hQeVZtZGpaU2Z4c1hQbnRHYy8wSE03OVNlMGwyWXY0Mll1QWpOejRTWnNsbVl2MW1McEJYWXhSM0x2b0RjMFJIYSEhPi0tPg==";//http://tqapi.mobile.360.cn/city/{0}?pkg={1}&cver={2}&ver={3}&token={4}";
		String pkg ="PC0tSVhab1JYWWxkM2FqOUdiajVDZGxkR1pwZG5MeVZHYWo1V2RoeG1MdjlHYXBGbkwwVm1iISE+";// "net.qihoo.launcher.widget.clockweather";
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
		Trace.i("fu", "result: "+ result);
		
		WallpaperInfo info = new WallpaperInfo(result);
		Trace.i("fu",info.toString());
	}

}
