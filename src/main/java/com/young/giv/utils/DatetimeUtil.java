package com.young.giv.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatetimeUtil {
	
	public static long formatDate(long time){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(time);
//		cal1.set(Calendar.HOUR_OF_DAY, 0);
//		cal1.set(Calendar.MINUTE, 0);
//		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		
		return cal1.getTimeInMillis();
	}
	
	public static long getCurrentDate(){
		Date date = new Date();
		return date.getTime();
	}
	
	public static long getBefore1M(){
//		Date date = new Date();
//		return date.getTime()-(60 *1000);
		return getCurrentDayPrecision();
	}
	
	public static long getBefore10M(){
//		Date date = new Date();
//		return date.getTime()-(10 * 60 *1000);
		return getCurrentDayPrecision();
	}
	
	public static long getBefore5M(){
//		Date date = new Date();
//		return date.getTime()-(5 * 60 *1000);
		return getCurrentDayPrecision();
	}
	
	
    public static long getCurrentDayPrecision() {
        Calendar calendar = GregorianCalendar.getInstance();
//        calendar.set(Calendar.DATE, -1); //һ
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long formatLongTimeToDayPrecision(long time)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        return getDayPrecisionLongTimeByCalendar(calendar);
    }

    public static long getDayPrecisionLongTimeByCalendar(Calendar calendar) throws IllegalArgumentException {
        if(null==calendar)
            throw new IllegalArgumentException("ܸnull");
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    
    public static void main(String[] args){
    	Date date = new Date();
    	long l = formatDate(date.getTime());
    	System.out.println(new Date(l));
    	System.out.println(l);
    	System.out.println(getCurrentDayPrecision());
    	
    }

    

}
