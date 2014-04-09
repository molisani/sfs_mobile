package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_screen, menu);
	    ActionBar ab = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0A286E"));     
        ab.setBackgroundDrawable(colorDrawable);
        getActionBar().setTitle("Home");   
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.make_appts_option:
	            return true;
	        case R.id.my_appts_action:
	        	Intent intent = new Intent(this, MyAppointments.class);
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

}
