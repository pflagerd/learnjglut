package org.pflager.redbook.vtrg;

import com.pflager.glut;

public class accanti extends glut {

	int ACSIZE=8;
	/*  Initialize lighting and other values.
	 */
	void init()
	{
	   double mat_ambient[] = { 1.0, 1.0, 1.0, 1.0 };
	   double mat_specular[] = { 1.0, 1.0, 1.0, 1.0 };
	   double light_position[] = { 0.0, 0.0, 10.0, 1.0 };
	   double lm_ambient[] = { 0.2, 0.2, 0.2, 1.0 };

	   glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
	   glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
	   glMaterialf(GL_FRONT, GL_SHININESS, 50.0);
	   glLightfv(GL_LIGHT0, GL_POSITION, light_position);
	   glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lm_ambient);
	    
	   glEnable(GL_LIGHTING);
	   glEnable(GL_LIGHT0);
	   glEnable(GL_DEPTH_TEST);
	   glShadeModel (GL_FLAT);

	   glClearColor(0.0, 0.0, 0.0, 0.0);
	   glClearAccum(0.0, 0.0, 0.0, 0.0);
	}
	void displayObjects()
	{
		double torus_diffuse[] = { 0.7, 0.7, 0.0, 1.0 };
		double cube_diffuse[] = { 0.0, 0.7, 0.7, 1.0 };
		double sphere_diffuse[] = { 0.7, 0.0, 0.7, 1.0 };
		double octa_diffuse[] = { 0.7, 0.4, 0.4, 1.0 };
		glPushMatrix ();
		glTranslatef (0.0, 0.0, -5.0);
		glRotatef (30.0, 1.0, 0.0, 0.0);
		glPushMatrix ();
		glTranslatef (-0.80, 0.35, 0.0);
		glRotatef (100.0, 1.0, 0.0, 0.0);
		glMaterialfv(GL_FRONT, GL_DIFFUSE, torus_diffuse);
		glutSolidTorus (0.275, 0.85, 16, 16);
		glPopMatrix ();
		glPushMatrix ();
		glTranslatef (-0.75, -0.50, 0.0);
		glRotatef (45.0, 0.0, 0.0, 1.0);
		glRotatef (45.0, 1.0, 0.0, 0.0);
		glMaterialfv(GL_FRONT, GL_DIFFUSE, cube_diffuse);
		glutSolidCube (1.5);
		glPopMatrix ();
		glPushMatrix ();
		glTranslatef (0.75, 0.60, 0.0);
		glRotatef (30.0, 1.0, 0.0, 0.0);
		glMaterialfv(GL_FRONT, GL_DIFFUSE, sphere_diffuse);
		glutSolidSphere (1.0, 16, 16);
		glPopMatrix ();
		glPushMatrix ();
		glTranslatef (0.70, -0.90, 0.25);
		glMaterialfv(GL_FRONT, GL_DIFFUSE, octa_diffuse);
		glutSolidOctahedron ();
		glPopMatrix ();
		glPopMatrix ();
	}
	
	void display()
	{
		int viewport[]=new int[4];
		int jitter;
		glGetIntegerv (GL_VIEWPORT, viewport);
		glClear(GL_ACCUM_BUFFER_BIT);
		for (jitter = 0; jitter < ACSIZE; jitter++) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glPushMatrix ();
		/* Note that 4.5 is the distance in world space between
		* left and right and bottom and top.
		* This formula converts fractional pixel movement to
		* world coordinates.
		*/
		
		//glTranslatef (j8[jitter]*4.5/viewport[2],j8[jitter]*4.5/viewport[3], 0.0);
		glTranslatef(0.0, 0.0, 0.0);
		displayObjects ();
		glPopMatrix ();
		glAccum(GL_ACCUM, 1.0/ACSIZE);
		}
		glAccum (GL_RETURN, 1.0);
		glFlush();
	}

	void reshape(int w, int h)
	{
	   glViewport(0, 0,  w,  h);
	   glMatrixMode(GL_PROJECTION);
	   glLoadIdentity();
	   if (w <= h) 
	      glOrtho (-2.25, 2.25, -2.25*h/w, 2.25*h/w, -10.0, 10.0);
	   else 
	      glOrtho (-2.25*w/h, 2.25*w/h, -2.25, 2.25, -10.0, 10.0);
	   glMatrixMode(GL_MODELVIEW);
	   glLoadIdentity();
	}

	void keyboard(char key, int x, int y)
	{
	   switch (key) {
	      case 27:
	         System.exit(0);
	         break;
	   }
	}

	/*  Main Loop
	 *  Be certain to request an accumulation buffer.
	 */
	int main(int argc, String[] argv)
	{
	   glutInit(argc, argv);
	   glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB
				| GLUT_ACCUM | GLUT_DEPTH);
	   glutInitWindowSize (250, 250);
	   glutInitWindowPosition (100, 100);
	   glutCreateWindow ("accanti");
	   init();
	   glutReshapeFunc(this::reshape);
	   glutDisplayFunc(this::display);
	   glutKeyboardFunc(this::keyboard);
	   glutMainLoop();
	   return 0; 
	}
	public static void main(String[] args) {
		System.exit(new accanti().main(args.length, args));

	}
}
