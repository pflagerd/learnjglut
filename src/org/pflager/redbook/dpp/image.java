package org.pflager.redbook.dpp;

/*
 * Copyright (c) 1993-1997, Silicon Graphics, Inc.
 * ALL RIGHTS RESERVED
 * Permission to use, copy, modify, and distribute this software for
 * any purpose and without fee is hereby granted, provided that the above
 * copyright notice appear in all copies and that both the copyright notice
 * and this permission notice appear in supporting documentation, and that
 * the name of Silicon Graphics, Inc. not be used in advertising
 * or publicity pertaining to distribution of the software without specific,
 * written prior permission.
 *
 * THE MATERIAL EMBODIED ON THIS SOFTWARE IS PROVIDED TO YOU "AS-IS"
 * AND WITHOUT WARRANTY OF ANY KIND, EXPRESS, IMPLIED OR OTHERWISE,
 * INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY OR
 * FITNESS FOR A PARTICULAR PURPOSE.  IN NO EVENT SHALL SILICON
 * GRAPHICS, INC.  BE LIABLE TO YOU OR ANYONE ELSE FOR ANY DIRECT,
 * SPECIAL, INCIDENTAL, INDIRECT OR CONSEQUENTIAL DAMAGES OF ANY
 * KIND, OR ANY DAMAGES WHATSOEVER, INCLUDING WITHOUT LIMITATION,
 * LOSS OF PROFIT, LOSS OF USE, SAVINGS OR REVENUE, OR THE CLAIMS OF
 * THIRD PARTIES, WHETHER OR NOT SILICON GRAPHICS, INC.  HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH LOSS, HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, ARISING OUT OF OR IN CONNECTION WITH THE
 * POSSESSION, USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 * US Government Users Restricted Rights
 * Use, duplication, or disclosure by the Government is subject to
 * restrictions set forth in FAR 52.227.19(c)(2) or subparagraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause at DFARS 252.227-7013 and/or in similar or successor
 * clauses in the FAR or the DOD or NASA FAR Supplement.
 * Unpublished-- rights reserved under the copyright laws of the
 * United States.  Contractor/manufacturer is Silicon Graphics,
 * Inc., 2011 N.  Shoreline Blvd., Mountain View, CA 94039-7311.
 *
 * OpenGL(R) is a registered trademark of Silicon Graphics, Inc.
 */

/*  image.c
 *  This program demonstrates drawing pixels and shows the effect
 *  of glDrawPixels(), glCopyPixels(), and glPixelZoom().
 *  Interaction: moving the mouse while pressing the mouse button
 *  will copy the image in the lower-left corner of the window
 *  to the mouse position, using the current pixel zoom factors.
 *  There is no attempt to prevent you from drawing over the original
 *  image.  If you press the 'r' key, the original image and zoom
 *  factors are reset.  If you press the 'z' or 'Z' keys, you change
 *  the zoom factors.
 */
import com.pflager.glut;

public class image extends glut {
	/* Create checkerboard image */
	final int checkImageWidth = 64;
	final int checkImageHeight = 64;

	byte[] checkImage = new byte[checkImageHeight * checkImageWidth * 3];

	double zoomFactor = 1.0;
	int height;

	void makeCheckImage() {
		int i, j, c;

		for (i = 0; i < checkImageHeight; i++) {
			for (j = 0; j < checkImageWidth; j++) {
				c = ((i & 0x8) == 0 ? 1 : 0) ^ ((j & 0x8) == 0 ? 1 : 0) * 255;
				checkImage[i * checkImageWidth * 3 + j * 3 + 0] = (byte) c;
				checkImage[i * checkImageWidth * 3 + j * 3 + 1] = (byte) c;
				checkImage[i * checkImageWidth * 3 + j * 3 + 2] = (byte) c;
			}
		}
	}

	void init() {
		glClearColor(0.0, 0.0, 0.0, 0.0);
		glShadeModel(GL_FLAT);
		makeCheckImage();
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	}

	void display() {
		glClear(GL_COLOR_BUFFER_BIT);
		glRasterPos2i(0, 0);
		glDrawPixels(checkImageWidth, checkImageHeight, GL_RGB, GL_UNSIGNED_BYTE, (byte[])checkImage);
		glFlush();
	}

	void reshape(int w, int h) {
		glViewport(0, 0, w, h);
		height = h;
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0.0, (double) w, 0.0, (double) h);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	void motion(int x, int y) {
		int screeny = height - y;

		glRasterPos2i(x, screeny);
		glPixelZoom(zoomFactor, zoomFactor);
		glCopyPixels(0, 0, checkImageWidth, checkImageHeight, GL_COLOR);
		glPixelZoom(1.0, 1.0);
		glFlush();
	}

	void keyboard(char key, int x, int y) {
		switch (key) {
		case 'r':
		case 'R':
			zoomFactor = 1.0;
			glutPostRedisplay();
			System.out.printf("zoomFactor reset to 1.0\n");
			break;
		case 'z':
			zoomFactor += 0.5;
			if (zoomFactor >= 3.0)
				zoomFactor = 3.0;
			System.out.printf("zoomFactor is now %4.1f\n", zoomFactor);
			break;
		case 'Z':
			zoomFactor -= 0.5;
			if (zoomFactor <= 0.5)
				zoomFactor = 0.5;
			System.out.printf("zoomFactor is now %4.1f\n", zoomFactor);
			break;
		case 27:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	public int main(int argc, String[] argv) {
		glutInit(argc, argv);
		glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
		glutInitWindowSize(250, 250);
		glutInitWindowPosition(100, 100);
		glutCreateWindow("image");
		init();
		glutDisplayFunc(this::display);
		glutReshapeFunc(this::reshape);
		glutKeyboardFunc(this::keyboard);
		glutMotionFunc(this::motion);
		glutMainLoop();
		return 0;
	}

	public static void main(String[] args) {
		System.exit(new image().main(args.length, args));
	}

}
