package com.huewu.alarme.view;

import java.util.ArrayList;

import com.huewu.alarme.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;

public class SelectMemberFragment extends DialogFragment {
	
	private String mMessage = null;
	private IAlarmeUIEvent mEventListener = null;
	
	public SelectMemberFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NORMAL, android.R.style.Holo_ButtonBar_AlertDialog);
		
		if(savedInstanceState != null)
			mMessage = savedInstanceState.getString("message");
	}	
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		if( getActivity() instanceof IAlarmeUIEvent )
			mEventListener = (IAlarmeUIEvent) getActivity();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("message", mMessage);
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final String[] members = getActivity().getResources().getStringArray(R.array.friends);
		final SparseBooleanArray selectedMembers = new SparseBooleanArray();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Members");
		builder.setIcon(R.drawable.add_group);
		builder.setMultiChoiceItems(R.array.friends, null, new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				selectedMembers.put(which, isChecked);
			}
		});
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				ArrayList<String> result = new ArrayList<String>();
				for( int i = 0; i < members.length; ++ i ){
					if( selectedMembers.get(i) ) 
						result.add(members[i]);
				}
				mEventListener.onAddMembers(result);
			}
		});
		
//		ProgressDialog dialog = new Al(getActivity());
//		dialog.setIndeterminate(true);
//		dialog.setTitle("Waiting...");
//		dialog.setMessage(mMessage);
//		dialog.setCancelable(false);
		return builder.create();
	};	

}//end of class
