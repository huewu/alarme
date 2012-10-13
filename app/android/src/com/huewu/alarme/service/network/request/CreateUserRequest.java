package com.huewu.alarme.service.network.request;

import com.huewu.alarme.model.UserInfo;
import com.huewu.libs.network.JsonRequest;

public class CreateUserRequest extends JsonRequest<UserInfo> {
	
	public final static String URL = NetworkEnv.HOST + "user";
	
	//send message to clock server.
	//POST /user 
	//Request Body: JSON
	//{ username: mail_account, registerId: from_gcm, clockId: from_nfc }
	//Response Body: JSON
	//{ userId: unique_string }

	//scoped constructor.
	CreateUserRequest(UserInfo user) {
		super(Method.POST, URL);
		data = user.toPostData().getBytes();
	}

}//end of class
