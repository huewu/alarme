package com.huewu.alarme;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.view.AlarmFragment;
import com.huewu.alarme.view.IAlarmeUIEvent;
import com.huewu.alarme.view.SyncFragment;
import com.huewu.alarme.view.WelcomeFragment;

/**
 * 
 * @author huewu.yang
 * @date 
 *
 */
public class Launcher extends FragmentActivity implements IAlarmeUIEvent, TabListener {
	
	private static final String TAG = "Alarme Launcher";
	private static final String SENDER_ID = "293378875694";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		registerGCM();
		setUpActionbar();
	}

	private void setUpActionbar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
		
		Tab tab1 = actionBar.newTab()
	            .setText("Home").setTabListener(this);
		getActionBar().addTab(tab1);
		
		Tab tab2 = actionBar.newTab()
	            .setText("Alarms").setTabListener(this);
		getActionBar().addTab(tab2);
		
		Tab tab3 = actionBar.newTab()
	            .setText("Friends").setTabListener(this);
		getActionBar().addTab(tab3);
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
		
		UserInfo user = AlarmePreference.getUser(this);
		
//		if( user == null )
//			showWelcomeFragment();
//		else
			showAlarmFragment();
		
	}

	private void showAlarmFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.workspace, new AlarmFragment(), "setting");
		ft.commit();
	}

	private void showWelcomeFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.workspace, new WelcomeFragment(), "wlecome");
		ft.commit();
	}
	
	private void showAlarmsListFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.workspace, new WelcomeFragment(), "wlecome");
		ft.commit();
	}
	
	private void showFriendsListFragment() {
		
	}

	@Override
	public void onSyncToClock() {
		Fragment f = getSupportFragmentManager().findFragmentByTag("loading");
		if (f == null) {
			SyncFragment frag = new SyncFragment("Please tocuh to clock");
			frag.show(getSupportFragmentManager(), "loading");
		}
	}

	@Override
	public void onSetAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetGroupAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnOffAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateGroupAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		int pos = tab.getPosition();
		
		switch(pos){
		case 0:
			showAlarmFragment();
			break;
		case 1:
			showWelcomeFragment();
			break;
		case 2:
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
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
