package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuItem;
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
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));
        ab.setBackgroundDrawable(colorDrawable);
		setContentView(R.layout.activity_login);
        getActionBar().setTitle("SFS Mobile");  
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_calendar, menu);
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
