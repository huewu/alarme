package com.huewu.alarme.service.network;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public abstract class JsonRequest<T> {

	public static String HOST = "www.example.com";
	public static String SECURE_HOST = "secure.example.com";
	public final static int PORT = 80;	
	public final static int RETRY_COUNT = 3;

	protected URL url = null;
	protected Method method = Method.GET;
	protected HashMap<String, String> headers = new HashMap<String, String>();
	protected ResponseDecoder<T> decoder;
	protected ResponseCallback<T> callback;
	protected ArrayList<T> response = new ArrayList<T>(5);
	protected Exception exception = null;
	protected byte[] data = null;
	protected boolean useSecure = false;
	protected int retryCount = 0;
	protected int maxRetryCount = RETRY_COUNT;

	private boolean mForceUseCache = false;

	public enum Method
	{
		GET, POST, PUT, DELETE
	}

	public JsonRequest( Method method, URL url ){
		this.method = method;
		this.url = url;
	}

	public URL getURL() {
		return url;
	}

	public Method getMethod() {
		return method;
	}

	/**
	 * return read-only unmodifiable map.
	 * 
	 * @return
	 */
	public HashMap<String, String> getHeaders() {
		return (HashMap<String, String>) Collections.unmodifiableMap(headers);
	}

	public boolean useForceCache() {
		return mForceUseCache;
	}

	public void setForceCache(boolean flag) {
		mForceUseCache = flag;
	}

	public void addResponse( Object obj ){
		response.add((T) obj);
	}

	public ArrayList<T> getResponse() {
		return response;
	}

	public void notifiyOnStart() {
		if(callback != null )
			callback.onBeforeRequest(this);
	}

	public void notifyOnFinishOrError() {
		if(callback != null){
			if(exception == null)
				callback.onFinsh(this);
			else
				callback.onError(this, exception);
		}
	}

	public void notifiyResponse( Object obj) {
		if(callback != null){
			callback.onResponse(this, (T) obj);
		}
	}

	public boolean isCanceled() {
		return false;
	}
}// end of class
