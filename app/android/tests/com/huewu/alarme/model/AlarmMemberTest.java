package com.huewu.alarme.model;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.Gson;
import com.huewu.alarme.runner.AlarmeTestRunner;
import com.huewu.alarme.service.AlameServiceTest;


@RunWith(AlarmeTestRunner.class)
public class AlarmMemberTest {
	
	private String dummy_json = "";		//{"uid":"KL","status":"ON","_id":"50592cfa9752bc0200000009"}
	private AlarmMember member = null;		
	
	@Before
	public void init() throws IOException{
		//load dummy json file.
		FileReader fr = new FileReader("../android/tests/dummy_alarm_member_json.txt");
		BufferedReader br = new BufferedReader(fr);

		String line = br.readLine();
		
		while(line != null){
			dummy_json += line;
			line = br.readLine();
		}
		
		br.close();
		fr.close();
		
		member = new AlarmMember();
		member.uid = "huewu";
		member.status = "OFF";
	}
	
	@Test
	public void testParseFromJson(){
		
		Gson gson = new Gson();
		AlarmMember member = gson.fromJson(dummy_json, AlarmMember.class);

		assertEquals("KL", member.uid);
		assertEquals("ON", member.status);
	}
	
	@Test
	public void testToPostData(){
		
	}


}//end of class
	