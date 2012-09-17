package com.huewu.alarme.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Preference Helper
 * @author huewu.yang
 *
 */
public class AlarmePreference {
	
	private static SharedPreferences mPref = null;
	private static Object mSingletonLock = new Object();
	private static SharedPreferences getInstance(Context ctx){
		synchronized (mSingletonLock) {
			if( mPref != null )
				return mPref;
			
			if( ctx != null ){
				mPref = ctx.getSharedPreferences("vinglef", Context.MODE_PRIVATE);
			}
			return mPref;
		}
	}
	
	public static void clearAuth(Context ctx){
		Editor edit = getInstance(ctx).edit();
		edit.remove("auth_token");
		edit.remove("access_token");
		edit.commit();
	}
	
	public static String getUserName(Context ctx){
		return getInstance(ctx).getString("username", "");
	}
	
	public static String getUserID(Context ctx){
		return getInstance(ctx).getString("uid", "");
	}
		
	public static void setUserName(Context ctx, String username){
		Editor edit = getInstance(ctx).edit();
		edit.putString("username", username);
		edit.commit();
	}
	
	public static void setUserUserID(Context ctx, String userid){
		Editor edit = getInstance(ctx).edit();
		edit.putString("uid", userid);
		edit.commit();
	}

	private AlarmePreference(){}

}//end of class
