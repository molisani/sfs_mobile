package edu.upenn.cis350.sfs_mobile;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AppointmentBookActivity extends Activity implements OnItemClickListener {
	
	ListView listView;
	String username, session_id, date, dept, booking_id;
	AppointmentBookActivity recentActivity;
	LinkedList<Appointment> apptArr; // holds the most recent grabbed list of appointments

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		recentActivity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_book);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		System.out.println("bk dep: " + extras.getString(AppointmentListActivity.DEPARTMENT));
		System.out.println("bk date: " + extras.getLong(AppointmentListActivity.DATE));
		long dateLong = extras.getLong(AppointmentListActivity.DATE);
		
		date = convLongToDate(dateLong);
		username = extras.getString("Session_Username");
		session_id = extras.getInt("Session_ID") + "";
		dept = extras.getString(AppointmentListActivity.DEPARTMENT);
		
		// convert content to string array
		/*ArrayList<BasicNameValuePair> content = null; // set content (need to grab from php)
		listView = (ListView) findViewById(R.id.availableTimesListView);
		ArrayAdapter atlAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, content);
		listView.setAdapter(atlAdapter);
		listView.setOnItemClickListener(this);*/
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
								null);
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
			post.addField("subtype", "");
			post.addField("immunization", "");
			return post.execute();
		}
		
		protected void onPostExecute(JSONObject input) {
			try {
				if (input.getBoolean("success")) {
					Intent i = new Intent(recentActivity, HomeScreen.class);
					i.putExtra("Session_Username", recentActivity.getIntent().getExtras().getString("Session_Username"));
					i.putExtra("Session_ID", recentActivity.getIntent().getExtras().getInt("Session_ID"));
					Toast.makeText(recentActivity, "Booking successful", Toast.LENGTH_SHORT).show();
					startActivity(i);
					return;
				} else {
					System.out.println("Failed booking");
				}
			} catch (JSONException e) {
				System.out.println("Error: Failed booking.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appointment_book, menu);
		return true;
	}

}
