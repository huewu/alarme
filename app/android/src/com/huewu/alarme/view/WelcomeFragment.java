package com.huewu.alarme.view;

import com.huewu.alarme.R;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.util.Util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WelcomeFragment extends Fragment implements OnClickListener {

	protected static final String TAG = "WelcomeFragment";
	private IAlarmeUIEvent mEventListener = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.welcome_frag, null);
		//		TextView tv1 = (TextView) view.findViewById(R.id.username);
		//		tv1.setText("Welcome! " + Util.getCurrentUserAccount(getActivity()));
		//		
		//		TextView tv2  = (TextView) view.findViewById(R.id.desc);
		//		tv2.setText("some description will be shown here...");

		ImageButton btn = (ImageButton) view.findViewById(R.id.sync);
		btn.setOnClickListener(this);		

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mEventListener = (IAlarmeUIEvent) getActivity();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
	}
	
	public void discoverNfc(Intent intent) {
		mEventListener.onClockRegistered();
	}

}//end of class
