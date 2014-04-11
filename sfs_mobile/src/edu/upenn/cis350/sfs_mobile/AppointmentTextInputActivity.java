package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AppointmentTextInputActivity extends Activity {
	
	private TextView messageView;
	private EditText editField;
	private String lastScreen, valueClicked;
	private Button submitButton;
	
	final static String callbackMessage = "Please enter a callback number where you can be reached in case it is " +
			"necessary to contact you concerning the appointment you are scheduling.";
	
	final static String visitReasonMessage = "Please enter the reason for your visit.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_text_input);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		// currently unnecessary
		/*lastScreen = extras.getString(AppointmentListActivity.LAST_SCREEN).toLowerCase();
		if (extras != null && extras.containsKey(AppointmentListActivity.VALUE_CLICKED))
			valueClicked = intent.getExtras().getString(AppointmentListActivity.VALUE_CLICKED);
			*/
		
		messageView = (TextView) findViewById(R.id.textView1);
		String content;
		if (intent.getStringExtra(AppointmentListActivity.NEXT_SCREEN).equals("callback")) {
			content = callbackMessage; 
		} else if (intent.getStringExtra(AppointmentListActivity.NEXT_SCREEN).equals("reason")) {
			content = visitReasonMessage;
		} else {
			System.out.println("Error 80: Can't determine what next activity is.");
			return;
		}
		messageView.setText(content);
		editField = (EditText) findViewById(R.id.editField);
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnSubmitListener(content, this, 
				extras));
		
	}
	
	/** 
	 * Implementation of OnClickListener that allows passing of variables.
	 * @author Tadas
	 *
	 */
	private class OnSubmitListener implements OnClickListener {

		String currentStatus;
		Context context;
		Bundle extras;
		
		public OnSubmitListener(String currentStatus, Context context, Bundle extras) {
			this.currentStatus = currentStatus;
			this.context = context;
			this.extras = extras;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent;
			if (currentStatus.equals(visitReasonMessage)) {
				intent = new Intent(context, AppointmentTextInputActivity.class);
				intent.putExtras(extras);
				intent.putExtra(AppointmentListActivity.NEXT_SCREEN, "callback");
			} else if (currentStatus.equals(callbackMessage)) {
				intent = new Intent(context, AppointmentCalendarActivity.class);
				intent.putExtras(extras);
			} else {
				System.out.println("Error 81: Can't determine next activity.");
				return;
			}
			
			context.startActivity(intent);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_text_input, menu);
		return true;
	}

}
