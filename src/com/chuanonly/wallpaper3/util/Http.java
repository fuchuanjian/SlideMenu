package com.chuanonly.wallpaper3.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;

public class Http
{
	private static final String USER_AGENT = URLUtil.decodeURL("90TT6l1dUdVOpF2V4xmUHZlehNjU2NWQ");

	public static HttpResponse getResponse(Context context, String url)
			throws ClientProtocolException, IOException
	{
		HttpClient client = Http.createHttpClient();
		HttpGet request = Http.createHttpRequest(context, url, true, true);
		return client.execute(request);
	}

	public static InputStream getResponseStream(HttpResponse response)
			throws IOException
	{
		if (response == null)
		{
			return null;
		}
		InputStream in = response.getEntity().getContent();
		Header contentEncoding = response.getFirstHeader("Content-Encoding");
		if (contentEncoding != null
				&& contentEncoding.getValue().equalsIgnoreCase("gzip"))
		{
			in = new GZIPInputStream(in);
		}
		return in;
	}

	public static String parseIputStreamToString(InputStream in)
			throws IOException, JSONException
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String readLine = null;
			StringBuilder html = new StringBuilder();
			while ((readLine = reader.readLine()) != null)
			{
				html.append(readLine);
			}
			return html.toString();
		} finally
		{
			if (reader != null)
			{
				reader.close();
			}
		}

	}

	public static HttpClient createHttpClient()
	{
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpProtocolParams.setUserAgent(httpParams, USER_AGENT);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	@SuppressWarnings("unchecked")
	public static <T> T createHttpRequest(Context context, String url,
			boolean isGetRequest, boolean needGzip)
	{
		String proxyHost = null;
		int proxyPort = -1;
		boolean useProxy = false;

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivity == null ? null : connectivity
				.getActiveNetworkInfo();

		if (networkInfo != null
				&& networkInfo.getType() != ConnectivityManager.TYPE_WIFI)
		{
			proxyHost = Proxy.getHost(context);
			proxyPort = Proxy.getPort(context);
			useProxy = (proxyHost != null && proxyPort > 0);
		}

		HttpRequest request;

		if (isGetRequest)
		{
			request = new HttpGet(url);
		} else
		{
			request = new HttpPost(url);
		}

		if (useProxy)
		{
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			ConnRouteParams.setDefaultProxy(request.getParams(), proxy);
		}

		if (needGzip)
		{
			request.addHeader("Accept-Encoding", "gzip");
		}

		return (T) request;
	}

}
