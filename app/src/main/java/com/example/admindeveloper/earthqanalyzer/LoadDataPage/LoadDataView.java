package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.CSVFileDecoder;
import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;
import com.example.admindeveloper.earthqanalyzer.DisplayGraph;
import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import com.example.admindeveloper.earthqanalyzer.MediaRescan;
import com.example.admindeveloper.earthqanalyzer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

//import com.nbsp.materialfilepicker.MaterialFilePicker;

public class LoadDataView extends Fragment implements SensorEventListener
{
    View myView;
    Button loadDatabtn;
    EarthQuakeDataClass data;
    LoadDataController controller;
    SensorManager senSensorManager;
    CompassPageController cpc;
    float compassDegree;
    public final int FILE_REQUEST_CODE=0;
    public LineChart rawDataGraph;
    public TextView hypocenterBox;
    public TextView directionBox;
    public TextView debugBox;
    DisplayGraph dG=new DisplayGraph();
    public void displayHypocenterBox(float distance)
    {
        hypocenterBox.setText("Hypocenter: "+distance);
    }
    public void displayDirectionBox(float degree)
    {
        String where=null;
        where = cpc.getDirection(degree);
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
        cpc = new CompassPageController();
        dG.setup(rawDataGraph);
        //debugBox=myView.findViewById(R.id.debugView);
        //debugBox.setMovementMethod(new ScrollingMovementMethod());
        loadDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDocumentOpener(view);
                //OpenDocumentOpener2();
            }
        });
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        MediaRescan mr = new MediaRescan();
        return myView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case FILE_REQUEST_CODE:
            {
                dG.clearData(rawDataGraph);
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                MediaFile file=files.get(0);
                if(file.getMediaType()==MediaFile.TYPE_FILE)
                {
                    this.data= CSVFileDecoder.decodeCSVFile(file);
                    hypocenterBox.setText(file.getPath());
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