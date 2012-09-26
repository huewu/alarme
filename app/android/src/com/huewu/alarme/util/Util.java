package com.huewu.alarme.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");	

	public static String getTimeString(long time){
		return format.format(new Date(time));
	}
	
	private Util(){}

}//end of class
