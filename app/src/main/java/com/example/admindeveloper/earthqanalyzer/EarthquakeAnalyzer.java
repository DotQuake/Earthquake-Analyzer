package com.example.admindeveloper.earthqanalyzer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EarthquakeAnalyzer {
    public static String detectEarthquake(EarthQuakeDataClass earthQuakeDataClass,float PWthreshold)
    {
        int definedNumberOfSamples=50,maxPWNumberOfSamples=50,maxSWNumberOfSamples=1000,initcount=0,startTime=0,endTime=0;
        Date currentTime = Calendar.getInstance().getTime();
        ArrayList<Float> arX=new ArrayList<>();
        ArrayList<Float> arY=new ArrayList<>();
        ArrayList<Float> arZ=new ArrayList<>();
        ArrayList<Float> sampleX=new ArrayList<>();
        ArrayList<Float> sampleY=new ArrayList<>();
        ArrayList<Float> sampleZ=new ArrayList<>();
        float averageX=0,averageY=0,averageZ=0,SWthresholdX=0,SWthresholdY=0,SWthresholdZ=0;
        boolean flagStartofSample=false,PWdetected=false,SWdetected=false;
        for(int count=0;count<earthQuakeDataClass.getEHE().size();count++)
        {
            if(count>definedNumberOfSamples)
            {
                sampleX.remove(0);
                sampleY.remove(0);
                sampleZ.remove(0);
            }
            sampleX.add(Math.abs(earthQuakeDataClass.getEHE().get(count)));
            sampleY.add(Math.abs(earthQuakeDataClass.getEHN().get(count)));
            sampleZ.add(Math.abs(earthQuakeDataClass.getEHZ().get(count)));
            for(int countSample=0;countSample<sampleX.size();countSample++)
            {
                averageX+=sampleX.get(countSample);
                averageY+=sampleY.get(countSample);
                averageZ+=sampleZ.get(countSample);
            }
            if(earthQuakeDataClass.getEHE().get(count)>PWthreshold||earthQuakeDataClass.getEHN().get(count)>PWthreshold||earthQuakeDataClass.getEHZ().get(count)>PWthreshold)
            {
                if(!flagStartofSample)
                {
                    flagStartofSample=true;
                }
            }
            if(arX.size()<maxPWNumberOfSamples)
            {
                flagStartofSample=false;
                if(averageX>PWthreshold||averageY>PWthreshold||averageZ>PWthreshold)
                {
                    PWdetected=true;
                    initcount=arX.size();
                    SWthresholdX=(float)(averageX+0.01);
                    SWthresholdY=(float)(averageY+0.01);
                    SWthresholdZ=(float)(averageZ+0.01);
                }
                else
                {
                    arX.clear();arY.clear();arZ.clear();
                }
            }
            if(flagStartofSample)
            {
                arX.add(earthQuakeDataClass.getEHE().get(count));
                arY.add(earthQuakeDataClass.getEHN().get(count));
                arZ.add(earthQuakeDataClass.getEHZ().get(count));
                startTime=currentTime.getSeconds();
            }
            if(PWdetected)
            {
                arX.add(earthQuakeDataClass.getEHE().get(count));
                arY.add(earthQuakeDataClass.getEHN().get(count));
                arZ.add(earthQuakeDataClass.getEHZ().get(count));
                if(averageX>=SWthresholdX||averageY>=SWthresholdY||averageZ>=SWthresholdZ)
                {
                    PWdetected=false;
                    SWdetected=true;
                    endTime=currentTime.getSeconds();
                }
                if((arX.size()-initcount)==maxSWNumberOfSamples)
                {
                    PWdetected=false;
                }
            }
        }
        if(SWdetected)
        {
            String str=(arX.size()-sampleX.size())+","+startTime+","+endTime+",";
            return str;
        }
        else
        {
            return null;
        }
    }
    public static float calculateHypocenter(int startTime,int endTime)
    {
        float hypocenter=(endTime-startTime)*4;
        return hypocenter;
    }
    public static float calculateDirection(EarthQuakeDataClass data,float compassDegree,int startCount)
    {
        float direction=0;
        double angle=Math.toDegrees(Math.atan(Math.abs((data.getEHN().get(startCount))/Math.abs((data.getEHE().get(startCount))))));
        if(data.getEHE().get(startCount)>0&&data.getEHN().get(startCount)>0) {
            direction=(float)(90-angle)+compassDegree;
        }
        else if(data.getEHE().get(startCount)>0&&data.getEHN().get(startCount)<0) {
            direction=(float)(90+angle)+compassDegree;
        }
        else if(data.getEHE().get(startCount)<0&&data.getEHN().get(startCount)>0) {
            direction=(float)(90-angle+180)+compassDegree;
        }
        else
        {
            direction=(float)(90-angle+270)+compassDegree;
        }
        return direction;
    }
}
