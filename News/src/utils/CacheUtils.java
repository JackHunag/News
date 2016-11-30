package utils;

import android.content.Context;

/**
 * 网络缓存工具类，以url为key,json为value,通过Sp来存储
 * 
 * @author hsssf 2016-11-10
 */
public class CacheUtils {

	/**
	 * 设置缓存方法
	 * 
	 * @param json
	 * @param url
	 * @param cxt
	 */
	public static void setCache(String json, String url, Context cxt) {
		SpUtils.setString(cxt, url, json);

	}

	/**
	 * 获取存储在Sp中的缓存
	 * 
	 * @param url
	 * @param cxt
	 * @return 返回json数据
	 */
	public static String getCache(String url, Context cxt) {
		return SpUtils.getString(cxt, url, null);

	}
}
