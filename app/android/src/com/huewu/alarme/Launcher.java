package com.huewu.alarme;

import com.google.android.gcm.GCMRegistrar;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.util.Util;
import com.huewu.alarme.view.SettingFragment;
import com.huewu.alarme.view.WelcomeFragment;

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
		registerGCM();
		getActionBar().setSubtitle("Sync your alarm to alarme.");
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
		
		//for initial user. show welcome fragment.
		
		//for already registered user, show alarm fragment.
		
		UserInfo user = AlarmePreference.getCurrentUser();
		
		if( user == null )
			showWelcomeFragment();
		else
			showAlarmFragment();
		
	}

	private void showAlarmFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.workspace, new SettingFragment(), "setting");
		ft.commit();
	}

	private void showWelcomeFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.workspace, new WelcomeFragment(), "wlecome");
		ft.commit();
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
