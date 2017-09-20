package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec;

/**
 * Created by vitaliyhtc on 19.09.17.
 */

public class LightSourceSpec {

    private Point3f color;
    private Point3f directtion;

    public LightSourceSpec(Point3f color, Point3f directtion) {
        this.color = color;
        this.directtion = directtion;
    }

    public Point3f getColor() {
        return color;
    }

    public void setColor(Point3f color) {
        this.color = color;
    }

    public Point3f getDirecttion() {
        return directtion;
    }

    public void setDirecttion(Point3f directtion) {
        this.directtion = directtion;
    }
}
