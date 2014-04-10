package edu.upenn.cis350.sfs_mobile;

public class Appointment {
	String immun, dep, subtype;
	int dur, id;
	Timestamp cal;
	String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday"};
	String[] months = {"January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December"};
	
	public Appointment(String immun, String dur, Timestamp date, 
			String id, String dep, String subtype) {
		this.immun = immun;
		this.dep = dep;
		this.subtype = subtype;
		this.dur = Integer.parseInt(dur);
		this.id = Integer.parseInt(id);
		this.cal = date;
	}
	
	@Override
	public String toString() {
		String s = "";
		s += cal.getDate() + " at " + cal.getTime();
		return s;
	}
}
