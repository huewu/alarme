package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.network.JsonNetworkWorker;
import com.huewu.alarme.service.network.JsonRequest;
import com.huewu.alarme.service.network.ResponseCallback;
import com.huewu.alarme.service.network.request.RegisterUserRequest;
import com.huewu.alarme.service.network.request.RequestFactory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlamreServiceWorker extends Service implements IAlarmService, OnAlarmGCMListener {
	
	private JsonNetworkWorker mNewtorkWorekr = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNewtorkWorekr = new JsonNetworkWorker(this);
	}
		
	@Override
	public void createUser(UserInfo user) {
		//send message to clock server.
		//POST /user 
		//Request Body: JSON
		//{ username: mail_account, registerId: from_gcm, clockId: from_nfc }
		//Response Body: JSON
		//{ userId: unique_string }
		
		ResponseCallback<UserInfo> callback = new ResponseCallback<UserInfo>() {
			
			@Override
			public void onResponse(JsonRequest<UserInfo> req, UserInfo resp) {
			}
			
			@Override
			public void onFinsh(JsonRequest<UserInfo> req) {
				//check userId.
				//save it to preference.
			}
			
			@Override
			public void onError(JsonRequest<UserInfo> req, Exception exception) {
			}
			
			@Override
			public void onBeforeRequest(JsonRequest<UserInfo> req) {
			}
		};
		
		RegisterUserRequest req = RequestFactory.createRegisterUserRequest( user, callback );
		mNewtorkWorekr.sendRequest(req);
	}

	@Override
	public void deleteUser(UserInfo user) {
		//send message to clock server.
		//DELETE /user/:uid 
		//Request Body: JSON
		//{ userId: userId }
		//Response Body: null
		
		mNewtorkWorekr.sendRequest(null);
	}

	@Override
	public void setAlaram( AlarmInfo alarm ){
		//send message to clock server.
		//POST /user/:uid/alarm
		//Request Body: JSON
		//{ type: private, time: long?, status: on }
		//Response Body: JSON
		//{ alarmId: aid }
		
		mNewtorkWorekr.sendRequest(null);
	}

	@Override
	public void offAlaram( AlarmInfo alarm ){
		//update alarm status in clock server.
		//send message to clock server.
		//PUT /alarm/:aid
		//Request Body: JSON
		//{ alarmId: aid, status: off }
	}

	@Override
	public void setGroupAlarm( AlarmInfo alarm ){
		//create new group alarm to clock server.
		//POST /user/:uid/alarm
		//Request Body: JSON
		//{ type: group, time: long?, users: { { userId: me,  status: on}, { userId: friend, status: off }  } }
		//Response Body: JSON
		//{ alarmId: aid }
	}
	
	@Override
	public void updateGroupAlaram(AlarmInfo alarm) {
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
