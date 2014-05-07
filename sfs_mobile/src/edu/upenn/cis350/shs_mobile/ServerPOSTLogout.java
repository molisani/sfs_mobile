package edu.upenn.cis350.shs_mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ServerPOSTLogout {
	private String url;
	private List<NameValuePair> query;
	private JSONObject jsobj;


	public ServerPOSTLogout(String php, String username, String id) {
		System.out.println("Username1: " + username + " ID: " + id);
		url = "https://fling.seas.upenn.edu/~molisani/cgi-bin/" + php;
		query = new ArrayList<NameValuePair>();
    	addField("logout", "");
    	addField("pennkey", username);
    	addField("auth_token", id);
    	execute();
	}

	private void addField(String key, String value) {
		query.add(new BasicNameValuePair(key, value));
	}

	public void execute() {
		(new BackgroundTask()).execute();
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
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println("returning " + jsobj.toString());
			return null;
		}
	}
}
