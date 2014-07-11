package com.chuanonly.wallpaper3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chuanonly.wallpaper3.service.WallpaperService;
import com.chuanonly.wallpaper3.util.Util;
import com.google.android.gms.drive.internal.t;

public class SettingActivity extends Activity
{
	private CheckBox checkBoxs[] = new CheckBox[3];
	private View layouts[] = new View[3];
	private TextView weatherInfoTV;
	private TextView mCurWallpaerTV;
	private CheckBox checkBoxC, checkBoxF;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		for (int i = 0; i < 3; i++)
		{
			int id = getResources().getIdentifier("checkbox_" + (i + 1), "id",
					getPackageName());
			checkBoxs[i] = (CheckBox) findViewById(id);
		}
		for (int i = 0; i < 3; i++)
		{
			int id = getResources().getIdentifier("layout_" + (i + 1), "id",
					getPackageName());
			layouts[i] = findViewById(id);
			layouts[i].setOnClickListener(click);
		}
		findViewById(R.id.set_city).setOnClickListener(click);
		findViewById(R.id.return_btn).setOnClickListener(click);
		mCurWallpaerTV = (TextView) findViewById(R.id.current_wallpaper);
		View rateView = findViewById(R.id.rate_layout);
		int loginCnt = Util.getIntFromSharedPref(Util.LOG_INT_CNT, 0);
		if (MyApplication.language == 2 || loginCnt <= 2)
		{
			rateView.setVisibility(View.GONE);
			findViewById(R.id.last_divider).setVisibility(View.GONE);
		}else {			
			rateView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try
					{
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id=" + getPackageName()));
						startActivity(intent);					
					}catch (Exception e)
					{
						
					}
				}
			});
		}
		findViewById(R.id.thank_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=com.chuanonly.livewallpaper"));
					startActivity(intent);					
				}catch (Exception e)
				{
					
				}
				
			}
		});
		
		
		checkBoxC = (CheckBox) findViewById(R.id.checkbox_temperC);
		checkBoxF = (CheckBox) findViewById(R.id.checkbox_temperF);
		
		int tempType = Util.getIntFromSharedPref(Util.TEMP_TYPE, 0);
		checkBoxC.setChecked(tempType == 0? true : false);
		checkBoxF.setChecked(tempType == 0? false : true);
		findViewById(R.id.checkbox_temperC).setOnClickListener(tempClick);
		findViewById(R.id.checkbox_temperF).setOnClickListener(tempClick);
	}

	private OnClickListener tempClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.checkbox_temperC)
			{
				Util.setIntToSharedPref(Util.TEMP_TYPE, 0);
			}else if (v.getId() == R.id.checkbox_temperF){
				Util.setIntToSharedPref(Util.TEMP_TYPE, 1);
			}
			int tempType = Util.getIntFromSharedPref(Util.TEMP_TYPE, 0);
			checkBoxC.setChecked(tempType == 0? true : false);
			checkBoxF.setChecked(tempType == 0? false : true);
			showWeatherInfo();
		}
		
	};
	private void showWeatherInfo()
	{
		String cityName = Util.getCityName();
		String info = Util.getWeatherInfoOfReal();
		String temperatrue = Util.getStringFromSharedPref(Util.SCENE_TEMPERATUR, "");
		if (!TextUtils.isEmpty(temperatrue))
		{
//			temperatrue = temperatrue + MyApplication.getContext().getString(R.string.temp_unit);
			temperatrue = Util.getTemperatrue(temperatrue);
		}
		String weatherInfo = MyApplication.getContext().getString(R.string.weather_info, cityName, info, temperatrue);
		String total =  MyApplication.getContext().getString(R.string.weather_city) + weatherInfo;
		SpannableStringBuilder style = new SpannableStringBuilder(total);
		int start = total.length() - weatherInfo.length();
		int end = total.length();
        style.setSpan(new ForegroundColorSpan(0xFF47AAEC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(16, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        weatherInfoTV.setText(style);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		Util.checkIfNeedToUpdateWeather();
		
		String cityName = Util.getCityName();
		weatherInfoTV  = (TextView)findViewById(R.id.weather_info_txt);
		if (!TextUtils.isEmpty(cityName))
		{
			showWeatherInfo();
		}
		String wallpaperStr = getString(R.string.no_change_desc, Util.getWeatherInfoOfWallpaper());
		mCurWallpaerTV.setText(wallpaperStr);
		registerReceiver();
		int mode = Util.getIntFromSharedPref(Util.MODE, -1);
		String city = Util.getStringFromSharedPref(Util.CODE, "");
		if (mode == -1 )
		{
			if (!TextUtils.isEmpty(city))
			{
				mode = 2;
				Util.setIntToSharedPref(Util.MODE, 2);
			}else
			{
				mode = 1;
				Util.setIntToSharedPref(Util.MODE, 1);
			}
		}
		for (int i = 0; i < checkBoxs.length; i++)
		{
			if (i == mode)
			{
				checkBoxs[i].setChecked(true);
			}else
			{
				checkBoxs[i].setChecked(false);
			}
		}
	}
	
	@Override
	protected void onPause()
	{
		unregisterReceiver();
		super.onPause();
	}
	private OnClickListener click = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			
			int pos = -1;
			if (v.getId() == R.id.layout_1)
			{
				pos = 0;
				Util.setIntToSharedPref(Util.MODE, 0);
			} else if (v.getId() == R.id.layout_2)
			{
				pos = 1;
				Util.setIntToSharedPref(Util.MODE, 1);
			} else if (v.getId() == R.id.layout_3)
			{
				pos = 2;
				String citycode = Util.getStringFromSharedPref(Util.CODE, "");
				if (TextUtils.isEmpty(citycode))
				{
					Intent intent = new Intent(SettingActivity.this, ChooseCityActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_defalut);
				}else
				{					
					Util.setIntToSharedPref(Util.MODE, 2);
					Util.setLongToSharedPref(Util.LAST_PICK_TIME, 0);
					int saveType = Util.getIntFromSharedPref(Util.REAL_TYPE, -1);
					if (saveType != -1)
					{
						Util.setIntToSharedPref(Util.TYPE, saveType);
					}
					Util.checkIfNeedToUpdateWeather();
				}
				
			}else if (v.getId() == R.id.set_city){
				Intent intent = new Intent(SettingActivity.this, ChooseCityActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_defalut);
			}else if (v.getId() == R.id.return_btn)
			{
				finish();
			}
			
			if (pos != -1)
			for (int i = 0; i < 3; i++)
			{
				if (i == pos)
				{
					checkBoxs[i].setChecked(true);
				} else
				{
					checkBoxs[i].setChecked(false);
				}
			}

		}
	};
	
    private IntentFilter mFilter;
    private void registerReceiver()
	{
		if (mFilter == null)
		{
			mFilter = new IntentFilter();
			mFilter.addAction(WallpaperService.ACTION_CHANGE_BROCAST);
		}
		try
		{
			registerReceiver(mReceiver, mFilter);
		} catch (Exception e)
		{
		}
	}
    private void unregisterReceiver()
	{
		try
		{
			this.unregisterReceiver(mReceiver);
		} catch (Exception e)
		{
		}
	}
    private BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (WallpaperService.ACTION_CHANGE_BROCAST.equals(action))
			{
				showWeatherInfo();
			}
		};
	};
	@Override
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.anim_defalut, R.anim.anim_right_exit);
	}
}
