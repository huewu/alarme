package com.huewu.alarme;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context arg0, String arg1) {
		// Typically, there is nothing to be done other than evaluating the error (returned by errorId) and trying to fix the problem.
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
	}

	@Override
	protected void onRegistered(Context arg0, String regID) {
		//Typically, you should send the regid to your server so it can use it to send messages to this device.
		//send GCM reg id to our clock server.
	}

	@Override
	protected void onUnregistered(Context arg0, String regID) {
		//Typically, you should send the regid to the server so it unregisters the device.
	}

}//end of class
