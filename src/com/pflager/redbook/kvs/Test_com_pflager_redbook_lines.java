package com.pflager.redbook.kvs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.pflager.redbook.lines;

class Test_com_pflager_redbook_lines extends ImageCompareJNA {

	@Test
	void test() throws Throwable {
		assertTrue(CaptureCImage("lines"));
		RunNewProcess(lines.class.getName());
		assertTrue(CompareImageSec("lines"));
		super.finalize();
	}

}
