package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import com.example.admindeveloper.earthqanalyzer.CSVFileDecoder;
import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;
import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import com.example.admindeveloper.earthqanalyzer.EarthquakeAnalyzer;
import com.jaiselrahman.filepicker.model.MediaFile;

public class LoadDataController{

    private float hypocenter;
    private String direction;
    private EarthQuakeDataClass data;
    private String status;
    public LoadDataController()
    {

    }
    public String getStatus(){return this.status;}
    public void calculateHypocenter(Float seconds){this.hypocenter=seconds*8;}
    public EarthQuakeDataClass getEarthQuakeData()
    {
        return this.data;
    }
    public boolean initializeData(MediaFile file,float Threshold, int maxNumberOfSamples,float differenceSWAndPW)
    {
        this.data= CSVFileDecoder.decodeCSVFile(file);
        EarthquakeAnalyzer ea=new EarthquakeAnalyzer(Threshold, maxNumberOfSamples, differenceSWAndPW);
        if(this.data!=null)
        {
            try {
                for(int count=0;count < this.data.getEHE().size();count++)
                {
                    CompassPageController cpc=new CompassPageController();
                    String result =ea.detectEarthquake(this.data.getEHE().get(count), this.data.getEHN().get(count), this.data.getEHZ().get(count));
                    {
                        this.status=ea.getStatus();
                        if(ea.getStatus()=="EARTHQUAKEDETECTED")
                        {
                            String[] values=result.split(",");
                            Float startX=Float.parseFloat(values[0]);
                            Float startY=Float.parseFloat(values[1]);
                            Float startZ=Float.parseFloat(values[2]);
                            Integer seconds=Integer.parseInt(values[3]);
                            this.calculateHypocenter((float)seconds);
                            this.direction=cpc.getDirection(cpc.calculateDirection(startX, startY, startZ, 0, 0));
                            return true;
                        }
                        else if(ea.getStatus()=="FINISH")
                        {
                            return true;
                        }
                    }
                }
            }
            catch(Exception e)
            {
            }
        }
        return false;
    }
    public float getHypocenter()
    {
        return hypocenter;
    }
    public String getDirection()
    {
        return direction;
    }
}