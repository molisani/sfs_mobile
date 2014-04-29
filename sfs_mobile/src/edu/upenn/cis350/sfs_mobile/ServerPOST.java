package edu.upenn.cis350.sfs_mobile;

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

import org.json.*;

public class ServerPOST {
	private String url;
	private List<NameValuePair> query;
	private JSONObject jsobj;
	private String reauth = "Pennkey and Auth Token did not pass verification,"
			+ " may need to re-auth";


	public ServerPOST(String php) {
		url = "https://fling.seas.upenn.edu/~molisani/cgi-bin/" + php;
		query = new ArrayList<NameValuePair>();
	}
	
	public ServerPOST(String php, String... keyvals){
		ServerPOST(php);
		if (keyvals.length % 2 != 1) {
			for (int i = 0; i < keyvals.length; i += 2) {
				addField(keyvals[i], keyvals[i + 1]);
			}
		} 
	}

	public void addField(String key, String value) {
		query.add(new BasicNameValuePair(key, value));
	}

	public JSONObject execute() {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			
			post.setEntity(new UrlEncodedFormEntity(query));
			HttpResponse response = client.execute(post);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String json = "";
			for (String line = null; (line = reader.readLine()) != null;) json += line;
		    jsobj = new JSONObject(json);
			if (!jsobj.getBoolean("success") && jsobj.getString("message").equals(reauth)) {
				return null;
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
		return jsobj;
	}
}
