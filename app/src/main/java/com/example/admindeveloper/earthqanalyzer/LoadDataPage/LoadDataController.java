package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import com.example.admindeveloper.earthqanalyzer.CSVFileDecoder;
import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import com.example.admindeveloper.earthqanalyzer.EarthquakeAnalyzer;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class LoadDataController{

    private float hypocenter;
    private float direction;
    private EarthQuakeDataClass data;
    public LoadDataController()
    {

    }
    public EarthQuakeDataClass getEarthQuakeData()
    {
        return this.data;
    }
    public boolean initializeData(float PWThreshold,int compassDegree,MediaFile file)
    {
        this.data= CSVFileDecoder.decodeCSVFile(file);
        String[] val=EarthquakeAnalyzer.detectEarthquake(this.data,(float)0.2).split(",");
        if(val!=null)
        {
            int startCount=Integer.parseInt(val[0]);
            int startTime=Integer.parseInt(val[1]);
            int endTime=Integer.parseInt(val[2]);
            this.hypocenter= EarthquakeAnalyzer.calculateHypocenter(startTime,endTime);
            this.direction=EarthquakeAnalyzer.calculateDirection(this.data,compassDegree,startCount);
            return true;
        }
        return false;
    }
    public float getHypocenter()
    {
        return hypocenter;
    }
    public float getDirection()
    {
        return direction;
    }
}