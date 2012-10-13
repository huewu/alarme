package com.huewu.alarme.service.network.request;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.libs.network.JsonRequest;

public class SetAlarmRequest extends JsonRequest<AlarmInfo>{
	
	public final static String URL_FORMT = NetworkEnv.HOST + "user/%s/alarm";


	public SetAlarmRequest(AlarmInfo alarm) {
		super(Method.POST, String.format(URL_FORMT, alarm.getOwner().uid));
		data = alarm.toPostData().getBytes();
	}

}//end of class
