package com.example.admindeveloper.earthqanalyzer.RealTimePage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindeveloper.earthqanalyzer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;

public class RealTimeView extends Fragment implements SensorEventListener {
    LineDataSet set1;
    LineDataSet set2;
    LineDataSet set3;


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

    //long yourmilliseconds ;
    SimpleDateFormat sdf ;
    //Date resultdate ;

    boolean recordflag = false;
   // public List<String> time_values;
    RealTimeController rtc;
    EditText filename;
    //-------------------------------
    public void displayHypocenterBox(float distance){

    }
    public void displaydirectionBox(float degree){

    }
    // IMPLEMENTED -------------------------------------
    public void saveDataBtnClick(){
        String file = filename.getText().toString();
        if(!file.equals("")) {
            Toast.makeText(getActivity(),"Saved",Toast.LENGTH_SHORT).show();
            rtc.saveData(file);
        }

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
    private void setup(){
        // enable description text
        rawDataGraph.getDescription().setEnabled(true);
        rawDataGraph.getDescription().setText("Real Time Accelerometer Data Plot ( X , Y , Z )");

        // enable touch gestures
        rawDataGraph.setTouchEnabled(false);

        // enable scaling and dragging
        rawDataGraph.setDragEnabled(false);
        rawDataGraph.setScaleEnabled(false);
        rawDataGraph.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        rawDataGraph.setPinchZoom(false);

        // set an alternative background color
        rawDataGraph.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        rawDataGraph.setData(data);

        // get the legend (only possible after setting data)
        Legend l = rawDataGraph.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = rawDataGraph.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = rawDataGraph.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(1f);
        leftAxis.setAxisMinimum(-1f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = rawDataGraph.getAxisRight();
        rightAxis.setEnabled(false);

        rawDataGraph.getAxisLeft().setDrawGridLines(false);
        rawDataGraph.getXAxis().setDrawGridLines(false);
        rawDataGraph.setDrawBorders(false);
        //----------------------------------------------------------------------------
        set1 = new LineDataSet(null, "X");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setLineWidth(1f);
        set1.setColor(Color.MAGENTA);
        set1.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        //----------------------------------------------------------------------------
        //----------------------------------------------------------------------------
        set2 = new LineDataSet(null, "Y");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(1f);
        set2.setColor(Color.BLACK);
        set2.setHighlightEnabled(false);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setCubicIntensity(0.2f);
        //----------------------------------------------------------------------------
        //----------------------------------------------------------------------------
        set3 = new LineDataSet(null, "Z");
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setLineWidth(1f);
        set3.setColor(Color.BLUE);
        set3.setHighlightEnabled(false);
        set3.setDrawValues(false);
        set3.setDrawCircles(false);
        set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set3.setCubicIntensity(0.2f);
        //----------------------------------------------------------------------------
    }
    private void displayRawDataGraph(SensorEvent event) {
        LineData data = rawDataGraph.getData();

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            rtc.updateXYZ(event.values[0],event.values[1],event.values[2]);
            mX.setText(Float.toString(rtc.getX()));
            mY.setText(Float.toString(rtc.getY()));
            mZ.setText(Float.toString(rtc.getZ()));
               if (recordflag) {
                rtc.recordData();
            //time_values.add(index++,sdf.format(resultdate));
            //rawtimemilis.add(index++,yourmilliseconds);
              }

            if (data != null) {

                ILineDataSet setx = data.getDataSetByIndex(0);
                ILineDataSet sety = data.getDataSetByIndex(1);
                ILineDataSet setz = data.getDataSetByIndex(2);

                // set.addEntry(...); // can be called as well

                if (setx == null) {
                    setx = set1;
                    data.addDataSet(setx);
                }
                if (sety == null) {
                    sety = set2;
                    data.addDataSet(sety);
                }
                if (setz == null) {
                    setz = set3;
                    data.addDataSet(setz);
                }

                data.addEntry(new Entry(setx.getEntryCount(), rtc.getX()), 0);
                data.addEntry(new Entry(sety.getEntryCount(), rtc.getY()), 1);
                data.addEntry(new Entry(setz.getEntryCount(), rtc.getZ()), 2);
                data.notifyDataChanged();

                // let the chart know it's data has changed
                rawDataGraph.notifyDataSetChanged();

                // limit the number of visible entries
                rawDataGraph.setVisibleXRangeMaximum(200);
                // rawDataGraph.setVisibleYRange(30, AxisDependency.LEFT);

                // move to the latest entry
                rawDataGraph.moveViewToX(data.getEntryCount());

            }
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
        filename = (EditText) myView.findViewById(R.id.filenametx);
        //time_values = new ArrayList<>();
        recordDataBtn = (Button) myView.findViewById(R.id.recordbt);
        saveDataBtn = (Button) myView.findViewById(R.id.savebt);
        rtc = new RealTimeController();

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


        //displayTime();
        smooththread();
        setup();
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
