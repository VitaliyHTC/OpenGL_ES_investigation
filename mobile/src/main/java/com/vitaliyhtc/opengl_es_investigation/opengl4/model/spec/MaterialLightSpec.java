package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec;

/**
 * Created by vitaliyhtc on 19.09.17.
 * see table: http://devernay.free.fr/cours/opengl/materials.html
 * for OpenGL multiply the shininess by 128!
 */

public class MaterialLightSpec {
    private Point3f ambientIntensity;
    private Point3f diffuseIntensity;
    private Point3f specularIntencity;
    private float shininess;

    public MaterialLightSpec(Point3f ambientIntensity, Point3f diffuseIntensity, Point3f specularIntencity, float shininess) {
        this.ambientIntensity = ambientIntensity;
        this.diffuseIntensity = diffuseIntensity;
        this.specularIntencity = specularIntencity;
        this.shininess = shininess;
    }

    public Point3f getAmbientIntensity() {
        return ambientIntensity;
    }

    public void setAmbientIntensity(Point3f ambientIntensity) {
        this.ambientIntensity = ambientIntensity;
    }

    public Point3f getDiffuseIntensity() {
        return diffuseIntensity;
    }

    public void setDiffuseIntensity(Point3f diffuseIntensity) {
        this.diffuseIntensity = diffuseIntensity;
    }

    public Point3f getSpecularIntencity() {
        return specularIntencity;
    }

    public void setSpecularIntencity(Point3f specularIntencity) {
        this.specularIntencity = specularIntencity;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }
}
