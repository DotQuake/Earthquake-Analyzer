package com.example.admindeveloper.earthqanalyzer.CompassPage;

public class CompassPageController {
    public float degree;
    public boolean deviceTurned;
    public float computedDegree;
    public void setComputedDegree(float degree){this.computedDegree=degree;}
    public float getComputedDegree(){return this.computedDegree;}
    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public boolean isDeviceTurned() {
        return deviceTurned;
    }

}
