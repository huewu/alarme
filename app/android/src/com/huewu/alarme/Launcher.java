package com.huewu.alarme;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.AlarmeService;
import com.huewu.alarme.service.AlarmeService.LocalBinder;
import com.huewu.alarme.service.IAlarmService;
import com.huewu.alarme.util.Util;
import com.huewu.alarme.view.AlarmsListFragment;
import com.huewu.alarme.view.SetAlarmFragment;
import com.huewu.alarme.view.IAlarmeUIEvent;
import com.huewu.alarme.view.SyncFragment;
import com.huewu.alarme.view.WelcomeFragment;

/**
 * 
 * @author huewu.yang
 * @date 
 *
 */
public class Launcher extends FragmentActivity implements IAlarmeUIEvent, TabListener, ServiceConnection {
	
	private static final String TAG = "Alarme Launcher";
	private static final String SENDER_ID = "293378875694";
	
	private IAlarmService mService = null;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		initAlarmeService();
		
		registerGCM();
		registerUser();
		setUpActionbar();
	}

	private void initAlarmeService() {
		bindService(new Intent(this, AlarmeService.class), this, Context.BIND_AUTO_CREATE);
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
		showSetAlarmFragment();
	}

	private void registerUser() {
		UserInfo user = AlarmePreference.getUser(this);
		if( user != null )
			return;
		
		String accountName = Util.getCurrentUserAccount(this);
		String regId = GCMRegistrar.getRegistrationId(this);
		String cId = "1234";
		
		user = new UserInfo(accountName, regId, cId);
		mService.createUser(user, null);
		
		AlarmePreference.setUser(this, user);
	}

	private void showSetAlarmFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.workspace, new SetAlarmFragment(), "setting");
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
		ft.replace(R.id.workspace, new AlarmsListFragment(), "wlecome");
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
	public void onSetAlarm( AlarmInfo alarm ) {
		Log.v(TAG, "Set Alarm: " + alarm.toPostData());
	}

	@Override
	public void onSetGroupAlarm() {
	}

	@Override
	public void onTurnOffAlarm() {
	}

	@Override
	public void onUpdateGroupAlarm() {
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		int pos = tab.getPosition();
		
		switch(pos){
		case 0:
			showSetAlarmFragment();
			break;
		case 1:
			showAlarmsListFragment();
			break;
		case 2:
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = ((LocalBinder)service).getService();	
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}

}//end of class
