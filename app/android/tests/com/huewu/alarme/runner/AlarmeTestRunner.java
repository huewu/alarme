package com.huewu.alarme.runner;
import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class AlarmeTestRunner extends RobolectricTestRunner {

	/**
	 * Call this constructor to specify the location of resources and AndroidManifest.xml.
	 * 
	 * @param testClass
	 * @throws InitializationError
	 */	
	public AlarmeTestRunner(@SuppressWarnings("rawtypes") Class testClass) throws InitializationError {
		super(testClass, new RobolectricConfig(new File("../android/AndroidManifest.xml"), new File("../android/res")));
	}
}