package com.huewu.alarme.service.network;



public interface ResponseCallback<T> {

	void onBeforeRequest(JsonRequest<T> req);
	
	void onResponse(JsonRequest<T> req, T resp);

	void onFinsh(JsonRequest<T> req);

	void onError(JsonRequest<T> req, Exception exception);

}// end of class
