package com.pflager.redbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_com_pflager_redbook_unproject extends ImageCompareJNA {

	@Test
	void test() {
		assertTrue(CaptureCImage("unproject"));
		unproject unprojectObject = new unproject();
		new Thread(() -> {
			String argv[] = new String[0];
			unprojectObject.main(0, argv);
		}).start();
		assertTrue(CompareImageSec("unproject"));
		unprojectObject.glutLeaveMainLoop();
	}

}
