package com.huewu.alarme.service;

import com.huewu.alarme.model.UserInfo;
import com.huewu.libs.network.JsonRequest;
import com.huewu.libs.network.ResponseListener;

public class MockUserInfoResponseListener implements ResponseListener {
	
	private Object mWaitObj = new Object();
	private JsonRequest<UserInfo> mBeforeRequest = null;
	private JsonRequest<UserInfo> mResponseRequest = null;
	private JsonRequest<UserInfo> mFinishRequest = null;
	private JsonRequest<UserInfo> mRetryRequest = null;
	private JsonRequest<UserInfo> mErrorRequest = null;
	
	public JsonRequest<UserInfo> getBeforeRequest(){
		return mBeforeRequest;
	}
	
	public JsonRequest<UserInfo> getResponseRequest(){
		return mResponseRequest;
	}

	public JsonRequest<UserInfo> getFinishRequest(){
		return mFinishRequest;
	}

	public JsonRequest<UserInfo> getErrorRequest(){
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
		mBeforeRequest = (JsonRequest<UserInfo>) req;
	}

	@Override
	public void onRequestRetrying(JsonRequest<?> req) {
		mRetryRequest = (JsonRequest<UserInfo>) req;
	}

	@Override
	public void onRequestResponse(JsonRequest<?> req) {
		mResponseRequest = (JsonRequest<UserInfo>) req;
	}

	@Override
	public void onRequestFinished(JsonRequest<?> req) {
		mFinishRequest = (JsonRequest<UserInfo>) req;
		notifiyAll();
	}

	@Override
	public void onRequestFailed(JsonRequest<?> req) {
		mErrorRequest = (JsonRequest<UserInfo>) req;
		notifiyAll();
	}
	
	private void notifiyAll(){
		synchronized (mWaitObj) {
			mWaitObj.notifyAll();
		}
	}

}//end of classs
