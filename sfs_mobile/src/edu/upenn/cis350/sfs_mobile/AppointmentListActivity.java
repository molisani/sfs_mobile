package edu.upenn.cis350.sfs_mobile;

/**
 * Used in the initial screen after you click make appointment, also at various places within the workflow
 */

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentListActivity extends AppointmentGeneralActivity implements OnItemClickListener {
	
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// initialize
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		nextAction = extras.getString(NEXT_ACTION).toLowerCase();
		String[] content = null;
		System.out.println("cur: " + nextAction);
		
		// set content
		if (nextAction.equals("appointment_types")) { 
			content = appointmentTypes;
		} else if (nextAction.equals("immunization_types")) {
			content = immunizationTypes;
		} else if (nextAction.equals("health_and_wellness_types")) {
			content = healthAndWellnessTypes;
		} else if (nextAction.equals("womens_health_types")) { 
			content = womensHealthTypes;
		} else if (nextAction.equals("sub_immunization_types")) {
			content = subImmunizationTypes;
		} else if (nextAction.equals("acupuncture_types")) {
			content = acupunctureTypes;
		} else {
			System.out.println("Error 500: Could not determine what content to display.");
			return;
		}
		
		// display
		listView = (ListView) findViewById(R.id.appointmentListView);
		ArrayAdapter atlAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 , content);
		listView.setAdapter(atlAdapter);
		listView.setOnItemClickListener(this);
	}
	
	/**
	 * Logic for determining the next activity
	 */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
		
		// initialize
		String itemText = ((String) ((TextView) view).getText());
		Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		Class nextClass = null;
		String dept = null, nextAct = null, subtype = null, immunization = null;
		
		// appointment_types screen 
		if (nextAction.equals("appointment_types")) { 
			if (itemText.equals(appointmentTypes[0])) { // immunizations
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class;
				dept = "i";
				nextAct = "immunization_types";
			} else if (itemText.equals(appointmentTypes[1])) { // health & wellness
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class;
				dept = "h";
				nextAct = "health_and_wellness_types";
			} else if (itemText.equals(appointmentTypes[4])) { // women's health
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class;
				dept = "w";
				nextAct = "womens_health_types";
			} else if (itemText.equals(appointmentTypes[2])) { // primary care
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentConfirmActivity.class;
				dept = "p";
				nextAct = "confirm_nonemergency";
			} else if (itemText.equals(appointmentTypes[3])) { // sports medicine
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentConfirmActivity.class;
				dept = "s";
				nextAct = "confirm_nonemergency";
			} else {
				System.out.println("ERROR 100: Could not identify list item clicked");
				return;
			}
		
		// immunization_types, health_and_wellness_types, women's_health_types screens
		} else if (nextAction.equals("immunization_types")) {
			if (itemText.equals(immunizationTypes[0])) { // other immunizations
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class;
				nextAct = "sub_immunization_types";
			} else if (itemText.equals(immunizationTypes[1]) || // ppd placement
					itemText.equals(immunizationTypes[2]) || // flu vaccination
					itemText.equals(immunizationTypes[3])) { // designed group immunization clinic
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentPhoneInputActivity.class;
				nextAct = "callback";
			} else {
				System.out.println("ERROR 101: Could not identify list item clicked");
				return;
			}
			immunization = itemText;
		} else if (nextAction.equals("health_and_wellness_types")) { 
			if (itemText.equals(healthAndWellnessTypes[0])) { // acupuncture
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentListActivity.class;
				nextAct = "acupuncture_types";
			} else if (
					itemText.equals(healthAndWellnessTypes[1]) || // HIV
					itemText.equals(healthAndWellnessTypes[2]) || // smoking
					itemText.equals(healthAndWellnessTypes[3])) { // stress
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class;
				nextAct = "reason";
			} else {
				System.out.println("Error 505: Could not determine next activity.");
				return;
			}
		} else if (nextAction.equals("womens_health_types")) { 
			if (
					itemText.equals(womensHealthTypes[0]) ||
					itemText.equals(womensHealthTypes[1]) ||
					itemText.equals(womensHealthTypes[2])) {
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class;
				nextAct = "reason";
			} else if (itemText.equals(womensHealthTypes[3])) {
				nextClass = edu.upenn.cis350.sfs_mobile.AppointmentMessageActivity.class;
				nextAct = "womens_health_message";
			} else {
				System.out.println("ERROR 103: Could not identify next activity");
				return;
			}
		
		// subImmunizationTypes
		} else if (nextAction.equals("sub_immunization_types")) {
			nextClass = edu.upenn.cis350.sfs_mobile.AppointmentPhoneInputActivity.class;
			nextAct = "callback";
			subtype = itemText;
		
		// acupunctureTypes
		} else if (nextAction.equals("acupuncture_types")) {
			nextClass = edu.upenn.cis350.sfs_mobile.AppointmentTextInputActivity.class;
			nextAct = "reason";
			subtype = itemText;
			
		} else {
			System.out.println("Error 50: Could not determine next activity.");
		}
		
		// progress
		System.out.println("next: " + nextAct);
		intent = new Intent(this, nextClass);
		intent.putExtras(getIntent().getExtras());
		intent.putExtra(NEXT_ACTION, nextAct != null ? nextAct : getIntent().getExtras().getString(NEXT_ACTION));
		intent.putExtra(DEPARTMENT, dept != null ? dept : getIntent().getExtras().getString(DEPARTMENT));
		intent.putExtra(IMMUNIZATION, immunization != null ? immunization : getIntent().getExtras().getString(IMMUNIZATION));
		intent.putExtra(SUBTYPE, subtype != null ? subtype : getIntent().getExtras().getString(SUBTYPE));
		startActivity(intent);
	}

}
