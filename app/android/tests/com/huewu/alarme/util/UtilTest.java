package com.huewu.alarme.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {
	
	@Test
	public void testGetTimeString(){
		String timeStr = Util.getTimeString(0);
		assertEquals("1970-01-01T09:00:00.000Z", timeStr);
	}

}//end of class
