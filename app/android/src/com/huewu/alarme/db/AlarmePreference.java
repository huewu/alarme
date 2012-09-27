package com.huewu.alarme.db;

import com.google.gson.Gson;
import com.huewu.alarme.model.UserInfo;

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
	
	public static void setClockID(Context ctx, String cid){
		Editor edit = getInstance(ctx).edit();
		edit.putString("cid", cid);
		edit.commit();
	}
	
	public static String getClockID(Context ctx){
		return getInstance(ctx).getString("cid", "");
	}
			
	public static void setRegisterID(Context ctx, String rid){
		Editor edit = getInstance(ctx).edit();
		edit.putString("rid", rid);
		edit.commit();
	}
	
	public static String getRegisterID(Context ctx){
		return getInstance(ctx).getString("rid", "");
	}
	
	public static UserInfo getUser(Context ctx) {
		String userStr = getInstance(ctx).getString("user", "");
		if( userStr.isEmpty() )
			return null;

		Gson gson = new Gson();
		return gson.fromJson(userStr, UserInfo.class);
	}
	
	public static void setUser( Context ctx, UserInfo user ) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(user);
		Editor editor = getInstance(ctx).edit();
		editor.putString("user", jsonStr);
		editor.commit();
	}
	
	private AlarmePreference(){}

}//end of class
