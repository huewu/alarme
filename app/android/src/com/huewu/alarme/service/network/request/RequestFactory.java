package com.huewu.alarme.service.network.request;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.network.JsonRequest;
import com.huewu.alarme.service.network.ResponseCallback;
import com.huewu.alarme.service.network.ResponseDecoder;

/**
 * factory class in order to create various request.
 * @author huewu.yang
 */
public class RequestFactory {
	
	public static CreateUserRequest createCreateUserRequest(UserInfo user, ResponseCallback<UserInfo> callback){
		CreateUserRequest req = new CreateUserRequest(user);

		req.setCallback(callback);
		req.setDecoder(new UserInfoDecoder());
		
		return req;
	}
	
	public static DeleteUserRequest createDeleteUserRequest(UserInfo user, ResponseCallback<UserInfo> callback) {
		DeleteUserRequest req = new DeleteUserRequest(user);
		
		req.setCallback(callback);
		req.setDecoder(new UserInfoDecoder());
		
		return req;
	}
	
	public static SetAlarmRequest createSetAlarmRequest(AlarmInfo alarm, ResponseCallback<AlarmInfo> callback) {
		SetAlarmRequest req = new SetAlarmRequest(alarm);
		req.setCallback(callback);
		req.setDecoder(new AlarmInfoDecoder());
		
		return req;
	}
	
	public static OffAlarmRequest createOffAlarmRequest(AlarmInfo alarm,
			ResponseCallback<AlarmInfo> callback) {
		OffAlarmRequest req = new OffAlarmRequest(alarm);
		req.setCallback(callback);
		req.setDecoder(new AlarmInfoDecoder());
		return req;
	}
	
	private static class UserInfoDecoder implements ResponseDecoder<UserInfo>{
		
		private Gson gson = new Gson();

		@Override
		public UserInfo decode(JsonReader reader) {
			return gson.fromJson(reader, UserInfo.class);
		}
	}
	
	private static class AlarmInfoDecoder implements ResponseDecoder<AlarmInfo>{
		
		private Gson gson = new Gson();

		@Override
		public AlarmInfo decode(JsonReader reader) {
			return gson.fromJson(reader, AlarmInfo.class);
		}
	}
	
	private RequestFactory(){}

}//end of class
