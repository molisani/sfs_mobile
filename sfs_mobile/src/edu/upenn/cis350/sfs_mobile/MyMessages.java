package edu.upenn.cis350.sfs_mobile;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.cis350.sfs_mobile.MyAppointments.BackgroundTask;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyMessages extends Activity {
	private int id = 0;
	private String username = "";
	private Bundle extras = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_messages);
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E")); 
        getActionBar().setTitle("SFS Mobile");  
        ab.setBackgroundDrawable(colorDrawable);
        getActionBar().setTitle("My Messages");  
        Intent i = getIntent();
        extras = i.getExtras();
	    id = i.getExtras().getInt("Session_ID");
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
		getMenuInflater().inflate(R.menu.my_messages, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent intent = null;
	    switch (item.getItemId()) {
	        case R.id.make_appts_option:
	        	intent = new Intent(this, AppointmentListActivity.class);
	        	intent.putExtra("edu.upenn.cis350.sfs_mobile.LAST_SCREEN", "make_appointment");
	        	intent.putExtras(extras);
	        	startActivity(intent);
	            return true;
	        case R.id.home_action:
	        	intent = new Intent(this, HomeScreen.class);
	        	intent.putExtras(extras);
	        	startActivity(intent);
	            return true;
	        case R.id.messages_action:
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
	
	public void refresh(View v) {
		(new BackgroundTask()).execute();
	}
	
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_messages,
					container, false);
			return rootView;
		}
	}
	
	class BackgroundTask extends AsyncTask<String, Void, JSONObject> {
		protected JSONObject doInBackground(String... inputs) {
			ServerPOST post = new ServerPOST("msgs.php");
			post.addField("pennkey", username);
			post.addField("auth_token", extras.getInt("Session_ID")+ "");
			post.addField("list", "");
			return post.execute();
		}
		
		protected void onPostExecute(JSONObject input) {
			if (input != null) {
				JSONArray arr = null;
				final LinkedList<Message> msgArr = new LinkedList<Message>();
				try {
					arr = input.getJSONArray("msgs");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject curr = (JSONObject) arr.get(i);
						Message tempMsg = new Message(
								curr.getString("message_id").toString(),
								curr.getString("appointment_id").toString(),
								new Timestamp(curr.getString("sent")),
								curr.getString("sender").toString(),
								curr.getString("fullname").toString(),
								curr.getString("subj").toString(),
								curr.getInt("read"));
						msgArr.add(tempMsg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String printMsgs = "";
				String[] content = new String[msgArr.size()];
				for (int i = 0; i < msgArr.size(); i++) {
					content[i] = msgArr.get(i).toString();
				}
				final ListView listView = (ListView) findViewById(R.id.my_msgs_list);
				ArrayAdapter atlAdapter = new ArrayAdapter(MyMessages.this,
						android.R.layout.simple_list_item_1, content);
				listView.setAdapter(atlAdapter);
				
				listView.setClickable(true);
				listView.setOnItemClickListener(new OnItemClickListener() {					
					public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					     openMsg(msgArr.get(position).getId(), msgArr.get(position).getApt());				         
					}
				});
			} else {
				DialogFragment newFragment = ReAuth.newInstance();
				newFragment.show(getFragmentManager(), "dialog");
			}
		}
	}
	
	public void openMsg(int id, int apt) {
<<<<<<< HEAD
		Intent intent=new Intent(this, MessageDetail.class);
		extras.putInt("id", id);
		extras.putInt("apt", apt);
	    intent.putExtras(extras);
=======
		Intent intent=new Intent(this,MessageDetail.class);
	    intent.putExtra("id", id);
	    intent.putExtra("apt", apt);
	    intent.putExtras(getIntent().getExtras());
>>>>>>> df0aeb012752aa22cc742893abcc9ce43186a781
	    startActivity(intent);
	}
	
	public void doPositiveClick(Dialog dialog, String user, String pass,
			Spinner y, Spinner m, Spinner d ) {
		ServerPOSTLogin post = new ServerPOSTLogin(user, pass, y, m, d);
		int message;
		post.execute();
		if ((message = post.getMessage()) != -1) {
			extras.remove("Session_ID");
			extras.putInt("Session_ID", message);
			id = message;
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
