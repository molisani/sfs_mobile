package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AppointmentTextInputActivity extends AppointmentGeneralActivity {
	
	private TextView messageView;
	private EditText editField;
	private Button submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// initialize
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_text_input);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		messageView = (TextView) findViewById(R.id.messageView);
		String content;
		
		// content
		if (extras.getString(NEXT_ACTION).equals("reason")) {
			content = reasonMessage; 
		} else {
			System.out.println("Error 80: Can't determine what next activity is.");
			return;
		}
		
		// display
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
			if (currentStatus.equals(reasonMessage)) {
				intent = new Intent(context, AppointmentCalendarActivity.class);
				intent.putExtras(extras);
				intent.putExtra(NEXT_ACTION, "calendar");
				intent.putExtra(REASON, editField.getText().toString());
			} else {
				System.out.println("Error 81: Can't determine next activity.");
				return;
			}
			
			context.startActivity(intent);
		}
		
	}

}
