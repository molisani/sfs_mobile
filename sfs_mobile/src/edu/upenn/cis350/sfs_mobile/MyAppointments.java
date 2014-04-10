package edu.upenn.cis350.sfs_mobile;

import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAppointments extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_appointments);
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));     
        ab.setBackgroundDrawable(colorDrawable);
        getActionBar().setTitle("My Appointments");  
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		(new BackgroundTask()).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_appointments, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.make_appts_option:
	            return true;
	        case R.id.home_action:
	        	Intent intent = new Intent(this, HomeScreen.class);
	        	startActivity(intent);
	            return true;
	        case R.id.immun_actions:
	            return true;
	        case R.id.messages_action:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_appointments,
					container, false);
			return rootView;
		}
	}
	class BackgroundTask extends AsyncTask<String, Void, JSONObject> {
		protected JSONObject doInBackground(String... inputs) {
			ServerPOST post = new ServerPOST("appt.php");
			post.addField("pennkey", "alice");
			post.addField("auth_token", "173737141");
			post.addField("get_my_appts", "");
			return post.execute();
		}
		protected void onPostExecute(JSONObject input) {
			TextView jsonTextView = (TextView) findViewById(R.id.my_appts_json);
			JSONArray arr = null;
			LinkedList<Appointment> apptArr = new LinkedList<Appointment>();
			try {
				arr = input.getJSONArray("appts");
				for (int i = 0; i < arr.length(); i++) {
						JSONObject curr = (JSONObject) arr.get(i);
						Appointment tempAppt = new Appointment(
								curr.getString("immunization").toString(),
								curr.getString("duration").toString(),
								curr.getString("appt_time").toString(),
								curr.getString("appointment_id").toString(),
								curr.getString("department").toString(),
								curr.getString("subtype").toString());
						apptArr.add(tempAppt);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
			String printAppts = "";
			for (Appointment a : apptArr) {
				printAppts += a.toString() + "\n\n";
			}
			jsonTextView.setText(printAppts);

	
		}
	}


}
