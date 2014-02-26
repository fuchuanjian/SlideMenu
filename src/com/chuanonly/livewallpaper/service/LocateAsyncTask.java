package com.chuanonly.livewallpaper.service;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.util.Trace;
import com.chuanonly.livewallpaper.util.Util;

public class LocateAsyncTask  implements Runnable
{
	private LocationManagerProxy mAMapLocManager;
	private Handler mhHandler = new Handler();
	private AMapLocation aMapLocation;// 用于判断定位超时
	public void getLocate()
	{
		LocationManagerProxy mAMapLocManager = LocationManagerProxy.getInstance(MyApplication.getContext());
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 100, locateListener);
		mhHandler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位
	}
	@Override
	public void run() {
		if (aMapLocation == null) {
			if (mAMapLocManager != null) {
				mAMapLocManager.removeUpdates(locateListener);
				mAMapLocManager.destory();
			}
			mAMapLocManager = null;
		}
	}
	
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
				aMapLocation = location;// 判断超时机制
				Double geoLat = location.getLatitude();
				Double geoLng = location.getLongitude();
				String cityCode = "";
				String desc = "";
				Bundle locBundle = location.getExtras();
				if (locBundle != null) {
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
				String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
						+ "\n精    度    :" + location.getAccuracy() + "米"
						+ "\n定位方式:" + location.getProvider() +  "\n城市编码:"
						+ cityCode + "\n位置描述:" + desc + "\n省:"
						+ location.getProvince() + "\n市:" + location.getCity()
						+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
						.getAdCode());
				Util.showToast(desc);
				Trace.i("fu","---"+str);
			}
		}
	};
}
