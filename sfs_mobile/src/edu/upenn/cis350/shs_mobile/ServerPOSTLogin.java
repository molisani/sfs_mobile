package edu.upenn.cis350.shs_mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Spinner;

public class ServerPOSTLogin {
	private String url;
	private List<NameValuePair> query;
	private JSONObject jsobj;
	private HashMap<String, String> monthint = new HashMap<String, String>();
	private int message;


	public ServerPOSTLogin(String username, String password, Spinner year,
			Spinner month, Spinner day) {
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
		url = "https://fling.seas.upenn.edu/~molisani/cgi-bin/auth.php";
		query = new ArrayList<NameValuePair>();
    	addField("login", "");
    	addField("pennkey", username);
    	addField("password", password);
		addField("birthday", String.valueOf(year.getSelectedItem()) + "-" +
				monthint.get(String.valueOf(month.getSelectedItem())) + "-" +
				String.valueOf(day.getSelectedItem()));
	}

	private void addField(String key, String value) {
		query.add(new BasicNameValuePair(key, value));
	}

	public void execute() {
		(new BackgroundTask()).execute();
	}
	
	public int getMessage() {
		while (message == 0) {}
		return message;
	}
	
	class BackgroundTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... inputs) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			try {
				post.setEntity(new UrlEncodedFormEntity(query));
				HttpResponse response = client.execute(post);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String json = "";
				for (String line = null; (line = reader.readLine()) != null;) json += line;
			    jsobj = new JSONObject(json);
			    if (jsobj.getBoolean("success")) {
					message = jsobj.getInt("message");
				} else {
					message = -1;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}

