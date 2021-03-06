package edu.upenn.cis350.shs_mobile;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.upenn.cis350.shs_mobile.R;

public class MyAppointments extends Activity {
	private String username = "";
	private Bundle extras = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_appointments);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#0A286E"));
		getActionBar().setTitle("SHS Mobile");
		ab.setBackgroundDrawable(colorDrawable);
		getActionBar().setTitle("My Appointments");
		Intent i = getIntent();
		extras = i.getExtras();
		username = i.getExtras().getString("Session_Username");
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
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.make_appts_option:
			intent = new Intent(this, AppointmentListActivity.class);
			intent.putExtra(AppointmentGeneralActivity.NEXT_ACTION, "appointment_types");
			intent.putExtras(extras);
			startActivity(intent);
			return true;
		case R.id.home_action:
			intent = new Intent(this, HomeScreen.class);
			intent.putExtras(extras);
			startActivity(intent);
			return true;
		case R.id.messages_action:
			intent = new Intent(this, MyMessages.class);
        	intent.putExtras(extras);
        	startActivity(intent);
            return true;
		case R.id.logout:
			Bundle b = getIntent().getExtras();
			ServerPOSTLogout logout = new ServerPOSTLogout("auth.php",
					b.getString("Session_Username"), b.getInt("Session_ID")
							+ "");
			logout.execute();
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void refresh(View v) {
		(new BackgroundTask()).execute();
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
			post.addField("pennkey", username);
			post.addField("auth_token",
					extras.getInt("Session_ID")+ "");
			post.addField("get_my_appts", "");
			return post.execute();
		}

		protected void onPostExecute(JSONObject input) {
			if (input != null) {
				JSONArray arr = null;
				LinkedList<Appointment> apptArr = new LinkedList<Appointment>();
				try {
					arr = input.getJSONArray("appts");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject curr = (JSONObject) arr.get(i);
						System.out.println("appt " + curr);
						Appointment tempAppt = new Appointment(curr.getString(
								"immunization").toString(), curr.getString(
								"duration").toString(), new Timestamp(curr
								.getString("appt_time").toString()), curr
								.getString("appointment_id").toString(), curr
								.getString("department").toString(), curr
								.getString("subtype").toString(), //curr
								"",//.getString("callback").toString(), curr
								"");//.getString("reason"));
						apptArr.add(tempAppt);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String printAppts = "";
				String[] content = new String[apptArr.size()];
				for (int i = 0; i < apptArr.size(); i++) {
					content[i] = apptArr.get(i).toString();
				}
				ListView listView = (ListView) findViewById(R.id.my_appts_list);
				ArrayAdapter atlAdapter = new ArrayAdapter(MyAppointments.this,
						android.R.layout.simple_list_item_1, content);
				listView.setAdapter(atlAdapter);
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
			(new BackgroundTask()).execute();
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
