package com.huewu.alarme.db;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.huewu.alarme.GCMIntentService;
import com.huewu.alarme.runner.AlarmeTestRunner;

@RunWith(AlarmeTestRunner.class)
public class AlarmePreferenceTest {
	
	private GCMIntentService service = null;
	
	@Before
	public void init(){
		service = new GCMIntentService();
	}
	
	@Test
	public void testRegisterID() {
		AlarmePreference.setRegisterID(service, "1234");
		String rid = AlarmePreference.getRegisterID(service);
		assertEquals("1234", rid);
	}
	

	@Test
	public void testClockID() {
		AlarmePreference.setClockID(service, "1234");
		String cid = AlarmePreference.getClockID(service);
		assertEquals("1234", cid);
	}

}//end of class
