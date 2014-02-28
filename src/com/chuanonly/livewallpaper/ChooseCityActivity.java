package com.chuanonly.livewallpaper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chuanonly.livewallpaper.data.City;
import com.chuanonly.livewallpaper.task.HTTPTask;
import com.chuanonly.livewallpaper.util.QueryCityHandler;
import com.chuanonly.livewallpaper.util.Trace;
import com.chuanonly.livewallpaper.util.Util;

public class ChooseCityActivity extends Activity implements OnClickListener
{
	
	private EditText mSearchEdit;
	private ListView mList;
	private QueryCityItemAdapter mAdapter;
	private InputMethodManager mimm = null;
	private QueryCityHandler queryHandler = null;
    private City mCity;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_city);
		initView();
		findViewById(R.id.return_btn).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				finish();
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		synchronized (this)
		{
			if (queryHandler == null)
			{
				queryHandler = new QueryCityHandler(this);
				initListView();
			}
		}
		
	}

	private void initView()
	{
		mSearchEdit = (EditText) findViewById(R.id.queryCityText);
		mList = (ListView) findViewById(R.id.cityList);
		mimm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mimm.hideSoftInputFromWindow(mSearchEdit.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		mSearchEdit.addTextChangedListener(textWatcher);
		mSearchEdit.clearFocus();
		mSearchEdit.setOnClickListener(this);
		mList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				mCity = setChecked(view, position);
				mSearchEdit.removeTextChangedListener(textWatcher);
				mSearchEdit.setText(mCity.name);
				mSearchEdit.selectAll();
				mSearchEdit.addTextChangedListener(textWatcher);
				
				Util.setIntToSharedPref(Util.MODE, 2);
				String lasCityCode = Util.getStringFromSharedPref(Util.CODE, "");
				if (mCity.code .equals(lasCityCode))
				{
					finish();
				}
				
				Util.setStringToSharedPref(Util.CODE, mCity.code);
				Util.setStringToSharedPref(Util.NAME, mCity.name);
				Util.setStringToSharedPref(Util.EN_NAME, mCity.enName);
				Util.setStringToSharedPref(Util.SCENE_INFO, "");
				Util.setStringToSharedPref(Util.SCENE_TEMPERATUR, "");
				Util.setLongToSharedPref(Util.LAST_UPDATETIME, 0);
				Util.setLongToSharedPref(Util.LAST_PICK_TIME, 0);
				Trace.i("fu",mCity.enName);
				Util.showToast("Choose City "+ mCity.enName, 1);
				Util.checkIfNeedToUpdateWeather();
				finish();
			}
		});
		mList.setScrollbarFadingEnabled(true);

	}
    private City setChecked(View view, int pos) {
        TextView nameView = (TextView) view.findViewById(R.id.radioText1);
        City tag = (City) nameView.getTag();
        this.mCity = tag;
        return tag;
    }

	private TextWatcher textWatcher = new TextWatcher()
	{
		@Override
		public void afterTextChanged(Editable arg0)
		{
			updateQueryCityList();
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3)
		{
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3)
		{
		}
	};
	
	private void initListView()
	{
		if (mAdapter == null)
		{
			mAdapter = new QueryCityItemAdapter(this, R.layout.text_list_item);
		}
		Collection<City> queryResult = queryHandler.queryHotcities();
		if (queryResult == null || queryResult.isEmpty())
		{
			// mNoCityText.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
		} else
		{
			// mNoCityText.setVisibility(View.GONE);
			mAdapter.resetItems(queryResult);
			mList.setAdapter(mAdapter);
			mList.setVisibility(View.VISIBLE);
		}
		
	}

	private void updateQueryCityList()
	{
		if (queryHandler == null)
		{
			return;
		}
		String query = mSearchEdit.getText().toString().toLowerCase();
		if (mAdapter == null)
		{
			mAdapter = new QueryCityItemAdapter(this, R.layout.text_list_item);
		}
		Collection<City> queryResult = null;
		if (query == null || query.length() == 0)
		{
			mList.setVisibility(View.GONE);
			return;
		} else
		{
			queryResult = queryHandler.queryCities(query,
					getLocaleLanguage());
		}
		mAdapter.resetItems(queryResult);
		mList.setAdapter(mAdapter);
		mList.setVisibility(View.VISIBLE);
		if (queryResult == null || queryResult.isEmpty())
		{
			// mNoCityText.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
		} else
		{
			// mNoCityText.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
		}
	}

	private String getLocaleLanguage()
	{
		Locale l = Locale.getDefault();
		return String.format("%s-%s", l.getLanguage(), l.getCountry());
	}

	private class QueryCityItemAdapter extends ArrayAdapter<City>
	{
		private final int resource;

		private final LayoutInflater mInflater;

		public QueryCityItemAdapter(Context context, int resource)
		{
			super(context, resource, new ArrayList<City>());
			this.resource = resource;
			this.mInflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		public void resetItems(Collection<City> cities)
		{
			this.clear();
			if (cities != null)
			{
				for (City city : cities)
				{
					this.add(city);
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			QueryCityListViewHolder holder = null;
			if (convertView == null)
			{
				convertView = mInflater.inflate(resource, parent, false);
				holder = new QueryCityListViewHolder();
				holder.nameView = (TextView) convertView
						.findViewById(R.id.radioText1);
				holder.parentView = (TextView) convertView
						.findViewById(R.id.radioText3);
				holder.splitView = (TextView) convertView
						.findViewById(R.id.radioText2);
				convertView.setTag(holder);
			} else
			{
				holder = (QueryCityListViewHolder) convertView.getTag();
			}

			City city = this.getItem(position);

			holder.nameView.setText(city.name);
			holder.nameView.setTag(city);
			holder.parentView.setText(city.parentName);
			holder.splitView.setText("-");
			return convertView;
		}
	}

	static class QueryCityListViewHolder
	{
		TextView nameView, parentView, splitView;
	}

	@Override
	public void onClick(View v)
	{
		if (v == mSearchEdit)
		{
			mimm.showSoftInput(mSearchEdit, InputMethodManager.SHOW_IMPLICIT);
		}

	}
	
	@Override
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.anim_defalut, R.anim.anim_right_exit);
	}
}
