package com.huewu.alarme.service;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.text.format.Time;

import com.huewu.alarme.Constants;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.AlarmMember;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.runner.AlarmeTestRunner;
import com.huewu.alarme.service.network.JsonRequest;
import com.huewu.alarme.service.network.JsonRequest.Method;

@RunWith(AlarmeTestRunner.class)
public class AlameServiceTest {
	
	private AlarmeService service = null;
	private UserInfo user = null;
	private AlarmInfo private_alarm = null;
	private AlarmInfo group_alarm = null;
	
	@Before
	public void init(){
		
		initAlarmService();
		initDummyUser();
		initDummyPrivateAlarm();
	}

	private void initAlarmService() {
		service = new AlarmeService();
		service.onCreate();
	}

	private void initDummyUser() {
		user = new UserInfo();
		user.uname = "huewu";
		user.rid = "443322";
		user.cid = "12345";
	}

	private void initDummyPrivateAlarm() {
		private_alarm = new AlarmInfo();
		//private_alarm.time = System.currentTimeMillis();
		private_alarm.type = AlarmInfo.PRIVATE_ALARM;
		private_alarm.member = new AlarmMember[]{ };
	}

	@Test
	public void testCreateUser() throws MalformedURLException{
		//actually send dummy userinfo to server.
		MockUserInfoResponseListener listener = new MockUserInfoResponseListener();
		//check result.
		service.createUser(user, listener);
		
		listener.waitResponse(Constants.DEFAULT_WAIT_TIMEOUT);
		
		//test request is created correctly.
		JsonRequest<UserInfo> beforeReq = listener.getBeforeRequest();
		assertEquals(Method.POST, beforeReq.getMethod());
		assertEquals(new URL("http://ghfal.herokuapp.com/user"), beforeReq.getURL());
		assertArrayEquals("uname=huewu&rid=443322&cid=12345".getBytes(), beforeReq.getData());
		
		//test response is success.
		assertNull(listener.getErrorRequest());
		assertNotNull(listener.getFinishRequest());
		
		//uid should be returned. 
		UserInfo response = listener.getFinishRequest().getResponse().get(0);
		assertNotNull(response);
		assertNotNull(response.uid);
	}

	@Test
	public void testDeleteUser() throws MalformedURLException{
		
		//first, create a random name user.
		Random rand = new Random();
		
		MockUserInfoResponseListener listener1 = new MockUserInfoResponseListener();
		MockUserInfoResponseListener listener2 = new MockUserInfoResponseListener();
		
		user.uname = user.uname + rand.nextInt(100);
		service.createUser(user, listener1);
		
		listener1.waitResponse(Constants.DEFAULT_WAIT_TIMEOUT);
		
		UserInfo response = listener1.getFinishRequest().getResponse().get(0);
		assertNotNull(response.uid);
		
		//then, delete that user.
		service.deleteUser(response, listener2);
		
		listener2.waitResponse(Constants.DEFAULT_WAIT_TIMEOUT);
		
		//test request is created correctly.
		JsonRequest<UserInfo> beforeReq = listener2.getBeforeRequest();
		assertEquals(Method.DELETE, beforeReq.getMethod());
		assertEquals(new URL("http://ghfal.herokuapp.com/user/" + response.uid), beforeReq.getURL());
		
		//test response is success.
		assertNull(listener2.getErrorRequest());
		assertNotNull(listener2.getFinishRequest());
	}

	@Test
	public void testSetAlaram(){
		
		AlarmInfo alarm = new AlarmInfo();
		service.setAlaram(alarm, new MockAlarmInfoResponseListener());
		
		//should check something.
	}

	public void testOffAlaram( AlarmInfo alarm ){
		
	}

	public void testSetGroupAlarm( AlarmInfo alarm ){
		
	}

	public void testUpdateGroupAlaram( AlarmInfo alarm ){
		
	}

	public void testGetAlarmInfoList( String cid ){
	}

}//end of class
