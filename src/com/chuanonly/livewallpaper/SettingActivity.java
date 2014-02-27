package com.chuanonly.livewallpaper;

import com.chuanonly.livewallpaper.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class SettingActivity extends Activity
{
	private CheckBox checkBoxs[] = new CheckBox[3];
	private View layouts[] = new View[3];

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
		
		
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		int mode = Util.getIntFromSharedPref(Util.MODE, 1);
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
			}else if (v.getId() == R.id.set_city){
				Util.setIntToSharedPref(Util.MODE, 2);
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
}
