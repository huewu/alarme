package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.service.network.JsonRequest;
import com.huewu.alarme.service.network.ResponseCallback;

public class MockAlarmInfoResponseListener implements ResponseCallback<AlarmInfo>{
	
	private Object mWaitObj = new Object();
	private JsonRequest<AlarmInfo> mBeforeRequest = null;
	private JsonRequest<AlarmInfo> mResponseRequest = null;
	private JsonRequest<AlarmInfo> mFinishRequest = null;
	private JsonRequest<AlarmInfo> mErrorRequest = null;
	
	public JsonRequest<AlarmInfo> getBeforeRequest(){
		return mBeforeRequest;
	}
	
	public JsonRequest<AlarmInfo> getResponseRequest(){
		return mResponseRequest;
	}

	public JsonRequest<AlarmInfo> getFinishRequest(){
		return mFinishRequest;
	}

	public JsonRequest<AlarmInfo> getErrorRequest(){
		return mErrorRequest;
	}
	
	public void waitResponse( long ms ){
		synchronized (mWaitObj) {
			try {
				mWaitObj.wait(ms);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void onBeforeRequest(JsonRequest<AlarmInfo> req) {
		mBeforeRequest = req;
	}

	@Override
	public void onResponse(JsonRequest<AlarmInfo> req, AlarmInfo resp) {
		mResponseRequest = req;
	}

	@Override
	public void onFinsh(JsonRequest<AlarmInfo> req) {
		mFinishRequest = req;
		synchronized (mWaitObj) {
			mWaitObj.notifyAll();
		}
	}

	@Override
	public void onError(JsonRequest<AlarmInfo> req, Exception exception) {
		mErrorRequest = req;
		synchronized (mWaitObj) {
			mWaitObj.notifyAll();
		}
	}

}//end of classs
