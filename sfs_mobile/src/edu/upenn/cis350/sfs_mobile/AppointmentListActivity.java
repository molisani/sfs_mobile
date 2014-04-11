package edu.upenn.cis350.sfs_mobile;

/**
 * Used in the initial screen after you click make appointment, also at various places within the workflow
 */

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter.LengthFilter;
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
	
	ListView listView;
	
	String[] appointmentTypes = {
			"Immunizations", "Health and Wellness", "Primary Care", 
			"Sports Medicine", "Women's Health"
	};
	String[] immunizationTypes = {
			"Immunizations (required and optional)", "PPD Placement", "Flu Vaccination",
			"Designated Group Immunization Clinic"
	};
	String[] subImmunizationTypes = {
			"Gardasil (HPV) (optional)", "Hepatitis A (optional)", "Hepatitis B (satisfies requirement)", 
			"Meningococcal (satisfied requirement)", "MMR (required for measles, mumps, rubella",
			"Multiple Immunizations", "Tdap (satisfied requirement)", "Varicella (satisfies requirement)"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		System.out.println(extras == null);
		lastScreen = extras.getString(LAST_SCREEN).toLowerCase();
		if (extras != null && extras.containsKey(VALUE_CLICKED))
			valueClicked = intent.getExtras().getString(VALUE_CLICKED);
		String[] content = null;
		
		if (lastScreen.equals("make_appointment")) { // TODO - need to add this extra & set appropriate value from Alex's button
			content = appointmentTypes;
		} else if (lastScreen.equals("appointment_types")) {
			if (valueClicked.equals(appointmentTypes[0])) { // immunizations
				content = immunizationTypes;
			} else if (valueClicked.equals(appointmentTypes[1])) { // health & wellness
				content = null;
			} else if (valueClicked.equals(appointmentTypes[4])) { // women's health
				content = null;
			} else {
				content = null;
			}
		} else if (lastScreen.equals("immunization_types")) {
			content = subImmunizationTypes;
		}
		listView = (ListView) findViewById(R.id.appointmentListView);
		ArrayAdapter atlAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 , content);
		listView.setAdapter(atlAdapter);
		listView.setOnItemClickListener(this);
		// TODO - add rest of possible screens
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
		String itemText = ((String) ((TextView) view).getText());
		Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		
		// appointment_types screen 
		if (lastScreen.equals("make_appointment")) { // TODO - same as above
			if (itemText.equals(appointmentTypes[0])) { // immunizations
				intent = new Intent(this, AppointmentListActivity.class);
				System.out.println("Set lastScreen");
				intent.putExtra(LAST_SCREEN, "appointment_types");
			} else if (itemText.equals(appointmentTypes[1])) { // health & wellness
				intent = new Intent(this, AppointmentListActivity.class);
				intent.putExtra(LAST_SCREEN, "appointment_types");
			} else if (itemText.equals(appointmentTypes[2])) { // primary care
				// TODO
			} else if (itemText.equals(appointmentTypes[3])) { // sports medicine
				// TODO
			} else if (itemText.equals(appointmentTypes[4])) { // women's health
				intent = new Intent(this, AppointmentListActivity.class);
				intent.putExtra(LAST_SCREEN, "appointment_types");
			} else {
				System.out.println("ERROR 100: Could not identify list item clicked");
				return;
			}
			
		// immunization_types, health_and_wellness_types, women's_health_types screens
		} else if (lastScreen.equals("appointment_types")) {
			if (valueClicked.equals(appointmentTypes[0])) { // immunizations
				if (itemText.equals(immunizationTypes[0])) { // other immunizations
					
				} else if (itemText.equals(immunizationTypes[1])) { // ppd placement
					
				} else if (itemText.equals(immunizationTypes[2])) { // flu vaccination
					
				} else if (itemText.equals(immunizationTypes[3])) { // designed group immunization clinic
				
				} else {
					System.out.println("ERROR 101: Could not identify list item clicked");
					return;
				}
			} else if (valueClicked.equals(appointmentTypes[1])) { // health and wellness
				// options
			} else if (valueClicked.equals(appointmentTypes[4])) { // women's health
				// options
			} else {
				// error
			}
			//if (itemText.equals("immunizations"))
		}
		// TODO - add rest of possible screens
		intent.putExtra(VALUE_CLICKED, itemText);
		startActivity(intent);
	}

}
