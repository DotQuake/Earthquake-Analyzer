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
        double angle=0;
        try
        {
            angle=Math.atan(y/x)*(360/2*Math.PI);
            if(angle/360>0)
            {
                angle=angle%360;
            }
            if(x<0&&y<0)
            {
                angle+=180;
            }
            if(angle<0)
            {
                angle+=360;
            }
        }
        catch (Exception e)
        {
            angle=90;
        }
        return (float) angle;
        /*degree = Math.round(degree);
        if (z < threshold) {


            if (x < -threshold && y < -threshold) {// pushed to SOUTHWEST
                return (degree + 135);
            } else if (x > threshold && y < -threshold) {// pushed to SOUTHEAST
                return (degree - 135);
            } else if (x < -threshold && y > threshold) {// pushed to NORTHWEST
                return (degree + 45);
            } else if (x > threshold && y > threshold) {// pushed to NORTHEAST
                return (degree - 45);
            } else  if (x > threshold ) { // pushed to EAST
                return (degree - 90);
            } else if (x < -threshold) { // pushed to WEST
                return (degree + 90);
            } else if (y > threshold) { // pushed to NORTH
                return (degree - 180);
            } else if (y < -threshold) {// pushed to SOUTH
                return (degree);}

        } else {


            if (x < -threshold && y < -threshold) {// pushed to SOUTHWEST
                return (degree - 135);
            } else if (x > threshold && y < -threshold) {// pushed to SOUTHEAST
                return (degree + 135);
            } else if (x < -threshold && y > threshold) {// pushed to NORTHWEST
                return (degree - 45);
            } else if (x > threshold && y > threshold) {// pushed to NORTHEAST
                return (degree + 45);
            }else if (x > threshold ) { // pushed to EAST
                return (degree + 90);
            } else if (x < -threshold ) { // pushed to WEST
                return (degree - 90);
            } else if ( y > threshold) { // pushed to NORTH
                return (degree);
            } else if ( y < -threshold) {// pushed to SOUTH
                return (degree - 180);}

        }
        return 90;*/
    }
    public String getDirection(float d){
        String where = "NO";
        d = Math.round(d);
        if (d >= 338 || d <= 23) {
            where = "North";
        }
        if (d < 338 && d > 293) {
            where = "NorthWest";
        }
        if (d <= 293 && d > 248) {
            where = "West";
        }
        if (d <= 248 && d > 202) {
            where = "SouthWest";
        }
        if (d <= 202 && d > 158) {
            where = "South";
        }
        if (d <= 158 && d > 112) {
            where = "SouthEast";
        }
        if (d <= 112 && d > 68) {
            where = "East";
        }
        if (d <= 68 && d > 23) {
            where = "NorthEast";
        }

        return where;
    }

    public boolean isDeviceTurned() {
        return deviceTurned;
    }

}
