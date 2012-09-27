package com.huewu.alarme;

import com.google.android.gcm.GCMRegistrar;
import com.huewu.alarme.util.Util;
import com.huewu.alarme.view.SettingFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * 
 * @author huewu.yang
 * @date 
 *
 */
public class Launcher extends FragmentActivity {
	
	private static final String TAG = "Alarme Launcher";
	private static final String SENDER_ID = "293378875694";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.workspace, new SettingFragment(), "setting");
		ft.commit();
		
		registerGCM();
	}

	private void registerGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
		  GCMRegistrar.register(this, SENDER_ID);
		} else {
		  Log.v(TAG, "Already registered");
		}	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		String name = Util.getCurrentUserAccount(this);
		Log.v(TAG, "Hello! " + name);
	}

	//setting activity.
	
	//when touch NFC tag setting activity should be launched.

	//how it looks like.
	
	//clock.
	
	//TODO
	
	//nfc connectivity.
	
	//server connectivity.
	
	//set alarm time
	
	//show current alarm
	
	//snooze / dismiss
	
	//check my firends alarm / log

}//end of class
