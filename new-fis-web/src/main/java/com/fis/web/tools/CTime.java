package com.fis.web.tools;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CTime {
	private static final ThreadLocal<SimpleDateFormat> df = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
			sd.setLenient(false);
			return sd;
		}

	};
	private static final ThreadLocal<SimpleDateFormat> df2 = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			sd.setLenient(false);
			return sd;

		}

	};
	private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
			sd.setLenient(false);
			return sd;

		}

	};;
	private static final ThreadLocal<SimpleDateFormat> sdfk = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");;
			sd.setLenient(false);
			return sd;

		}

	};
	private static final ThreadLocal<SimpleDateFormat> dfk = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sd.setLenient(false);
			return sd;

		}

	};
	private static final ThreadLocal<SimpleDateFormat> ff = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {

			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-01");
			sd.setLenient(false);
			return sd;

		}

	};
	private static final ThreadLocal<SimpleDateFormat> time = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {

			SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
			sd.setLenient(false);
			return sd;
		}

	};

	private static final ThreadLocal<SimpleDateFormat> yyyy = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {

			SimpleDateFormat sd = new SimpleDateFormat("yyyy");
			sd.setLenient(false);
			return sd;
		}

	};
	private static final ThreadLocal<SimpleDateFormat> mm = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {

			SimpleDateFormat sd = new SimpleDateFormat("MM");;
			sd.setLenient(false);
			return sd;

		}

	};
	private static final ThreadLocal<SimpleDateFormat> dd = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {

			SimpleDateFormat sd = new SimpleDateFormat("dd");
			sd.setLenient(false);
			return sd;

		}

	};

	public static long MILSECOND_DAY = 1000 * 60 * 60 * 24;

	public static String getCurrentDate() {
		String s = "";
		Calendar calend;
		TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
		// TimeZone tzm = TimeZone.getDefault();
		// System.out.println(tz);
		// System.out.println(tzm);
		calend = Calendar.getInstance(tz);
		int year = calend.get(Calendar.YEAR);
		int M = calend.get(Calendar.MONTH) + 1;
		int d = calend.get(Calendar.DATE);
		int h = calend.get(Calendar.HOUR_OF_DAY);
		int m = calend.get(Calendar.MINUTE);
		int ss = calend.get(Calendar.SECOND);

		String yearStr = "" + year;
		String MMStr = "" + M;
		String ddStr = "" + d;
		String hhStr = "" + h;
		String mmStr = "" + m;
		String ssStr = "" + ss;

		if (M < 10)
			MMStr = "0" + MMStr;
		if (d < 10)
			ddStr = "0" + ddStr;
		if (h < 10)
			hhStr = "0" + hhStr;
		if (m < 10)
			mmStr = "0" + mmStr;
		if (ss < 10)
			ssStr = "0" + ssStr;
		StringBuilder sb = new StringBuilder();
		sb.append(yearStr);
		sb.append("-");
		sb.append(MMStr);
		sb.append("-");
		sb.append(ddStr);
		sb.append(" ");
		sb.append(hhStr);
		sb.append(":");
		sb.append(mmStr);
		sb.append(":");
		sb.append(ssStr);

		return sb.toString();
		// return CTime.formatWholeDate(calend.getTime());
	}

	public static String formatTimeDate(Date dt) {
		// SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		if (dt == null) {
			return "00:00:00.0";
		}
		return time.get().format(dt);
	}

	public static Date getCurrentDateTime() {
		String s = "";
		Calendar calend;
		TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
		// TimeZone tzm = TimeZone.getDefault();
		// System.out.println(tz);
		// System.out.println(tzm);
		calend = Calendar.getInstance(tz);
		return calend.getTime();
	}

	public static Date getDate() {
		return CTime.getCurrentDateTime();
	}

	public static String formatDate(Date dt) {
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		if (dt == null) {
			return "00000000000000";
		}

		return df.get().format(dt);

	}

	public static String formatDate() {
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.get().format(getCurrentDateTime());
	}

	public static String formatWholeDate(Date dt) {
		// SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dt == null) {
			return "0000-00-00 00:00:00.0";
		}
		return dfk.get().format(dt);
	}

	public static String formatLogDate(Date dt) {
		// SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		if (dt == null) {
			return "0000-00-00 00:00:00.0";
		}
		return df2.get().format(dt);
	}

	public static String formatLogDate() {
		// SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

		return df2.get().format(getCurrentDateTime());
	}

	public static String formatWholeDate() {
		// SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dfk.get().format(getCurrentDateTime());
	}

	/**
	 * 把2008-11-01 13:34:56 这个格式的日期转换成20081101133456格式
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatDate(String dt) {
		StringBuffer strbf = new StringBuffer();
		StringTokenizer st = new StringTokenizer(dt.substring(0, 10), "-");
		while (st.hasMoreTokens()) {
			strbf.append(st.nextToken());
		}
		st = new StringTokenizer(dt.substring(11, 19), ":");
		while (st.hasMoreTokens()) {
			strbf.append(st.nextToken());
		}
		return strbf.toString();
	}

	public static String formatShortDate(Date dt) {

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (dt == null) {
			return "00000000";
		}
		return sdf.get().format(dt);
	}

	public static String formatShortDate() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.get().format(getCurrentDateTime());
	}

	public static String formatRealDate(Date dt) {
		// SimpleDateFormat sdfk = new SimpleDateFormat("yyyy-MM-dd");
		if (dt == null) {
			return "0000-00-00";
		}
		return sdfk.get().format(dt);
	}

	public static String formatYearDay(Date dt) {
		// SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-01");
		if (dt == null) {
			return "0000-00-00";
		}
		return ff.get().format(dt);
	}

	public static String getYYYY(Date dt) {
		// SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		if (dt == null)
			return "0000";
		return yyyy.get().format(dt);
	}

	public static String getYYYY(String wholeDate) {
		Assert.notNull(wholeDate, "time string is null");
		wholeDate = wholeDate.trim();
		// yyyy-MM-dd HH:mm:ss.sss
		return wholeDate.substring(0, 4);

	}

	public static String getMM(String wholeDate) {
		Assert.notNull(wholeDate, "time string is null");
		wholeDate = wholeDate.trim();
		// yyyy-MM-dd HH:mm:ss.sss
		return wholeDate.substring(5, 7);
	}

	public static String getDD(String wholeDate) {
		Assert.notNull(wholeDate, "time string is null");
		wholeDate = wholeDate.trim();
		// yyyy-MM-dd HH:mm:ss.sss
		return wholeDate.substring(8, 10);
	}

	public static String getYYYY() {
		return getYYYY(getCurrentDateTime());
	}

	public static String getMM(Date dt) {
		// SimpleDateFormat mm = new SimpleDateFormat("MM");
		if (dt == null)
			return "00";
		return mm.get().format(dt);
	}

	public static String getMM() {
		return getMM(getCurrentDateTime());
	}

	public static String getDD(Date dt) {

		// SimpleDateFormat dd = new SimpleDateFormat("dd");
		if (dt == null)
			return "00";
		return dd.get().format(dt);
	}

	public static String getDD() {
		return getDD(getCurrentDateTime());
	}

	public static String formatShortDate(String dt) {
		StringBuffer strbf = new StringBuffer();
		StringTokenizer st = new StringTokenizer(dt.substring(0, 10), "-");
		while (st.hasMoreTokens()) {
			strbf.append(st.nextToken());
		}

		return strbf.toString();
	}

	private static Date parseDate(String dateString) {
		// SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (dateString == null)
				return null;
			SimpleDateFormat sf = dfk.get();
			sf.setLenient(false);// //严格匹配
			return sf.parse(dateString);
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}

	/**
	 * 解析 yyyy-mm-dd hh:mi:ss格式的日期 具有简单的容错
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date parseWholeDate(String dateString) {

		if (dateString == null)
			return null;

		dateString = dateString.trim();
		if (dateString.length() < 11)
			return null;
		else if (dateString.length() > 11) {
			String[] strs = dateString.split(" +");
			dateString = strs[0] + " " + strs[1];
		}
		return CTime.parseDate(dateString);
	}

	/**
	 * 解析yyyy-MM-dd模式的字符串转换成日期
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date parseDayDate(String dateString) {
		// SimpleDateFormat sdfk = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (dateString == null)
				return null;

			sdfk.get().setLenient(false);// //严格匹配
			return sdfk.get().parse(dateString);
		} catch (Exception e) {
			log.error("",e);
		}
		return null;
	}

	/**
	 * 得到距离今天晚上23:59:59之前的秒数, 得到距离今天晚上23:59:59之前的秒数
	 * 
	 * @return
	 */

	public static long getExpireTime() {
		// java.util.Date todayDate = new java.util.Date();
		// java.util.Date todayDate=CTime.getDate();

		DateTime dt = new DateTime();
		DateTime dt2 = new DateTime();
		dt2.setHours(23);
		dt2.setMinutes(59);
		dt2.setSeconds(59);
		long result = (dt2.getTimeInMillis() - dt.getTimeInMillis()) / 1000;
		if (result < 0)
			result = 0;
		return result;

		// return 1000 * seconds;
	}

	/**
	 * 
	 * @param s1
	 *            被减日期字符串格式 yyyy-MM-dd
	 * @param s2
	 *            减日期字符串格式 yyyy-MM-dd
	 * @return 两个日期相差的天数
	 */
	public static long datediff(String s1, String s2) {
		Date d1 = parseDayDate(s1);
		Date d2 = parseDayDate(s2);
		return datediff(d1, d2);
	}

	public static Date parseYMDHMSDate(String dateString) {
		if (dateString == null)
			return null;

		dateString = dateString.trim();

		try {
			return df.get().parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("",e);
			return null;
		}
	}

	/**
	 * 根据给定的format格式返回日期
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate(String date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date s = null;
		try {
			sf.setLenient(false);// 严格验证
			s = sf.parse(date);
		} catch (ParseException e) {
			log.error("",e);
			s = null;
		}
		return s;
	}

	/**
	 * 
	 * @param date
	 * @param format
	 *            年yyyy 月MM 日dd 时HH 分mm 秒ss
	 * @return
	 */
	public static String formatDate(Date date, String format) {

		SimpleDateFormat sf = new SimpleDateFormat(format);
		sf.setLenient(false);// 严格匹配
		return sf.format(date);
	}

	/**
	 * 
	 * @param s1
	 *            被减日期字符串格式 yyyy-MM-dd
	 * @return 当前日期-S1 相差的天数
	 */
	public static long datediff(String s1) {
		Date d1 = parseDayDate(s1);
		Date d2 = getDate();
		return datediff(d2, d1);
	}

	/**
	 * 
	 * @param d1 被减日期
	 * @param d2 减日期
	 * @return 两个日期相差的天数
	 */
	public static long datediff(Date d1, Date d2) {
		TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(d2);
		long timethis = calendar.getTimeInMillis();
		calendar.setTime(d1);
		long timeend = calendar.getTimeInMillis();
		return (timeend - timethis);

	}

	/**
	 * 得到date后days天的日期对象
	 * 
	 * @param date
	 * @param days
	 *            可以为负数
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, days);
			return calendar.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
		}
		return null;
	}

	public static Date addHours(Date date, int hours) {
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, hours);
			return calendar.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
		}
		return null;
	}

	public static Date addMinutes(Date date, int minutes) {
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, minutes);
			return calendar.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
		}
		return null;
	}
	public static Date addSenconds(Date date, int seconds) {
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(date);
			calendar.add(Calendar.SECOND, seconds);
			return calendar.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
		}
		return null;
	}
	/**
	 * 对月份进行加减
	 * 
	 * @param date
	 * @param num
	 *            可以为负数
	 * @return
	 */
	public static Date addMonths(Date date, int num) {
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, num);
			return calendar.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
		}
		return null;
	}
	/**
	 * 得到date后days天的日期对象
	 *
	 * @param days
	 *            可以为负数
	 * @return
	 */
	public static Date addDays(int days) {
		return addDays(CTime.getCurrentDateTime(), days);
	}

	public static Date addHours(int hours) {
		return addHours(CTime.getCurrentDateTime(), hours);
	}

	public static Date addMinutes(int minutes) {
		return addMinutes(CTime.getCurrentDateTime(), minutes);
	}
	public static Date addSenconds(int seconds) {
		return addMinutes(CTime.getCurrentDateTime(), seconds);
	}
	/**
	 * 返回日期属于该年的第几周。返回0表示错误
	 * 
	 * @param date
	 * @return
	 */
	public static int weekOfYear(Date date) {
		try {
			TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(date);
			return calendar.get(Calendar.WEEK_OF_YEAR);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("", e);
		}
		return 0;// 错误时返回0
	}
	/**
	 * 转换时间格式为人易懂的描述串,如1小时前。。。
	 * 
	 * @return
	 */
	public static String toHumanString(Date d) {
		TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
		Calendar calendar = Calendar.getInstance(tz);
		long timethis = calendar.getTimeInMillis();// 当前时间
		calendar.setTime(d);
		long timeend = calendar.getTimeInMillis();// d
		long tt = timethis - timeend;// 时间差 单位ms
		int seconds = Math.abs((int) (tt / 1000));
		String s = null;
		if (seconds > 60 && seconds < 3600)// 超过60秒，使用分
		{
			s = seconds / 60 + "分钟";
		} else if (seconds >= 3600 && seconds < 86400)// 超过60分，使用小时
		{
			s = seconds / 3600 + "小时";
		} else if (seconds >= 86400 && seconds < 2592000)// 超过24小时，使用天
		{
			s = seconds / 86400 + "天";
		} else if (seconds >= 2592000 && seconds < 31104000)// 超过30天，使用月
		{
			s = seconds / 2592000 + "个月";
		} else if (seconds > 31104000) {// 超过12个月使用年
			s = seconds / 31104000 + "年";
		} else {
			s = seconds + "秒钟";
		}

		if (tt > 0) {
			s += "前";
		} else {
			s += "后";
		}
		return s;
	}
	
	public static String GetDate() {
		Date now = new Date();
		String format = "yyyy-MM-dd";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String year = sf.format(now);
		return year;
	}
	public static void main(String[] args) throws Exception {

		// System.out.println(getCurrentDate());
		// System.out.println(datediff("2009-02-01"));

		// String spider_time = CTime.formatWholeDate();// 抓取时间
		// String pubtime = spider_time;
		// String yy = CTime.getYYYY(spider_time);
		// String mm = CTime.getMM(spider_time);
		// String dd = CTime.getDD(spider_time);
		// System.out.println(spider_time+"--"+yy+"-"+mm+"-"+dd);
		// System.out.println(CTime.formatWholeDate(CTime.parseWholeDate("2009-12-13     12:34:35")));
		//
		// //System.out.println(CTime.formatWholeDate(CTime.addHours(48)));
		// String s="2009年12月12日";
		// Date dd= CTime.parseDate(s, "yyyy年MM月dd日");
		// System.out.println(CTime.formatWholeDate(dd));
		// int t=weekOfYear(CTime.parseDate("2010-01-04","yyyy-MM-dd"));
		// System.out.println(t);
		String d = "Sun,10 Jun 2012 20:00:00 +0200";// Sun,10 Jun 2012 20:00:00
													// +0200
		String format = "EEE,d MMM yyyy HH:mm:ss Z";
		SimpleDateFormat sf = new SimpleDateFormat(format, Locale.US);

		// Calendar calend;
		// TimeZone tz = TimeZone.getTimeZone("GMT+000");
		// calend = Calendar.getInstance(tz);
		// TimeZone.setDefault(tz);

		System.out.println(CTime.formatDate(sf.parse(d), format));
		// System.out.println(CTime.parseDate(d, format));
		// String[] tt=TimeZone.getAvailableIDs();
		// for (int i = 0; i < tt.length; i++) {
		// System.out.println(tt[i]);
		// }
	}

}

