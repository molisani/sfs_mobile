package edu.upenn.cis350.sfs_mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MyAppointments extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_appointments);

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
	class BackgroundTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... inputs) {
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("https://fling.seas.upenn.edu/~molisani/cgi-bin/appt.php");
		
		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair("pennkey", "alice"));
		        nameValuePairs.add(new BasicNameValuePair("auth_token", "1172611454"));
		        nameValuePairs.add(new BasicNameValuePair("get_my_appts", ""));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        System.out.println("hello " + httppost.toString());

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		        StringBuilder builder = new StringBuilder();
		        for (String line = null; (line = reader.readLine()) != null;) {
		            builder.append(line).append("\n");
		        }

		        JSONTokener tokener = new JSONTokener(builder.toString());
		        return tokener.toString();
		        //JSONArray finalResult = new JSONArray(tokener);
		        //return finalResult.toString();
		    } catch (ClientProtocolException e) {
		        System.out.println("Exception1--");
		    } catch (IOException e) {
		        System.out.println("Exception2--");
		    } /*catch (JSONException e) {
		        System.out.println("Exception3--");
				e.printStackTrace();
			} */
			return null;
		}
		protected void onPostExecute(String input) {
			TextView jsonTextView = (TextView) findViewById(R.id.my_appts_json);
	        jsonTextView.setText(input);
		}
	}


}
