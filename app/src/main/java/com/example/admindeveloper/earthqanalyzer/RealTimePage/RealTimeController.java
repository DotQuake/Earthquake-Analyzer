package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class RealTimeController {
    public float direction;
    public float hypocenter;
    public boolean earthquakeDetected;
    public boolean fileSaved;



    public void saveData(String filename, List<Float> d1, List<Float> d2, List<Float> d3,List<String> time){
        String fileName = filename+ ".csv";
        File myDir = new File(Environment.getExternalStorageDirectory()+"/SoftEng");
        myDir.mkdir();
        File file = new File(myDir,fileName);
        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);
        try {
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            OutputStreamWriter myoutwriter = new OutputStreamWriter(fos);
            myoutwriter.append(" X    Y    Z    \n");
            for (int i = 0; i < d1.size(); i++) {
                //myoutwriter.append(time.get(i));
                //myoutwriter.append("\t\t");
                myoutwriter.append(d1.get(i).toString());
                myoutwriter.append(",");
                myoutwriter.append(d2.get(i).toString());
                myoutwriter.append(",");
                myoutwriter.append(d3.get(i).toString());
                //myoutwriter.append(rawtimemilis.get(i).toString());
                myoutwriter.append("\n");
            }
            myoutwriter.close();
            fos.close();
            // Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(this,"File not found!",Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
            //Toast.makeText(this,"Error Saving",Toast.LENGTH_SHORT).show();
        }

    }
    public void calculateHypocenter(List<Float> x, List<Float> y, List<Float> z, boolean earthquakeDetected){

    }
    public void calculateDirection(float compassDegree,boolean earthquakeDetected){

    }
    public void updateXYZ(){

    }
    public boolean detectEarthquake(List<Float> x,List<Float> y,List<Float> z){
        return true;
    }
}
