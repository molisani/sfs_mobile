package edu.upenn.cis350.sfs_mobile;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.cis350.sfs_mobile.HomeScreen;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText username = null;
	private EditText password = null;
	private EditText DOB = null;
	private Spinner year, month, day;
	private HashMap<String, String> monthint = new HashMap<String, String>();
	private Button login;
	int counter = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));
        ab.setBackgroundDrawable(colorDrawable);
		setContentView(R.layout.activity_login);
        getActionBar().setTitle("SFS Mobile");  
		username = (EditText)findViewById(R.id.pennkey);
		password = (EditText)findViewById(R.id.editText2);
		//DOB = (EditText)findViewById(R.id.editText3);
		year = (Spinner)findViewById(R.id.spinneryear);
		month = (Spinner)findViewById(R.id.spinnermonth);
		day = (Spinner)findViewById(R.id.spinnerday);
		login = (Button)findViewById(R.id.submitButton);
		monthint.put("January", "01");
		monthint.put("February", "02");
		monthint.put("March", "03");
		monthint.put("April", "04");
		monthint.put("May", "05");
		monthint.put("June", "06");
		monthint.put("July", "07");
		monthint.put("August", "08");
		monthint.put("September", "09");
		monthint.put("October", "10");
		monthint.put("November", "11");
		monthint.put("December", "12");
	}
	
	public void login (View v) {
		new BackgroundTask().execute();
    }
	
	class BackgroundTask extends AsyncTask<String, Void, JSONObject> {
		protected JSONObject doInBackground(String... inputs) {
			ServerPOST post = new ServerPOST("auth.php");
			post.addField("login", "");
			post.addField("pennkey", username.getText().toString());
			post.addField("password", password.getText().toString());
			post.addField("birthday", String.valueOf(year.getSelectedItem() + "-" + monthint.get(String.valueOf(month.getSelectedItem())) + "-" + String.valueOf(day.getSelectedItem())));
			JSONObject json = post.execute();
			try {
				if (json.getBoolean("success")) {
					GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					String registration_id = gcm.register(639566165629);
					ServerPOST regPost = new ServerPOST("auth.php", "register", "");
					regPost.addField("pennkey", username.getText().toString());
					regPost.addField("auth_token", json.getInt("message"));
					regPost.addField("registration_id", registration_id);
					regPost.execute();
				}
			} catch (JSONException e) {}
			return json;
		}
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getBoolean("success")) {
					Intent i = new Intent(LoginActivity.this, HomeScreen.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("Session_ID", json.getInt("message"));
					i.putExtra("Session_Username", username.getText().toString());
					startActivityForResult(i, 1);
				} else {
					Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				
			}	
		}
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

}
