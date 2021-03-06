package com.huewu.alarme.service.network.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.libs.network.JsonRequest;

public class OffAlarmRequest extends JsonRequest<AlarmInfo>{
	
	public final static String URL_FORMT = NetworkEnv.HOST + "alarm/%s";
	public final static String POST_FORMT = "uid=%s&status=OFF";

	public OffAlarmRequest(Context ctx, AlarmInfo alarm) {
		super(Method.PUT, String.format(URL_FORMT, alarm.aid));
		try {
			data = String.format(POST_FORMT, URLEncoder.encode(AlarmePreference.getUser(ctx).uid, "UTF-8")).getBytes();
		} catch (UnsupportedEncodingException e) {
			data = new byte[]{};
		}
	}

}//end of class
