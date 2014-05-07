package edu.upenn.cis350.shs_mobile;

import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.upenn.cis350.shs_mobile.R;

public class AppointmentConfirmActivity extends AppointmentGeneralActivity {
	
	private TextView textView;
	private Button continueButton, callButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_confirm); 
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		nextAction = extras.getString(NEXT_ACTION).toLowerCase();
		textView = (TextView) findViewById(R.id.messageView);
		continueButton = (Button) findViewById(R.id.continueButton);
		callButton = (Button) findViewById(R.id.callButton);
		
		
		if (nextAction.equals("confirm_nonemergency")) {
			textView.setText("If you require immediate medical attention, please call 215-746-3535 during " +
					"SHS operating hours and choose option 2 to book an appointment.");
			continueButton.setOnClickListener(new OnContinueListener(this, 
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

		Context context;
		Bundle extras;
		
		public OnContinueListener(Context context, Bundle extras) {
			this.context = context;
			this.extras = extras;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent;
			intent = new Intent(context, AppointmentTextInputActivity.class);
			intent.putExtras(extras);
			intent.putExtra(NEXT_ACTION, "reason");
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

}
