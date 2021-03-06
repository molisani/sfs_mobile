package edu.upenn.cis350.shs_mobile;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.cis350.shs_mobile.R;
import edu.upenn.cis350.shs_mobile.MyAppointments.BackgroundTask;
import edu.upenn.cis350.shs_mobile.MyAppointments.PlaceholderFragment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.os.Build;

public class MessageDetail extends Activity {
	private int id, apt;
	private String username = "";
	private Bundle extras = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detail);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));
		getActionBar().setTitle("SHS Mobile");
		ab.setBackgroundDrawable(colorDrawable);
		getActionBar().setTitle("Message");
		Intent i = getIntent();
		extras = i.getExtras();
		id = i.getExtras().getInt("id");
		apt = i.getExtras().getInt("apt");
		username = i.getExtras().getString("Session_Username");
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		(new BackgroundTask1()).execute();
		(new BackgroundTask2()).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_detail, menu);
		return true;
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
	        case R.id.home_action:
	        	intent = new Intent(this, HomeScreen.class);
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
	
	public void reply(View v) {
		Intent intent = new Intent(this, MessageReply.class);
		intent.putExtras(extras);
		startActivity(intent);
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
			View rootView = inflater.inflate(R.layout.fragment_message_detail,
					container, false);
			return rootView;
		}
	}
	

	class BackgroundTask1 extends AsyncTask<String, Void, JSONObject> {
		protected JSONObject doInBackground(String... inputs) {
			ServerPOST postMsg = new ServerPOST("msgs.php");
			postMsg.addField("pennkey", username);
			postMsg.addField("auth_token", extras.getInt("Session_ID")+ "");
			postMsg.addField("read", "");
			postMsg.addField("message_id", id+"");
			return postMsg.execute();
		}
		
		protected void onPostExecute(JSONObject input) {
			if (input != null) {
				String body = "";
				try {
					body = input.getString("body").toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				TextView tv = (TextView)findViewById(R.id.msg_body);
				tv.setText(body);
			} else {
				DialogFragment newFragment = ReAuth.newInstance();
				newFragment.show(getFragmentManager(), "dialog");
			}
		}
	}
	
	class BackgroundTask2 extends AsyncTask<String, Void, JSONObject> {
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
				String relatedApt = "";
				try {
					arr = input.getJSONArray("appts");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject curr = (JSONObject) arr.get(i);
						if (Integer.parseInt(curr.getString("appointment_id").toString()) == apt) {
							Appointment tempAppt = new Appointment(curr.getString(
									"immunization").toString(), curr.getString(
									"duration").toString(), new Timestamp(curr
									.getString("appt_time").toString()), curr
									.getString("appointment_id").toString(), curr
									.getString("department").toString(), curr
									.getString("subtype").toString(), //curr
									"",//.getString("callback").toString(), curr
									"");//.getString("reason"));
							relatedApt = tempAppt.toString();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				TextView tv = (TextView)findViewById(R.id.msg_apt);
				tv.setText(relatedApt);
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
			getIntent().getExtras().putInt("Session_ID", message);
			(new BackgroundTask1()).execute();
			(new BackgroundTask2()).execute();
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
