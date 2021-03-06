package edu.upenn.cis350.shs_mobile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import edu.upenn.cis350.shs_mobile.R;

public class HomeScreen extends Activity {
	private Bundle extras = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_screen, menu);
	    ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));     
        ab.setBackgroundDrawable(colorDrawable);
        getActionBar().setTitle("SHS Mobile");  
	    Intent i = getIntent();
	    extras = i.getExtras();
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.make_appts_option:
	        	Intent intent = new Intent(this, AppointmentListActivity.class);
	        	intent.putExtra(AppointmentGeneralActivity.NEXT_ACTION, "appointment_types");
	        	intent.putExtras(extras);
	        	startActivity(intent);
	            return true;
	        case R.id.my_appts_action:
	        	intent = new Intent(this, MyAppointments.class);
	        	intent.putExtras(extras);
	        	startActivity(intent);
	            return true;
	        case R.id.messages_action:
	        	intent = new Intent(this, MyMessages.class);
	        	intent.putExtras(extras);
	        	startActivity(intent);
	            return true;
	        case R.id.logout:
				ServerPOSTLogout logout = new ServerPOSTLogout("auth.php",
						extras.getString("Session_Username"), extras.getInt("Session_ID") + "");
	        	logout.execute();
	        	intent = new Intent(this, LoginActivity.class);
	        	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


}
