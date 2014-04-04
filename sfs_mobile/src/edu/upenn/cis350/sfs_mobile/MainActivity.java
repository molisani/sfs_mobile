package edu.upenn.cis350.sfs_mobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	
	static final String APPOINTMENT_TYPE = "edu.upenn.cis350.sfs_mobile.APPOINTMENT_TYPE";
	ListView appointmentTypeListView;
	String[] appointmentTypes = {
			"Immunizations", "Health and Wellness", "Primary Care", 
			"Sports Medicine", "Women's Health"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appointmentTypeListView = (ListView) findViewById(R.id.appointmentTypeListView);
		ArrayAdapter atlAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,appointmentTypes);
		appointmentTypeListView.setAdapter(atlAdapter);
		appointmentTypeListView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
		String itemText = (String) ((TextView) view).getText();
		Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
		Intent intent = null;
		if (itemText.toLowerCase().equals("immunizations")) {
			intent = new Intent(this, DisplayImmunizations.class);
			intent.putExtra(APPOINTMENT_TYPE, itemText);
		}
		startActivity(intent);
	}

}
