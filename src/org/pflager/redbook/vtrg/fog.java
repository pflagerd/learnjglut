package org.pflager.redbook.vtrg;

import com.pflager.glut;

public class fog  extends glut{

	static int fogMode;
	
	 void init()
	{
		double position[] = { 0.5, 0.5, 3.0, 0.0 };
		glEnable(GL_DEPTH_TEST);
		glLightfv(GL_LIGHT0, GL_POSITION, position);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		{
			double mat[] = {0.1745, 0.01175, 0.01175,0.0};
			glMaterialfv (GL_FRONT, GL_AMBIENT, mat);
			mat[0] = 0.61424; mat[1] = 0.04136; mat[2] = 0.04136;
			glMaterialfv (GL_FRONT, GL_DIFFUSE, mat);
			mat[0] = 0.727811; mat[1] = 0.626959; mat[2] = 0.626959;
			glMaterialfv (GL_FRONT, GL_SPECULAR, mat);
			glMaterialf (GL_FRONT, GL_SHININESS, 0.6*128.0);
		}
		glEnable(GL_FOG);
		{
			double fogColor[] = {0.5, 0.5, 0.5, 1.0};
			fogMode = GL_EXP;
			glFogi (GL_FOG_MODE, fogMode);
			glFogfv (GL_FOG_COLOR, fogColor);
			glFogf (GL_FOG_DENSITY, 0.35);
			glHint (GL_FOG_HINT, GL_DONT_CARE);
			glFogf (GL_FOG_START, 1.0);
			glFogf (GL_FOG_END, 5.0);
		}
		glClearColor(0.5, 0.5, 0.5, 1.0); /* fog color */
	}
	 
	  void renderSphere (double x, double y, double z)
	 {
		 glPushMatrix();
		 glTranslatef (x, y, z);
		 glutSolidSphere(0.4, 16, 16);
		 glPopMatrix();
	 }
	  
	  /* display() draws 5 spheres at different z positions.
	  */
	  void display()
	  {
		  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		  renderSphere (-2., -0.5, -1.0);
		  renderSphere (-1., -0.5, -2.0);
		  renderSphere (0., -0.5, -3.0);
		  renderSphere (1., -0.5, -4.0);
		  renderSphere (2., -0.5, -5.0);
		  glFlush();
	  }
	  
	  void reshape(int w, int h)
	  {
		  glViewport(0, 0,  w,  h);
		  glMatrixMode(GL_PROJECTION);
		  glLoadIdentity();
		  if (w <= h)
		  glOrtho (-2.5, 2.5, -2.5*(double)h/(double)w,2.5*(double)h/(double)w, -10.0, 10.0);
		  else
		  glOrtho (-2.5*(double)w/(double)h, 2.5*(double)w/(double)h, -2.5, 2.5, -10.0, 10.0);
		  glMatrixMode(GL_MODELVIEW);
		  glLoadIdentity ();
	  }
	  
	  void keyboard(char key, int x, int y)
	  {
		  switch (key) {
		  case 'f':
		  case 'F':
			  if (fogMode == GL_EXP) 
			  {
				  fogMode = GL_EXP2;
				  System.out.printf ("Fog mode is GL_EXP2\n");
			  }
			  else if (fogMode == GL_EXP2) 
			  {
				  fogMode = GL_LINEAR;
				  System.out.printf ("Fog mode is GL_LINEAR\n");
			  }
			  else if (fogMode == GL_LINEAR) 
			  {
				  fogMode = GL_EXP;
				  System.out.printf ("Fog mode is GL_EXP\n");
			  }
			  glFogi (GL_FOG_MODE, fogMode);
			  glutPostRedisplay();
			  break;
		  case 27:
			  System.exit(0);
			  break;
		  		default:
		  		break;
		  }
	  }
	  
	  int main(int argc, String[] argv)
	  {
		  glutInit(argc, argv);
		  glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
		  glutInitWindowSize(500, 500);
		  glutCreateWindow("fog");
		  init();
		  glutReshapeFunc (this::reshape);
		  glutKeyboardFunc (this::keyboard);
		  glutDisplayFunc (this::display);
		  glutMainLoop();
		  return 0;
	  }
	  
	  public static void main(String[] args) {
			System.exit(new fog().main(args.length, args));
		}
}
