package com.huewu.alarme;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
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
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.db.AlarmeProvider;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.AlarmeService;
import com.huewu.alarme.service.AlarmeService.LocalBinder;
import com.huewu.alarme.service.IAlarmService;
import com.huewu.alarme.util.Util;
import com.huewu.alarme.view.AlarmsListFragment;
import com.huewu.alarme.view.IAlarmeUIEvent;
import com.huewu.alarme.view.SelectMemberFragment;
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
public class Launcher extends FragmentActivity implements IAlarmeUIEvent, ServiceConnection {

	private static final String TAG = "Alarme Launcher";
	private static final String SENDER_ID = "293378875694";
	
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;

	private IAlarmService mService = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		initAlarmeService();
		registerGCM();
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case R.id.menu_alarm_list:
			showAlarmsListFragment();
			break;
		case R.id.menu_reset:
			AlarmePreference.setUser(this, null);
			finish();
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
				showSetAlarmFragment();
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
			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in, R.anim.slide_out);
			ft.replace(R.id.workspace, new SetAlarmFragment(), "setting");
			ft.commit();
		}
	}

	private void showWelcomeFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.workspace, new WelcomeFragment(), "wlecome");
		ft.commit();
		
		initNFC();
	}
	
	private void initNFC() {
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Setup intent filters for all types of NFC dispatches to intercept all discovered tags
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}

		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		tech.addDataScheme("vnd.android.nfc"); 
		mFilters = new IntentFilter[] {
				ndef,
				tech,
				new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
		};		
		
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, null);
	}
	
	private void releaseNFC() {
		if(mAdapter != null)
			mAdapter.disableForegroundDispatch(this);
	}
    
    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent.getExtras());
        
		FragmentManager fm = getSupportFragmentManager();
		WelcomeFragment fragment = (WelcomeFragment) fm.findFragmentByTag("wlecome");
		
		if(fragment != null)
			fragment.discoverNfc(intent);
//    	}
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
	public void onClockRegistered() {
		//releaseNFC();
		registerUser();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		releaseNFC();
	}

	@Override
	public void onSetAlarm( AlarmInfo alarm ) {
		if(alarm.type.equals(AlarmInfo.PRIVATE_ALARM))
			setPrivateAlamr(alarm);
		else if(alarm.type.equals(AlarmInfo.GROUP_ALARM))
			setGroupAlarm(alarm);
	}

	private void setGroupAlarm(AlarmInfo alarm) {
		
		mService.setAlarm(alarm, new ResponseListener<AlarmInfo>() {

			@Override
			public void onRequsetReady(JsonRequest<?> req) {
			}

			@Override
			public void onRequestRetrying(JsonRequest<?> req) {
			}

			@Override
			public void onRequestResponse(JsonRequest<?> req, AlarmInfo alarm) {
				AlarmeProvider.addAlarm(alarm);
			}

			@Override
			public void onRequestFinished(JsonRequest<?> req) {
			}

			@Override
			public void onRequestFailed(JsonRequest<?> req, Exception e) {
			}
		});
	}

	private void setPrivateAlamr(AlarmInfo alarm) {
		mService.setAlarm(alarm, new ResponseListener<AlarmInfo>() {

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
				registerAlarm(alarm);
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

				Toast.makeText(Launcher.this, "Fail to set an alarm: " + e.toString() , Toast.LENGTH_LONG).show();
			}
		});	
	}
	
	private void registerAlarm(AlarmInfo alarm){

		//register alarm job.
		Intent i = new Intent(Launcher.this, RingAlarm.class);
		i.putExtra("aid", alarm.aid);

		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent pi = PendingIntent.getActivity(Launcher.this, 0, 
				i, Intent.FLAG_ACTIVITY_NEW_TASK );
		am.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), pi);
	}

	@Override
	public void onTurnOffAlarm() {
	}

	@Override
	public void onUpdateGroupAlarm() {
	}

	@Override
	public void onSelectMember() {
		showSelectMemberFragment();
	}

	private void showSelectMemberFragment() {

		Fragment f = getSupportFragmentManager().findFragmentByTag("loading");
		if (f == null) {
			SelectMemberFragment frag = new SelectMemberFragment();
			frag.show(getSupportFragmentManager(), "add friends");
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = ((LocalBinder)service).getService();
		
		if(AlarmePreference.getUser(this) == null){
			showWelcomeFragment();
		}else{
			showSetAlarmFragment();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}

	@Override
	public void onAddMembers(ArrayList<String> members) {
		SetAlarmFragment frag = (SetAlarmFragment) getSupportFragmentManager().findFragmentByTag("setting");
		if(frag != null)
			frag.updateMembers( members );
	}

}//end of class
