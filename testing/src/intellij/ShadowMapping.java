package intellij;

import frames.processing.Scene;
import frames.processing.Shape;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.event.MouseEvent;

public class ShadowMapping extends PApplet {
  Scene scene;
  Shape[] shapes;
  Shape light;
  PGraphics shadowMap;
  boolean show;

  //Choose one of P3D for a 3D scene or P2D for a 2D one.
  String renderer = P3D;
  int w = 1000;
  int h = 1000;

  public void settings() {
    size(w, h, renderer);
  }

  public void setup() {
    scene = new Scene(this, createGraphics(w, h, renderer));
    scene.setRadius(max(w, h));

    shapes = new Shape[20];
    for (int i = 0; i < shapes.length; i++) {
      shapes[i] = new Shape(scene);
      shapes[i].setGraphics(caja());
      shapes[i].randomize();
    }
    light = new Shape(scene) {
      // Note that within visit() geometry is defined at the
      // frame local coordinate system.
      @Override
      public void setGraphics(PGraphics pg) {
        pg.pushStyle();
        scene.drawAxes(pg, 150);
        pg.fill(isTracked() ? 255 : 25, isTracked() ? 0 : 255, 255);
        pg.noStroke();
        pg.sphere(50);
        pg.popStyle();
      }
    };
    scene.setFieldOfView(PI / 3);
    scene.fitBallInterpolation();

    shadowMap = createGraphics(w / 2, h / 2, renderer);
  }

  public void draw() {
    // 1. Fill in and display front-buffer
    scene.beginDraw();
    scene.frontBuffer().background(10, 50, 25);
    scene.traverse();
    scene.endDraw();
    scene.display();
    // 2. Display shadow map
    shadowMap.beginDraw();
    shadowMap.background(120);
    scene.traverse(shadowMap, light);
    shadowMap.endDraw();
    // 3. display shadow map
    if (show)
      image(shadowMap, w / 2, h / 2);
  }

  public void mouseMoved() {
    scene.cast();
  }

  public void mouseDragged() {
    if (mouseButton == LEFT)
      scene.spin();
    else if (mouseButton == RIGHT)
      scene.translate();
    else
      scene.zoom(mouseX - pmouseX);
  }

  public void mouseWheel(MouseEvent event) {
    scene.zoom(event.getCount() * 20);
  }

  public void keyPressed() {
    if (key == ' ')
      show = !show;
    if (show)
      println("show!");
    else
      println("DON't show");
  }

  PShape caja() {
    PShape caja = scene.is3D() ? createShape(BOX, random(60, 100)) : createShape(RECT, 0, 0, random(60, 100), random(60, 100));
    caja.setStrokeWeight(3);
    caja.setStroke(color(random(0, 255), random(0, 255), random(0, 255)));
    caja.setFill(color(random(0, 255), random(0, 255), random(0, 255), random(0, 255)));
    return caja;
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"intellij.ShadowMapping"});
  }
}
