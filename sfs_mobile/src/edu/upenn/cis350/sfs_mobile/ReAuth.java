package edu.upenn.cis350.sfs_mobile;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Build;

public class ReAuth extends DialogFragment {

	public static ReAuth newInstance() {
        ReAuth frag = new ReAuth();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    final View view = inflater.inflate(R.layout.fragment_re_auth, null);
	    builder.setView(view);
	    // Add action buttons
	    builder.setView(view)
	           .setPositiveButton(R.string.reauth, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   ((MyAppointments) getActivity()).doPositiveClick(
	            			ReAuth.this.getDialog(),
	            			((EditText)view.findViewById(R.id.phoneNumberField)).getText().toString(),
	            			((EditText)view.findViewById(R.id.editText2)).getText().toString(),
	            	   		(Spinner) view.findViewById(R.id.spinneryear),
	   	   					(Spinner) view.findViewById(R.id.spinnermonth),
		   					(Spinner) view.findViewById(R.id.spinnerday));
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ((MyAppointments) getActivity()).doNegativeClick(ReAuth.this.getDialog());
	               }
	           });
	    return builder.create();  
	}

	

}
