package com.huewu.alarme.service.network;


import android.content.Context;

/**
 * Json Network Worekr
 * @author huewu.yang
 *
 */
public class DeubuggableJsonNetworkWorker extends JsonNetworkWorker {

	public DeubuggableJsonNetworkWorker(Context ctx) {
		super(ctx);
	}
	
	//provide a way to check handled request & response.
	
	public JsonRequest<?> getLastRequest(){
		return mLastRequest;
	}
}// end of class
