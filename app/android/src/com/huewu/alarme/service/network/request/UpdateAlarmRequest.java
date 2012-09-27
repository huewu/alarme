package com.huewu.alarme.service.network.request;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.service.network.JsonRequest;

public class UpdateAlarmRequest extends JsonRequest<AlarmInfo>{

	public UpdateAlarmRequest(AlarmInfo info) {
		super(Method.POST, "");
	}

}//end of class
