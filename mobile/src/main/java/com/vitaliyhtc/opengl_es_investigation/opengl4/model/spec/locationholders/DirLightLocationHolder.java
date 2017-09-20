package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.locationholders;

/**
 * Created by vitaliyhtc on 20.09.17.
 */

public class DirLightLocationHolder {

    private int uDirectionLocation;
    private int uAmbientLocation;
    private int uDiffuseLocation;
    private int uSpecularLocation;

    public DirLightLocationHolder(int uDirectionLocation, int uAmbientLocation, int uDiffuseLocation, int uSpecularLocation) {
        this.uDirectionLocation = uDirectionLocation;
        this.uAmbientLocation = uAmbientLocation;
        this.uDiffuseLocation = uDiffuseLocation;
        this.uSpecularLocation = uSpecularLocation;
    }

    public int getuDirectionLocation() {
        return uDirectionLocation;
    }

    public void setuDirectionLocation(int uDirectionLocation) {
        this.uDirectionLocation = uDirectionLocation;
    }

    public int getuAmbientLocation() {
        return uAmbientLocation;
    }

    public void setuAmbientLocation(int uAmbientLocation) {
        this.uAmbientLocation = uAmbientLocation;
    }

    public int getuDiffuseLocation() {
        return uDiffuseLocation;
    }

    public void setuDiffuseLocation(int uDiffuseLocation) {
        this.uDiffuseLocation = uDiffuseLocation;
    }

    public int getuSpecularLocation() {
        return uSpecularLocation;
    }

    public void setuSpecularLocation(int uSpecularLocation) {
        this.uSpecularLocation = uSpecularLocation;
    }
}
