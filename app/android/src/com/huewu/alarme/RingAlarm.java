package com.huewu.alarme;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gcm.GCMBaseIntentService;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.db.AlarmeProvider;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.service.AlarmeService;
import com.huewu.alarme.service.AlarmeService.LocalBinder;
import com.huewu.alarme.service.IAlarmService;

public class RingAlarm extends Activity implements ServiceConnection {
	
	private Button mAlarmOff = null;
	private IAlarmService mService = null;
	private Ringtone mRingtone = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ring_alarm);
		
		//should connect to a alarmeservice...
		initAlarmeService();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent i = getIntent();
		final String aid = i.getStringExtra("aid");
		
		mAlarmOff = (Button) findViewById(R.id.off_alarm);
		mAlarmOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//send update alarm msg to server.
				AlarmInfo alarm = AlarmeProvider.findAlarm(aid);
				mService.offAlaram(alarm, null);
				mute();
			}
		});
		
		ring();
	}
	
	
	private void ring() {
		
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	     if(alert == null){
	         // alert is null, using backup
	         alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	         if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
	             // alert backup is null, using 2nd backup
	             alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
	         }
	     }
	     
	     mRingtone = RingtoneManager.getRingtone(getApplicationContext(), alert);
	     mRingtone.play();	     
	}
	
	private void mute(){
		if(mRingtone != null && mRingtone.isPlaying()){
			mRingtone.stop();
			mRingtone = null;
		}
	}

	private void initAlarmeService() {
		bindService(new Intent(this, AlarmeService.class), this, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mute();
	};


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = ((LocalBinder)service).getService();	
	}


	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}


}//end of class
