package com.chuanonly.livewallpaper;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.chuanonly.livewallpaper.service.WallpaperService;
import com.chuanonly.livewallpaper.util.Util;

public class SettingActivity extends Activity
{
	private CheckBox checkBoxs[] = new CheckBox[3];
	private View layouts[] = new View[3];
	private TextView weatherInfoTV;
	
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
		
		String cityName = Util.getStringFromSharedPref(Util.NAME, "");
		weatherInfoTV  = (TextView)findViewById(R.id.weather_info_txt);
		if (!TextUtils.isEmpty(cityName))
		{
			showWeatherInfo();
		}
	}

	private void showWeatherInfo()
	{
		String cityName = Util.getStringFromSharedPref(Util.NAME, "");
		String info = Util.getStringFromSharedPref(Util.SCENE_INFO, "");
		String temperatrue = Util.getStringFromSharedPref(Util.SCENE_TEMPERATUR, "");
		if (!TextUtils.isEmpty(temperatrue))
		{
			temperatrue = temperatrue + MyApplication.getContext().getString(R.string.temp_unit);
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
		registerReceiver();
		int mode = Util.getIntFromSharedPref(Util.MODE, -1);
		String city = Util.getStringFromSharedPref(Util.CODE, "");
		if (mode == -1 && !TextUtils.isEmpty(city))
		{
			mode = 2;
			Util.setIntToSharedPref(Util.MODE, 2);
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
				Util.setIntToSharedPref(Util.MODE, 2);
				Util.setLongToSharedPref(Util.LAST_PICK_TIME, 0);
			}else if (v.getId() == R.id.set_city){
				Intent intent = new Intent(SettingActivity.this, ChooseCityActivity.class);
				startActivity(intent);
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
	
}
