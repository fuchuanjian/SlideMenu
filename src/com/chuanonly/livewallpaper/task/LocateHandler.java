package com.chuanonly.livewallpaper.task;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.R;
import com.chuanonly.livewallpaper.data.City;
import com.chuanonly.livewallpaper.util.Http;
import com.chuanonly.livewallpaper.util.QueryCityHandler;
import com.chuanonly.livewallpaper.util.Trace;
import com.chuanonly.livewallpaper.util.Util;

public class LocateHandler
{
	private LocationManagerProxy mAMapLocManager;
	private Handler mHandler = new Handler();
	private static ArrayList<String> MDN_CITY_CODES; // 直辖市区号
	private QueryCityHandler suggestionSource;
	private Context context;
	
	private boolean isSucess = false;
	public LocateHandler(Context context)
	{
		this.context = context;
		if (MDN_CITY_CODES == null) {
            MDN_CITY_CODES = new ArrayList<String>();
            MDN_CITY_CODES.add("010"); // 北京
            MDN_CITY_CODES.add("021"); // 上海
            MDN_CITY_CODES.add("022"); // 天津
            MDN_CITY_CODES.add("023"); // 重庆
        }

	}

	public void tryToLacate()
	{
		if (suggestionSource == null)
		{
			suggestionSource = new QueryCityHandler(MyApplication.getContext());
			LocationManagerProxy mAMapLocManager = LocationManagerProxy.getInstance(context);
			mAMapLocManager.setGpsEnable(false);
			mAMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 20000, 1000, locationListener);
			mHandler.postDelayed(cancelRuanble, 10000);// 设置超过12秒还没有定位到就停止定位
		}
	}
	
	private AMapLocationListener locationListener = new AMapLocationListener()
	{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onProviderEnabled(String provider)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(final AMapLocation location)
		{
			if (location == null || isSucess ) return;
			
			
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					parseCitycode(location);
				}
			}).start();
			release();
		}

		

	};
	
	private Runnable cancelRuanble =  new Runnable()
	{
		public void run()
		{
			release();
		}
	};
	
	
	 private City getCityByName(QueryCityHandler suggestionSource, String cityName, String provinceName) {
	        List<City> cities = suggestionSource.queryCities(cityName, "");
	        City tempCity = null;
	        if (cities != null) {
	            for (City city : cities) {
	                if (city.name.startsWith(cityName) || cityName.startsWith(city.name)) {
	                    // tempCity = city; // 遇到同名地区时，会返回错误信息。如:湖南张家界的永定区会返回福建永定
	                    if (city.parentName.startsWith(provinceName) || provinceName.startsWith(city.parentName)) {
	                        return city;
	                    }
	                }
	            }
	        }
	        return tempCity;
	    }

	public void release()
	{
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(locationListener);
			mAMapLocManager.destory();
		}
		
	}
		
	private void parseCitycode(AMapLocation location)
	{
		if (location != null) {

			String cityCode = "";
			String desc = "";
	        String country;
	        String province;
	        String city;
	        String subCity;
			if (location != null)
			{
				Bundle locBundle = location.getExtras();
				if (locBundle != null)
				{
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
			}
			String result = desc;
			String[] args = result.split(" ");
			boolean isMDN = false; // 是否是直辖市
			if (!TextUtils.isEmpty(cityCode) && MDN_CITY_CODES != null)
			{ // 先根据区域号判断
				if (MDN_CITY_CODES.contains(cityCode))
				{
					isMDN = true;
				}
			} else
			{ // 根据desc判断
				if (args.length < 5)
				{
					isMDN = true;
				}
			}

			if (isMDN)
			{
				// 直辖市
				city = args[0];
				subCity = args[1];
			} else
			{
				// 普通城市
				city = args[1];
				subCity = args[2];
			}
			country = MyApplication.getContext().getText(R.string.china).toString();
			province = args[0];
			
			City retCity = null;
			if ( subCity != null) {
                // 避免出现单字城市，虽然在高德的返回值里面都带上了地区行政单位，理论上不会出现一个字的地名，但是为避免出现意外，还是做一个判断
                int length = subCity.length();
                if (length > 2) {
                    retCity = getCityByName(suggestionSource, subCity.substring(0, length - 1), province);
                } else {
                    retCity = getCityByName(suggestionSource, subCity, province);
                }
            }
            if (retCity == null && city != null) {
                int length = city.length();
                if (length > 2) {
                    retCity = getCityByName(suggestionSource, city.substring(0, length - 1), province);
                } else {
                    retCity = getCityByName(suggestionSource, city, province);
                }
            }
            if (retCity == null && province != null) {
                retCity = getCityByName(suggestionSource, province, country);
            }
            if (retCity == null  && retCity == null && country!= null) {
                retCity = getCityByName(suggestionSource, country, country);
            }
			
            
            if (retCity != null)
            {
            	isSucess = true;
            	Util.setStringToSharedPref(Util.CODE, retCity.code);
            	Util.setStringToSharedPref(Util.NAME, retCity.name);
            	Util.setStringToSharedPref(Util.EN_NAME, retCity.enName);
            	Util.setIntToSharedPref(Util.MODE, 2);
            	
            	new HTTPTask().execute();
            	Trace.i("fu",retCity.toString());
            }
		}
		
	}
}
