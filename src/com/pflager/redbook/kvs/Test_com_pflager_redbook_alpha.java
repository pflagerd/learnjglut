package com.pflager.redbook.kvs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.pflager.redbook.alpha;

@SuppressWarnings("serial")
class Test_com_pflager_redbook_alpha extends ImageCompareJNA {

	@Test
	void test() throws Throwable {
		assertTrue(CaptureCImage("alpha"));
		RunNewProcess(alpha.class.getName());
		boolean CompareImage = CompareImageSec("alpha");
		super.finalize();
		assertTrue(CompareImage);
	}

}