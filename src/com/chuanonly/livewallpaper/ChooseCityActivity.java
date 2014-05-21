package com.chuanonly.livewallpaper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.chuanonly.livewallpaper.model.WOEIDInfo;
import com.chuanonly.livewallpaper.model.WOEIDUtils;
import com.chuanonly.livewallpaper.model.WeatherInfo;
import com.chuanonly.livewallpaper.util.QueryCityHandler;
import com.chuanonly.livewallpaper.util.Util;

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
    private TextView mSeachBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_city);
		mEmptyLayout =  findViewById(R.id.empty_city);
		mEmptyText = (TextView) findViewById(R.id.empty_text);
		mSeachBtn = (TextView) findViewById(R.id.search_btn); 
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
		try
		{
			mimm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
		
		mSeachBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String cityStr = mSearchEdit.getText().toString();
				new WeatherQueryByPlaceTask().execute(new String[]{cityStr});
			}
		});
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
				weatherInfo.WoeidNumber = mWoeidNumber;
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
				Log.e("fu", result.WoeidNumber+"------ "+result.mWOEIDCountry+" "+result.getCurrentTempC()+"  "+result.getLocationCity()+"  code="+result.getCurrentCode()+" "+result.getCurrentText());
			}else {
				Log.e("fu","result==null");
			}
		}
	}
}
