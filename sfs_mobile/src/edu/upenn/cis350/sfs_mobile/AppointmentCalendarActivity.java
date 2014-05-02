package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;

public class AppointmentCalendarActivity extends AppointmentGeneralActivity {
	
	CalendarView calendar;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_appointment_calendar);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		calendar = (CalendarView) findViewById(R.id.calendarView1);
		button = (Button) findViewById(R.id.appointmentCheckButton);
		button.setOnClickListener(new OnSubmitListener(this, 
				extras));
	}
	
	/** 
	 * Implementation of OnClickListener that allows passing of variables.
	 * @author Tadas
	 *
	 */
	private class OnSubmitListener implements OnClickListener {

		Context context;
		Bundle extras;
		
		public OnSubmitListener(Context context, Bundle extras) {
			this.context = context;
			this.extras = extras;
		}
		
		@Override
		public void onClick(View v) {
			System.out.println("Date = " + calendar.getDate());
			Intent intent;
			intent = new Intent(context, AppointmentBookActivity.class);
			intent.putExtras(extras);
			intent.putExtra(DATE, calendar.getDate());
			context.startActivity(intent);
		}
		
	}

}
