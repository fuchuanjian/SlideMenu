package com.chuanonly.livewallpaper.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.model.WeatherType;
import com.chuanonly.livewallpaper.service.WallpaperService;

public class Util
{
	
	public static final String SCENE_TYPE = "scene_type";
	
	public static final String SCENE_REAL_TYPE = "scene_real_type";

	public static final String SCENE_INFO = "info";
	
	public static final String SCENE_TEMPERATUR = "temperature";
	
	public static final String SUN_RISE = "rise_time";

	public static final String SUN_SET = "set_time";
	
	public static final String CODE = "code";

	public static final String NAME = "name";

	public static final String EN_NAME = "en_name";
	
	public static final String MODE = "mode";

	public static final String LAST_PICK_TIME = "last_pick_time";
	
	public static final long HOUR_2 = 2*60*60*1000;
	
	public static final long HOUR_1 = 1*60*60*1000;
	
	public static final String LAST_UPDATETIME = "lasttime";
	
    public static int convertIndex2Category(int index) {
		int category = WeatherType.RAINY_LIGHT;
		
		switch (index) {
		case 0:
			category = WeatherType.RAINY_LIGHT;
			break;
		case 1:
			category = WeatherType.RAINY_MODERATE;
			break;
		case 2:
			category = WeatherType.RAINY_HEAVY;
			break;
		case 3:
			category = WeatherType.RAINY_STORM;
			break;
		case 4:
			category = WeatherType.SHOWER;
			break;
		case 5:
			category = WeatherType.SHOWER_THUNDER;
			break;
		case 6:
			category = WeatherType.CLOUDY;
			break;
		case 7:
			category = WeatherType.FINE;
			break;
		case 8:
			category = WeatherType.FOG;
			break;
		case 9:
			category = WeatherType.HAZE;
			break;
		case 10:
			category = WeatherType.DUST_FLOATING;
			break;
		case 11:
			category = WeatherType.SAND_STORM;
			break;
		case 12:
			category = WeatherType.HAILSTONE;
			break;
		case 13:
			category = WeatherType.SNOW_LIGHT;
			break;
		case 14:
			category = WeatherType.SNOW_MODERATE;
			break;
		case 15:
			category = WeatherType.SNOW_HEAVY;
			break;
		case 16:
			category = WeatherType.SNOW_STORM;
			break;
		case 17:
			category = WeatherType.SNOW_SHOWER;
			break;
		case 18:
			category = WeatherType.OVERCAST;
			break;
		case 19:
			category = WeatherType.FINE_NIGHT;
			break;
		case 20:
			category = WeatherType.CLOUDY_NIGHT;
			break;
		}
		
		return category;
    }
    
    
    public static int normalDayOrNight(int weatherType, boolean isDay) {
        int category = weatherType;
        switch (weatherType) {
            case WeatherType.FINE:
            case WeatherType.FINE_NIGHT:
                if (!isDay) {
                    category = WeatherType.FINE_NIGHT;
                } else {
                    category = WeatherType.FINE;
                }
                break;
            case WeatherType.CLOUDY:
            case WeatherType.CLOUDY_NIGHT:
                if (!isDay) {
                    category = WeatherType.CLOUDY_NIGHT;
                } else {
                    category = WeatherType.CLOUDY;
                }
                break;
            case WeatherType.RAIN_L_M:
            case WeatherType.SEE_RAIN:
            case WeatherType.RAIN_FREEZING:
            case WeatherType.RAINY_AND_SNOW:
            case WeatherType.GLAZE:
                category = WeatherType.RAINY_LIGHT;
                break;
            case WeatherType.RAIN_M_H:
                category = WeatherType.RAINY_MODERATE;
                break;
            case WeatherType.RAIN_H_S:
                category = WeatherType.RAINY_HEAVY;
                break;
            case WeatherType.DOWNPOUR:
            case WeatherType.RAIN_S_E:
            case WeatherType.RAIN_E2:
            case WeatherType.RAINY_STORM_EXTRA:
                category = WeatherType.RAINY_STORM;
                break;

            case WeatherType.LIGHTING:
            case WeatherType.THUNDER_WIND:
            case WeatherType.THUNDER_NO_RAIN:
                category = WeatherType.SHOWER_THUNDER;
                break;
            case WeatherType.SNOW_SHOWER:
            case WeatherType.SNOW_L_M:
                category = WeatherType.SNOW_LIGHT;
                break;
            case WeatherType.SNOW_M_H:
                category = WeatherType.SNOW_MODERATE;
                break;
            case WeatherType.SNOW_H_S:
            case WeatherType.SNOW_STORM:
                category = WeatherType.SNOW_HEAVY;
                break;
            case WeatherType.DUST_DEVIL:
            case WeatherType.DUST:
//                category = WeatherType.DUST_FLOATING;
            	category = WeatherType.FOG;
                break;
            case WeatherType.SAND_BLOWING:
            case WeatherType.SAND_STORM_HEAVY:
            case WeatherType.TORNADO:
                category = WeatherType.OVERCAST;
                break;
            case WeatherType.ICE_PARTICLES:
            case WeatherType.GRAUPEL:
                category = WeatherType.HAILSTONE;
                break;
            case WeatherType.ICE_NEEDLE:
                if (isDay) {
                    category = WeatherType.FINE;
                } else {
                    category = WeatherType.FINE_NIGHT;
                }
                break;
            case WeatherType.NA_SCENE:
                category = WeatherType.NA_SCENE;
                break;
        }
        return category;
    }
    
    public static int getCurrentCategory(int category) {
        boolean isDay = true;
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(Util.SPF_SETTING, Context.MODE_MULTI_PROCESS );
        String dayTimeStr = "06:00";
        String nightTimeStr = "18:00";
        if (sp != null)
        {
        	
        	dayTimeStr = sp.getString(SUN_RISE, "");
        	nightTimeStr = sp.getString(SUN_SET, "");
        }
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String nowTimeStr = timeFormat.format(nowTime);
        if ("".equals(dayTimeStr) || "".equals(nightTimeStr)) {
            if (nowTimeStr.compareToIgnoreCase("18:00") < 0 && nowTimeStr.compareToIgnoreCase("06:00") > 0) {
                isDay = true;
            } else {
                isDay = false;
            }

        } else {
            int result1 = nowTimeStr.compareToIgnoreCase(nightTimeStr);
            int result2 = nowTimeStr.compareToIgnoreCase(dayTimeStr);
            if (nightTimeStr.compareToIgnoreCase(dayTimeStr) > 0) {
                if (result1 >= 0) {
                    isDay = false;
                } else {
                    if (result2 >= 0) {
                        isDay = true;
                    } else
                        isDay = false;
                }
            } else {
                if (result1 >= 0 && result2 < 0) {
                    isDay = false;
                } else {
                    isDay = true;
                }

            }
        }
        Trace.i("fu",category +" "+ isDay);
        int index = Util.normalDayOrNight(category, isDay);

        return index;
    }
    private static Toast toast;
    public static void showToast(String str) {
        try {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.getContext(), str, Toast.LENGTH_SHORT);
            }
            toast.setText(str);
            toast.setDuration(0);
            toast.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    public static void release() {
        toast = null;
    }
	public static final String SPF_SETTING = "setting";
    public static void setStringToSharedPref(String key, String value)
    {
    	SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SPF_SETTING, Context.MODE_MULTI_PROCESS );
    	Editor editor =  sp.edit();
    	editor.putString(key, value);
    	editor.commit();
    }
    
    
    public static void setIntToSharedPref(String key, int value)
    {
    	SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SPF_SETTING, Context.MODE_MULTI_PROCESS );
    	Editor editor =  sp.edit();
    	editor.putInt(key, value);
    	editor.commit();
    }
    public static void setLongToSharedPref(String key, long value)
    {
    	SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SPF_SETTING, Context.MODE_MULTI_PROCESS );
    	Editor editor =  sp.edit();
    	editor.putLong(key, value);
    	editor.commit();
    }
    
    public static String getStringFromSharedPref(String key, String defValue)
    {
    	SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SPF_SETTING, Context.MODE_MULTI_PROCESS );
    	return sp.getString(key, defValue);
    }
    public static int getIntFromSharedPref(String key, int defValue)
    {
    	SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SPF_SETTING, Context.MODE_MULTI_PROCESS );
    	return sp.getInt(key, defValue);
    }
    
    public static int getWeathType()
    {
    	long lastpickTime = Util.getLongFromSharedPref(LAST_PICK_TIME, 0);
    	int realType = Util.getIntFromSharedPref(SCENE_REAL_TYPE, 0);
    	int tpye = Util.getIntFromSharedPref(SCENE_TYPE, 0);
    	int mode = Util.getIntFromSharedPref(Util.MODE, -1);
    	if (mode == 2 && lastpickTime + HOUR_1 < System.currentTimeMillis())
    	{
    		return realType;
    	}else {
			return tpye;
		}
    }
    
    public static long getLongFromSharedPref(String key, long defValue)
    {
    	SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SPF_SETTING, Context.MODE_MULTI_PROCESS );
    	return sp.getLong(key, defValue);
    }
    public static String getToken()
    {
    	
    	String token = Util.getStringFromSharedPref("id", "");
    	if (token != null && !token.equals(""))
    	{
    		return token;
    	}
    	boolean success = true;
    	 String imei  = ((TelephonyManager)MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
         boolean isZero = true;
    	 for (int i=0; i< imei.length(); i++)
         {
        	 if (imei.charAt(i) != '0')
        	 {
        		 isZero = false;
        		 break;
        	 }
         }
    	 if (isZero)
    	 {
    		 imei = String.valueOf(System.currentTimeMillis());
    	 }
    	 String sn = SystemProperties.get("ro.serialno");
         String data = imei+"_"+sn;
         Trace.e("fu","imei="+imei+"   sn="+sn);
         try
         {
 	        MessageDigest hash = MessageDigest.getInstance("MD5");
 	        hash.update(data.getBytes("UTF-8"));
 	        byte[] bytes = hash.digest();
 			token = new String(URLUtil.encodeBytesToBytes(bytes), "UTF-8");
 		} catch (UnsupportedEncodingException e)
 		{
 			success = false;
 			e.printStackTrace();
 		} catch (NoSuchAlgorithmException e)
 		{
 			success = false;
 			e.printStackTrace();
 		}
         if (success)
        	 Util.setStringToSharedPref("id", token);
        return token;
    }
    
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
            if (networkInfos == null) {
                return false;
            }
            for (NetworkInfo networkInfo : networkInfos) {
                if (networkInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }
    
}
