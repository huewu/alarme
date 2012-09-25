package com.huewu.alarme.service;

import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.service.network.JsonRequest;
import com.huewu.alarme.service.network.ResponseCallback;

public class MockUserInfoResponseListener implements ResponseCallback<UserInfo>{
	
	private Object mWaitObj = new Object();
	private JsonRequest<UserInfo> mBeforeRequest = null;
	private JsonRequest<UserInfo> mResponseRequest = null;
	private JsonRequest<UserInfo> mFinishRequest = null;
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
	public void onBeforeRequest(JsonRequest<UserInfo> req) {
		mBeforeRequest = req;
	}

	@Override
	public void onResponse(JsonRequest<UserInfo> req, UserInfo resp) {
		mResponseRequest = req;
	}

	@Override
	public void onFinsh(JsonRequest<UserInfo> req) {
		mFinishRequest = req;
		synchronized (mWaitObj) {
			mWaitObj.notifyAll();
		}
	}

	@Override
	public void onError(JsonRequest<UserInfo> req, Exception exception) {
		mErrorRequest = req;
		synchronized (mWaitObj) {
			mWaitObj.notifyAll();
		}
	}

}//end of classs
