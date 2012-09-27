package com.huewu.alarme.model;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.Gson;
import com.huewu.alarme.DummyFactory;
import com.huewu.alarme.runner.AlarmeTestRunner;

@RunWith(AlarmeTestRunner.class)
public class AlarmInfoTest {
	
	private String dummy_json = "";
	//{
	//"aid":"KL_PRIVATE_1348021106421",
	//"type":"PRIVATE",
	//"time":"2012-09-19T02:18:26.421Z",
	//"_id":"50592c319752bc0200000003",
	//"__v":0,
	//"member":[{"uid":"KL","status":"ON","_id":"50592c319752bc0200000004"}]
	//}
	
	@Before
	public void init() throws IOException{
		//load dummy json file.
		FileReader fr = new FileReader("../android/tests/dummy_private_alarm_json.txt");
		BufferedReader br = new BufferedReader(fr);

		String line = br.readLine();
		
		while(line != null){
			dummy_json += line;
			line = br.readLine();
		}
		
		br.close();
		fr.close();
	}
	
	@Test
	public void testConstructor(){
		UserInfo user = DummyFactory.createDummyUserInfo();

		AlarmInfo alarm = new AlarmInfo(user, 1000);
		assertEquals("PRIVATE", alarm.type);
		assertEquals("1970-01-01T09:00:01.000Z", alarm.time);
		assertEquals(1, alarm.member.length);
		assertEquals(AlarmMember.STATUS_ON, alarm.member[0].status);
	}
	
	@Test
	public void testGetters(){
		UserInfo user = DummyFactory.createDummyUserInfo();
		
		AlarmInfo alarm = new AlarmInfo(user, 1000);
		assertEquals( user, alarm.getOwner() );
	}
	
	@Test
	public void testParseFromJson(){
		
		Gson gson = new Gson();
		AlarmInfo alarm = gson.fromJson(dummy_json, AlarmInfo.class);
		assertEquals("KL_PRIVATE_1348021106421", alarm.aid);
		assertEquals("PRIVATE", alarm.type);
		assertEquals("2012-09-19T02:18:26.421Z", alarm.time);
		assertEquals(1, alarm.member.length);
		AlarmMember member = alarm.member[0];
		
		assertEquals("KL", member.uid);
		assertEquals("ON", member.status);
	}
	
	@Test
	public void testToPostData() throws UnsupportedEncodingException{
		//FIXME member should be renamed to members.
		//type:PRIVATE
		//time:1348711260486
		//member[0][uid]:KL
		//member[0][status]:ON	
		
		UserInfo user = DummyFactory.createDummyUserInfo();
		String postStr = String.format("type=%s&time=%s&member[0][uid]=%s&member[0][status]=%s", 
				URLEncoder.encode("PRIVATE","utf-8"),
				URLEncoder.encode("1970-01-01T09:00:01.000Z","utf-8"),
				URLEncoder.encode("xxxx","utf-8"),
				URLEncoder.encode("ON","utf-8")
				);

		AlarmInfo alarm = new AlarmInfo(user, 1000);
		assertEquals( postStr, alarm.toPostData() );
	}

}//end of class
	