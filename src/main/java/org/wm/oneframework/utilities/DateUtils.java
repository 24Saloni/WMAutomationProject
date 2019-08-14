package org.wm.oneframework.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateUtils {

	private static Logger logger = LogManager.getLogger(DateUtils.class.getName());
	private static Random random = new Random();

	private DateUtils() {

	}

	/**
	 * This method parses the Date object into specific String format.
	 * 
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String parseIntoString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * This method parses the String object of specific format to Date object.
	 * 
	 * @param date
	 * @param format
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parseIntoDate(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	/**
	 * This method checks if input String is a valid Date.
	 * 
	 * @param date
	 * @param format
	 * @return Date
	 * @throws ParseException
	 */
	public static boolean isDate(String date, String format) {
		Date dateVal = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			dateVal = sdf.parse(date);
		} catch (ParseException e) {
			logger.warn(e.getMessage());
		}
		return dateVal != null;
	}

	/**
	 * This method will return the current date in the specific format.
	 * 
	 * @param format
	 * @return String
	 */
	public static String getCurrentDate(String format) {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return parseIntoString(date, format);
	}

	/**
	 * This method will return tommorrow's date in String format.
	 * 
	 * @param format
	 * @return String
	 */
	public static String getTomorrowDate(String format) {
		return parseIntoString(getDateWrtCurrentDate(1), format);
	}

	/**
	 * This method will return yesterday's date in String format.
	 * 
	 * @param format
	 * @return String
	 */
	public static String getYesterdayDate(String format) {
		return parseIntoString(getDateWrtCurrentDate(-1), format);
	}

	/**
	 * This method will return future's date in String format.
	 * 
	 * @param format
	 * @return String
	 */
	public static String getFutureDate(String format) {
		int diff = random.nextInt(3);
		return parseIntoString(getDateWrtCurrentDate(diff), format);
	}

	/**
	 * This method will return past date in String format.
	 * 
	 * @param format
	 * @return String
	 */
	public static String getPastDate(String format) {
		int diff = random.nextInt(3);
		return parseIntoString(getDateWrtCurrentDate(-diff), format);
	}

	private static Date getDateWrtCurrentDate(int diff) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, diff);
		return cal.getTime();
	}

}
