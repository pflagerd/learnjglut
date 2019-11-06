package org.pflager.redbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.pflager.redbook.lines;

@SuppressWarnings("serial")
class Test_com_pflager_redbook_lines extends ImageCompareJNA {

	@Test
	void test() throws Throwable {
		assertTrue(captureCRedbookReferencePng("lines"));
		RunNewProcess(lines.class.getName());
		boolean CompareImage = captureAndCompareJGlutRedbookWithCRedbook("lines");
		super.finalize();
		assertTrue(CompareImage);
	}

}
