class Box extends Node {
  float w, h, d;
  int c;

  Box(color c) {
    setPickingThreshold(25);
    setSize();
    setColor(c);
    randomize(new Vector(), 200, g.is3D());
  }

  // detached-nub drawing require to
  // manually apply the node transformation
  void draw() {
    pushStyle();
    pushMatrix();
    scene.applyTransformation(this);
    if (drawAxes)
      scene.drawAxes(max(w, h, d) * 1.3f);
    noStroke();
    if (isTagged(scene))
      fill(255, 0, 0);
    else
      fill(c);
    //Draw a box
    box(w, h, d);
    popMatrix();
    stroke(255);
    if (drawShooterTarget)
      scene.drawBullsEye(this);
    popStyle();
  }

  void setSize() {
    w = CajasOrientadas.this.random(10, 40);
    h = CajasOrientadas.this.random(10, 40);
    d = CajasOrientadas.this.random(10, 40);
    setPickingThreshold(max(w, h, d));
  }

  void setSize(float myW, float myH, float myD) {
    w = myW;
    h = myH;
    d = myD;
  }

  void setColor(int myC) {
    c = myC;
  }

  void setOrientation(Vector v) {
    Vector to = Vector.subtract(v, position());
    setOrientation(new Quaternion(new Vector(0, 1, 0), to));
  }
}
