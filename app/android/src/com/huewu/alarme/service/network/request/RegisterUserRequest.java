package com.huewu.alarme.service.network.request;

import java.net.URL;

import com.huewu.alarme.service.network.JsonRequest;

public class RegisterUserRequest extends JsonRequest<Object> {
	
	//send message to clock server.
	//POST /user 
	//Request Body: JSON
	//{ username: mail_account, registerId: from_gcm, clockId: from_nfc }
	//Response Body: JSON
	//{ userId: unique_string }

	//scoped constructor.
	RegisterUserRequest(URL url) {
		super(Method.POST, url);
	}

}//end of class
