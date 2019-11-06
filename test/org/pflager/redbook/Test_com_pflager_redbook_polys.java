package org.pflager.redbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.pflager.redbook.polys;

@SuppressWarnings("serial")
class Test_com_pflager_redbook_polys extends ImageCompareJNA {

	@Test
	void test() throws Throwable {
		assertTrue(captureCRedbookReferencePng("polys"));
		RunNewProcess(polys.class.getName());
		boolean CompareImage = captureAndCompareJGlutRedbookWithCRedbook("polys");
		super.finalize();
		assertTrue(CompareImage);
	}

}
