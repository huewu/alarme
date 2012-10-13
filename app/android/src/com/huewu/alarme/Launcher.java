package com.huewu.alarme;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.huewu.alarme.controller.SelectMemberFragment;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.db.AlarmeProvider;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.AlarmeService;
import com.huewu.alarme.service.AlarmeService.LocalBinder;
import com.huewu.alarme.service.IAlarmService;
import com.huewu.alarme.util.Util;
import com.huewu.alarme.view.AlarmsListFragment;
import com.huewu.alarme.view.IAlarmeUIEvent;
import com.huewu.alarme.view.SetAlarmFragment;
import com.huewu.alarme.view.SyncFragment;
import com.huewu.alarme.view.WelcomeFragment;
import com.huewu.libs.network.JsonRequest;
import com.huewu.libs.network.ResponseListener;

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
		//setUpActionbar();
	}

	private void initAlarmeService() {
		bindService(new Intent(this, AlarmeService.class), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//set default menus. 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	};

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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.menu_alarm_list:
			showAlarmsListFragment();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void registerUser() {
		UserInfo user = AlarmePreference.getUser(this);
		if( user != null ){
			//user is already registered.
			return;
		}
		
		String accountName = Util.getCurrentUserAccount(this);
		String regId = GCMRegistrar.getRegistrationId(this);
		String cId = "1234";
		
		user = new UserInfo(accountName, regId, cId);
		mService.createUser(user, new ResponseListener<UserInfo>() {

			@Override
			public void onRequsetReady(JsonRequest<?> req) {
			}

			@Override
			public void onRequestRetrying(JsonRequest<?> req) {
			}

			@Override
			public void onRequestResponse(JsonRequest<?> req, UserInfo user) {
				AlarmePreference.setUser(Launcher.this, user);
			}

			@Override
			public void onRequestFinished(JsonRequest<?> req) {
			}

			@Override
			public void onRequestFailed(JsonRequest<?> req, Exception e) {
			}
		});
	}

	private void showSetAlarmFragment() {
		FragmentManager fm = getSupportFragmentManager();

		if(fm.findFragmentByTag("setting") == null){
		
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.workspace, new SetAlarmFragment(), "setting");
			ft.commit();
		}
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

		ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in, R.anim.slide_out);
		ft.replace(R.id.workspace, new AlarmsListFragment(), "alarms");
		ft.addToBackStack(null);
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
		mService.setAlaram(alarm, new ResponseListener<AlarmInfo>() {

			@Override
			public void onRequsetReady(JsonRequest<?> req) {
				Fragment f = getSupportFragmentManager().findFragmentByTag("loading");
				if (f == null) {
					SyncFragment frag = new SyncFragment("Connection to server...");
					frag.show(getSupportFragmentManager(), "loading");
				}
			}

			@Override
			public void onRequestRetrying(JsonRequest<?> req) {
			}

			@TargetApi(16)
			@Override
			public void onRequestResponse(JsonRequest<?> req, AlarmInfo alarm) {
				AlarmeProvider.addAlarm(alarm);
				
				//register alarm job.
				Intent i = new Intent(Launcher.this, RingAlarm.class);
				i.putExtra("aid", alarm.aid);
				
				AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				PendingIntent pi = PendingIntent.getActivity(Launcher.this, 0, 
						i, Intent.FLAG_ACTIVITY_NEW_TASK );
				am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), pi);
			}

			@Override
			public void onRequestFinished(JsonRequest<?> req) {
				SyncFragment f = (SyncFragment) getSupportFragmentManager().findFragmentByTag("loading");
				if(f != null)
					f.dismiss();
			}

			@Override
			public void onRequestFailed(JsonRequest<?> req, Exception e) {
				SyncFragment f = (SyncFragment) getSupportFragmentManager().findFragmentByTag("loading");
				if(f != null)
					f.dismiss();
				
				Toast.makeText(Launcher.this, "Fail to set an alarm: " + e.toString() , Toast.LENGTH_LONG);
			}
		});
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
	public void onSelectMember() {
		showSelectMemberFragment();
	}

	private void showSelectMemberFragment() {
		
		Fragment f = getSupportFragmentManager().findFragmentByTag("loading");
		if (f == null) {
			SelectMemberFragment frag = new SelectMemberFragment();
			//frag.show(getSupportFragmentManager(), "loading");
		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = ((LocalBinder)service).getService();	
		registerUser();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}

}//end of class
