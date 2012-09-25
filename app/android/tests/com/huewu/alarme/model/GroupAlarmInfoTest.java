package com.huewu.alarme.model;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class GroupAlarmInfoTest {
	
	private String dummy_json = "";
	//{
	//	"aid":"KL_GROUP_1348021106421",
	//	"type":"GROUP",
	//	"time":"2012-09-19T02:18:26.421Z",
	//	"_id":"50592cfa9752bc0200000005",
	//	"__v":0,
	//	"member":[
	//		{"uid":"KL","status":"ON","_id":"50592cfa9752bc0200000009"},
	//		{"uid":"AA","status":"ON","_id":"50592cfa9752bc0200000008"},
	//		{"uid":"BB","status":"ON","_id":"50592cfa9752bc0200000007"},
	//		{"uid":"CC","status":"ON","_id":"50592cfa9752bc0200000006"}
	//	]}	
	
	@Before
	public void init() throws IOException{
		//load dummy json file.
		FileReader fr = new FileReader("../android/tests/dummy_public_alarm_json.txt");
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
	public void testParseFromJson(){
		
		Gson gson = new Gson();
		AlarmInfo alarm = gson.fromJson(dummy_json, AlarmInfo.class);
		assertEquals(4, alarm.member.length);
		AlarmMember member1 = alarm.member[0];
		AlarmMember member2 = alarm.member[1];
		AlarmMember member3 = alarm.member[2];
		AlarmMember member4 = alarm.member[3];
		
		assertEquals("KL", member1.uid);
		assertEquals("AA", member2.uid);
		assertEquals("BB", member3.uid);
		assertEquals("CC", member4.uid);
	}

}//end of class
