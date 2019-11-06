package org.pflager.redbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.pflager.redbook.kvs.bezsurf;

@SuppressWarnings("serial")
class Test_com_pflager_redbook_bezsurf extends ImageCompareJNA {

	@Test
	void test() throws Throwable {
		assertTrue(captureCRedbookReferencePng("bezsurf"));
		RunNewProcess(bezsurf.class.getName());
		boolean CompareImage = captureAndCompareJGlutRedbookWithCRedbook("bezsurf");
		super.finalize();
		assertTrue(CompareImage);
	}

}
