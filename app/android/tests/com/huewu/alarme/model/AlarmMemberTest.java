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
	public void testParseFromJson(){
		
		Gson gson = new Gson();
	}
	
	@Test
	public void testToPostData(){
		
	}


}//end of class
	