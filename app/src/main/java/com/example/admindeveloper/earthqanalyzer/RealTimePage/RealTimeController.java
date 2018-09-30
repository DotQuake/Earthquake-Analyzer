package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;

import java.util.List;

public class RealTimeController {
    public float direction;
    public float hypocenter;
    public boolean earthquakeDetected=false;
    public boolean fileSaved;
    private final float alpha = 0.8f;
    private float[] gravity = {0,0,0};
    private float[] linear_acceleration = {0,0,0};
    private float x,y,z;
    private CompassPageController cpc = new CompassPageController();


    public void updateXYZ(float x , float y ,float z){
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        linear_acceleration[0] = x - gravity[0];
        this.x = linear_acceleration[0];

        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        linear_acceleration[1] = y - gravity[1];
        this.y = linear_acceleration[1];

        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;
        linear_acceleration[2] = z - gravity[2];
        this.z = linear_acceleration[2];

        earthquakeDetected = detectEarthquake(this.x,this.y,this.z);
        if(earthquakeDetected){
            //calculateHypocenter();
            calculateDirection(cpc.getDegree());
        }
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getZ() {
        return z;
    }
    public void calculateHypocenter(List<Float> x, List<Float> y, List<Float> z, boolean earthquakeDetected){

    }
    public void calculateDirection(float compassData){

    }
    public float getCompassData(){
        return cpc.getDegree();
    }
    public boolean detectEarthquake(float x,float y,float z){
        return true;
    }
}
