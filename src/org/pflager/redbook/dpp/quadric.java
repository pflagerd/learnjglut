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

/*
 *  quadric.c
 *  This program demonstrates the use of some of the gluQuadric*
 *  routines. Quadric objects are created with some quadric
 *  properties and the callback routine to handle errors.
 *  Note that the cylinder has no top or bottom and the circle
 *  has a hole in it.
 */
package org.pflager.redbook.dpp;

import com.pflager.glut;

public class quadric extends glut {
	int startList;

	void errorCallback(int errorCode) {
		String estring = gluErrorString(errorCode);
		System.err.printf("Quadric Error: %s\n", estring);
		System.exit(0);
	}

	GLUquadric qobj;

	void init() {
		double mat_ambient[] = { 0.5, 0.5, 0.5, 1.0 };
		double mat_specular[] = { 1.0, 1.0, 1.0, 1.0 };
		double mat_shininess[] = { 50.0 };
		double light_position[] = { 1.0, 1.0, 1.0, 0.0 };
		double model_ambient[] = { 0.5, 0.5, 0.5, 1.0 };

		glClearColor(0.0, 0.0, 0.0, 0.0);

		glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
		glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
		glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);
		glLightfv(GL_LIGHT0, GL_POSITION, light_position);
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, model_ambient);

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_DEPTH_TEST);

		/*
		 * Create 4 display lists, each with a different quadric object. Different drawing styles and surface normal specifications are demonstrated.
		 */
		startList = glGenLists(4);
		qobj = gluNewQuadric();
		gluQuadricCallback(qobj, GLU_ERROR, this::errorCallback);

		gluQuadricDrawStyle(qobj, GLU_FILL); /* smooth shaded */
		gluQuadricNormals(qobj, GLU_SMOOTH);
		glNewList(startList, GL_COMPILE);
		gluSphere(qobj, 0.75, 15, 10);
		glEndList();

		gluQuadricDrawStyle(qobj, GLU_FILL); /* flat shaded */
		gluQuadricNormals(qobj, GLU_FLAT);
		glNewList(startList + 1, GL_COMPILE);
		gluCylinder(qobj, 0.5, 0.3, 1.0, 15, 5);
		glEndList();

		gluQuadricDrawStyle(qobj, GLU_LINE); /* all polygons wireframe */
		gluQuadricNormals(qobj, GLU_NONE);
		glNewList(startList + 2, GL_COMPILE);
		gluDisk(qobj, 0.25, 1.0, 20, 4);
		glEndList();

		gluQuadricDrawStyle(qobj, GLU_SILHOUETTE); /* boundary only */
		gluQuadricNormals(qobj, GLU_NONE);
		glNewList(startList + 3, GL_COMPILE);
		gluPartialDisk(qobj, 0.0, 1.0, 20, 4, 0.0, 225.0);
		glEndList();
	}

	void display() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glPushMatrix();

		glEnable(GL_LIGHTING);
		glShadeModel(GL_SMOOTH);
		glTranslatef(-1.0, -1.0, 0.0);
		glCallList(startList);

		glShadeModel(GL_FLAT);
		glTranslatef(0.0, 2.0, 0.0);
		glPushMatrix();
		glRotatef(300.0, 1.0, 0.0, 0.0);
		glCallList(startList + 1);
		glPopMatrix();

		glDisable(GL_LIGHTING);
		glColor3f(0.0, 1.0, 1.0);
		glTranslatef(2.0, -2.0, 0.0);
		glCallList(startList + 2);

		glColor3f(1.0, 1.0, 0.0);
		glTranslatef(0.0, 2.0, 0.0);
		glCallList(startList + 3);

		glPopMatrix();
		glFlush();
	}

	void reshape(int w, int h) {
		glViewport(0, 0, w, h);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		if (w <= h)
			glOrtho(-2.5, 2.5, -2.5 * (double) h / (double) w, 2.5 * (double) h / (double) w, -10.0, 10.0);
		else
			glOrtho(-2.5 * (double) w / (double) h, 2.5 * (double) w / (double) h, -2.5, 2.5, -10.0, 10.0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	void keyboard(char key, int x, int y) {
		switch (key) {
		case 27:
			gluDeleteQuadric(qobj);
			System.exit(0);
			break;
		}
	}

	public int main(int argc, String[] argv) {
		glutInit(argc, argv);
		glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
		glutInitWindowSize(500, 500);
		glutInitWindowPosition(100, 100);
		glutCreateWindow("quadric");
		init();
		glutDisplayFunc(this::display);
		glutReshapeFunc(this::reshape);
		glutKeyboardFunc(this::keyboard);
		glutMainLoop();
		gluDeleteQuadric(qobj);
		return 0;
	}

	public static void main(String[] args) {
		System.exit(new quadric().main(args.length, args));
	}

}
