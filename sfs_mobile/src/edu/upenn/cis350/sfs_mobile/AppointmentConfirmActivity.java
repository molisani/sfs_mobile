package edu.upenn.cis350.sfs_mobile;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AppointmentConfirmActivity extends Activity {
	
	private TextView textView;
	private String lastScreen, valueClicked;
	private Button continueButton, callButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("launching confirm");
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));
        ab.setBackgroundDrawable(colorDrawable);
		setContentView(R.layout.activity_appointment_confirm);
		getActionBar().setTitle("SFS Mobile");  
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		lastScreen = extras.getString(AppointmentListActivity.LAST_SCREEN).toLowerCase();
		if (extras != null && extras.containsKey(AppointmentListActivity.VALUE_CLICKED))
			valueClicked = intent.getExtras().getString(AppointmentListActivity.VALUE_CLICKED);
		textView = (TextView) findViewById(R.id.messageView);
		continueButton = (Button) findViewById(R.id.continueButton);
		callButton = (Button) findViewById(R.id.callButton);
		if (lastScreen.equals("appointment_types")) {
			textView.setText("If you require immediate medical attention, please call 215-746-3535 during " +
					"SHS operating hours and choose option 2 to book an appointment.");
			continueButton.setOnClickListener(new OnContinueListener(valueClicked, this, 
					extras));
			
			callButton.setOnClickListener(new OnCallListener());
		} 
	}
	
	/** 
	 * Implementation of OnClickListener that allows passing of variables.
	 * @author Tadas
	 *
	 */
	private class OnContinueListener implements OnClickListener {

		String currentStatus;
		Context context;
		Bundle extras;
		
		public OnContinueListener(String currentStatus, Context context, Bundle extras) {
			this.currentStatus = currentStatus;
			this.context = context;
			this.extras = extras;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent;
			if (currentStatus.equals(AppointmentListActivity.appointmentTypes[2]) ||
					currentStatus.equals(AppointmentListActivity.appointmentTypes[3])) { // primary care
				intent = new Intent(context, AppointmentTextInputActivity.class);
				intent.putExtras(extras);
				intent.putExtra(AppointmentListActivity.NEXT_SCREEN, "reason");
			} else {
				System.out.println("Error 81: Can't determine next activity.");
				return;
			}
			context.startActivity(intent);
		}
		
	}
	
	/** 
	 * Implementation of OnClickListener that opens the dialer and populates the phone number with SHS's
	 * @author Tadas
	 *
	 */
	private class OnCallListener implements OnClickListener {
		
		public OnCallListener() {
			
		}
		
		@Override
		public void onClick(View v) {
			Intent intent;
			intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:215-746-3535"));
			startActivity(intent);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_confirm, menu);
		return true;
	}

}
