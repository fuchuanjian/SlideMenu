package com.chuanonly.livewallpaper.service;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.os.AsyncTask;
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
import com.chuanonly.livewallpaper.util.QueryCityHandler;
import com.chuanonly.livewallpaper.util.Trace;
import com.chuanonly.livewallpaper.util.Util;

public class LocateAsyncTask 
{
	private LocationManagerProxy mAMapLocManager;
	private Handler mhHandler = new Handler();
	private AMapLocation aMapLocation;// 用于判断定位超时
	private static ArrayList<String> MDN_CITY_CODES; // 直辖市区号
	private QueryCityHandler suggestionSource;
	public LocateAsyncTask()
	{
		if (MDN_CITY_CODES == null) {
            MDN_CITY_CODES = new ArrayList<String>();
            MDN_CITY_CODES.add("010"); // 北京
            MDN_CITY_CODES.add("021"); // 上海
            MDN_CITY_CODES.add("022"); // 天津
            MDN_CITY_CODES.add("023"); // 重庆
        }
		synchronized (this)
		{
			if (suggestionSource == null)
			{
				suggestionSource = new QueryCityHandler(MyApplication.getContext());
			}
		}
	}

	public void getLocate()
	{
		LocationManagerProxy mAMapLocManager = LocationManagerProxy.getInstance(MyApplication.getContext());
		mAMapLocManager.setGpsEnable(false);
		mAMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 1000, locateListener);
		mhHandler.postDelayed(cancelRuanble, 10000);// 设置超过12秒还没有定位到就停止定位
	}
	private Runnable cancelRuanble =  new Runnable()
	{
		public void run()
		{
			if (aMapLocation == null) {
				relese();
			}
		}
	};
	private AMapLocationListener locateListener = new AMapLocationListener()
	{
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{
		}
		
		@Override
		public void onProviderEnabled(String arg0)
		{
		}
		
		@Override
		public void onProviderDisabled(String arg0)
		{
		}
		
		@Override
		public void onLocationChanged(Location arg0)
		{
		}
		
		@Override
		public void onLocationChanged(AMapLocation location)
		{
			if (location != null) {
				relese();
				mhHandler.removeCallbacks(cancelRuanble);
				aMapLocation = location;
//				Double geoLat = location.getLatitude();
//				Double geoLng = location.getLongitude();
//				String cityCode = "";
//				String desc = "";
//				Bundle locBundle = location.getExtras();
//				if (locBundle != null) {
//					cityCode = locBundle.getString("citycode");
//					desc = locBundle.getString("desc");
//				}
//				String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
//						+ "\n精    度    :" + location.getAccuracy() + "米"
//						+ "\n定位方式:" + location.getProvider() +  "\n城市编码:"
//						+ cityCode + "\n位置描述:" + desc + "\n省:"
//						+ location.getProvince() + "\n市:" + location.getCity()
//						+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
//						.getAdCode());
//				Trace.i("fu","---"+str);

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
				Trace.d("fu", "getCityOfLocation() location = " + location.toString());
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
	            	Util.setStringToSharedPref(Util.CODE, retCity.code);
	            	Util.setStringToSharedPref(Util.NAME, retCity.name);
	            	Util.setStringToSharedPref(Util.EN_NAME, retCity.enName);
	            	Trace.i("fu",retCity.toString());
	            }
			}
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

	public void relese()
	{
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(locateListener);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
		
	}
}
