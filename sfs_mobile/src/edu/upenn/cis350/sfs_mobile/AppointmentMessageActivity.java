package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class AppointmentMessageActivity extends Activity {
	
	TextView topTextView, middleTextView, bottomTextView;
	String lastScreen, valueClicked;
	
	String[] primaryCareMessages = {
			"If you have a serious injury, call 215-746-3535 to speak with a Nurse.",
			"All appointments in Primary Care are made by phone to ensure adequate patient care.",
			"Please call 215-746-3535 during SHS operating hours and choose option 2 to book an appointment."
	};
	String[] sportsMedicineMessages = {
			"", "Currently we are unable to accept mobile booked appointments for sports medicine visits.",
			"Please call SHS at 215-746-3535 during SHS operating hours and choose option 2 to book an appointment."
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_message);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		lastScreen = extras.getString(AppointmentListActivity.LAST_SCREEN).toLowerCase();
		if (extras != null && extras.containsKey(AppointmentListActivity.VALUE_CLICKED))
			valueClicked = intent.getExtras().getString(AppointmentListActivity.VALUE_CLICKED);
		String[] content = null;
		if (lastScreen.equals("appointment_types")) {
			if (valueClicked.equals(AppointmentListActivity.appointmentTypes[2])) { // primary care
				content = primaryCareMessages;
			} else if (valueClicked.equals(AppointmentListActivity.appointmentTypes[3])) { // sports medicine
				content = sportsMedicineMessages;
			} else {
				System.out.println("Error 200: Could not determine button clicked.");
				return;
			}
		}
		topTextView = (TextView) findViewById(R.id.textView1);
		topTextView.setText(content[0]);
		middleTextView = (TextView) findViewById(R.id.textView2);
		middleTextView.setText(content[1]);
		bottomTextView = (TextView) findViewById(R.id.textView3);
		bottomTextView.setText(content[2]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appt_list_activity, menu);
		return true;
	}

}
