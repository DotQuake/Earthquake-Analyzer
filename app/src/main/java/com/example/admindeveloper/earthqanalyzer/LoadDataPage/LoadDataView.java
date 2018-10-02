package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.DisplayGraph;
import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import com.example.admindeveloper.earthqanalyzer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoadDataView extends Fragment implements SensorEventListener
{
    View myView;
    Button loadDatabtn;
    EarthQuakeDataClass data;
    LoadDataController controller;
    SensorManager senSensorManager;
    float compassDegree;
    public final int FILE_REQUEST_CODE=0;
    public LineChart rawDataGraph;
    public TextView hypocenterBox;
    public TextView directionBox;
    public TextView debugBox;
    DisplayGraph dG=new DisplayGraph();
    public void displayRawDataGraph(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z)
    {
        LineDataSet lineX,lineY,lineZ;
        //--------------------------------------------
        lineX = new LineDataSet(null, "X");
        lineX.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineX.setLineWidth(3f);
        lineX.setColor(Color.MAGENTA);
        lineX.setHighlightEnabled(false);
        lineX.setDrawValues(false);
        lineX.setDrawCircles(false);
        lineX.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineX.setCubicIntensity(0.2f);
        //--------------------------------------------
        lineY = new LineDataSet(null, "Y");
        lineY.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineY.setLineWidth(3f);
        lineY.setColor(Color.BLACK);
        lineY.setHighlightEnabled(false);
        lineY.setDrawValues(false);
        lineY.setDrawCircles(false);
        lineY.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineY.setCubicIntensity(0.2f);
        //--------------------------------------------
        lineZ = new LineDataSet(null, "Z");
        lineZ.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineZ.setLineWidth(3f);
        lineZ.setColor(Color.BLUE);
        lineZ.setHighlightEnabled(false);
        lineZ.setDrawValues(false);
        lineZ.setDrawCircles(false);
        lineZ.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineZ.setCubicIntensity(0.2f);
        //--------------------------------------------
        for(int count=0;count<x.size();count++)
        {
            lineX.addEntry(new Entry(count,x.get(count)));
            lineY.addEntry(new Entry(count,y.get(count)));
            lineZ.addEntry(new Entry(count,z.get(count)));
        }
        LineData lData=new LineData();
        lData.addDataSet(lineX);
        lData.addDataSet(lineY);
        lData.addDataSet(lineZ);
        rawDataGraph.setData(lData);
        rawDataGraph.notifyDataSetChanged();
        rawDataGraph.invalidate();
    }
    public void displayHypocenterBox(float distance)
    {
        hypocenterBox.setText("Hypocenter: "+distance);
    }
    public void displayDirectionBox(float degree)
    {
        String where=null;
        if (degree >= 338 || degree <= 23)
            where = "N";
        if (degree < 338 && degree > 293)
            where = "NW";
        if (degree <= 293 && degree > 248)
            where = "W";
        if (degree <= 248 && degree > 180)
            where = "SW";
        if (degree <= 180 && degree > 157)
            where = "S";
        if (degree <= 157 && degree > 112)
            where = "SE";
        if (degree <= 112 && degree > 68)
            where = "E";
        if (degree <= 68 && degree > 23)
            where = "NE";
        directionBox.setText("Direction: "+where);
    }
    public void openDocumentOpener(View view){
        Intent intent = new Intent(view.getContext(),FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setShowImages(false)
                .setShowAudios(false)
                .setShowFiles(true)
                .setShowVideos(false)
                .setSuffixes("csv")
                .setCheckPermission(true)
                .setMaxSelection(1)
                .build());
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        senSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.loaddata,container,false);
        hypocenterBox=myView.findViewById(R.id.hypocenter);
        directionBox=myView.findViewById(R.id.direction);
        loadDatabtn =  myView.findViewById(R.id.loadButton);
        rawDataGraph=myView.findViewById(R.id.rawDataChart);
        dG.setup(rawDataGraph);
        debugBox=myView.findViewById(R.id.debugView);
        debugBox.setMovementMethod(new ScrollingMovementMethod());
        loadDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDocumentOpener(view);
            }
        });
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case FILE_REQUEST_CODE:
            {
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                MediaFile file=files.get(0);
                if(file.getMediaType()==MediaFile.TYPE_FILE)
                {
                    this.data=LoadDataController.decodeCSVFile(file);
                }
                if(this.data!=null) {
                    for(int count=0;count<this.data.getEHZ().size();count++) {
                        dG.displayRawDataGraph(this.data.getEHE().get(count),this.data.getEHN().get(count),this.data.getEHZ().get(count),rawDataGraph);
                    }
                    //displayRawDataGraph(this.data.getEHE(),this.data.getEHN(),this.data.getEHZ());
                   /*if(LoadDataController.detectEarthquake(this.data))
                   {
                        displayHypocenterBox(LoadDataController.calculateHypocenter(this.data));
                        displayDirectionBox(LoadDataController.calculateDirection(this.data,compassDegree));
                   }*/
                }
                break;
            }
        }
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // get the angle around the z-axis rotated
            compassDegree = Math.round(sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
}