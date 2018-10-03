package com.example.admindeveloper.earthqanalyzer;

import java.util.ArrayList;
import java.util.List;

public class EarthQuakeDataClass
{
    private String siteName;
    private List<String> components;
    private String authority;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private float second;
    private int samplePerSecond;
    private int numberOfSamples;
    private int sync;
    private String syncSource;
    private ArrayList<Float> EHE;
    private ArrayList<Float> EHN;
    private ArrayList<Float> EHZ;
    public EarthQuakeDataClass(String siteName,List<String> components,String authority,int year,int month,int day,
                               int hour,int minute,float second,int samplePerSecond,int numberOfSamples,int sync,
                               String syncSource,ArrayList<Float> EHE,ArrayList<Float> EHN,ArrayList<Float> EHZ)
    {
        this.siteName=siteName;
        this.components=components;
        this.authority=authority;
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.minute=minute;
        this.second=second;
        this.samplePerSecond=samplePerSecond;
        this.numberOfSamples=numberOfSamples;
        this.sync=sync;
        this.syncSource=syncSource;
        this.EHE=EHE;
        this.EHN=EHN;
        this.EHZ=EHZ;
    }

    public String getSiteName() {
        return siteName;
    }

    public List<String> getComponents() {
        return components;
    }

    public String getAuthority() {
        return authority;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public float getSecond() {
        return second;
    }

    public int getSamplePerSecond() {
        return samplePerSecond;
    }

    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    public int getSync() {
        return sync;
    }

    public String getSyncSource() {
        return syncSource;
    }

    public ArrayList<Float> getEHE() {
        return EHE;
    }

    public ArrayList<Float> getEHN() {
        return EHN;
    }

    public ArrayList<Float> getEHZ() {
        return EHZ;
    }

    public void AppendData(float x, float y, float z)
    {
        this.EHE.add(x);
        this.EHZ.add(z);
        this.EHN.add(y);
        return;
    }
}

