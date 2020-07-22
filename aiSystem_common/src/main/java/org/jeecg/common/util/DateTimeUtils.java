package org.jeecg.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtils {

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long week = 7 * day;// 1周
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

	public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmssSSS";

   /**
    * 获取未来 第 past 天的日期
    * @param past
    * @return
    */
	public static String getFetureDate(Date date,int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(today);
	}

   /**
    * 获取过去 第 past 天的日期
    * @param past
    * @return
    */
	public static Date getPassDate(Date date,int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		return calendar.getTime();
	}

    /**
     * @return
     * @author
     * @date 2015-5-21
     */
    public static String getDateSequence() {
        return new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date());
    }


	/**
	 * @author
	 * @date 2016年8月10日
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}
	//计算时间差，以天数为单位。如：2018-08-08 和 2018-08-05 相差3天
	public static Map<String,List<String>> getDistanceTime(String startTime, String endTime) {
		Date startTimeDate = strToDate(startTime);
		Date endTimeDate = strToDate(endTime);
		Map<String,List<String>> daysMap = new HashMap<>();

		int days = 0;
		int weeks = 0;
		int months = 0;
		int years = 0;
		long time1 = startTimeDate.getTime();
		long time2 = endTimeDate.getTime();
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		days = (int) (diff / day);
		if(days<14){
			daysMap.put("day",getXList(startTime,endTime,"day",days));
		}else {
			weeks = (int) (diff / week);
			if(weeks <8){
				daysMap.put("week",getXList(startTime,endTime,"week",weeks));
			}else {
				months = (int) (diff / month);
				if(months < 24){
					daysMap.put("month",getXList(startTime,endTime,"month",months));
				}else {
					years = (int) (diff / year);
					daysMap.put("year",getXList(startTime,endTime,"year",years));
				}
			}
		}
		return daysMap;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	public static List<String> getXList(String startTime,String endTime,String yayOrWeek,int length){
		List<String> result = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(strToDate(startTime));
		result.add( new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		for (int i = 0; i < length; i++) {
			if(yayOrWeek.equals("day")){
				calendar.add(Calendar.DAY_OF_MONTH,1);
				result.add( new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
			if(yayOrWeek.equals("week")){
				calendar.add(Calendar.DAY_OF_MONTH,7);
				result.add( new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
			if(yayOrWeek.equals("month")){
				calendar.add(Calendar.MONTH,1);
				result.add( new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
			if(yayOrWeek.equals("year")){
				calendar.add(Calendar.YEAR,1);
				result.add( new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
			}
		}
		if(result.size()>0){
			String finalTime= result.get(result.size() - 1);
			Date finalDate = strToDate(finalTime);
			Date endTimeDate = strToDate(endTime);
			long time1 = finalDate.getTime();
			long time2 = endTimeDate.getTime();
			if (time1 -time2 > 0 ) {
				result.set(result.size() - 1,new SimpleDateFormat("yyyy-MM-dd").format(endTimeDate));
			}else if (time2 - time1 >0) {
				result.add(result.size(), new SimpleDateFormat("yyyy-MM-dd").format(endTimeDate));
			}
		}
		return  result;
	}

    public static String getTimeFormatText(Long date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date;
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将时间戳转换成当天0点
     * @param timestamp
     * @return
     */
    public static long getDayBegin(long timestamp) {
        String format = "yyyy-MM-DD";
        String toDayString = new SimpleDateFormat(format).format(new Date(timestamp));
        Date toDay = null;
        try {
            toDay = org.apache.commons.lang3.time.DateUtils.parseDate(toDayString, new String[]{format});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toDay.getTime();
    }

    /**
     * 获取一个月之前的时间戳
     * @return
     */
    public static long getLastMonthTime() {
        return getDayBegin(getCurrentTime())-86400000l*30;
    }

	/**
	 * 获取星期几
	 * @author: wangxiangyang
	 * @Date: 2018年3月13日下午3:43:45
	 * @return
	 */
	public static int getWeekDay() {
		Date myDate=new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(myDate);
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		return weekDay-1;
		/*if(Calendar.MONDAY == weekDay){
			return "星期一";
		}
		else return "";*/

    }

	/**
	   * 获取现在时间
	   *
	   * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	   */
	public static Date getNowDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   ParsePosition pos = new ParsePosition(8);
	   Date currentTime_2 = formatter.parse(dateString, pos);
	   return currentTime_2;
	}

	/**
	   * 获取现在时间
	   *
	   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	   */
	public static String getStringDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 获取现在时间
	   *
	   * @return 返回短时间字符串格式yyyy-MM-dd
	   */
	public static String getStringDateShort() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 获取时间 小时:分;秒 HH:mm:ss
	   *
	   * @return
	   */
	public static String getTimeShort() {
	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   Date currentTime = new Date();
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}

	/**
	 * Description:(将时间字符串转换为适配elasticsearch的时间格式)
	 * author: zhoumingjie
	 * Date: 2018年7月11日下午6:24:06
	 * @param strDate
	 * @return
	 */
	public static String getDateStrAdaptEs(String strDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return formatter.format(strtodate);
	}

	/**
	   * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	   *
	   * @param strDate
	   * @return
	   */
	public static Date strToDateLong(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}
	/**
	   * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	   *
	   * @param dateDate
	   * @return
	   */
	public static String dateToStrLong(Date dateDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	/**
	   * 将短时间格式时间转换为字符串 yyyy-MM-dd
	   *
	   * @param dateDate
	   * @param
	   * @return
	   */
	public static String dateToStr(Date dateDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	/**
	   * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	   * 
	   * @param strDate
	   * @return
	   */
	public static Date strToDate(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return
	   */
	public static Date getNow() {
	   Date currentTime = new Date();
	   return currentTime;
	}
	/**
	   * 提取一个月中的最后一天
	   * 
	   * @param day
	   * @return
	   */
	public static Date getLastDate(long day) {
	   Date date = new Date();
	   long date_3_hm = date.getTime() - 3600000 * 34 * day;
	   Date date_3_hm_date = new Date(date_3_hm);
	   return date_3_hm_date;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return 字符串 yyyyMMdd HHmmss
	   */
	public static String getStringToday() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 得到现在小时
	   */
	public static String getHour() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   String hour;
	   hour = dateString.substring(11, 13);
	   return hour;
	}
	/**
	   * 得到现在分钟
	   * 
	   * @return
	   */
	public static String getTime() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   String min;
	   min = dateString.substring(14, 16);
	   return min;
	}
	/**
	   * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	   * 
	   * @param sformat
	   *             yyyyMMddhhmmss
	   * @return
	   */
	public static String getUserDate(String sformat) {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat(sformat);
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}

	/**
	 * 根据日期获取星期 
	 * @author: wudong 
	 * @Date: 2018年4月4日下午7:01:51   
	 * @param date
	 * @return
	 */
	public static int getWeekByDate(Date date) {
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
		return weekDay-1;
    }
	
	/**
	 * 根据时间获取时分秒
	 * @author: wudong 
	 * @Date: 2018年4月8日上午9:45:57   
	 * @return
	 */
	public static String getTimeShortByDate(Date date) {
	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   String dateString = formatter.format(date);
	   return dateString;
	}
	
	/**  
	 * 格式化时间
	 * @author: niekaijie 
	 * @Date: 2018年4月8日下午4:57:40   
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getFormatDateString(Date date, String format) {
	   SimpleDateFormat formatter = new SimpleDateFormat(format);
	   String dateString = formatter.format(date);
	   return dateString;
	}
	
	/**
	 * Description:(根据秒获取当前时间字符串). 
	 * author: zhoumingjie 
	 * Date: 2018年4月11日上午10:50:30   
	 * @param time
	 * @return
	 */
	public static String getFormatDateBySecond(Long time) {
		Date date = new Date(time*1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	/**
	 * Description:(根据秒获取当前时间[时分秒]字符串). 
	 * author: zhoumingjie 
	 * Date: 2018年4月11日上午10:50:30   
	 * @param time
	 * @return
	 */
	public static String getFormatDateShortBySecond(Long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}

	/**
	 * Description:(毫秒转日期). 
	 * author: zhoumingjie 
	 * Date: 2018年7月12日上午10:46:01   
	 * @param time
	 * @return
	 */
	public static Date getDateBySec(Long time) {
		return new Date(time);
	}
	
	/**
	 * 毫秒转化时分秒毫秒
	 *
	 * @author: sihanqiu
	 * @Date: 2018年04月14日 11:02:54
	 * @param:
	 * @return:
	 */
	public static String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;


		Long hour = ms / hh;
		Long minute = (ms - hour * hh) / mi;
		Long second = (ms - hour * hh - minute * mi) / ss;
		//Long milliSecond = ms - hour * hh - minute * mi - second * ss;
		//if(milliSecond >= 500) {
		//	second = second + 1;
		//}
		StringBuffer sb = new StringBuffer();
		if(hour > 0) {
			sb.append(hour+"小时");
		}
		if(minute > 0) {
			sb.append(minute+"分");
		}
		if(second > 0) {
			sb.append(second+"秒");
		}

		return sb.toString();
	}


	/**
	 * 根据起始时间，和间隔获取
	 *
	 * @author: sihanqiu
	 * @Date: 2018年04月17日 18:14:43
	 * @param:
	 * @return:
	 */
	public static List<Date> getBeforeRangeDays(Date startDay, Date endDay, String timeDimension) {
		List<Date> days = new ArrayList<>();

		if("1".equals(timeDimension)){
			Date thisMonthFirstDay = getThisMonthFirstDay(endDay);
			for(Long i = thisMonthFirstDay.getTime(); i >= getThisMonthFirstDay(startDay).getTime();) {
				days.add(new Date(i));
				i = getPreMonthFirstDay(new Date(i)).getTime();
			}
		} else if("2".equals(timeDimension)) {
			Date thisWeekFirstDay = getThisWeekFirstDay(endDay);
			for(Long i = thisWeekFirstDay.getTime(); i >= getThisWeekFirstDay(startDay).getTime();) {
				days.add(new Date(i));
				i -= 1000*60*60*24*7;
			}
		} else if("3".equals(timeDimension)) {
			for(Long i = endDay.getTime() - 1000*60*60*24; i >= startDay.getTime();) {
				days.add(new Date(i));
				i -= 1000*60*60*24;
			}
		}
		return days;
	}


	/**
	 * 获取当月的第一天
	 *
	 * @author: sihanqiu
	 * @Date: 2018年04月17日 18:40:56
	 * @param:
	 * @return:
	 */
	public static Date getThisMonthFirstDay(Date day) {
		//获取前月的第一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//获取当前月第一天：
		String dayStr = sdf.format(day);

		Date first = null;
		try {
			first = sdf.parse(dayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return first;
	}

	/**
	 * 获取下个月的第一天
	 *
	 * @author: sihanqiu
	 * @Date: 2018年04月17日 19:36:43
	 * @param:
	 * @return:
	 */
	public static Date getNextMonthFirstDay(Date day) {
		//获取前月的第一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//获取当前月第一天：
		String dayStr = sdf.format(day);
		String[] strs = dayStr.split("-");
		if(Integer.parseInt(strs[1]) < 12) {
			strs[1] = Integer.parseInt(strs[1]) + 1 + "";
		} else {
			strs[0] = Integer.parseInt(strs[0]) + 1 + "";
			strs[1] = "1";
		}
		dayStr = strs[0] + "-" + strs[1];
		Date first = null;
		try {
			first = sdf.parse(dayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return first;
	}

	/**
	 * 获取上个月的第一天
	 *
	 * @author: sihanqiu
	 * @Date: 2018年04月17日 19:36:43
	 * @param:
	 * @return:
	 */
	public static Date getPreMonthFirstDay(Date day) {
		//获取前月的第一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		//获取当前月第一天：
		String dayStr = sdf.format(day);
		String[] strs = dayStr.split("-");
		if(Integer.parseInt(strs[1]) > 1) {
			strs[1] = Integer.parseInt(strs[1]) - 1 + "";
		} else {
			strs[0] = Integer.parseInt(strs[0]) - 1 + "";
			strs[1] = "12";
		}
		dayStr = strs[0] + "-" + strs[1];
		Date first = null;
		try {
			first = sdf.parse(dayStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return first;
	}

	/**
	 * 获取指定日期所在周的第一天
	 *
	 * @author: sihanqiu
	 * @Date: 2018年04月17日 19:42:21
	 * @param:
	 * @return:
	 */
	public static Date getThisWeekFirstDay(Date day) {
		//获取前月的第一天
		Calendar cal = Calendar.getInstance();

		cal.setTime(day);

		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		// 所在周开始日期
		return cal.getTime();
	}
	
	/**
	 * Description:(将时间字符串解析成时间). 
	 * author: zhoumingjie 
	 * Date: 2018年5月7日下午8:13:45   
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date getFormatDate(String strDate,String format) {
		return new SimpleDateFormat(format).parse(strDate, new ParsePosition(0));
	}

	/**
	 * Description:(获取几月后的时间). 
	 * author: zhoumingjie 
	 * Date: 2018年5月7日下午3:06:17   
	 * @param date
	 * @param
	 * @return
	 */
	public static String getFetureDateString(Date date, String format, int future) {  
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, future);
		Date newDate = calendar.getTime();  
		return new SimpleDateFormat(format).format(newDate);  
	}
	/**
	 * 获取两个时间之间的所有时间集合
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param dateUnit 时间刻度
	 * @param timeFormat 时间格式
	 * @return
	 */
	public static List<String> getDatesBetweenTwoDate(String startTime, String endTime,int dateUnit,String timeFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		List<String> list = new ArrayList<String>();
		try {
			Date startDate = sdf.parse(startTime);
			Date endDate = sdf.parse(endTime);

			Calendar calendar = Calendar.getInstance();
			while (startDate.getTime() <= endDate.getTime()) {
				list.add(sdf.format(startDate));
				calendar.setTime(startDate);
				calendar.add(dateUnit, 1);
				// 获取增加后的日期
				startDate = calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		Map<String, List<String>> distanceTime = getDistanceTime("2019-07-09", "2019-8-24");
		List<String> day = distanceTime.get("day");
		System.out.println(day);
		List<String> week = distanceTime.get("week");
		for (int i = 0; i < week.size(); i++) {
			if(i == 0){
				System.out.println(week.get(0)+" ***** "+ week.get(0));
			}else {
				System.out.println(week.get(i-1)+" ***** "+ week.get(i));
			}
		}
		System.out.println(week);
		List<String> month = distanceTime.get("month");
		System.out.println(month);
		List<String> year = distanceTime.get("year");
		System.out.println(year);
	}
}
