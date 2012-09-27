package com.huewu.alarme.service.network.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.service.network.JsonRequest;

public class OffAlarmRequest extends JsonRequest<AlarmInfo>{
	
	public final static String URL_FORMT = "http://ghfal.herokuapp.com/alarm/%s";
	public final static String POST_FORMT = "uid=%s&status=OFF";

	public OffAlarmRequest(AlarmInfo alarm) {
		super(Method.PUT, String.format(URL_FORMT, alarm.aid));
		try {
			data = String.format(POST_FORMT, URLEncoder.encode(alarm.getOwner().uid, "UTF-8")).getBytes();
		} catch (UnsupportedEncodingException e) {
			data = new byte[]{};
		}
	}

}//end of class
