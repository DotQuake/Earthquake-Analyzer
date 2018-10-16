package com.example.admindeveloper.earthqanalyzer;

import android.os.CountDownTimer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EarthquakeAnalyzer{
    private float SWThreshold;
    private float PWThreshold;
    private float highestRecordThreshold;
    private int maxNumberOfSamples;
    private ArrayList<Float> x;
    private ArrayList<Float> y;
    private ArrayList<Float> z;
    private String status;
    private Date startDate;
    private Date endDate;
    private float startX;
    private float startY;
    private float startZ;
    private float difference;
    private boolean startSampling;
    private CountDownTimer delay=null;
    private boolean delaybool=false;

    public ArrayList<Float> getListY(){return this.y;}
    public ArrayList<Float> getListZ(){return this.z;}
    public ArrayList<Float> getListX(){return this.x;}
    public void setPWThreshold(float PWThreshold)
    {this.PWThreshold=PWThreshold;return;}
    public float getPWThreshold()
    {return this.PWThreshold;}
    public void setMaxNumberOfSamples(int maxNumberOfSamples)
    {this.maxNumberOfSamples=maxNumberOfSamples;return;}
    public int getMaxNumberOfSamples()
    {return this.maxNumberOfSamples;}
    public String getStatus(){return this.status;}
    public float getHighestRecordThreshold(){return this.highestRecordThreshold;}



    public EarthquakeAnalyzer(float Threshold,int maxNumberOfSamples,float differenceSWAndPW)
    {
        this.PWThreshold=Threshold;
        this.SWThreshold=PWThreshold+differenceSWAndPW;
        this.maxNumberOfSamples=maxNumberOfSamples;
        this.difference=differenceSWAndPW;
        x=new ArrayList<>();
        y=new ArrayList<>();
        z=new ArrayList<>();
        status="DETERMINEPW";
        startSampling=false;
        this.startDate=null;
        this.endDate=null;
    }
    public void startDelay(){
        if(delay==null){
            delay = new CountDownTimer(1000, 200) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    delaybool = true;
                    delay = null;
                }
            }.start();
        }
    }

    public String detectEarthquake(float x,float y,float z)
    {
        String result=null;
        if(!delaybool)
        {
            startDelay();
        }
        else {
            if (this.x.size() > this.maxNumberOfSamples) {
                startSampling = true;
                this.x.remove(0);
                this.y.remove(0);
                this.z.remove(0);
            }
            this.x.add(x);
            this.y.add(y);
            this.z.add(z);
            if (startSampling) {
                switch (status) {
                    case "DETERMINEPW": {
                        determinePrimaryWave();
                        result = null;
                        if((Math.abs(x)>PWThreshold&&Math.abs(startX)<Math.abs(x)&&Math.abs(startX)>=Math.abs(startY))||(Math.abs(y)>PWThreshold&&Math.abs(startY)<Math.abs(y)&&Math.abs(startY)>=Math.abs(startX)))
                        {
                            startX=x;
                            startY=y;
                            startZ=z;
                        }
                        break;
                    }
                    case "PWTHRESHOLDEXCEED": {
                        calculatePrimaryWave();
                        result = null;
                        break;
                    }
                    case "DETERMINESW": {
                        result = null;
                        calculateSecondaryWave();
                        break;
                    }
                    case "EARTHQUAKEDETECTED": {
                        int seconds = (endDate.getMinutes()*60+endDate.getSeconds()) - (startDate.getMinutes()*60+startDate.getSeconds());
                        result = startX + "," + startY + "," + startZ + "," + seconds + ",";
                        SWonFinish();
                        break;
                    }
                    case "FINISH":
                    {
                        result=null;
                        break;
                    }
                }
            }
        }
        return result;
    }
    private void determinePrimaryWave()
    {
        Float[] values=getAverageOfSamples();
        if(values[0]>PWThreshold||values[1]>PWThreshold||values[2]>PWThreshold)
        {
            status="PWTHRESHOLDEXCEED";
            startDate=Calendar.getInstance().getTime();
            highestRecordThreshold=PWThreshold;
        }
        else {
            status = "DETERMINEPW";
        }
    }
    private void calculatePrimaryWave()
    {
        Float[] values=getAverageOfSamples();
        if(values[0]<=PWThreshold&&values[1]<=PWThreshold&&values[2]<=PWThreshold)
        {
            status="DETERMINESW";
            SWThreshold=highestRecordThreshold+difference;
        }
        else {
            highestRecordThreshold = values[0] > highestRecordThreshold ? values[0] : highestRecordThreshold;
            highestRecordThreshold = values[1] > highestRecordThreshold ? values[1] : highestRecordThreshold;
            highestRecordThreshold = values[2] > highestRecordThreshold ? values[2] : highestRecordThreshold;
            status = "PWTHRESHOLDEXCEED";
        }
    }
    private void calculateSecondaryWave()
    {
        Float[] values=getAverageOfSamples();
        if(values[0]>SWThreshold||values[1]>SWThreshold||values[2]>SWThreshold)
        {
            status="EARTHQUAKEDETECTED";
            endDate=Calendar.getInstance().getTime();
        }
        else {
            status = "DETERMINESW";
        }
    }
    private void SWonFinish()
    {
        Float[] values=getAverageOfSamples();
        if(values[0]<=SWThreshold&&values[1]<=SWThreshold&&values[2]<=SWThreshold)
        {
            status="FINISH";
        }
        else {
            status = "EARTHQUAKEDETECTED";
        }
    }
    public Float[] getAverageOfSamples()
    {
        float averageX=0,averageY=0,averageZ=0;
        for(int count=0;count<this.x.size();count++)
        {
            averageX+=Math.abs(this.x.get(count));
            averageY+=Math.abs(this.y.get(count));
            averageZ+=Math.abs(this.z.get(count));
        }
        averageX/=maxNumberOfSamples;
        averageY/=maxNumberOfSamples;
        averageZ/=maxNumberOfSamples;
        return new Float[]{averageX,averageY,averageZ};
    }
}
