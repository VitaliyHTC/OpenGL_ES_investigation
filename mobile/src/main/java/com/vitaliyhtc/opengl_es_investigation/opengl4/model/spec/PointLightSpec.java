package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec;

/**
 * Created by vitaliyhtc on 20.09.17.
 */

public class PointLightSpec {

    private Point3f position;

    private float constant;
    private float linear;
    private float quadratic;

    private Point3f ambient;
    private Point3f diffuse;
    private Point3f specular;


    public PointLightSpec(Point3f position, float constant, float linear, float quadratic, Point3f ambient, Point3f diffuse, Point3f specular) {
        this.position = position;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public Point3f getPosition() {
        return position;
    }

    public void setPosition(Point3f position) {
        this.position = position;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
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
