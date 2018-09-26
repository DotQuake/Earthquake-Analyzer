package com.example.admindeveloper.earthqanalyzer.CompassPage;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admindeveloper.earthqanalyzer.R;

public class CompassPageView extends Fragment implements SensorEventListener {
    View myView;
    private ImageView image;
    float degree, currentdegree = 0f;
    private SensorManager mSensorManager;
    TextView mcompass;

    CompassPageController cpc;

    private RotateAnimation displayAnimation(float degree, float currentdegree, ImageView image){
        RotateAnimation ra = new RotateAnimation(currentdegree,-degree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);
        return ra;
    }
    private void displayDirectionText(){
        String where = "NO";
        degree = cpc.getDegree();

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

        mcompass.setText("Heading: " + Float.toString(degree) + " degrees   " + where);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.orientation,container,false);
        mcompass = (TextView) myView.findViewById(R.id.tvcompass);
        image = (ImageView) myView.findViewById(R.id.imview);
        cpc = new CompassPageController();
        return myView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // get the angle around the z-axis rotated
            cpc.setDegree(Math.round(event.values[0]));
            image.startAnimation(displayAnimation(cpc.getDegree(),currentdegree,image));
            currentdegree = -cpc.getDegree();

            displayDirectionText();
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
            mSensorManager.registerListener(this,mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0),SensorManager.SENSOR_DELAY_GAME);
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
            mSensorManager.registerListener(this,mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0),SensorManager.SENSOR_DELAY_GAME);
        }
    }
}
