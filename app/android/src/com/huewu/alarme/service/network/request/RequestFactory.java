package com.huewu.alarme.service.network.request;

import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.network.ResponseCallback;

/**
 * factory class in order to create various request.
 * @author huewu.yang
 */
public class RequestFactory {
	
	public static RegisterUserRequest createRegisterUserRequest(UserInfo user, ResponseCallback<UserInfo> callback){
		return null;
	}
	
	private RequestFactory(){}

}//end of class
