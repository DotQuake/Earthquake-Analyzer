package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;

public class LoadDataController{

    private CountDownTimer countDown;

    CheckBox chkbox;

    public LoadDataController()
    {

    }
    public static float calculateHypocenter(EarthQuakeDataClass data)
    {
        float hypocenter=0;
        return hypocenter;
        //chkbox.check
    }
    public static float calculateDirection(EarthQuakeDataClass data,float compassDegree,float threshold,float earthQuakeSampling)
    {
        float direction=0;
        boolean flagStartSampling=false,flagStartEarthqyake;
        CountDownTimer countTimer;
        ArrayList<Float> EHE= new ArrayList<>();
        ArrayList<Float> EHZ=new ArrayList<>();
        ArrayList<Float> EHN=new ArrayList<>();
        ArrayList<Integer> earthQuakeIndexes=new ArrayList<Integer>();
        //checking the earthquake
        for(int count=0;count<data.getEHZ().size();count++)
        {
            if(data.getEHZ().get(count)>threshold||data.getEHN().get(count)>threshold||data.getEHZ().get(count)>threshold)
            {

            }
        }
        //----------------------
        for(int count=0;count<data.getEHE().size();count++)
        {
            if(data.getEHE().get(count)>threshold||data.getEHN().get(count)>threshold||data.getEHZ().get(count)>threshold||flagStartSampling)
            {
                flagStartSampling=true;
                if(EHE.size()<=earthQuakeSampling) {
                    EHE.add(data.getEHE().get(count));
                    EHZ.add(data.getEHZ().get(count));
                    EHN.add(data.getEHN().get(count));
                }
                else {
                    flagStartSampling=false;
                }
                if(!flagStartSampling) {
                    double angle=Math.toDegrees(Math.atan((float)Math.abs(EHN.get(0))/(float)Math.abs(EHE.get(0))));
                    if(EHE.get(0)>0&&EHN.get(0)>0) {
                        direction=(float)(90-angle)+compassDegree;
                    }
                    else if(EHE.get(0)>0&&EHN.get(0)<0) {
                        direction=(float)(90+angle)+compassDegree;
                    }
                    else if(EHE.get(0)<0&&EHN.get(0)>0) {
                        direction=(float)(90-angle+180)+compassDegree;
                    }
                    else
                    {
                        direction=(float)(90-angle+270)+compassDegree;
                    }
                    return direction;
                }
            }
        }
        return direction;
    }
    public float getCompassData(){
        return 0;
    }
    public static boolean detectEarthquake(EarthQuakeDataClass data){
        boolean earthQuakeDetected=true;

        return earthQuakeDetected;
    }
    public static EarthQuakeDataClass decodeCSVFile(MediaFile file)
    {
        String siteName=null;
        ArrayList<String> components=new ArrayList<>();
        String authority=null;
        int year=0;
        int month=0;
        int day=0;
        int hour=0;
        int minute=0;
        float second=0;
        int samplePerSecond=0;
        int numberOfSamples=0;
        int sync=0;
        String syncSource=null;
        ArrayList<Float> EHE=new ArrayList<>();
        ArrayList<Float> EHN=new ArrayList<>();
        ArrayList<Float> EHZ=new ArrayList<>();
        boolean flagStartOfSamples=false,flagStartInitialization=false,flagDone=false;
        try
        {
            FileInputStream fis = new FileInputStream(file.getPath());
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            String line=null;
            String[] decodeString=null;
            while((line=bfr.readLine())!=null&&!flagDone)
            {
                decodeString=line.split(",");
                if(line.contentEquals("       ,       ,       ,,"))
                {
                    flagStartOfSamples=false;
                    flagDone=true;
                }
                if(flagStartOfSamples)
                {
                    EHE.add(Float.parseFloat(decodeString[0]));
                    EHN.add(Float.parseFloat(decodeString[1]));
                    EHZ.add(Float.parseFloat(decodeString[2]));
                }
                if(line.contentEquals("--------,--------,--------,,"))
                {
                    flagStartInitialization=false;
                    flagStartOfSamples=true;
                }
                if(flagStartInitialization)
                {
                    if(decodeString[3].contentEquals("#sitename"))
                    {
                        siteName=decodeString[0];
                    }
                    else if(decodeString[3].contentEquals("#component"))
                    {
                        components.add(decodeString[0]);
                        components.add(decodeString[1]);
                        components.add(decodeString[2]);
                    }
                    else if(decodeString[3].contentEquals("#authority"))
                    {
                        authority=decodeString[0];
                    }
                    else if(decodeString[3].contentEquals("#year month day"))
                    {
                        year=Integer.parseInt((String)decodeString[0].subSequence(0,4));
                        month=Integer.parseInt((String)decodeString[0].subSequence(4,6));
                        day=Integer.parseInt((String)decodeString[0].subSequence(6,8));
                    }
                    else if(decodeString[3].contentEquals("#hour minute"))
                    {
                        if(decodeString[0].length()%2==0)
                        {
                            hour=Integer.parseInt((String)decodeString[0].subSequence(0,2));
                            minute=Integer.parseInt((String)decodeString[0].subSequence(2,4));
                        }
                        else
                        {
                            hour=Character.getNumericValue(decodeString[0].charAt(0));
                            minute=Integer.parseInt((String)decodeString[0].subSequence(1,3));
                        }
                    }
                    else if(decodeString[3].contentEquals("#second"))
                    {
                        second=Float.parseFloat(decodeString[0]);
                    }
                    else if(decodeString[3].contentEquals("#samples per second"))
                    {
                        samplePerSecond=Integer.parseInt(decodeString[0]);
                    }
                    else if(decodeString[3].contentEquals("#number of samples"))
                    {
                        numberOfSamples=Integer.parseInt(decodeString[0]);
                    }
                    else if(decodeString[3].contentEquals("#sync"))
                    {
                        sync=Integer.parseInt(decodeString[0]);
                    }
                    else if(decodeString[3].contentEquals("#sync source"))
                    {
                        syncSource=decodeString[0];
                    }
                    else
                    {
                        throw new Exception("Unknown Command! "+decodeString[3]);
                    }
                }
                if(line.contentEquals("TIME SERIES,,,,"))
                {
                    flagStartInitialization=true;
                }

            }
            fis.close();
        }
        catch(FileNotFoundException e)
        {
            return null;
        }
        catch(IOException e)
        {
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
        //return null;
        return new EarthQuakeDataClass(siteName,components,authority,year,month,day,hour,minute,second,samplePerSecond,numberOfSamples,sync,syncSource,EHE,EHN,EHZ);
    }


}