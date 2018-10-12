package com.example.admindeveloper.earthqanalyzer;

import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVFileDecoder {


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
            String[] decodeString=new String[4];
            while((line=bfr.readLine())!=null&&!flagDone)
            {
                decodeString=line.split(",");
                if(line.contentEquals("END,END,END,,"))
                {
                    flagStartOfSamples=false;
                    flagDone=true;
                }
                if(flagStartOfSamples)
                {
                    try {
                        EHE.add(Float.parseFloat(decodeString[0]));
                        EHN.add(Float.parseFloat(decodeString[1]));
                        EHZ.add(Float.parseFloat(decodeString[2]));
                    }
                    catch(Exception e){}
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
        return new EarthQuakeDataClass(siteName,components,authority,year,month,day,hour,minute,second,samplePerSecond,numberOfSamples,sync,syncSource,EHE,EHN,EHZ);
    }
}
