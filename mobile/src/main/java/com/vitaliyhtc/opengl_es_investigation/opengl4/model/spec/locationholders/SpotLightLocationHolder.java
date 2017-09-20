package com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.locationholders;

/**
 * Created by vitaliyhtc on 20.09.17.
 */

public class SpotLightLocationHolder {

    private int uPositionLocation;
    private int uDirectionLocation;
    private int uCutOffLocation;
    private int uOuterCutOffLocation;
    private int uConstantLocation;
    private int uLinearLocation;
    private int uQuadraticLocation;
    private int uAmbientLocation;
    private int uDiffuseLocation;
    private int uSpecularLocation;

    public SpotLightLocationHolder(int uPositionLocation, int uDirectionLocation, int uCutOffLocation, int uOuterCutOffLocation, int uConstantLocation, int uLinearLocation, int uQuadraticLocation, int uAmbientLocation, int uDiffuseLocation, int uSpecularLocation) {
        this.uPositionLocation = uPositionLocation;
        this.uDirectionLocation = uDirectionLocation;
        this.uCutOffLocation = uCutOffLocation;
        this.uOuterCutOffLocation = uOuterCutOffLocation;
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

    public int getuDirectionLocation() {
        return uDirectionLocation;
    }

    public void setuDirectionLocation(int uDirectionLocation) {
        this.uDirectionLocation = uDirectionLocation;
    }

    public int getuCutOffLocation() {
        return uCutOffLocation;
    }

    public void setuCutOffLocation(int uCutOffLocation) {
        this.uCutOffLocation = uCutOffLocation;
    }

    public int getuOuterCutOffLocation() {
        return uOuterCutOffLocation;
    }

    public void setuOuterCutOffLocation(int uOuterCutOffLocation) {
        this.uOuterCutOffLocation = uOuterCutOffLocation;
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
