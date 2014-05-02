package edu.upenn.cis350.sfs_mobile;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.cis350.sfs_mobile.MessageDetail.PlaceholderFragment;
import android.app.Activity;
import android.app.ActionBar;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MessageReply extends Activity {
	private int id, apt;
	private String username = "";
	private String subj = "";
	private String sender = "";
	private Bundle extras = null;
	private MessageReply curr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		curr = this;
		setContentView(R.layout.activity_message_reply);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));
		getActionBar().setTitle("SFS Mobile");
		ab.setBackgroundDrawable(colorDrawable);
		getActionBar().setTitle("Reply");
		Intent i = getIntent();
		extras = i.getExtras();
		id = i.getExtras().getInt("id");
		apt = i.getExtras().getInt("apt");
		username = i.getExtras().getString("Session_Username");
		subj = i.getExtras().getString("subj");
		sender = i.getExtras().getString("sender");
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_reply, menu);
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
	
	public void submit(View v) {
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
			View rootView = inflater.inflate(R.layout.fragment_message_reply,
					container, false);
			return rootView;
		}
	}
	
	class BackgroundTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			ServerPOST postMsg = new ServerPOST("msgs.php");
			postMsg.addField("pennkey", username);
			postMsg.addField("auth_token", extras.getInt("Session_ID")+ "");
			postMsg.addField("send", "");
			postMsg.addField("appointment_id", apt+"");
			postMsg.addField("recipient", sender);
			postMsg.addField("subj", subj);
			EditText replyField = (EditText)findViewById(R.id.reply_body);
			String replyText = replyField.getText().toString();
			System.out.println("reply value: "+replyText);
			postMsg.addField("body", replyText);
			return postMsg.execute();
		}
		
		protected void onPostExecute(JSONObject input) {
			if (input != null) {
				try {
					if (input.getBoolean("success")) {
						Intent i = new Intent(curr, MyMessages.class);
				    	i.putExtras(extras);
						Toast.makeText(curr, "Message Sent",Toast.LENGTH_SHORT).show();
						startActivity(i);
						return;
					} else {
						Toast.makeText(curr, input.getString("message").toString(), Toast.LENGTH_LONG).show();
						//System.out.println("Failed to send message");
					}
				} catch (JSONException e) {
					System.out.println("Error: Failed to send message.");
					e.printStackTrace();
				}
			} else {
				DialogFragment newFragment = ReAuth.newInstance();
				newFragment.show(getFragmentManager(), "dialog");
			}
		}
	}
}
