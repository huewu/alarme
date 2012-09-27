package com.huewu.alarme.model;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.Gson;
import com.huewu.alarme.runner.AlarmeTestRunner;

@RunWith(AlarmeTestRunner.class)
public class UserInfoTest {
	
	private UserInfo user = null;
	private String dummy_json = "";
//	 {
//		  "__v": 0,
//		  "uid": "test1",
//		  "uname": "test1",
//		  "rid": "12345",
//		  "cid": "1234",
//		  "_id": "505824e559331e0200000004",
//		  "msg": "success"
//	 }
	
	@Before
	public void init() throws IOException{
		user = new UserInfo();
		user.uname = "huewu";
		user.uid = "1234";
		user.rid = "4321";
		user.cid = "0000";
		user.msg = "hello world";
		
		//load dummy json file.
		FileReader fr = new FileReader("../android/tests/dummy_user_json.txt");
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
		String uname = "huewu";
		String rid = "12345";
		String cid = "qqqeee";
		UserInfo user = new UserInfo(uname, rid, cid);

		assertEquals(uname, user.uname);
		assertEquals(rid, user.rid);
		assertEquals(cid, user.cid);
	}
	
	@Test
	public void parseFromJsonStream(){
		
		Gson gson = new Gson();
		UserInfo user = gson.fromJson(dummy_json, UserInfo.class);
		
		assertEquals("test1", user.uname);
		assertEquals("test1", user.uid);
		assertEquals("12345", user.rid);
		assertEquals("1234", user.cid);
		assertEquals("success", user.msg);
	}
	
	@Test
	public void convertToPostData(){
		
		//user.toBytes();
		//in order to post this json object to setver it should converted post form style data.
		String form = "uname=huewu&rid=4321&cid=0000";
		assertEquals(form, user.toPostData());
	}

}//end of class
