package org.pflager.redbook;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.pflager.redbook.kvs.image;

@SuppressWarnings("serial")
class Test_com_pflager_redbook_image extends ImageCompareJNA {
	@Test
	void test() throws Throwable {
		assertTrue(captureReferencePng("image"));
		RunNewProcess(image.class.getName());
		boolean CompareImage = CompareImageSec("image");
		super.finalize();
		assertTrue(CompareImage);
	}

}