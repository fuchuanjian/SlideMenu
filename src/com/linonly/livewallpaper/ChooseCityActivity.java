package com.linonly.livewallpaper;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Document;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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

import com.linonly.livewallpaper.data.City;
import com.linonly.livewallpaper.model.WOEIDUtils;
import com.linonly.livewallpaper.model.WeatherInfo;
import com.linonly.livewallpaper.util.QueryCityHandler;
import com.linonly.livewallpaper.util.Util;

public class ChooseCityActivity extends Activity implements OnClickListener
{
	
	private EditText mSearchEdit;
	private ListView mList;
	private QueryCityItemAdapter mAdapter;
	private InputMethodManager mimm = null;
	private QueryCityHandler queryHandler = null;
    private City mCity;
    private View mEmptyLayout;
    private TextView mEmptyText;
    private View mSearchBg;
    private TextView mOK;
    private TextView mCancel;
    private TextView mSearchResult;
    private WeatherInfo mWeatherInfoTmp;
    private View mBtnLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_city);
		mimm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mEmptyLayout =  findViewById(R.id.empty_city);
		mBtnLayout = findViewById(R.id.btn_layout);
		mEmptyText = (TextView) findViewById(R.id.empty_text);
		mSearchBg = findViewById(R.id.search_layout);
		mSearchResult = (TextView) findViewById(R.id.search_result);
		mOK = (TextView) findViewById(R.id.ok);
		mCancel = (TextView) findViewById(R.id.no);
		mOK.setOnClickListener(mClick);
		mCancel.setOnClickListener(mClick);
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

private OnClickListener mClick = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		if (v.getId()== R.id.ok)
		{
			if (mWeatherInfoTmp == null) return;
			Util.setIntToSharedPref(Util.MODE, 2);
			Util.setIntToSharedPref(Util.ISYAHOO, 1);
			Util.setStringToSharedPref(Util.CODE, mWeatherInfoTmp.WoeidNumber);
			Util.setStringToSharedPref(Util.NAME, mWeatherInfoTmp.getLocationCity());
			Util.setStringToSharedPref(Util.EN_NAME, mWeatherInfoTmp.getLocationCity());
			Util.setStringToSharedPref(Util.SCENE_INFO, mWeatherInfoTmp.getCurrentText());
			Util.setIntToSharedPref(Util.REAL_TYPE, Util.changeYahooType(mWeatherInfoTmp.getCurrentCode()));
			Util.setStringToSharedPref(Util.YAHOO_DESC, mWeatherInfoTmp.getCurrentText());
			Util.setStringToSharedPref(Util.SCENE_TEMPERATUR, String.valueOf(mWeatherInfoTmp.getCurrentTempC()));
			Util.setLongToSharedPref(Util.LAST_UPDATETIME, 0);
			Util.setLongToSharedPref(Util.LAST_PICK_TIME, 0);
			Util.checkIfNeedToUpdateWeather();
			finish();
		}else if (v.getId() == R.id.no)
		{
			mSearchBg.setVisibility(View.INVISIBLE);
		}else if (v.getId() == R.id.empty_text)
		{
			if (!Util.isNetworkAvailable(getApplicationContext()))
			{
				Util.showToast("Can't Search from Internet");
				return;
			}
			mWeatherInfoTmp = null;
			String cityStr = mSearchEdit.getText().toString();
			if (TextUtils.isEmpty(cityStr)) return;
			try
			{
				 mimm.hideSoftInputFromWindow(v.getWindowToken(), 0);		
			} catch (Exception e)
			{
				// TODO: handle exception
			}
			mSearchBg.setVisibility(View.VISIBLE);
			mSearchResult.setText("Search...");
			mBtnLayout.setVisibility(View.INVISIBLE);
			new WeatherQueryByPlaceTask().execute(new String[]{cityStr});
		}
		
	}
};
	private void initView()
	{
		mSearchEdit = (EditText) findViewById(R.id.queryCityText);
		mList = (ListView) findViewById(R.id.cityList);
		try
		{
			mimm.hideSoftInputFromWindow(mSearchEdit.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);			
		} catch (Exception e)
		{
			// TODO: handle exception
		}

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
				Util.setIntToSharedPref(Util.ISYAHOO, 0);
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
				Util.checkIfNeedToUpdateWeather();
				finish();
			}
		});
		mList.setScrollbarFadingEnabled(true);
		
		mEmptyText.setOnClickListener(mClick);
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
			mEmptyLayout.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
		} else
		{
			mEmptyLayout.setVisibility(View.GONE);
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
			mEmptyLayout.setVisibility(View.VISIBLE);
			return;
		} else
		{
			queryResult = queryHandler.queryCities(query);
		}
		mAdapter.resetItems(queryResult);
		mList.setAdapter(mAdapter);
		mList.setVisibility(View.VISIBLE);
		mEmptyLayout.setVisibility(View.GONE);
		if (queryResult == null || queryResult.isEmpty())
		{
			mList.setVisibility(View.GONE);
			mEmptyLayout.setVisibility(View.VISIBLE);
		} else
		{
			mEmptyLayout.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
		}
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

			if (MyApplication.language < 2)
			{
				holder.nameView.setText(city.name);
				holder.parentView.setText(city.parentName);				
			}else {
				holder.nameView.setText(city.enName);
				holder.parentView.setText(city.enProvice);	
			}
			holder.nameView.setTag(city);
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
		try
		{			
			if (v == mSearchEdit)
			{
				mimm.showSoftInput(mSearchEdit, InputMethodManager.SHOW_IMPLICIT);
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}

	}
	
	@Override
	public void finish()
	{
		super.finish();
		overridePendingTransition(R.anim.anim_defalut, R.anim.anim_right_exit);
	}
	
	
	private class WeatherQueryByPlaceTask extends AsyncTask<String, Void, WeatherInfo> {
		@Override
		protected WeatherInfo doInBackground(String... cityName) {
			WOEIDUtils woeidUtils = WOEIDUtils.getInstance();
			String mWoeidNumber = woeidUtils.getWOEID(MyApplication.getContext(), cityName[0]);
			if(!mWoeidNumber.equals(WOEIDUtils.WOEID_NOT_FOUND)) {
				String weatherString = Util.getWeatherString(MyApplication.getContext(), mWoeidNumber);
				Document weatherDoc = Util.convertStringToDocument(MyApplication.getContext(), weatherString);
				WeatherInfo weatherInfo = Util.parseWeatherInfo(MyApplication.getContext(), weatherDoc, woeidUtils.getWoeidInfo());
				if (weatherInfo != null)
				{					
					weatherInfo.WoeidNumber = mWoeidNumber;
				}
				return weatherInfo;
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
			if (result!= null)
			{			
				mBtnLayout.setVisibility(View.VISIBLE);
				mWeatherInfoTmp = result;
				String desc = result.mWOEIDCountry+" "+ result.getLocationCity()+"\n"
						+ result.getCurrentText()+"  "+ result.getCurrentTempC()+" "+MyApplication.getContext().getString(R.string.temp_unit);
				mSearchResult.setText(desc);
			}else {
				mSearchResult.setText(MyApplication.getContext().getString(R.string.not_find_city));
			}
		}
	}
	@Override
	public void onBackPressed() {
		if (mSearchBg.getVisibility() == View.VISIBLE)
		{
			mSearchBg.setVisibility(View.INVISIBLE);
		}else {			
			super.onBackPressed();
		}
	}
}
