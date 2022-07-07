package com.example.investmenttracker;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;


public class XAxisFormatter implements IAxisValueFormatter {

    private ArrayList<String> mValues;

    public XAxisFormatter(ArrayList<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues.get((int) value);
    }

    public int getDecimalDigits() { return 0; }
}
