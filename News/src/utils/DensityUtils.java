package utils;

import android.content.Context;

/**
 * 
 * 利用设备密度对px与dip之间转变的工具类
 * 
 * @author hsssf 2016-11-24
 */

public class DensityUtils {
/**
 * dip转px
 * @param cxt 上下文对象
 * @param dip dp值
 * @return  px的结果值
 */
	public static int dip2Px(Context cxt, float dip) {

		// 获取设备midu
		float density = cxt.getResources().getDisplayMetrics().density;

		int px = (int) (dip * density + 0.5f);// 四舍五入
		return px;

	}
/**
 * px 转dp
 * @param cxt cxt 上下文对象
 * @param px px值
 * @return
 */
	public static float px2Dip(Context cxt, int px) {
		
		// 获取设备midu
		float density = cxt.getResources().getDisplayMetrics().density;

		float dip = px / density;
		return dip;

	}

}
