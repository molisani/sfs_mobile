package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class AppointmentMessageActivity extends Activity {
	
	TextView topTextView, middleTextView, bottomTextView;
	String lastScreen, valueClicked;
	
	// NOTE: primaryCare and sportsMedicine messages no longer in use; app allows scheduling
	String[] primaryCareMessages = {
			"If you have a serious injury, call 215-746-3535 to speak with a Nurse.",
			"All appointments in Primary Care are made by phone to ensure adequate patient care.",
			"Please call 215-746-3535 during SHS operating hours and choose option 2 to book an appointment."
	};
	String[] sportsMedicineMessages = {
			"", "Currently we are unable to accept mobile booked appointments for sports medicine visits.",
			"Please call SHS at 215-746-3535 during SHS operating hours and choose option 2 to book an appointment."
	};
	String[] womensHealthMessages = {
			"", "", 
			"Please Call 215-746-3535 and speak to a women's health nurse for assistance scheduling your appointment."
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));
        ab.setBackgroundDrawable(colorDrawable);
		setContentView(R.layout.activity_login);
        getActionBar().setTitle("SFS Mobile");  
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
			} else if (valueClicked.equals(AppointmentListActivity.appointmentTypes[3])) { // sport medicine
				content = sportsMedicineMessages;
			} else {
				System.out.println("Error 200: Could not determine button clicked.");
				return;
			}
		} else if (lastScreen.equals("womens_health_types")) {
			content = womensHealthMessages;
		} else {
			System.out.println("Error 300: Could not determine next activity.");
		}
		topTextView = (TextView) findViewById(R.id.messageView);
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
