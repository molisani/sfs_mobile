package edu.upenn.cis350.sfs_mobile;

import edu.upenn.cis350.sfs_mobile.HomeScreen;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
		// credential verification
		ServerPOST post = new ServerPost();
		// direct to homescreen if validated
		Intent i = new Intent(this, HomeScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("EXIT", true);
		startActivityForResult(i, 1);
    }
}
