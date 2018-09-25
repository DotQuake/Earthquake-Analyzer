package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import java.util.ArrayList;

public class LoadDataController {
    public float direction;
    public float hypocenter;
    public boolean earthquakeDetected;
    public ArrayList<Float> x_values ;
    public ArrayList<Float> y_values ;
    public ArrayList<Float> z_values ;
    public float compassDegree;

    public void calculateHypocenter(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z,boolean earthquakeDetected){

    }
    public void calculateDirection(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z,float compassDegree,boolean earthquakeDetected){

    }
    public float getCompassData(){
        return 0;
    }
    public boolean detectEarthquake(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z){
        return true;
    }
}