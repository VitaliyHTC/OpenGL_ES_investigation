package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.locationholders;

/**
 * Created by vitaliyhtc on 20.09.17.
 */

public class PointLightLocationHolder {

    private int uPositionLocation;
    private int uConstantLocation;
    private int uLinearLocation;
    private int uQuadraticLocation;
    private int uAmbientLocation;
    private int uDiffuseLocation;
    private int uSpecularLocation;

    public PointLightLocationHolder(int uPositionLocation, int uConstantLocation, int uLinearLocation, int uQuadraticLocation, int uAmbientLocation, int uDiffuseLocation, int uSpecularLocation) {
        this.uPositionLocation = uPositionLocation;
        this.uConstantLocation = uConstantLocation;
        this.uLinearLocation = uLinearLocation;
        this.uQuadraticLocation = uQuadraticLocation;
        this.uAmbientLocation = uAmbientLocation;
        this.uDiffuseLocation = uDiffuseLocation;
        this.uSpecularLocation = uSpecularLocation;
    }

    public int getuPositionLocation() {
        return uPositionLocation;
    }

    public void setuPositionLocation(int uPositionLocation) {
        this.uPositionLocation = uPositionLocation;
    }

    public int getuConstantLocation() {
        return uConstantLocation;
    }

    public void setuConstantLocation(int uConstantLocation) {
        this.uConstantLocation = uConstantLocation;
    }

    public int getuLinearLocation() {
        return uLinearLocation;
    }

    public void setuLinearLocation(int uLinearLocation) {
        this.uLinearLocation = uLinearLocation;
    }

    public int getuQuadraticLocation() {
        return uQuadraticLocation;
    }

    public void setuQuadraticLocation(int uQuadraticLocation) {
        this.uQuadraticLocation = uQuadraticLocation;
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
