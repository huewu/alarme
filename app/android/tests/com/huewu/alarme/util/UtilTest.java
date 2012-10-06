package com.huewu.alarme.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Service;

import com.huewu.alarme.runner.AlarmeTestRunner;
import com.huewu.alarme.service.AlarmeService;
import com.xtremelabs.robolectric.Robolectric;

@RunWith(AlarmeTestRunner.class)
public class UtilTest {
	
	@Test
	public void testGetTimeString(){
		String timeStr = Util.getTimeString(0);
		assertEquals("1970-01-01T09:00", timeStr);
	}
	
	@Test
	public void testGetAccountName(){
		//TODO how to test this?
	}

}//end of class
