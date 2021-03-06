package org.pflager.redbook.vtrg;

import com.pflager.glut;

public class dof extends glut{
	int teapotList;
	void init()
	{
		double ambient[] = { 0.0, 0.0, 0.0, 1.0 };
		double diffuse[] = { 1.0, 1.0, 1.0, 1.0 };
		double position[] = { 0.0, 3.0, 3.0, 0.0 };
		double lmodel_ambient[] = { 0.2, 0.2, 0.2, 1.0 };
		double local_view[] = { 0.0 };
		glLightfv(GL_LIGHT0, GL_AMBIENT, ambient);
		glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lmodel_ambient);
		glLightModelfv(GL_LIGHT_MODEL_LOCAL_VIEWER, local_view);
		glFrontFace (GL_CW);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_AUTO_NORMAL);
		glEnable(GL_NORMALIZE);
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.0, 0.0, 0.0, 0.0);
		glClearAccum(0.0, 0.0, 0.0, 0.0);
		/* make teapot display list */
		teapotList = glGenLists(1);
		glNewList (teapotList, GL_COMPILE);
		glutSolidTeapot (0.5);
		glEndList ();
	}
	
	void renderTeapot (double x, double y, double z,double ambr, double ambg, double ambb,double difr, double difg, double difb,double specr, double specg, double specb, double shine)
	{
		double mat[]=new double[4];
		glPushMatrix();
		glTranslatef (x, y, z);
		mat[0] = ambr; mat[1] = ambg; mat[2] = ambb; mat[3] = 1.0;
		glMaterialfv (GL_FRONT, GL_AMBIENT, mat);
		mat[0] = difr; mat[1] = difg; mat[2] = difb;
		glMaterialfv (GL_FRONT, GL_DIFFUSE, mat);
		mat[0] = specr; mat[1] = specg; mat[2] = specb;
		glMaterialfv (GL_FRONT, GL_SPECULAR, mat);
		glMaterialf (GL_FRONT, GL_SHININESS, shine*128.0);
		glCallList(teapotList);
		glPopMatrix();
	}
	/* accFrustum()
	 * The first 6 arguments are identical to the glFrustum() call.
	 *  
	 * pixdx and pixdy are anti-alias jitter in pixels. 
	 * Set both equal to 0.0 for no anti-alias jitter.
	 * eyedx and eyedy are depth-of field jitter in pixels. 
	 * Set both equal to 0.0 for no depth of field effects.
	 *
	 * focus is distance from eye to plane in focus. 
	 * focus must be greater than, but not equal to 0.0.
	 *
	 * Note that accFrustum() calls glTranslatef().  You will 
	 * probably want to insure that your ModelView matrix has been 
	 * initialized to identity before calling accFrustum().
	 */
	void accFrustum(double left, double right, double bottom, double top, double near, double far, double pixdx, double pixdy, double eyedx, double eyedy, double focus)
	{
	   double xwsize, ywsize; 
	   double dx, dy;
	   int viewport[]=new int[4];

	   glGetIntegerv (GL_VIEWPORT, viewport);
		
	   xwsize = right - left;
	   ywsize = top - bottom;
		
	   dx = -(pixdx*xwsize/(double) viewport[2] + eyedx*near/focus);
	   dy = -(pixdy*ywsize/(double) viewport[3] + eyedy*near/focus);
		
	   glMatrixMode(GL_PROJECTION);
	   glLoadIdentity();
	   glFrustum (left + dx, right + dx, bottom + dy, top + dy, near, far);
	   glMatrixMode(GL_MODELVIEW);
	   glLoadIdentity();
	   glTranslatef (-eyedx, -eyedy, 0.0);
	}

	/*  accPerspective()
	 * 
	 *  The first 4 arguments are identical to the gluPerspective() call.
	 *  pixdx and pixdy are anti-alias jitter in pixels. 
	 *  Set both equal to 0.0 for no anti-alias jitter.
	 *  eyedx and eyedy are depth-of field jitter in pixels. 
	 *  Set both equal to 0.0 for no depth of field effects.
	 *
	 *  focus is distance from eye to plane in focus. 
	 *  focus must be greater than, but not equal to 0.0.
	 *
	 *  Note that accPerspective() calls accFrustum().
	 */
	void accPerspective(double fovy, double aspect, double near, double far, double pixdx, double pixdy, double eyedx, double eyedy, double focus)
	{
	   double fov2,left,right,bottom,top;

	   fov2 = ((fovy*Math.PI) / 180.0) / 2.0;

	   top = near / (Math.cos(fov2) / Math.sin(fov2));
	   bottom = -top;

	   right = top * aspect;
	   left = -right;

	   accFrustum (left, right, bottom, top, near, far,pixdx, pixdy, eyedx, eyedy, focus);
	}

	void display()
	{
		int jitter;
		int viewport[]=new int[4];
		glGetIntegerv (GL_VIEWPORT, viewport);
		glClear(GL_ACCUM_BUFFER_BIT);
		for (jitter = 0; jitter < 8; jitter++) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//accPerspective (45.0,(double) viewport[2]/(double) viewport[3],1.0, 15.0, 0.0, 0.0,0.33*j8[jitter].x, 0.33*j8[jitter].y, 5.0);
			accPerspective (45.0,(double) viewport[2]/(double) viewport[3],1.0, 15.0, 0.0, 0.0,0.0, 0.0, 5.0);
			/* ruby, gold, silver, emerald, and cyan teapots */
			renderTeapot (-1.1, -0.5, -4.5, 0.1745, 0.01175,
			0.01175, 0.61424, 0.04136, 0.04136,
			0.727811, 0.626959, 0.626959, 0.6);
			renderTeapot (-0.5, -0.5, -5.0, 0.24725, 0.1995,
			0.0745, 0.75164, 0.60648, 0.22648,
			0.628281, 0.555802, 0.366065, 0.4);
			renderTeapot (0.2, -0.5, -5.5, 0.19225, 0.19225,
			0.19225, 0.50754, 0.50754, 0.50754,
			0.508273, 0.508273, 0.508273, 0.4);
			renderTeapot (1.0, -0.5, -6.0, 0.0215, 0.1745, 0.0215,
			0.07568, 0.61424, 0.07568, 0.633,
			0.727811, 0.633, 0.6);
			renderTeapot (1.8, -0.5, -6.5, 0.0, 0.1, 0.06, 0.0,
			0.50980392, 0.50980392, 0.50196078,
			0.50196078, 0.50196078, .25);
			glAccum (GL_ACCUM, 0.125);
		}
		glAccum (GL_RETURN, 1.0);
		glFlush();
	}
	
	void reshape(int w, int h)
	{
		glViewport(0, 0,  w,  h);
	}
	void keyboard( char key, int x, int y)
	{
	   switch (key) {
	      case 27:
	         System.exit(0);
	         break;
	   }
	}
	/* Main Loop
	* Be certain you request an accumulation buffer.
	*/
	int main(int argc, String[] argv)
	{
		glutInit(argc, argv);
		glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB
		| GLUT_ACCUM | GLUT_DEPTH);
		glutInitWindowSize (400, 400);
		glutInitWindowPosition (100, 100);
		glutCreateWindow ("dof");
		init();
		glutReshapeFunc(this::reshape);
		glutDisplayFunc(this::display);
		glutKeyboardFunc(this::keyboard);
		glutMainLoop();
		return 0;
	}
	public static void main(String[] args) {
		System.exit(new dof().main(args.length, args));

	}
}
