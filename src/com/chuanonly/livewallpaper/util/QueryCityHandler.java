package com.chuanonly.livewallpaper.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.chuanonly.livewallpaper.MyApplication;
import com.chuanonly.livewallpaper.R;
import com.chuanonly.livewallpaper.R.array;
import com.chuanonly.livewallpaper.data.City;
import com.chuanonly.livewallpaper.data.Constant;

public class QueryCityHandler
{

	public static final String CHINA = "zh-CN";

	private Context mContext;

	private String mDBPackageName;

	public QueryCityHandler(Context context)
	{
		this.mContext = context;
		SQLiteDatabase db = null;
		try
		{
			db = fetchDbInstance();
		} catch (Exception e)
		{
		} finally
		{
			if (db != null)
			{
				db.close();
			}
		}
	}

	public ArrayList<City> queryCities(String key, String locale)
	{
		SQLiteDatabase db = null;
		Cursor c = null;
		ArrayList<City> retCities = new ArrayList<City>();

		try
		{
			db = fetchDbInstance();

			String sql = "select name, province, code, fullpinyin from city";
			if (!TextUtils.isEmpty(key))
			{
				sql += " where name like '%" + key + "%' or province like '%"
						+ key + "%'";
				key = "%," + key + "%";
				sql += " or fullpinyin like '" + key
						+ "' or shortpinyin like '" + key + "'";
			}

			c = db.rawQuery(sql, null);
			if (c.getCount() > 0 && c.moveToFirst())
			{
				final int nameIndex = c.getColumnIndexOrThrow("name");
				final int provinceIndex = c.getColumnIndexOrThrow("province");
				final int codeIndex = c.getColumnIndexOrThrow("code");
				final int pinyinIndex = c.getColumnIndexOrThrow("fullpinyin");
				do
				{
					String name = c.getString(nameIndex);
					String code = c.getString(codeIndex);
					String province = c.getString(provinceIndex);
					String pinyin = c.getString(pinyinIndex);
					String enName = "";
					String enProvice = "";
					int pos1 = pinyin.lastIndexOf(",") + 1;
					int pos2 = pinyin.lastIndexOf(".") + 1;
					if (pos2 > pos1)
					{
						enName = pinyin.substring(pos1, pos2 - 1);
						enProvice = pinyin.substring(pos2, pinyin.length());
					} else
					{
						enName = pinyin.substring(pos1, pinyin.length());
						enProvice = "China";
					}

					retCities.add(new City(name, province, code, enName,
							enProvice));
				} while (c.moveToNext());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (c != null)
			{
				c.close();
			}
			if (db != null)
			{
				db.close();
			}
		}
		if (MyApplication.language == 2 && retCities.size() > 0) 
		{
			retCities = sortCities(retCities);
		}
		
		return retCities;

	}
	 public ArrayList<City> queryHotcities() {
	        SQLiteDatabase db = null;
	        Cursor c = null;
	        ArrayList<City> retCities = new ArrayList<City>();

	        try {
	            db = fetchDbInstance();

	            String sql = "select * from hotcity";
	            c = db.rawQuery(sql, null);
	            if (c.getCount() > 0 && c.moveToFirst()) {
					final int nameIndex = c.getColumnIndexOrThrow("name");
					final int provinceIndex = c.getColumnIndexOrThrow("province");
					final int codeIndex = c.getColumnIndexOrThrow("code");
					final int pinyinIndex = c.getColumnIndexOrThrow("fullpinyin");
	                do {
						String name = c.getString(nameIndex);
						String code = c.getString(codeIndex);
						String province = c.getString(provinceIndex);
						String pinyin = c.getString(pinyinIndex);
						String enName = "";
						String enProvice = "";
						int pos1 = pinyin.lastIndexOf(",") + 1;
						int pos2 = pinyin.lastIndexOf(".") + 1;
						if (pos2 > pos1)
						{
							enName = pinyin.substring(pos1, pos2 - 1);
							enProvice = pinyin.substring(pos2, pinyin.length());
						} else
						{
							enName = pinyin.substring(pos1, pinyin.length());
							enProvice = "China";
						}

						retCities.add(new City(name, province, code, enName,
								enProvice));
	                } while (c.moveToNext());
	            }
	        } catch (Exception e) {
	        } finally {
	            if (c != null) {
	                c.close();
	            }
	            if (db != null) {
	                db.close();
	            }
	        }
			if (MyApplication.language == 2 && retCities.size() > 0) 
			{
				retCities = sortCities(retCities);
			}
	        return retCities;
	    }
	public List<City> getAllCityNames(String locale)
	{
		return queryCities(null, locale);
	}

	private SQLiteDatabase fetchDbInstance() throws IOException
	{
		String packageName = mDBPackageName;
		if (packageName == null)
		{
			packageName = mContext.getPackageName();
		}
		String databasePath = Environment.getDataDirectory().getPath()
				+ "/data/" + packageName + "/databases/cities.db";
		SQLiteDatabase db = null;

		File databaseFile = new File(databasePath);
		if (!databaseFile.getParentFile().exists())
		{
			databaseFile.getParentFile().mkdirs();
		}
		if (!databaseFile.exists())
		{
			copyInputStreamToFile(
					mContext.getResources().openRawResource(R.raw.cities),
					databaseFile);
			db = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
			db.setVersion(Constant.DATABASE_VERSION);
		} else
		{
			db = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
			if (db.getVersion() < Constant.DATABASE_VERSION)
			{
				db.close();
				databaseFile.delete();
				copyInputStreamToFile(
						mContext.getResources().openRawResource(R.raw.cities),
						databaseFile);

				db = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
				db.setVersion(Constant.DATABASE_VERSION);
			}
		}
		return db;
	}

	public static void copyInputStreamToFile(InputStream source,
			File destination) throws IOException
	{
		try
		{
			FileOutputStream output = openOutputStream(destination);
			try
			{
				copy(source, output);
			} catch (Exception e)
			{
				if (destination.exists())
				{
					destination.delete();
				}
			} finally
			{
				closeQuietly(output);
			}
		} catch (Exception e)
		{
			if (destination.exists())
			{
				destination.delete();
			}
		} finally
		{
			closeQuietly(source);
		}
	}

	public static FileOutputStream openOutputStream(File file)
			throws IOException
	{
		if (file.exists())
		{
			if (file.isDirectory())
			{
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (!file.canWrite())
			{
				throw new IOException("File '" + file
						+ "' cannot be written to");
			}
		} else
		{
			File parent = file.getParentFile();
			if (parent != null && !parent.exists())
			{
				if (!parent.mkdirs())
				{
					throw new IOException("File '" + file
							+ "' could not be created");
				}
			}
		}
		return new FileOutputStream(file);
	}

	public static void closeQuietly(Closeable closeable)
	{
		try
		{
			if (closeable != null)
			{
				closeable.close();
			}
		} catch (IOException ioe)
		{
			// ignore
		}
	}

	public static int copy(InputStream input, OutputStream output)
			throws IOException
	{
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE)
		{
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException
	{
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer)))
		{
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	private ArrayList<City> sortCities(ArrayList<City> list)
	{
		ArrayList<City> sortList = new ArrayList<City>();
		Iterator<City> iterator = list.iterator();
		while (iterator.hasNext())
		{
			City city = iterator.next();
			if (!city.enProvice.equals("China"))
			{
				sortList.add(city);
				iterator.remove();
			}
		}
		sortList.addAll(list);
		return sortList;
		
	}
}
