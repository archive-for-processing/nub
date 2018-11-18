package ik.interactive;

import frames.core.Frame;
import frames.core.Graph;
import frames.primitives.Point;
import frames.primitives.Vector;
import frames.processing.Scene;
import ik.common.Joint;
import processing.core.PApplet;
import processing.core.PGraphics;

public class InteractiveJoint extends Joint {
    protected Vector _desiredTranslation;
    public InteractiveJoint(Scene scene, int color, float radius) {
        super(scene, color, radius);
    }
    public InteractiveJoint(Scene scene, float radius) {
        super(scene, radius);
    }

    @Override
    public void interact(Object... gesture){
        String command = (String) gesture[0];
        if(command.matches("Add")){
            if(_desiredTranslation != null) {
                addChild((Vector) gesture[1]);
            }
            _desiredTranslation = null;
        } else if(command.matches("OnAdding")){
            _desiredTranslation = translateDesired((Vector) gesture[1]);
        } else if(command.matches("Reset")){
            _desiredTranslation = null;
        } else if(command.matches("Remove")){
            removeChild();
        }
    }
    @Override
    public void visit(){
        super.visit();
        //Draw desired position
        PGraphics pg = _pGraphics;
        if(_desiredTranslation != null){
            pg.pushStyle();
            pg.stroke(pg.color(0,255,0));
            pg.strokeWeight(_radius/2);
            pg.line(0,0,0, _desiredTranslation.x()/this.scaling(), _desiredTranslation.y()/this.scaling(), _desiredTranslation.z()/this.scaling());
            pg.popStyle();
        }
    }

    public void addChild(Vector mouse){
        InteractiveJoint joint = new InteractiveJoint((Scene)this._graph, this.radius());
        joint.setReference(this);
        joint.setTranslation(joint.translateDesired(mouse));
    }

    public void removeChild(){
        _graph.pruneBranch(this);
    }

    //------------------------------------
    //Interactive actions - same method found in Graph Class (duplicated cause of visibility)
    protected Vector _translateDesired(float dx, float dy, float dz, int zMax, Frame frame) {
        Scene scene = (Scene) _graph;
        if (scene.is2D() && dz != 0) {
            System.out.println("Warning: graph is 2D. Z-translation reset");
            dz = 0;
        }
        dx = scene.isEye(frame) ? -dx : dx;
        dy = scene.isRightHanded() ^ scene.isEye(frame) ? -dy : dy;
        dz = scene.isEye(frame) ? dz : -dz;
        // Scale to fit the screen relative vector displacement
        if (scene.type() == Graph.Type.PERSPECTIVE) {
            float k = (float) Math.tan(scene.aperture() / 2.0f) * Math.abs(
                    scene.eye().location(scene.isEye(frame) ? scene.anchor() : frame.position())._vector[2] * scene.eye().magnitude());
            //TODO check me weird to find height instead of width working (may it has to do with fov?)
            dx *= 2.0 * k / (scene.height() * scene.eye().magnitude());
            dy *= 2.0 * k / (scene.height() *scene. eye().magnitude());
        }
        // this expresses the dz coordinate in world units:
        //Vector eyeVector = new Vector(dx, dy, dz / eye().magnitude());
        Vector eyeVector = new Vector(dx, dy, dz * 2 * scene.radius() / zMax);
        return frame.reference() == null ? scene.eye().worldDisplacement(eyeVector) : frame.reference().displacement(eyeVector, scene.eye());
    }


    public Vector translateDesired(Vector mouse){
        Scene scene = (Scene) _graph;
        float dx = mouse.x() - scene.screenLocation(position()).x();
        float dy = mouse.y() - scene.screenLocation(position()).y();
        return _translateDesired(dx, dy, 0, Math.min(scene.width(), scene.height()), this);
    }
}
