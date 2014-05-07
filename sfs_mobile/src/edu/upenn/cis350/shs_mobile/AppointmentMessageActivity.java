package edu.upenn.cis350.shs_mobile;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import edu.upenn.cis350.shs_mobile.R;

public class AppointmentMessageActivity extends AppointmentGeneralActivity {
	
	TextView topTextView, middleTextView, bottomTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// initialize
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_message);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		nextAction = extras.getString(NEXT_ACTION).toLowerCase();
		String[] content = null;
		
		// content
		if (nextAction.equals("primary_care_message")) {
			content = primaryCareMessages;
		} else if (nextAction.equals("sports_medicine_message")) { // sport medicine
			content = sportsMedicineMessages;
		} else if (nextAction.equals("womens_health_message")) {
			content = womensHealthMessages;
		} else {
			System.out.println("Error 300: Could not determine next activity.");
		}
		
		// display
		topTextView = (TextView) findViewById(R.id.messageView);
		topTextView.setText(content[0]);
		middleTextView = (TextView) findViewById(R.id.textView2);
		middleTextView.setText(content[1]);
		bottomTextView = (TextView) findViewById(R.id.textView3);
		bottomTextView.setText(content[2]);
	}

}
