package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;

public class AppointmentCalendarActivity extends Activity {
	
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
			intent.putExtra(AppointmentListActivity.DATE, calendar.getDate());
			context.startActivity(intent);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_calendar, menu);
		return true;
	}

}
