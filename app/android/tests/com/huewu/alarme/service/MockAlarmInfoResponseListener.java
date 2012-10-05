package com.huewu.alarme.service;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.libs.network.JsonRequest;
import com.huewu.libs.network.RequestEvents.RequestFailedEvent;
import com.huewu.libs.network.RequestEvents.RequestFinishedEvent;
import com.huewu.libs.network.RequestEvents.RequestReadyEvent;
import com.huewu.libs.network.RequestEvents.RequestResponsedEvent;
import com.huewu.libs.network.RequestEvents.RequestRetryingEvent;
import com.huewu.libs.network.ResponseListener;
import com.squareup.otto.Subscribe;

public class MockAlarmInfoResponseListener implements ResponseListener{
	
	private Object mWaitObj = new Object();
	private JsonRequest<AlarmInfo> mBeforeRequest = null;
	private JsonRequest<AlarmInfo> mResponseRequest = null;
	private JsonRequest<AlarmInfo> mRetryRequest = null;
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
	public void onRequsetReady(JsonRequest<?> req) {
		mBeforeRequest = (JsonRequest<AlarmInfo>) req;
	}

	@Override
	public void onRequestRetrying(JsonRequest<?> req) {
		mRetryRequest = (JsonRequest<AlarmInfo>) req;
	}

	@Override
	public void onRequestResponse(JsonRequest<?> req) {
		mResponseRequest = (JsonRequest<AlarmInfo>) req;
	}

	@Override
	public void onRequestFinished(JsonRequest<?> req) {
		mFinishRequest = (JsonRequest<AlarmInfo>) req;
		notifiyAll();
	}

	@Override
	public void onRequestFailed(JsonRequest<?> req) {
		mErrorRequest = (JsonRequest<AlarmInfo>) req;
		notifiyAll();
	}
	
	private void notifiyAll(){
		synchronized (mWaitObj) {
			mWaitObj.notifyAll();
		}
	}

}//end of classs
