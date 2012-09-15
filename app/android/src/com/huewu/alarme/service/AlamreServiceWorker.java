package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlamreServiceWorker extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	//register GCM
	public void registerGCM( String regID ){
		//send message to clock server.
	}
	
	//unregister GCM
	public void unregisterGCM( String regID ){
		//send message to clock server.
		
	}
	
	//set alarm
	public void setAlaram( AlarmInfo alarm ){
		//send message to clock server or pusher?
	}
	
	//off
	public void offAlaram( AlarmInfo alarm ){
		//send message to clock server or pusher?
	}

}//end of class
