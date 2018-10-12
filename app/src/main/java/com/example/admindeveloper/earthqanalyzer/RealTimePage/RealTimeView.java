package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageController;
import com.example.admindeveloper.earthqanalyzer.CompassPage.CompassPageView;
import com.example.admindeveloper.earthqanalyzer.DisplayGraph;
import com.example.admindeveloper.earthqanalyzer.MediaRescan;
import com.example.admindeveloper.earthqanalyzer.R;
import com.example.admindeveloper.earthqanalyzer.RecordSaveDataXYZ;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;

public class RealTimeView extends Fragment implements SensorEventListener {

    View myView;
    private ImageView image;
    float currentdegree = 0f;


    public LineChart rawDataGraph;
    public TextView hypocenterBox;
    public TextView directionBox;
    public Button saveDataBtn, recordDataBtn,restartBtn;
    TextView timeBox , dtx;
    //-------------------------------
    private SensorManager mSensorManager;
    private Thread thread;
    private boolean plotData = true;
    TextView status;
    SimpleDateFormat sdf ;
    boolean recordflag = false;
    RealTimeController rtc;
    DisplayGraph dg;
    RecordSaveDataXYZ rsdata;
    CompassPageController cpc = new CompassPageController();
    CompassPageView cpv = new CompassPageView();
    //-------------------------------
    public void displayHypocenterBox(float distance){

    }
    public void displaydirectionBox(float degree){

    }
    // IMPLEMENTED -------------------------------------
    public void saveDataBtnClick(){
        rsdata.saveEarthquakeData("unknown",100);
        MediaScannerConnection.scanFile(getActivity(), new String[] {Environment.getExternalStorageDirectory().getPath()+"/Samples"}, null, null);
        Toast.makeText(getActivity(),"Saved",Toast.LENGTH_SHORT).show();
        MediaRescan mr=new MediaRescan();
    }
    public void recordDataBtnClick(){
        if(recordflag){
            recordflag = false;
            Toast.makeText(getActivity(),"Record Stopped", Toast.LENGTH_SHORT).show();
            recordDataBtn.setText("Start Recording");

        }else {
            recordflag = true;
            Toast.makeText(getActivity(),"Record Start", Toast.LENGTH_SHORT).show();
            recordDataBtn.setText("Stop Recording");
        }

    }
    private void smooththread() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }
    private void displayRawDataGraph(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            status.setText(rtc.getStatus());
            if(rtc.updateXYZ(event.values[0],event.values[1],event.values[2],cpc))
            {
               // status.append("\r\n"+rtc.getHypocenter()+"\r\n"+rtc.getDirection());
                this.hypocenterBox.setText(rtc.getHypocenter()+"");
                this.directionBox.setText(rtc.getDirection());
            }
               if (recordflag) {
                   rsdata.recordData(rtc.getX(), rtc.getY(), rtc.getZ());
               }
              dg.displayRawDataGraph(rtc.getX(),rtc.getY(),rtc.getZ(),rawDataGraph);
            }
        }
        public void showMessage(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setCancelable(false);
            builder.setTitle("Real Time");
            builder.setMessage("Please put your device on a Flat Surface Area");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(),"Thank you", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }

    //-----------------------------------------------------------------------------------------------------




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.realtime,container,false);
        rawDataGraph = (LineChart) myView.findViewById(R.id.chart1);
        dtx = (TextView) myView.findViewById(R.id.directiontx);
        image = (ImageView) myView.findViewById(R.id.imview2);
        timeBox = (TextView) myView.findViewById(R.id.statustx);
        hypocenterBox = (TextView) myView.findViewById(R.id.hypocentertx);
        directionBox = (TextView) myView.findViewById(R.id.directtx);
        //time_values = new ArrayList<>();
        recordDataBtn = (Button) myView.findViewById(R.id.recordbt);
        saveDataBtn = (Button) myView.findViewById(R.id.savebt);
        rtc = new RealTimeController();
        rtc.initializeAnalyzer(0.3F, 100, 0.1F);
        dg = new DisplayGraph();
        rsdata = new RecordSaveDataXYZ();
        hypocenterBox.setText("");
        directionBox.setText("");
        status=myView.findViewById(R.id.statustx);
        restartBtn=myView.findViewById(R.id.restart);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hypocenterBox.setText("");
                directionBox.setText("");
                rtc.initializeAnalyzer(0.3F,100 ,0.1F );
            }
        });
        recordDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDataBtnClick();
            }
        });
        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataBtnClick();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }
        showMessage();
        //displayTime();
        smooththread();
        dg.setup(rawDataGraph);
        return myView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(),"Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Permission not granted", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
       // mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (plotData) {
                displayRawDataGraph(event);
                plotData = false;
            }

        }else if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            cpc.setDegree(Math.round(event.values[0]));
            dtx.setText(Float.toString(cpc.getDegree())+"°   "+cpc.getDirection(cpc.getDegree()));
            image.startAnimation(cpv.displayAnimation(cpc.getDegree(),currentdegree,image));
            currentdegree = -cpc.getDegree();
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(mSensorManager == null){
            return;
        }
        if(menuVisible){
            mSensorManager.registerListener(this,mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this,mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0), SensorManager.SENSOR_DELAY_GAME);
        }else{
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.getUserVisibleHint()){
            mSensorManager.registerListener(this,mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this,mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        mSensorManager.unregisterListener(this);
        thread.interrupt();
        super.onDestroyView();
    }
    //-----------------------------------------------------------------------------------------------------


}
