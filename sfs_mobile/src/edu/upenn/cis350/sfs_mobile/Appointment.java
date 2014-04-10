package edu.upenn.cis350.sfs_mobile;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Appointment {
	String immun, dep, subtype;
	int dur, id;
	Calendar cal;
	String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday"};
	String[] months = {"January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December"};
	
	public Appointment(String immun, String dur, String date, 
			String id, String dep, String subtype) {
		this.immun = immun;
		this.dep = dep;
		this.subtype = subtype;
		this.dur = Integer.parseInt(dur);
		this.id = Integer.parseInt(id);
		this.cal = parseDate(date);
	}
	
	private Calendar parseDate(String date) {
		String day = date.split(" ")[0];
		String time = date.split(" ")[1];
		String[] d = day.split("-");
		String[] t = time.split(":");
		Calendar cal = new GregorianCalendar(Integer.parseInt(d[0]), 
				Integer.parseInt(d[1]), Integer.parseInt(d[2]),
				Integer.parseInt(t[0]), Integer.parseInt(t[1]));
		return cal;
	}
	
	@Override
	public String toString() {
		String s = "";
		s += daysOfWeek[cal.get(Calendar.DAY_OF_WEEK) - 1] + ", " + months[cal.get(Calendar.MONTH)] + 
				" " + cal.get(Calendar.DATE) + "\n";
		s += "\t" + dur + " minute appointment";
		return s;
	}
}
