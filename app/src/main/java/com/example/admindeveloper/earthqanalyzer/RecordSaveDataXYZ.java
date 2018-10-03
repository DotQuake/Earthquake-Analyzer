package com.example.admindeveloper.earthqanalyzer;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.spi.FileSystemProvider;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            myoutwriter.append("ARRIVALS,,,,");
            myoutwriter.append("#sitename,,,,");
            myoutwriter.append("#onset,,,,");
            myoutwriter.append("#first motion,,,,");
            myoutwriter.append("#phase,,,,");
            myoutwriter.append("#year month day,,,,");
            myoutwriter.append("#hour minute,,,,");
            myoutwriter.append("#second,,,,");
            myoutwriter.append("#uncertainty in seconds,,,,");
            myoutwriter.append("#peak amplitude,,,,");
            myoutwriter.append("#frequency at P phase,,,,");
            myoutwriter.append(",,,,");
            myoutwriter.append("TIME SERIES,,,,");
            myoutwriter.append("LLPS,LLPS,LLPS,#sitename");
            myoutwriter.append("EHE _,EHN _,EHZ _,#component");
            myoutwriter.append("unnamed,unnamed,unnamed,#authority");
            myoutwriter.append("20180828,20180828,20180828,#year month day,");
            myoutwriter.append("438,438,438,#hour minute,");
            myoutwriter.append("100,100,100,#samples per second,");
            myoutwriter.append("27126,27126,27126,#number of samples,");
            myoutwriter.append("0,0,0,#sync,");
            myoutwriter.append(",,,#sync source,");
            myoutwriter.append("--------,--------,--------,,");
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
    public void saveEarthquakeData(String authority,int samplesPerSecond)
    {
        Date currentTime = Calendar.getInstance().getTime();
        String fileName=(currentTime.getYear()+1900)+"-"+currentTime.getMonth()+"-"+currentTime.getDate()+"-"+currentTime.getHours()+currentTime.getMinutes()+"-"+currentTime.getSeconds()+".csv";
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
            bw.write(samplesPerSecond+","+samplesPerSecond+","+samplesPerSecond+",#samples per second,\r\n");
            bw.write(x_values.size()+","+x_values.size()+","+x_values.size()+",#number of samples,\r\n");
            bw.write("0,0,0,#sync,\r\n");
            bw.write(",,,#sync source,\r\n");
            bw.write("--------,--------,--------,,\r\n");
            for(int count=0;count<x_values.size();count++)
            {
                bw.write(x_values.get(count)+","+y_values.get(count)+","+z_values.get(count)+","+count+",");
                bw.write("\r\n");
                //bw.newLine();
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
