package com.huewu.alarme.service.network.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.libs.network.ResponseDecoder;
import com.huewu.libs.network.ResponseListener;

/**
 * factory class in order to create various request.
 * @author huewu.yang
 */
public class RequestFactory {
	
	public static CreateUserRequest createCreateUserRequest(UserInfo user, ResponseListener listener){
		CreateUserRequest req = new CreateUserRequest(user);

		req.setResponseListener(listener);
		req.setDecoder(new UserInfoDecoder());
		
		return req;
	}
	
	public static DeleteUserRequest createDeleteUserRequest(UserInfo user, ResponseListener listener) {
		DeleteUserRequest req = new DeleteUserRequest(user);
		
		req.setResponseListener(listener);
		req.setDecoder(new UserInfoDecoder());
		
		return req;
	}
	
	public static SetAlarmRequest createSetAlarmRequest(AlarmInfo alarm, ResponseListener listener) {
		SetAlarmRequest req = new SetAlarmRequest(alarm);
		req.setResponseListener(listener);
		req.setDecoder(new AlarmInfoDecoder());
		
		return req;
	}
	
	public static OffAlarmRequest createOffAlarmRequest(Context ctx, AlarmInfo alarm,
			ResponseListener listener) {
		OffAlarmRequest req = new OffAlarmRequest(ctx, alarm);
		req.setResponseListener(listener);
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
