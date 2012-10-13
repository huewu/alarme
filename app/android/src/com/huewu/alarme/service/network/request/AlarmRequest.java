package com.huewu.alarme.service.network.request;

import java.net.URL;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.libs.network.JsonRequest;

public class AlarmRequest extends JsonRequest<AlarmInfo>{
	
	public final static String URL = NetworkEnv.HOST + "/user";

	public AlarmRequest( Method method, URL url ) {
		super(method, url);
	}

}//end of class
