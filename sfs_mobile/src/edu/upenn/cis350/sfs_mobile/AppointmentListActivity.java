package edu.upenn.cis350.sfs_mobile;

/**
 * Used in the initial screen after you click make appointment, also at various places within the workflow
 */

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentListActivity extends Activity implements OnItemClickListener {
	
	private String lastScreen, valueClicked; // values from the keys defined below
	static final String LAST_SCREEN = "edu.upenn.cis350.sfs_mobile.LAST_SCREEN"; // key for the extra telling which screen we're coming from
	static final String VALUE_CLICKED = "edu.upenn.cis350.sfs_mobile.VALUE_CLICKED"; // key for the extra telling which button we last clicked
	static final String NEXT_SCREEN = "edu.upenn.cis350.sfs_mobile.NEXT_SCREEN"; // key for code indicating what screen should come next
	static final String DEPARTMENT = "edu.upenn.cis350.sfs_mobile.DEPARTMENT"; // key for holding what department we'll be querying for an appt
	static final String DATE = "edu.upenn.cis350.sfs_mobile.DATE"; // key for potential booking date
	
	ListView listView;
	
	static final String[] appointmentTypes = {
			"Immunizations", "Health and Wellness", "Primary Care", 
			"Sports Medicine", "Women's Health"
	};
	static final String[] immunizationTypes = {
			"Immunizations (required and optional)", "PPD Placement", "Flu Vaccination",
			"Designated Group Immunization Clinic"
	};
	static final String[] healthAndWellnessTypes = {
			"Acupuncture or Massage Therapy Visit", "Rapid HIV Testing", "Smoking Cessation",
			"Stress Reduction"
	};
	static final String[] womensHealthTypes = {
			"Routine yearly gynecological exam", "STI Screening (except Rapid HIV Testing)", "Birth Control", 
			"Other"
	};
	static final String[] subImmunizationTypes = {
			"Gardasil (HPV) (optional)", "Hepatitis A (optional)", "Hepatitis B (satisfies requirement)", 
			"Meningococcal (satisfied requirement)", "MMR (required for measles, mumps, rubella",
			"Multiple Immunizations", "Tdap (satisfied requirement)", "Varicella (satisfies requirement)", "Other"
	};
	static final String[] acupunctureTypes = {
		"Acupuncture - initial visit", "Acupuncture Follow-Up Visit", "Massage Therapy Visit"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		lastScreen = extras.getString(LAST_SCREEN).toLowerCase();
		if (extras != null && extras.containsKey(VALUE_CLICKED))
			valueClicked = intent.getExtras().getString(VALUE_CLICKED);
		String[] content = null;
		
		if (lastScreen.equals("make_appointment")) { 
			content = appointmentTypes;
		} else if (lastScreen.equals("appointment_types")) {
			if (valueClicked.equals(appointmentTypes[0])) { // immunizations
				content = immunizationTypes;
			} else if (valueClicked.equals(appointmentTypes[1])) { // health & wellness
				content = healthAndWellnessTypes;
			} else if (valueClicked.equals(appointmentTypes[4])) { // women's health
				content = womensHealthTypes;
			} else {
				System.out.println("Error 90: Content not found");
				content = null;
			}
		} else if (lastScreen.equals("immunization_types")) {
			content = subImmunizationTypes;
		} else if (lastScreen.equals("health_and_wellness_types")) {
			content = acupunctureTypes;
		} else {
			System.out.println("Error 500: Could not determine what content to display.");
			return;
		}
		listView = (ListView) findViewById(R.id.appointmentListView);
		ArrayAdapter atlAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 , content);
		listView.setAdapter(atlAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appt_list_activity, menu);
		return true;
	}

	/**
	 * Logic for determining the next activity
	 */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
		String itemText = ((String) ((TextView) view).getText());
		Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		
		
		// appointment_types screen 
		if (lastScreen.equals("make_appointment")) { 
			if (
					itemText.equals(appointmentTypes[0]) || // immunizations
					itemText.equals(appointmentTypes[1]) || // health & wellness
					itemText.equals(appointmentTypes[4])) {	// women's health
				intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class);
				intent.putExtras(this.getIntent().getExtras());
				if (itemText.equals(appointmentTypes[0]))
					intent.putExtra(AppointmentListActivity.DEPARTMENT, "i");
				else if (itemText.equals(appointmentTypes[1]))
					intent.putExtra(AppointmentListActivity.DEPARTMENT, "h");
				else if (itemText.equals(appointmentTypes[4]))
					intent.putExtra(AppointmentListActivity.DEPARTMENT, "w");
			} else if (
					itemText.equals(appointmentTypes[2]) || // primary care
					itemText.equals(appointmentTypes[3])) {	// sports medicine
				intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentMessageActivity.class);
				intent.putExtras(this.getIntent().getExtras());
				if (itemText.equals(appointmentTypes[2]))
					intent.putExtra(AppointmentListActivity.DEPARTMENT, "p");
				else if (itemText.equals(appointmentTypes[3]))
					intent.putExtra(AppointmentListActivity.DEPARTMENT, "s");
			} else {
				System.out.println("ERROR 100: Could not identify list item clicked");
				return;
			}
			intent.putExtra(LAST_SCREEN, "appointment_types");
		
		// immunization_types, health_and_wellness_types, women's_health_types screens
		} else if (lastScreen.equals("appointment_types")) {
			if (valueClicked.equals(appointmentTypes[0])) { // immunizations
				if (itemText.equals(immunizationTypes[0])) { // other immunizations
					intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class);
					intent.putExtras(this.getIntent().getExtras());
				} else if (
						itemText.equals(immunizationTypes[1]) || // ppd placement
						itemText.equals(immunizationTypes[2]) || // flu vaccination
						itemText.equals(immunizationTypes[3])) { // designed group immunization clinic
					intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class);
					intent.putExtras(this.getIntent().getExtras());
					intent.putExtra(NEXT_SCREEN, "callback");
				} else {
					System.out.println("ERROR 101: Could not identify list item clicked");
					return;
				}
				intent.putExtra(LAST_SCREEN, "immunization_types");
				intent.putExtra(AppointmentListActivity.DEPARTMENT, "i");
			} else if (valueClicked.equals(appointmentTypes[1])) { // health and wellness
				if (itemText.equals(healthAndWellnessTypes[0])) { // acupuncture
					intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class);
					intent.putExtras(this.getIntent().getExtras());
				} else if (
						itemText.equals(healthAndWellnessTypes[1]) || // HIV
						itemText.equals(healthAndWellnessTypes[2]) || // smoking
						itemText.equals(healthAndWellnessTypes[3])) { // stress
					intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class);
					intent.putExtras(this.getIntent().getExtras());
					intent.putExtra(NEXT_SCREEN, "reason");
				} else {
					System.out.println("Error 505: Could not determine next activity.");
					return;
				}
				intent.putExtra(LAST_SCREEN, "health_and_wellness_types");
				intent.putExtra(AppointmentListActivity.DEPARTMENT, "h");
			} else if (valueClicked.equals(appointmentTypes[4])) { // women's health
				if (
						itemText.equals(womensHealthTypes[0]) ||
						itemText.equals(womensHealthTypes[1]) ||
						itemText.equals(womensHealthTypes[2])) {
					intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class);
					intent.putExtras(this.getIntent().getExtras());
					intent.putExtra(NEXT_SCREEN, "reason");	
				} else if (itemText.equals(womensHealthTypes[3])) {
					intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentMessageActivity.class);
					intent.putExtras(this.getIntent().getExtras());
				} else {
					System.out.println("ERROR 103: Could not identify next activity");
					return;
				}
				intent.putExtra(LAST_SCREEN, "womens_health_types");
				intent.putExtra(AppointmentListActivity.DEPARTMENT, "w");
				
			} else {
				System.out.println("ERROR 102: Could not identify list item clicked");
				return;
			}
		
		// subImmunizationTypes
		} else if (lastScreen.equals("immunization_types")) {
			intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class);
			intent.putExtras(this.getIntent().getExtras());
			intent.putExtra(NEXT_SCREEN, "callback");
			intent.putExtra(AppointmentListActivity.DEPARTMENT, "i");
		
		// acupunctureTypes
		} else if (lastScreen.equals("health_and_wellness_types")) {
			intent = new Intent(this, edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class);
			intent.putExtras(this.getIntent().getExtras());
			intent.putExtra(NEXT_SCREEN, "reason");
			intent.putExtra(AppointmentListActivity.DEPARTMENT, "h");
			
		} else {
			System.out.println("Error 50: Could not determine next activity.");
		}
		intent.putExtra(VALUE_CLICKED, itemText);
		startActivity(intent);
	}

}
