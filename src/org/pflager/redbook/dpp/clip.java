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

/*
 *  clip.c
 *  This program demonstrates arbitrary clipping planes.
 */
import com.pflager.glut;

public class clip extends glut {
	void init() 
	{
	   glClearColor (0.0, 0.0, 0.0, 0.0);
	   glShadeModel (GL_FLAT);
	}

	void display()
	{
	   double eqn[] = new double[] {0.0, 1.0, 0.0, 0.0};
	   double eqn2[] = new double[] {1.0, 0.0, 0.0, 0.0};

	   glClear(GL_COLOR_BUFFER_BIT);

	   glColor3f (1.0, 1.0, 1.0);
	   glPushMatrix();
	   glTranslatef (0.0, 0.0, -5.0);

	/*    clip lower half -- y < 0          */
	   glClipPlane (GL_CLIP_PLANE0, eqn);
	   glEnable (GL_CLIP_PLANE0);
	/*    clip left half -- x < 0           */
	   glClipPlane (GL_CLIP_PLANE1, eqn2);
	   glEnable (GL_CLIP_PLANE1);

	   glRotatef (90.0, 1.0, 0.0, 0.0);
	   glutWireSphere(1.0, 20, 16);
	   glPopMatrix();

	   glFlush ();
	}

	void reshape (int w, int h)
	{
	   glViewport (0, 0, w, h); 
	   glMatrixMode (GL_PROJECTION);
	   glLoadIdentity ();
	   gluPerspective(60.0, (double) w/(double) h, 1.0, 20.0);
	   glMatrixMode (GL_MODELVIEW);
	}

	void keyboard(char key, int x, int y)
	{
	   switch (key) {
	      case 27:
	         System.exit(0);
	         break;
	   }
	}

	public int main(int argc, String[] argv) 
	{
		   glutInit(argc, argv);
		   glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB);
		   glutInitWindowSize (500, 500); 
		   glutInitWindowPosition (100, 100);
		   glutCreateWindow ("clip");
		   init ();
		   glutDisplayFunc(this::display); 
		   glutReshapeFunc(this::reshape);
		   glutKeyboardFunc(this::keyboard);
		   glutMainLoop();
		   return 0;
	}
	
	
	public static void main(String[] args) {
		System.exit(new clip().main(args.length, args));
	}

}