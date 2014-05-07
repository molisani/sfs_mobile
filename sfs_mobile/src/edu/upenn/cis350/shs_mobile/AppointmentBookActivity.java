package edu.upenn.cis350.shs_mobile;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.upenn.cis350.shs_mobile.R;

public class AppointmentBookActivity extends AppointmentGeneralActivity implements OnItemClickListener {
	
	ListView listView;
	String username, session_id, date, dept, booking_id, callback, reason;
	AppointmentBookActivity recentActivity;
	LinkedList<Appointment> apptArr; // holds the most recent grabbed list of appointments
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		recentActivity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_book);
		Intent intent = getIntent();
		extras = intent.getExtras();
		System.out.println("bk dep: " + extras.getString(DEPARTMENT));
		System.out.println("bk date: " + extras.getLong(DATE));
		long dateLong = extras.getLong(DATE);
		
		date = convLongToDate(dateLong);
		username = extras.getString("Session_Username");
		session_id = extras.getInt("Session_ID") + "";
		dept = extras.getString(DEPARTMENT);
		callback = extras.getString(CALLBACK);
		reason = extras.getString(REASON);
		
		(new GrabbingTask()).execute();
	}
	
	class GrabbingTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			ServerPOST post = new ServerPOST("appt.php");
			post.addField("pennkey", username);
			post.addField("auth_token", session_id);
			post.addField("get_appts", "");
			post.addField("date", date);
			post.addField("department", dept);
			return post.execute();
		}
		
		protected void onPostExecute(JSONObject input) {
			if (input != null) {
			JSONArray arr = null;
			apptArr = new LinkedList<Appointment>();
			try {
				arr = input.getJSONArray("appts");
				for (int i = 0; i < arr.length(); i++) {
						JSONObject curr = (JSONObject) arr.get(i);
						
						Appointment tempAppt = new Appointment( 
								null, 
								curr.getString("duration").toString(),
								new Timestamp(curr.getString("appt_time").toString()),
								curr.getString("appointment_id").toString(),
								dept,
								null,
								null,
								null
								);
						apptArr.add(tempAppt);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
				
			listView = (ListView) findViewById(R.id.availableTimesListView);
			ArrayAdapter atlAdapter = new ArrayAdapter(AppointmentBookActivity.this, 
					android.R.layout.simple_list_item_1, apptArr);
			listView.setAdapter(atlAdapter);
			listView.setOnItemClickListener(recentActivity);
		} else {
			DialogFragment newFragment = ReAuth.newInstance();
			newFragment.show(getFragmentManager(), "dialog");
		}
	}
	}
	
	private String convLongToDate(long dateLong) {
		Date date = new Date(dateLong);
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date).toString();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
		System.out.println("BOOKING: " + apptArr.get(i).toString());
		booking_id = String.valueOf(apptArr.get(i).getId());
		(new SettingTask()).execute();
	}
	
	class SettingTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			ServerPOST post = new ServerPOST("appt.php");
			post.addField("pennkey", username);
			post.addField("auth_token", session_id);
			post.addField("set_appt", "");
			post.addField("appointment_id", booking_id);
			post.addField("patient", username);
			post.addField("subtype", extras.containsKey(SUBTYPE) ? 
					extras.getString(SUBTYPE) : null);
			post.addField("immunization", extras.containsKey(IMMUNIZATION) ? 
					extras.getString(IMMUNIZATION) : null);
			post.addField("callback_num", extras.containsKey(CALLBACK) ? 
					extras.getString(CALLBACK) : null);
			post.addField("reason", extras.containsKey(REASON) ? 
					extras.getString(REASON) : null);
			System.out.println("Made Appt: immun = " + extras.getString(IMMUNIZATION) + "; subtype = " + extras.getString(SUBTYPE) +
					"; id = " + booking_id + 
					"; callback = " + extras.getString(CALLBACK) + "; reason = " 
					+ extras.getString(REASON));
			return post.execute();
		}
		
		protected void onPostExecute(JSONObject input) {
			if (input != null) {
				try {
					if (input.getBoolean("success")) {
						Intent i = new Intent(recentActivity, HomeScreen.class);
						i.putExtra("Session_Username", recentActivity.getIntent().getExtras().getString("Session_Username"));
						i.putExtra("Session_ID", recentActivity.getIntent().getExtras().getInt("Session_ID"));
						Toast.makeText(recentActivity, "Booking successful",Toast.LENGTH_SHORT).show();
						startActivity(i);
						return;
					} else {
						System.out.println("Failed booking");
					}
				} catch (JSONException e) {
					System.out.println("Error: Failed booking.");
					e.printStackTrace();
				}
			} else {
				DialogFragment newFragment = ReAuth.newInstance();
				newFragment.show(getFragmentManager(), "dialog");
			}
		}
	}
	
	public void doPositiveClick(Dialog dialog, String user, String pass,
			Spinner y, Spinner m, Spinner d ) {
		ServerPOSTLogin post = new ServerPOSTLogin(user, pass, y, m, d);
		int message;
		post.execute();
		if ((message = post.getMessage()) != -1) {
			extras.remove("Session_ID");
			extras.putInt("Session_ID", message);
			recentActivity.getIntent().getExtras().putInt("Session_ID", message);
			session_id = message + "";
			(new GrabbingTask()).execute();
		} else {
			Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
			doNegativeClick(dialog);
		}
	}

	public void doNegativeClick(Dialog dialog) {
	    dialog.dismiss();
	    super.onBackPressed();
	}

}
