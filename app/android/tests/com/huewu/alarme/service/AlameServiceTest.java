package com.huewu.alarme.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.huewu.alarme.Constants;
import com.huewu.alarme.DummyFactory;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.AlarmMember;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.runner.AlarmeTestRunner;
import com.huewu.libs.network.JsonRequest;
import com.huewu.libs.network.JsonRequest.Method;

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
	public void testSetAlaram() throws MalformedURLException{
		
		MockAlarmInfoResponseListener listener = new MockAlarmInfoResponseListener();

		UserInfo user = DummyFactory.createDummyUserInfo();
		AlarmInfo alarm = new AlarmInfo(user, 0);
		service.setAlaram(alarm, listener);
		
		listener.waitResponse(Constants.DEFAULT_WAIT_TIMEOUT);
		
		JsonRequest<AlarmInfo> beforeReq = listener.getBeforeRequest();

		//check http method.
		assertEquals(Method.POST, beforeReq.getMethod());
		
		//check url.
		String urlStr = String.format("http://ghfal.herokuapp.com/user/%s/alarm", user.uid);
		assertEquals(new URL(urlStr), beforeReq.getURL());
		
		//check form data.
		assertArrayEquals(alarm.toPostData().getBytes(), beforeReq.getData());
		
		//request should be success.
		assertNotNull(listener.getFinishRequest());
		assertNull(listener.getErrorRequest());
		
		//should get aid. (alarm id)
		JsonRequest<AlarmInfo> response = listener.getResponseRequest();
		assertEquals(1, response.getResponse().size());
		assertNotNull(response.getResponse().get(0).aid);
	}

	@Test
	public void testOffAlaram() throws MalformedURLException{
		
		MockAlarmInfoResponseListener listener = new MockAlarmInfoResponseListener();
		
		AlarmInfo alarm = DummyFactory.createDummyAlarmInfo();

		service.offAlaram(alarm, listener);
		
		listener.waitResponse(Constants.DEFAULT_WAIT_TIMEOUT);
		
		JsonRequest<AlarmInfo> beforeReq = listener.getBeforeRequest();
		
		//check http method.
		assertEquals(Method.PUT, beforeReq.getMethod());
		
		//check url
		String urlStr = String.format("http://ghfal.herokuapp.com/alarm/%s", alarm.aid);
		assertEquals(new URL(urlStr), beforeReq.getURL());
		
		//request should be success.
		assertNotNull(listener.getFinishRequest());
		assertNull(listener.getErrorRequest());
	}

	@Test
	public void testSetGroupAlarm(){
		
		MockAlarmInfoResponseListener listener = new MockAlarmInfoResponseListener();

		UserInfo user1 = DummyFactory.createDummyUserInfo("1");
		UserInfo user2 = DummyFactory.createDummyUserInfo("2");
		UserInfo user3 = DummyFactory.createDummyUserInfo("3");
		AlarmInfo alarm = new AlarmInfo(user, 0);

		service.setGroupAlarm(alarm, new UserInfo[]{user1, user2, user3}, listener);
		
		listener.waitResponse(Constants.DEFAULT_WAIT_TIMEOUT);

//		//check http method.
//		assertEquals(Method.POST, beforeReq.getMethod());
//		
//		//check url.
//		String urlStr = String.format("http://ghfal.herokuapp.com/user/%s/alarm", user.uid);
//		assertEquals(new URL(urlStr), beforeReq.getURL());
//		
//		//check form data.
//		assertArrayEquals(alarm.toPostData().getBytes(), beforeReq.getData());
//		
//		//request should be success.
//		assertNotNull(listener.getFinishRequest());
//		assertNull(listener.getErrorRequest());
//		
//		//should get aid. (alarm id)
//		JsonRequest<AlarmInfo> response = listener.getResponseRequest();
//		assertEquals(1, response.getResponse().size());
//		assertNotNull(response.getResponse().get(0).aid);
	}

	@Test
	public void testUpdateGroupAlaram(){
		
	}

	public void testGetAlarmInfoList( String cid ){
	}

}//end of class
