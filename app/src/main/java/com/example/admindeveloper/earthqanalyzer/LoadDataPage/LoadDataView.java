package com.example.admindeveloper.earthqanalyzer.LoadDataPage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;
import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;
import com.example.admindeveloper.earthqanalyzer.DisplayGraph;
import com.example.admindeveloper.earthqanalyzer.EarthQuakeDataClass;
import com.example.admindeveloper.earthqanalyzer.EarthquakeAnalyzer;
import com.example.admindeveloper.earthqanalyzer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class LoadDataView extends Fragment implements SensorEventListener
{
    View myView;
    Button loadDatabtn;
    SensorManager senSensorManager;
    CompassPageController cpc;
    float compassDegree;
    public final int FILE_REQUEST_CODE=0;
    public LineChart rawDataGraph;
    DisplayGraph dG=new DisplayGraph();
    EarthquakeAnalyzer ea;
    public void openDocumentOpener(View view){
            Intent intent = new Intent(view.getContext(), FilePickerActivity.class);
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
        loadDatabtn =  myView.findViewById(R.id.loadButton);
        rawDataGraph=myView.findViewById(R.id.rawDataChart);
        cpc = new CompassPageController();
        dG.setup(rawDataGraph);
        ea=new EarthquakeAnalyzer((float)0.3,150 ,(float)0.1 );
        loadDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ea=new EarthquakeAnalyzer((float)0.3,150 ,(float)0.1 );
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
                try {
                    dG.clearData(rawDataGraph);
                    ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    MediaFile file = files.get(0);
                    if (file.getMediaType() == MediaFile.TYPE_FILE) {
                        LoadDataController ld = new LoadDataController();
                        ld.initializeData(file, 0.3F, 50, 0.1F);
                        EarthQuakeDataClass earthQuakeData = ld.getEarthQuakeData();
                        for (int count = 0; count < earthQuakeData.getEHE().size(); count++) {
                            dG.displayRawDataGraph(earthQuakeData.getEHE().get(count), earthQuakeData.getEHN().get(count), earthQuakeData.getEHZ().get(count), rawDataGraph);
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),"No File Selected!",Toast.LENGTH_LONG);
                }
                break;
            }
            default:
            {
            }
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