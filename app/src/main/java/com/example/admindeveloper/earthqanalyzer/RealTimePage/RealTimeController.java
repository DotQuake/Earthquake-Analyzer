package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import android.os.Environment;

import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class RealTimeController {
    public float direction;
    public float hypocenter;
    public boolean earthquakeDetected=false;
    public boolean fileSaved;
    private List<Float> x_values = new ArrayList<>();
    private List<Float> y_values = new ArrayList<>();
    private List<Float> z_values = new ArrayList<>();
    private final float alpha = 0.8f;
    private float[] gravity = {0,0,0};
    private float[] linear_acceleration = {0,0,0};
    private float x,y,z;
    private CompassPageController cpc = new CompassPageController();


    public void updateXYZ(float x , float y ,float z){
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        linear_acceleration[0] = x - gravity[0];
        this.x = linear_acceleration[0];

        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        linear_acceleration[1] = y - gravity[1];
        this.y = linear_acceleration[1];

        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;
        linear_acceleration[2] = z - gravity[2];
        this.z = linear_acceleration[2];

        earthquakeDetected = detectEarthquake(this.x,this.y,this.z);
        if(earthquakeDetected){
            //calculateHypocenter();
            calculateDirection(cpc.getDegree());
        }
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
    public void recordData(){
        x_values.add(this.x);
        y_values.add(this.y);
        z_values.add(this.z);
    }
    public void saveData(String filename){
        String fileName = filename+ ".csv";
        File myDir = new File(Environment.getExternalStorageDirectory()+"/SoftEng");
        myDir.mkdir();
        File file = new File(myDir,fileName);
        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);
        try {
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            OutputStreamWriter myoutwriter = new OutputStreamWriter(fos);
            myoutwriter.append(" X,Y,Z    \n");
            for (int i = 0; i < x_values.size(); i++) {
                //myoutwriter.append(time.get(i));
                //myoutwriter.append("\t\t");
                myoutwriter.append(x_values.get(i).toString());
                myoutwriter.append(",");
                myoutwriter.append(y_values.get(i).toString());
                myoutwriter.append(",");
                myoutwriter.append(z_values.get(i).toString());
                //myoutwriter.append(rawtimemilis.get(i).toString());
                myoutwriter.append("\n");
            }
            myoutwriter.close();
            fos.close();
            x_values.clear();
            y_values.clear();
            z_values.clear();
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
    public void calculateDirection(float compassData){

    }
    public float getCompassData(){return 0;}
    public boolean detectEarthquake(float x,float y,float z){
        return true;
    }
}
