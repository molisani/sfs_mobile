package edu.upenn.cis350.sfs_mobile;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Timestamp {
	String hour, minute, noon, day, month, year, dayName, monthName;
	String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday"};
	String[] months = {"January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December"};
	
	public Timestamp(String stamp) {
		String day = stamp.split(" ")[0];
		String time = stamp.split(" ")[1];
		String[] daySplit = day.split("-");
		String[] timeSplit = time.split(":");
		year = daySplit[0];
		month = daySplit[1];
		this.day = daySplit[2];
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.parseInt(year), Integer.parseInt(month),
				Integer.parseInt(this.day));
		dayName = daysOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1];
		monthName = months[cal.get(Calendar.MONTH) - 1];
		hour = timeSplit[0];
		minute = timeSplit[1];
		if (Integer.parseInt(hour) >= 12) {
			noon = "PM";
			int tempHour = Integer.parseInt(timeSplit[0]) % 12;
			if (tempHour == 0) {
				tempHour = 12;
			}
			hour = tempHour + "";	
		} else {
			noon = "AM";
		}
	}
	
	public String getTime() {
		return hour + ":" + minute + " " + noon;
	}
	
	public String getDate() {
		return dayName + ", " + monthName + " " + day;
	}
	
	public int totalTime() {
		
		int result = Integer.parseInt(minute) + Integer.parseInt(hour) + Integer.parseInt(day) + Integer.parseInt(month)*31 + Integer.parseInt(year);
		return result;
	}
}
