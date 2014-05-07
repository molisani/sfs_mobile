package edu.upenn.cis350.shs_mobile;

import java.util.HashMap;

public class Appointment {
	String immun, dep, subtype, callback, reason;
	int dur, id;
	Timestamp cal;
	String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday"};
	String[] months = {"January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December"};
	HashMap<String, String> departments = new HashMap<String, String>();
	
	public Appointment(String immun, String dur, Timestamp date, 
			String id, String dep, String subtype, String callback, String reason) {
		this.immun = immun;
		this.dep = dep;
		this.subtype = subtype;
		this.dur = Integer.parseInt(dur);
		this.id = Integer.parseInt(id);
		this.cal = date;
		this.callback = callback;
		this.reason = reason;
		makeDepartments();
	}
	
	public int getId() {
		return id;
	}
	
	private void makeDepartments() {
		departments.put("i", "Immunization");
		departments.put("p", "Primary Care");
		departments.put("s", "Sports Care");
		departments.put("h", "Health and Wellness");
		departments.put("w", "Women's Health");

	}
	
	@Override
	public String toString() {
		String s = "";
		s += cal.getDate() + " at " + cal.getTime();
		s += "\n";
		s += "Reason for Visit: " + departments.get(dep);
		s += "\n";
		return s;
	}
}
