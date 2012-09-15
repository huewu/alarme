package com.huewu.alarme.service.network.request;

import java.net.URL;

import com.huewu.alarme.service.network.JsonRequest;

public class RegisterRequest extends JsonRequest<Object>{

	public RegisterRequest(Method method, URL url) {
		super(method, url);
	}

}//end of class
