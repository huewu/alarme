package com.huewu.alarme.service.network.request;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.huewu.alarme.DummyFactory;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;

public class RequestFactoryTest {
	
	@Test
	public void testCreateUserRequestMethods(){
		UserInfo user = DummyFactory.createDummyUserInfo();
		CreateUserRequest req = RequestFactory.createCreateUserRequest(user, null);
		
		assertNotNull(req);
	}
	
	@Test
	public void testDeleteUserRequestMethods(){
		UserInfo user = DummyFactory.createDummyUserInfo();
		DeleteUserRequest req = RequestFactory.createDeleteUserRequest(user, null);

		assertNotNull(req);
	}

	@Test
	public void testSetAlarmRequestMethods(){
		AlarmInfo alarm = DummyFactory.createDummyAlarmInfo();
		SetAlarmRequest req = RequestFactory.createSetAlarmRequest(alarm, null);

		assertNotNull(req);
	}

	@Test
	public void testUpdateAlarmRequestMethods(){
		//RequestFactory.createCreateUserRequest(user, callback);
	}

}//end of class
