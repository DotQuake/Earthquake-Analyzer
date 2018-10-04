package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.DisplayGraph;
import com.example.admindeveloper.earthqanalyzer.EarthquakeAnalyzer;
import com.example.admindeveloper.earthqanalyzer.MediaRescan;
import com.example.admindeveloper.earthqanalyzer.R;
import com.example.admindeveloper.earthqanalyzer.RecordSaveDataXYZ;
import com.github.mikephil.charting.charts.LineChart;

import java.io.File;
import java.text.SimpleDateFormat;

public class RealTimeView extends Fragment implements SensorEventListener {


    public LineChart rawDataGraph;
    public TextView hypocenterBox;
    public TextView directionBox;
    public Button saveDataBtn, recordDataBtn;
    TextView timeBox;
    boolean result;
    //-------------------------------
    View myView;
    private SensorManager mSensorManager;
    TextView mX,mY,mZ;
    private Thread thread;
    private boolean plotData = true;
    TextView status;
    //long yourmilliseconds ;
    SimpleDateFormat sdf ;
    //Date resultdate ;
    EarthquakeAnalyzer ea;
    boolean recordflag = false;
   // public List<String> time_values;
    RealTimeController rtc;
    DisplayGraph dg;
    RecordSaveDataXYZ rsdata;
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
        File myDir = new File(Environment.getExternalStorageDirectory()+"/SoftEng");
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));
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
            rtc.updateXYZ(event.values[0],event.values[1],event.values[2]);
            mX.setText(Float.toString(rtc.getX()));
            mY.setText(Float.toString(rtc.getY()));
            mZ.setText(Float.toString(rtc.getZ()));
               if (recordflag) {
                rsdata.recordData(rtc.getX(),rtc.getY(),rtc.getZ());
            //time_values.add(index++,sdf.format(resultdate));
            //rawtimemilis.add(index++,yourmilliseconds);
              }
            String result=ea.detectEarthquake(rtc.getX(),rtc.getY(),rtc.getZ());
            if(ea.getStatus()=="DETERMINEPW")
            {
                recordflag=true;
            }
            else if(ea.getStatus()=="EARTHQUAKEDETECTED")
            {

            }
            Float[] val=ea.getAverageOfSamples();
            if(result!=null)
            {
                Toast.makeText(getActivity(),"Earthquake Detected",Toast.LENGTH_LONG);
            }
              dg.displayRawDataGraph(val[0],rtc.getX(),0,rawDataGraph);
            }
        }
    //-----------------------------------------------------------------------------------------------------




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.realtime,container,false);
        rawDataGraph = (LineChart) myView.findViewById(R.id.chart1);
        mX = (TextView) myView.findViewById(R.id.tvx);
        mY = (TextView) myView.findViewById(R.id.tvy);
        mZ = (TextView) myView.findViewById(R.id.tvz);
        timeBox = (TextView) myView.findViewById(R.id.mstx);
        //time_values = new ArrayList<>();
        recordDataBtn = (Button) myView.findViewById(R.id.recordbt);
        saveDataBtn = (Button) myView.findViewById(R.id.savebt);
        rtc = new RealTimeController();
        dg = new DisplayGraph();
        rsdata = new RecordSaveDataXYZ();
        status=myView.findViewById(R.id.mstx);
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

        ea=new EarthquakeAnalyzer((float)0.3,100,(float)0.1);
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
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (plotData) {
                displayRawDataGraph(event);
                plotData = false;
            }

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

    /*private void displayTime() {
        Thread th = new Thread(new Runnable() {
            private long startTime = System.currentTimeMillis();
            public void run() {
                while (true) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            yourmilliseconds = System.currentTimeMillis();
                            sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                            resultdate = new Date(yourmilliseconds);
                            timeBox.setText(""+sdf.format(resultdate));

                        }
                    });
                    try {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }*/

}
