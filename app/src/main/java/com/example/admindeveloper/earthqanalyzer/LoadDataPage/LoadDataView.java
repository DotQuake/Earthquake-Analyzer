package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.R;
import com.github.mikephil.charting.charts.LineChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoadDataView extends Fragment
{
    View myView;
    EditText outputdata;
    TextView fileName;
    Button loadDatabtn;
    int index;
    private List<Float> x_values = new ArrayList<>();
    private List<Float> y_values = new ArrayList<>();
    private List<Float> z_values = new ArrayList<>();


    public LineChart rawDataGraph;
    public TextView hypocenterBox;
    public TextView directionBox;
    //public ActionOpenDocument documentOpener;

    public void displayRawDataGraph(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z){

    }
    public void displayHypocenterBox(float distance){

    }
    public void displayDirectionBox(float degree){

    }
    public void openDocumentOpener(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.loaddata,container,false);
        outputdata = (EditText) myView.findViewById(R.id.multilinetx);
        fileName = (TextView) myView.findViewById(R.id.filenametx);
        loadDatabtn = (Button) myView.findViewById(R.id.loadbtn);
        loadDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFile();
                printdata();
            }
        });
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        return myView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void  readFile(){
        try {
            File myDir = new File(Environment.getExternalStorageDirectory()+"/SoftEng");
            myDir.mkdir();
            File file = new File(myDir,fileName.getText().toString());
            //File file = new File(Environment.getExternalStorageDirectory(),filename.getText().toString());

            FileInputStream fin =new FileInputStream(file);
            InputStreamReader inputStream = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String line = null;
            bufferedReader.readLine();
            while ((line= bufferedReader.readLine())!=null){
                String[] tokens = line.split(",");
                if(tokens[0].length()>0) {
                    x_values.add(Float.parseFloat(tokens[0]));
                }else{
                    x_values.add(0f);
                }
                if(tokens[1].length()>0) {
                    y_values.add(Float.parseFloat(tokens[1]));
                }else{
                    y_values.add(0f);
                }
                if(tokens.length >= 3 && tokens[2].length()>0) {
                    z_values.add(Float.parseFloat(tokens[1]));
                }else{
                    z_values.add(0f);
                }
            }
            fin.close();
            inputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void printdata() {
        for(index=0;index<x_values.size();index++) {
            outputdata.append(x_values.get(index)+"  \t"+y_values.get(index)+"\t"+z_values.get(index)+"\n");
        }
    }

}