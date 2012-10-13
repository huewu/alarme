package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.network.request.CreateUserRequest;
import com.huewu.alarme.service.network.request.RequestFactory;
import com.huewu.libs.network.JsonNetworkWorker;
import com.huewu.libs.network.JsonRequest;
import com.huewu.libs.network.ResponseListener;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AlarmeService extends Service implements IAlarmService, OnAlarmGCMListener {
	
	private JsonNetworkWorker mNewtorkWorekr = null;
	
	private LocalBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {

		public IAlarmService getService() {
			return AlarmeService.this;
		}

	}// end of inner class.

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNewtorkWorekr = new JsonNetworkWorker(this);
	}
		
	@Override
	public void createUser(UserInfo user, ResponseListener callback) {
		//send message to clock server.
		//POST /user 
		//Request Body: JSON
		//{ username: mail_account, registerId: from_gcm, clockId: from_nfc }
		//Response Body: JSON
		//{ userId: unique_string }
		
		JsonRequest<?> req = RequestFactory.createCreateUserRequest( user, callback );
		mNewtorkWorekr.sendRequest(req);
	}

	@Override
	public void deleteUser(UserInfo user, ResponseListener callback) {
		//send message to clock server.
		//DELETE /user/:uid 
		//Response Body: null
		
		JsonRequest<?> req = RequestFactory.createDeleteUserRequest( user, callback );
		mNewtorkWorekr.sendRequest(req);
	}

	@Override
	public void setAlaram(AlarmInfo alarm, ResponseListener callback){
		//send message to clock server.
		//POST /user/:uid/alarm
		//Request Body: JSON
		//{ type: private, time: long?, status: on }
		//Response Body: JSON
		//{ alarmId: aid }

		JsonRequest<?> req = RequestFactory.createSetAlarmRequest( alarm, callback );
		mNewtorkWorekr.sendRequest(req);
	}

	@Override
	public void offAlaram( AlarmInfo alarm, ResponseListener callback){
		//update alarm status in clock server.
		//send message to clock server.
		//PUT /alarm/:aid
		//Request Body: JSON
		//{ alarmId: aid, status: off }
		
		JsonRequest<?> req = RequestFactory.createOffAlarmRequest( this, alarm, callback );
		mNewtorkWorekr.sendRequest(req);
	}

	@Override
	public void setGroupAlarm( AlarmInfo alarm, UserInfo[] members, ResponseListener callback){
		//create new group alarm to clock server.
		//POST /user/:uid/alarm
		//Request Body: JSON
		//{ type: group, time: long?, users: { { userId: me,  status: on}, { userId: friend, status: off }  } }
		//Response Body: JSON
		//{ alarmId: aid }
	}
	
	@Override
	public void updateGroupAlaram(AlarmInfo alarm, ResponseListener callback) {
		mNewtorkWorekr.sendRequest(null);
	}
	
	@Override
	public void onOffAlarm(AlarmInfo alarm) {
		//clock server request to set off alarm. (user turn off clock device)
	}

	@Override
	public void onUpdateAlarm(GroupAlarmInfo alarm) {
		//one of group user turn off or on alarm. (show message!) 
	}

	@Override
	public void onRequestSetAlarm(GroupAlarmInfo alarm) {
		//your friend request to accept set alarm.
	}
	
	@Override
	public AlarmInfo[] getAlarmInfoList(String cid) {
		//get alarm list. 
		return null;
	}

}//end of class
