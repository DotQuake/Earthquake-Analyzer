package com.example.admindeveloper.earthqanalyzer.LoadDataPage;
import android.os.CountDownTimer;
import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;
import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import java.util.ArrayList;

public class LoadDataController{

    public static float calculateHypocenter(EarthQuakeDataClass data)
    {
        float hypocenter=0;
        return hypocenter;
    }
    public static float calculateDirection(EarthQuakeDataClass data,float compassDegree,float threshold,float earthQuakeSampling)
    {
        CompassPageController controller=new CompassPageController();
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
            controller.setDegree(direction);
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
}