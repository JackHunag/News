package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences存储工具类
 * 
 * @author hsssf 2016-11-10
 */
public class SpUtils {

	public static boolean getBoolean(Context cxt, String Key, boolean defValue) {
		SharedPreferences sp = cxt.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		return sp.getBoolean(Key, defValue);

	}

	public static void setBoolean(Context cxt, String Key, boolean defValue) {
		SharedPreferences sp = cxt.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(Key, defValue).commit();
	}

	public static String getString(Context cxt, String Key, String defValue) {
		SharedPreferences sp = cxt.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		return sp.getString(Key, defValue);

	}

	public static void setString(Context cxt, String Key, String defValue) {
		SharedPreferences sp = cxt.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sp.edit().putString(Key, defValue).commit();
	}

	public static int getInt(Context cxt, String Key, int defValue) {
		SharedPreferences sp = cxt.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		return sp.getInt(Key, defValue);

	}

	public static void setInt(Context cxt, String Key, int defValue) {
		SharedPreferences sp = cxt.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sp.edit().putInt(Key, defValue).commit();
	}

}
