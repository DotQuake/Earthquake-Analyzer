package com.example.admindeveloper.earthqanalyzer;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class RecordSaveDataXYZ {

    private List<Float> x_values = new ArrayList<>();
    private List<Float> y_values = new ArrayList<>();
    private List<Float> z_values = new ArrayList<>();
    public List<Float> getX_values() {
        return x_values;
    }
    public List<Float> getY_values() {
        return y_values;
    }
    public List<Float> getZ_values() {
        return z_values;
    }
    public void recordData(float x, float y, float z){
        x_values.add(x);
        y_values.add(y);
        z_values.add(z);
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
}
