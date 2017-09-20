package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec;

/**
 * Created by vitaliyhtc on 20.09.17.
 */

public class DirLightSpec {

    private Point3f direction;

    private Point3f ambient;
    private Point3f diffuse;
    private Point3f specular;


    public DirLightSpec(Point3f direction, Point3f ambient, Point3f diffuse, Point3f specular) {
        this.direction = direction;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public Point3f getDirection() {
        return direction;
    }

    public void setDirection(Point3f direction) {
        this.direction = direction;
    }

    public Point3f getAmbient() {
        return ambient;
    }

    public void setAmbient(Point3f ambient) {
        this.ambient = ambient;
    }

    public Point3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Point3f diffuse) {
        this.diffuse = diffuse;
    }

    public Point3f getSpecular() {
        return specular;
    }

    public void setSpecular(Point3f specular) {
        this.specular = specular;
    }
}
