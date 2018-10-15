package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;
import com.example.admindeveloper.earthqanalyzer.EarthquakeAnalyzer;

public class RealTimeController {
    private String direction;
    private float hypocenter;
    private final float alpha = 0.8f;
    private float[] gravity = {0,0,0};
    private float[] linear_acceleration = {0,0,0};
    private float x,y,z;
    private EarthquakeAnalyzer ea=null;

    public String getDirection(){return this.direction;}
    public float getHypocenter(){return this.hypocenter;}
    public void initializeAnalyzer(float Threshold, int maxNumberOfSamples, float differenceSWAndPW)
    {
            ea=new EarthquakeAnalyzer(Threshold, maxNumberOfSamples, differenceSWAndPW);
    }
    public boolean updateXYZ(float x , float y ,float z, CompassPageController cpc){
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        linear_acceleration[0] = x - gravity[0];
        this.x = linear_acceleration[0];

        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        linear_acceleration[1] = y - gravity[1];
        this.y = linear_acceleration[1];

        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;
        linear_acceleration[2] = z - gravity[2];
        this.z = linear_acceleration[2];
        return detectEarthquake(cpc);
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
    public void calculateHypocenter(Float seconds)
    {
        this.hypocenter=seconds*8;
    }
    public String getStatus()
    {
        if(ea!=null)
        {
            return ea.getStatus();
        }
        return null;
    }
    public boolean detectEarthquake(CompassPageController cpc2){
        if(ea!=null) {
            String result=ea.detectEarthquake(this.x, this.y, this.z);
            try {
                if(ea.getStatus()=="EARTHQUAKEDETECTED") {
                    String[] values = result.split(",");
                    Float startX = Float.parseFloat(values[0]);
                    Float startY = Float.parseFloat(values[1]);
                    Float startZ = Float.parseFloat(values[2]);
                    Integer seconds = Integer.parseInt(values[3]);
                    this.calculateHypocenter((float) seconds);
                    direction=cpc2.getDirection(cpc2.calculateDirection(startX,startY,startZ,0.3F,0))+"\r\n"+startX+"\r\n"+startY+"\r\n"+startZ;
                    return true;
                }
            }
            catch(Exception e){}
        }
        return false;
    }
}
