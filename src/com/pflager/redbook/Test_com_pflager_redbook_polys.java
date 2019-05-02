package com.pflager.redbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Test_com_pflager_redbook_polys extends ImageCompareJNA {

	@Test
	void test() {
		assertTrue(CaptureCImage("polys"));
		polys polysObject = new polys();
		new Thread(() -> {
			String argv[] = new String[0];
			polysObject.main(0, argv);
		}).start();
		assertTrue(CompareImageSec("polys"));
		polysObject.glutLeaveMainLoop();
	}

}
