package edu.upenn.cis350.shs_mobile;

/**
 * The purpose of this superclass is to hold all the duplicate code and data-like variables. It is the parent
 * of all Activities related to the scheduling workflow. It was created through a superclass extract process. 
 */

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import edu.upenn.cis350.shs_mobile.R;

public abstract class AppointmentGeneralActivity extends Activity {
	
	// Used to keep track of the workflow from screen to screen
	protected String nextAction;
	
	// Keys for keeping track of the workflow and associated values
	protected static final String PREV_TOKEN = "edu.upenn.cis350.shs_mobile.PREV_TOKEN"; // which button we last clicked
	protected static final String NEXT_ACTION = "edu.upenn.cis350.shs_mobile.NEXT_ACTION"; // indicates what screen should come next
	protected static final String DEPARTMENT = "edu.upenn.cis350.shs_mobile.DEPARTMENT"; // holding what department we'll be querying for an appt
	protected static final String DATE = "edu.upenn.cis350.shs_mobile.DATE"; // potential booking date
	protected static final String CALLBACK = "edu.upenn.cis350.shs_mobile.CALLBACK"; // callback number
	protected static final String REASON = "edu.upenn.cis350.shs_mobile.REASON"; // auxiliary reason field
	protected static final String IMMUNIZATION = "edu.upenn.cis350.shs_mobile.IMMUNIZATION"; // type of immunization field
	protected static final String SUBTYPE = "edu.upenn.cis350.shs_mobile.SUBTYPE"; // type of subtype of immunization field

	// Initial screen types
	protected static final String[] appointmentTypes = { "Immunizations",
			"Health and Wellness", "Primary Care", "Sports Medicine",
			"Women's Health" };
	
	// All the options after the initial screen
	protected static final String[] immunizationTypes = {
			"Immunizations (required and optional)", "PPD Placement",
			"Flu Vaccination", "Designated Group Immunization Clinic" };
	protected static final String[] healthAndWellnessTypes = {
			"Acupuncture or Massage Therapy Visit", "Rapid HIV Testing",
			"Smoking Cessation", "Stress Reduction" };
	protected static final String[] womensHealthTypes = {
			"Routine yearly gynecological exam",
			"STI Screening (except Rapid HIV Testing)", "Birth Control",
			"Other" };
	protected static final String[] subImmunizationTypes = { "Gardasil (HPV) (optional)",
			"Hepatitis A (optional)", "Hepatitis B (satisfies requirement)",
			"Meningococcal (satisfied requirement)",
			"MMR (required for measles, mumps, rubella",
			"Multiple Immunizations", "Tdap (satisfied requirement)",
			"Varicella (satisfies requirement)", "Other" };
	protected static final String[] acupunctureTypes = { "Acupuncture - initial visit",
			"Acupuncture Follow-Up Visit", "Massage Therapy Visit" };
	
	/*
	 * NOTE: primaryCare and sportsMedicine messages no longer in use; app allows scheduling in-app
	 * There are three text fields for these activities, so need 3 strings
	 */
	protected static final String[] primaryCareMessages = {
			"If you have a serious injury, call 215-746-3535 to speak with a Nurse.",
			"All appointments in Primary Care are made by phone to ensure adequate patient care.",
			"Please call 215-746-3535 during SHS operating hours and choose option 2 to book an appointment." };
	protected static final String[] sportsMedicineMessages = {
			"",
			"Currently we are unable to accept mobile booked appointments for sports medicine visits.",
			"Please call SHS at 215-746-3535 during SHS operating hours and choose option 2 to book an appointment." };
	protected static final String[] womensHealthMessages = {
			"",
			"",
			"Please Call 215-746-3535 and speak to a women's health nurse for assistance scheduling your appointment." };
	
	// Message given when prompted for an auxiliary reason for your visit
	protected final static String reasonMessage = "Please enter the reason for your visit.";
	
	// Message given when prompted for a callback number
	protected final static String callbackMessage = "Please enter a callback number where you can be reached in case it is " +
			"necessary to contact you concerning the appointment you are scheduling.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));     
        ab.setBackgroundDrawable(colorDrawable);
        getActionBar().setTitle("SHS Mobile");  
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_book, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.my_appts_action:
	        	intent = new Intent(this, MyAppointments.class);
	        	intent.putExtras(getIntent().getExtras());
	        	startActivity(intent);
	            return true;
	        case R.id.messages_action:
	        	intent = new Intent(this, MyMessages.class);
	        	intent.putExtras(getIntent().getExtras());
	        	startActivity(intent);
	            return true;
	        case R.id.logout:
	        	Bundle b = getIntent().getExtras();
	        	ServerPOSTLogout logout = new ServerPOSTLogout("auth.php",
						b.getString("Session_Username"), b.getInt("Session_ID") + "");
	        	logout.execute();
	        	intent = new Intent(this, LoginActivity.class);
	        	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
