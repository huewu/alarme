package com.huewu.alarme.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class Util {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");	

	public static String getTimeString(long time){
		return format.format(new Date(time));
	}
	
	public static long getTime(String timeStr) {
		try {
			return format.parse(timeStr).getTime();
		} catch (ParseException e) {
			return 0;
		}
	}
	
	public static String getCurrentUserAccount(Context context){
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccounts();
		
		return accounts[0].name;
	}
	
	private Util(){}


}//end of class
