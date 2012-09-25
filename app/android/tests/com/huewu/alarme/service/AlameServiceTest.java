package com.huewu.alarme.service;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.huewu.alarme.Constants;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.runner.AlarmeTestRunner;
import com.huewu.alarme.service.network.JsonRequest;
import com.huewu.alarme.service.network.JsonRequest.Method;

@RunWith(AlarmeTestRunner.class)
public class AlameServiceTest {
	
	private AlarmeService service = null;
	private UserInfo user = null;
	
	@Before
	public void init(){
		service = new AlarmeService();
		service.onCreate();
		user = new UserInfo();
		user.uname = "huewu";
		user.rid = "443322";
		user.cid = "12345";
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

	public void testDeleteUser( UserInfo user ){
	}

	public void testSetAlaram( AlarmInfo alarm ){
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
