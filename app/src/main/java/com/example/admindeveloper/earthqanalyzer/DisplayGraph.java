package com.example.admindeveloper.earthqanalyzer;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class DisplayGraph {

    LineDataSet set1;
    LineDataSet set2;
    LineDataSet set3;

    public void displayRawDataGraph(float x , float y ,float z, LineChart rawDataGraph) {
        LineData data = rawDataGraph.getData();

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

                data.addEntry(new Entry(setx.getEntryCount(), x), 0);
                data.addEntry(new Entry(sety.getEntryCount(), y), 1);
                data.addEntry(new Entry(setz.getEntryCount(), z), 2);
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
    public void setup(LineChart rawDataGraph){
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
}
