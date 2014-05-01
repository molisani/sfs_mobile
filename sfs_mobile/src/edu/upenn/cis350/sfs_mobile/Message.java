package edu.upenn.cis350.sfs_mobile;

public class Message implements Comparable {
	int msgId, apptID;
	Timestamp cal;
	String pennkey, name, subj;
	boolean read;
	
	public Message(String msgId, String apptId, Timestamp date, String pennkey, String name, String subj, int read) {
		this.msgId = Integer.parseInt(msgId);
		this.apptID = Integer.parseInt(apptId);
		this.cal = date;
		this.pennkey = pennkey;
		this.name = name;
		this.subj = subj;
		this.read = read == 1;
	}
	
	public int getId() {
		return msgId;
	}
	
	public int getApt() {
		return apptID;
	}
	
	public String getSubj() {
		return subj;
	}
	
	public String getSender() {
		return pennkey;
	}
	
	@Override
	public String toString() {
		String s = "";
		s += cal.getDate() + " at " + cal.getTime();
		s += "\n";
		s += "Subject: " + subj;
		s += "\n";
		s += "Sent By: " + name;
		return s;
	}

	@Override
	public int compareTo(Object m) {
		if (cal.totalTime() > ((Message)m).cal.totalTime())
            return -1;
        else if (cal.totalTime() < ((Message)m).cal.totalTime())
            return 1;
        else
            return 0;
	}	
}
