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

    public String getDirection(float d){
        String where = "NO";
        if (degree >= 338 || degree <= 23)
            where = "N";
        if (degree < 338 && degree > 293)
            where = "NW";
        if (degree <= 293 && degree > 248)
            where = "W";
        if (degree <= 248 && degree > 180)
            where = "SW";
        if (degree <= 180 && degree > 157)
            where = "S";
        if (degree <= 157 && degree > 112)
            where = "SE";
        if (degree <= 112 && degree > 68)
            where = "E";
        if (degree <= 68 && degree > 23)
            where = "NE";

        return where;
    }

    public boolean isDeviceTurned() {
        return deviceTurned;
    }

}
