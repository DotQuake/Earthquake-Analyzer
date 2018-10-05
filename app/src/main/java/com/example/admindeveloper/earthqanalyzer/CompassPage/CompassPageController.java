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

    public float calculateDirection(float x, float y,float z ,float threshold,float degree) {
// From the phone's point of view
        if (z > threshold) {

            if (x > threshold && y < threshold) { // pushed to EAST
                return (degree - 90);
            } else if (x < -threshold && y < threshold) { // pushed to WEST
                return (degree + 90);
            } else if (x < threshold && y > threshold) { // pushed to NORTH
                return (degree - 180);
            } else if (x < threshold && y < -threshold) {// pushed to SOUTH
                return (degree);
            } else if (x < -threshold && y < -threshold) {// pushed to SOUTHWEST
                return (degree + 135);
            } else if (x > threshold && y < -threshold) {// pushed to SOUTHEAST
                return (degree - 135);
            } else if (x < -threshold && y > threshold) {// pushed to NORTHWEST
                return (degree + 45);
            } else if (x > threshold && y > threshold) {// pushed to NORTHEAST
                return (degree - 45);
            }

        } else {

            if (x > threshold && y < threshold) { // pushed to EAST
                return (degree + 90);
            } else if (x < -threshold && y < threshold) { // pushed to WEST
                return (degree - 90);
            } else if (x < threshold && y > threshold) { // pushed to NORTH
                return (degree);
            } else if (x < threshold && y < -threshold) {// pushed to SOUTH
                return (degree - 180);
            } else if (x < -threshold && y < -threshold) {// pushed to SOUTHWEST
                return (degree - 135);
            } else if (x > threshold && y < -threshold) {// pushed to SOUTHEAST
                return (degree + 135);
            } else if (x < -threshold && y > threshold) {// pushed to NORTHWEST
                return (degree - 45);
            } else if (x > threshold && y > threshold) {// pushed to NORTHEAST
                return (degree + 45);
            }
        }
        return 90;
    }
    public String getDirection(float d){
        String where = "NO";
        d = Math.round(d);
        if (d >= 338 || d <= 23) {
            where = "N";
        }
        if (d < 338 && d > 293) {
            where = "NW";
        }
        if (d <= 293 && d > 248) {
            where = "W";
        }
        if (d <= 248 && d > 180) {
            where = "SW";
        }
        if (d <= 180 && d > 157) {
            where = "S";
        }
        if (d <= 157 && d > 112) {
            where = "SE";
        }
        if (d <= 112 && d > 68) {
            where = "E";
        }
        if (d <= 68 && d > 23) {
            where = "NE";
        }

        return where;
    }

    public boolean isDeviceTurned() {
        return deviceTurned;
    }

}
