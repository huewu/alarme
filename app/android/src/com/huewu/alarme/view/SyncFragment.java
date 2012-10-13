package com.huewu.alarme.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SyncFragment extends DialogFragment {
	
	private String mMessage = null;
	
	public SyncFragment(){
	}
	
	public SyncFragment(String message){
		mMessage = message;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NORMAL, android.R.style.Holo_ButtonBar_AlertDialog);
		
		if(savedInstanceState != null)
			mMessage = savedInstanceState.getString("message");
	}	
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("message", mMessage);
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setIndeterminate(true);
		dialog.setTitle("Waiting...");
		dialog.setMessage(mMessage);
		dialog.setCancelable(false);
		return dialog;
	};
}//end of class
