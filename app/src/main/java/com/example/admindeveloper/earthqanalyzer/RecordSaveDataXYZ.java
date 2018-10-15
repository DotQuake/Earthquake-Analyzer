package com.example.admindeveloper.earthqanalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordSaveDataXYZ {

    private List<Float> x_values = new ArrayList<>();
    private List<Float> y_values = new ArrayList<>();
    private List<Float> z_values = new ArrayList<>();
    public void clearData()
    {
        x_values.clear();
        y_values.clear();
        z_values.clear();
    }
    public void recordData(float x, float y, float z){
        x_values.add(x);
        y_values.add(y);
        z_values.add(z);
    }
    public void saveEarthquakeData(String authority,int samplesPerSecond)
    {
        Date currentTime = Calendar.getInstance().getTime();
        String fileName=(currentTime.getYear()+1900)+"-"+(currentTime.getMonth()+1)+"-"+currentTime.getDate()+"-"+currentTime.getHours()+currentTime.getMinutes()+"-"+currentTime.getSeconds()+".csv";
        File myDir = new File("storage/emulated/0/Samples");
        if(!myDir.exists())
        {
            myDir.mkdirs();
        }
        File file = new File(myDir,fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write("ARRIVALS,,,,\r\n");
            bw.write("#sitename,,,,\r\n");
            bw.write("#onset,,,,\r\n");
            bw.write("#first motion,,,,\r\n");
            bw.write("#phase,,,,\r\n");
            bw.write("#year month day,,,,\r\n");
            bw.write("#hour minute,,,,\r\n");
            bw.write("#second,,,,\r\n");
            bw.write("#uncertainty in seconds,,,,\r\n");
            bw.write("#peak amplitude,,,,\r\n");
            bw.write("#frequency at P phase,,,,\r\n");
            bw.write(",,,,\r\n");
            bw.write("TIME SERIES,,,,\r\n");
            bw.write("LLPS,LLPS,LLPS,#sitename,\r\n");
            bw.write("EHE _,EHN _,EHZ _,#component,\r\n");
            bw.write(authority+","+authority+","+authority+",#authority,\r\n");
            String hold=(currentTime.getYear()+1900)+"";
            hold=(currentTime.getMonth()+1)<=9?hold+"0"+(currentTime.getMonth()+1):hold+(currentTime.getMonth()+1);
            hold=currentTime.getDate()<=9?hold+"0"+currentTime.getDate():""+hold+currentTime.getDate();
            bw.write(hold+","+hold+","+hold+",#year month day,\r\n");
            hold=currentTime.getHours()+"";
            hold=currentTime.getMinutes()<=9?hold+"0"+currentTime.getMinutes():""+hold+currentTime.getMinutes();
            bw.write(hold+","+hold+","+hold+",#hour minute,\r\n");
            bw.write(currentTime.getSeconds()+","+currentTime.getSeconds()+","+currentTime.getSeconds()+",#second,\r\n");
            bw.write(samplesPerSecond+","+samplesPerSecond+","+samplesPerSecond+",#samples per second,\r\n");
            bw.write(x_values.size()+","+x_values.size()+","+x_values.size()+",#number of samples,\r\n");
            bw.write("0,0,0,#sync,\r\n");
            bw.write(",,,#sync source,\r\n");
            bw.write("g,g,g,g,\r\n");
            bw.write("--------,--------,--------,,\r\n");
            for(int count=0;count<x_values.size();count++)
            {
                bw.write(x_values.get(count)+","+y_values.get(count)+","+z_values.get(count)+","+count+",");
                bw.write("\r\n");
            }
            bw.write("       ,       ,       ,,\r\n" +
                    "       ,       ,       ,,\r\n" +
                    "END,END,END,,");
            bw.flush();
            bw.close();
            fos.flush();
            fos.close();
            fos.notifyAll();
            x_values.clear();
            y_values.clear();
            z_values.clear();
            file.notifyAll();
            myDir.notifyAll();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
