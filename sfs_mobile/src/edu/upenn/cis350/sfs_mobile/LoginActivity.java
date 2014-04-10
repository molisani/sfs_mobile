package edu.upenn.cis350.sfs_mobile;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.cis350.sfs_mobile.HomeScreen;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText username = null;
	private EditText password = null;
	private EditText DOB = null;
	private Button login;
	int counter = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		username = (EditText)findViewById(R.id.editText1);
		password = (EditText)findViewById(R.id.editText2);
		DOB = (EditText)findViewById(R.id.editText3);
		login = (Button)findViewById(R.id.button1);
	}
	
	public void login (View v) {
		new BackgroundTask().execute();
    }
	
	class BackgroundTask extends AsyncTask<String, Void, JSONObject> {
		protected JSONObject doInBackground(String... inputs) {
			ServerPOST post = new ServerPOST("auth.php");
			post.addField("login", "");
			post.addField("pennkey", username.getText().toString());
			System.out.println("poop1 " + username.getText().toString());
			post.addField("password", password.getText().toString());
			System.out.println(password.getText().toString());
			post.addField("birthday", DOB.getText().toString());
			System.out.println(DOB.getText().toString());
			JSONObject json = post.execute();
			return json;
		}
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getBoolean("success")) {
					//direct to homescreen if validated
					Intent i = new Intent(LoginActivity.this, HomeScreen.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//i.putExtra("Session_ID", json.getInt("message"));
					startActivityForResult(i, 1);
				} else {
					Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
			}	
		}
	}

}
