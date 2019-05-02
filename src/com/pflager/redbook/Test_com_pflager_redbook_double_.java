package com.pflager.redbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_com_pflager_redbook_double_ extends ImageCompareJNA {

	@Test
	void test() {
		assertTrue(CaptureCImage("double_"));
		double_ double_Object = new double_();
		new Thread(() -> {
			String argv[] = new String[0];
			double_Object.main(0, argv);
		}).start();
		assertTrue(CompareImageSec("double_"));
		double_Object.glutLeaveMainLoop();
	}

}
