package com.huewu.alarme;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.GroupAlarmInfo;
import com.huewu.alarme.runner.AlarmeTestRunner;

@RunWith(AlarmeTestRunner.class)
public class GCMIntentServiceTest {

	private GCMIntentService service = null;
	
	@Before
	public void init(){
		service = new GCMIntentService();
		service.onCreate();
	}
	
	@Test
	public void testOnRegistered(){
		service.onRegistered(service, "abcde");
		assertEquals("abcde", AlarmePreference.getRegisterID(service));
	}
	
	@Test
	public void testOnUnRegistered(){
		service.onUnregistered(service, "abcde");
		assertEquals("", AlarmePreference.getRegisterID(service));
	}
	
	@Test
	public void testOnMessage(){
		Intent i = new Intent();
		
		//onOffAlarm( AlarmInfo alarm );	//callback. 

		//onUpdateAlarm( GroupAlarmInfo alarm );	//callback.

		//onRequestSetAlarm( GroupAlarmInfo alarm );	//callback.
		
		//i.putExtra(name, value);
		service.onMessage(service, new Intent());
	}

}//end of class
