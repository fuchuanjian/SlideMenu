package com.chuanonly.livewallpaper.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.data.WallpaperInfo;
import com.chuanonly.livewallpaper.util.Http;
import com.chuanonly.livewallpaper.util.Trace;

import android.os.AsyncTask;

public class HTTPTask extends AsyncTask<Void, Void, String>
{

	@Override
	protected String doInBackground(Void... params)
	{
		String cityCode = "101010100";
		String url = "http://tqapi.mobile.360.cn/city/{0}?pkg={1}&cver={2}&ver={3}&token={4}";
		String pkg = "net.qihoo.launcher.widget.clockweather";
		String token = "rNXyXJ12nyG4oSA%3D%3D";
		String cver = "20";
		String api = "1";
		try
		{
			url = MessageFormat.format(url, URLEncoder.encode(cityCode, "utf-8"), URLEncoder.encode(pkg, "utf-8"), URLEncoder.encode(cver, "utf-8"),
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
