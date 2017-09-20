package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec;

public class MaterialLightMapSpec {
    private int diffuseMap;
    private int specularMap;
    private float shininess;

    public MaterialLightMapSpec(int diffuseMap, int specularMap, float shininess) {
        this.diffuseMap = diffuseMap;
        this.specularMap = specularMap;
        this.shininess = shininess;
    }

    public int getDiffuseMap() {
        return diffuseMap;
    }

    public void setDiffuseMap(int diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public int getSpecularMap() {
        return specularMap;
    }

    public void setSpecularMap(int specularMap) {
        this.specularMap = specularMap;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }
}
