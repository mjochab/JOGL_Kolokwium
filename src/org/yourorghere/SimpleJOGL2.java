package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.*;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL2 implements GLEventListener {

    private static float xrot = 0.0f, yrot = 0.0f;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new SimpleJOGL2());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

        //Obs³uga klawiszy strza³ek
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    xrot -= 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    xrot += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    yrot += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    yrot -= 1.0f;
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        //warto?ci sk³adowe o?wietlenia i koordynaty ?ród³a ?wiat³a
        float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};//swiat³o otaczajšce
        float diffuseLight[] = {0.7f, 0.7f, 0.7f, 1.0f};//?wiat³o rozproszone
        float specular[] = {1.0f, 1.0f, 1.0f, 1.0f}; //?wiat³o odbite
        float lightPos[] = {0.0f, 150.0f, 150.0f, 1.0f};//pozycja ?wiat³a
        //(czwarty parametr okre?la odleg³o?æ ?ród³a:
        //0.0f-nieskoñczona; 1.0f-okre?lona przez pozosta³e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametrów ?ród³a ?wiat³a nr. 0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat³o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //?wiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //?wiat³o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja ?wiat³a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?ród³a ?wiat³a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolorów
        //kolory bêdš ustalane za pomocš glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiektów
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f}; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref, 0);

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -10.0f); //przesuniêcie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó³ osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó³ osi Y

        gl.glColor3f(0.0f, 1.0f, 0.0f);
        kula(gl);

        gl.glTranslatef(2.0f, 0.0f, 0.0f);
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        walec(gl);

        gl.glTranslatef(2.0f, 0.0f, 0.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        stozek(gl);

        gl.glTranslatef(-4.0f, 3.0f, 0.0f);
        gl.glScalef(2f, 2f, 2f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        prostopadloscian(gl);

//       gl.glPushMatrix() ;
//       gl.glTranslatef(x, 0, y);
//       gl.glPopMatrix() ;  
//       gl.glRotatef(90-yrot, 0.0f, 1.0f, 0.0f);
//       gl.glScalef(0.5f, 0.5f, 0.5f);
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    public void kula(GL gl) {
        GLU glu = new GLU();
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluSphere(quad, 1, 20, 30);
        glu.gluDeleteQuadric(quad);
    }

    private void prostopadloscian(GL gl) {

        float x1 = 1;
        float y1 = 1;
        float z1 = 1;
        float x0 = 0;
        float y0 = 0;
        float z0 = 0;

        gl.glBegin(GL.GL_QUADS);
        //sciana przednia
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(x0, y0, z1);
        gl.glVertex3f(x1, y0, z1);
        gl.glVertex3f(x1, y1, z1);
        gl.glVertex3f(x0, y1, z1);
        //sciana tylnia
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glVertex3f(x0, y1, z0);
        gl.glVertex3f(x1, y1, z0);
        gl.glVertex3f(x1, y0, z0);
        gl.glVertex3f(x0, y0, z0);
        //sciana lewa
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glVertex3f(x0, y0, z0);
        gl.glVertex3f(x0, y0, z1);
        gl.glVertex3f(x0, y1, z1);
        gl.glVertex3f(x0, y1, z0);
        //sciana prawa
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(x1, y1, z0);
        gl.glVertex3f(x1, y1, z1);
        gl.glVertex3f(x1, y0, z1);
        gl.glVertex3f(x1, y0, z0);
        //sciana dolna
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glVertex3f(x0, y0, z1);
        gl.glVertex3f(x0, y0, z0);
        gl.glVertex3f(x1, y0, z0);
        gl.glVertex3f(x1, y0, z1);
        //sciana gorna
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(x1, y1, z1);
        gl.glVertex3f(x1, y1, z0);
        gl.glVertex3f(x0, y1, z0);
        gl.glVertex3f(x0, y1, z1);
        gl.glEnd();
    }

    void walec(GL gl) {
//wywo³ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta³ci
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_QUAD_STRIP);
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), 0.0f);
            gl.glVertex3f(x, y, -1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, -1.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void stozek(GL gl) {
//wywo³ujemy automatyczne normalizowanie normalnych
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), -2.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
