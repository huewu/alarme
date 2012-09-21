package com.huewu.alarme.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.runner.AlarmeTestRunner;

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
	public void testCreateUser(){
		//actually send dummy userinfo to server.
		//check result.
		service.createUser(user);
		
		//dummy server should be received HTTP request.
		//uid should be returned. 
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
