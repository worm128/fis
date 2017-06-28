package com.fis.web.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateTime implements Cloneable{

	private Calendar date =null;

	public DateTime(){
		TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
		date = Calendar.getInstance(tz);
	}
	/**
	 * 
	 * @param datetime yyyy-MM-dd HH:mm:ss
	 */
	public DateTime(Date dt){
		this();
		date.setTime(dt);
	}
	public DateTime(String datetime){
		this();
		Date d=CTime.parseWholeDate(datetime);
		date.setTime(d);
	}
	public DateTime(int year, int month, int date, int hourOfDay, int minute, int second){
		this.date.set(year, month, date, hourOfDay, minute, second);
	}
	public int getDay() {
		return this.date.get(Calendar.DAY_OF_MONTH);
	}
	public void setDay(int day) {
		this.date.set(Calendar.DAY_OF_MONTH, day);
	}
	public int getYear() {
		return this.date.get(Calendar.YEAR);
	}
	public void setYear(int year) {
		this.date.set(Calendar.YEAR, year);
	}
	public int getMonth() {
		return this.date.get(Calendar.MONTH)+1;
	}
	public void setMonth(int month) {
		this.date.set(Calendar.MONTH, month-1);
	}
	public int getHours() {
		return this.date.get(Calendar.HOUR_OF_DAY);
	}
	public void setHours(int hour) {
		this.date.set(Calendar.HOUR_OF_DAY, hour);
	}
	public int getSeconds() {
		return this.date.get(Calendar.SECOND);
	}
	public void setSeconds(int seconds) {
		this.date.set(Calendar.SECOND, seconds);
	}
	public int getMinutes() {
		return this.date.get(Calendar.MINUTE);
	}
	public void setMinutes(int minutes) {
		this.date.set(Calendar.MINUTE, minutes);
	}
	
	public Date getDate(){
		return this.date.getTime();
	}
	
	public void addDays(int i){
		date.add(Calendar.DAY_OF_MONTH, i);
	}
	public void addMonths(int i){
		date.add(Calendar.MONTH, i);
	}
	
	public void addYears(int i){
		date.add(Calendar.YEAR, i);
	}
	public void addHours(int i){
		date.add(Calendar.HOUR_OF_DAY, i);
	}
	public void addMinutes(int i){
		date.add(Calendar.MINUTE, i);
	}
	public void addSeconds(int i){
		date.add(Calendar.SECOND, i);
	}
	public long getTimeInMillis(){
		return this.date.getTimeInMillis();
	}
	public String toString(){
		return CTime.formatWholeDate(date.getTime());
	}

	public static Date getNowDate(){
		return (new DateTime()).getDate();
	}

	/**
	 * 时间比较
	 * @param dt
	 * @return  0:等于当前时间  1.大于当前时间  2.小于当前时间
	 */
	public static int GetTimeDiff(DateTime dt)
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = CTime.parseDate(sf.format(DateTime.getNowDate()),"yyyy-MM-dd HH:mm:ss");
		Date now2=dt.getDate();
		if(now.after(now2))
		{
			return 2;
		}
		else if(now.before(now2))
		{
			return 1;
		}
		else
			return 0;
			
	}
	
	protected DateTime clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		DateTime dt =(DateTime)super.clone();
		dt.date=(Calendar)this.date.clone();
		return dt;
	}
	public static void main(String[] args) throws Exception{
		DateTime dt=new DateTime();
		DateTime dt2=new DateTime();
		dt2.addHours(-2);
		dt2.setMinutes(59);
		dt2.setSeconds(59);
		System.out.println(CTime.formatWholeDate(dt.getDate()));
		DateTime[] luckyNumbers = { dt, dt2};
		DateTime[]sss=luckyNumbers.clone();
		sss[0].addHours(2);
		
		DateTime dt3=dt.clone();
		dt3.addHours(5);
		System.out.println(CTime.formatWholeDate(dt.getDate()));
		
		//System.out.println(CTime.formatWholeDate(dt2.getDate()));
		//DateTime dtt=(DateTime)dt.clone();
		
	}

}