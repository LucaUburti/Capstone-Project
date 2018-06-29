package uburti.luca.fitnessfordiabetics.glycemictrends;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;
import uburti.luca.fitnessfordiabetics.viewmodel.GlycemicTrendsViewModel;
import uburti.luca.fitnessfordiabetics.viewmodel.GlycemicTrendsViewModelFactory;

import static uburti.luca.fitnessfordiabetics.utils.Utils.getNumericDate;

public class GlycemicTrendsActivity extends AppCompatActivity {
    public static final int MEASURES_PER_DAY = 7;
    LineChart chart;
    HashMap<Integer, String> xAxisToDateMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_trends);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        chart = findViewById(R.id.chart);

        long startDate = Utils.getStartDate();

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        GlycemicTrendsViewModelFactory factory = new GlycemicTrendsViewModelFactory(appDatabase, startDate);    //retrieving all DiabeticDays starting from startDate
        GlycemicTrendsViewModel viewModel = ViewModelProviders.of(this, factory).get(GlycemicTrendsViewModel.class);
        viewModel.getDiabeticDays().observe(this, new Observer<List<DiabeticDay>>() {
            @Override
            public void onChanged(@Nullable List<DiabeticDay> diabeticDays) {
                ArrayList<Entry> entries = extractDataForChart(diabeticDays);
                int maxPossibleEntries = Utils.getDaysToRetrieve() * MEASURES_PER_DAY;
                setupChart(entries, maxPossibleEntries);
            }
        });


    }

    private void setupChart(ArrayList<Entry> entries, int maxPossibleEntries) {

        setupXAxis(maxPossibleEntries);

        setupYAxis();

        LineData lineData = setupLineData(entries);

        setupChartOptions(lineData);

        chart.invalidate(); //draw the chart
    }

    private void setupChartOptions(LineData lineData) {
        //various display settings for the chart itself, including a click listener when clicking an entry

        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setData(lineData);
        chart.animateX(500);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //Displays a snackbar when clicking a point in the chart. Content is retrieved from a HashMap populated in the extractDataForChart method
                String msg = xAxisToDateMap.get((int) e.getX()) + "\n" + getString(R.string.glycemia_header) + e.getY();
                Snackbar.make(findViewById(R.id.glycemic_trends_cl), msg, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        chart.setVisibleXRangeMinimum(3);
        chart.getDescription().setEnabled(false);


    }

    @NonNull
    private LineData setupLineData(ArrayList<Entry> entries) {
        //various display settings for the entries in the chart

        LineDataSet lineDataSet = new LineDataSet(entries, "");
        lineDataSet.enableDashedLine(10f, 5f, 0f);
        lineDataSet.setColor(Color.GRAY);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFillColor(0xffffff);

        return new LineData(lineDataSet);
    }

    private void setupYAxis() {
        //setup various display settings for the Y axis

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawLabels(true);

        yAxis.setDrawLimitLinesBehindData(true);
        int lowLimit = getResources().getInteger(R.integer.low_glycemia_threshold);
        int highLimit = getResources().getInteger(R.integer.high_glycemia_threshold);

        LimitLine limitLineTop = new LimitLine(highLimit, getResources().getString(R.string.hyperglycemia));
        limitLineTop.setLineColor(Color.RED);
        limitLineTop.setLineWidth(3f);
        limitLineTop.setTextColor(Color.GRAY);
        limitLineTop.setTextSize(12f);
        limitLineTop.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        yAxis.addLimitLine(limitLineTop);

        LimitLine limitLineBottom = new LimitLine(lowLimit, getResources().getString(R.string.hypoglycemia));
        limitLineBottom.setLineColor(Color.BLUE);
        limitLineBottom.setLineWidth(3f);
        limitLineBottom.setTextColor(Color.GRAY);
        limitLineBottom.setTextSize(12f);
        limitLineBottom.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        yAxis.addLimitLine(limitLineBottom);

        YAxis yAxisRight = chart.getAxisRight();
        yAxis.setSpaceBottom(10f);
        yAxisRight.setSpaceBottom(10f);
        yAxis.setSpaceTop(10f);
        yAxisRight.setSpaceTop(10f);

    }

    private void setupXAxis(int maxPossibleEntries) {
        //setup various display settings for the X axis

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);

        xAxis.setLabelRotationAngle(320f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        final ArrayList<String> xLabels = new ArrayList<>();
        for (int i = 0; i < maxPossibleEntries; i++) {      //loop to put descriptive labels in the X axis
            switch (i % MEASURES_PER_DAY) {
                case 0: //put labels starting with bedtime and going backwards since this is the chart "direction"
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.bedtime));
                    break;
                case 1:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.after_dinner_abbrv));
                    break;
                case 2:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.before_dinner_abbrv));
                    break;
                case 3:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.after_lunch_abbrv));
                    break;
                case 4:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.before_lunch_abbrv));
                    break;
                case 5:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.after_breakfast_abbrv));
                    break;
                case 6:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.before_breakfast_abbrv));
                    cal.add(Calendar.DATE, -1);
                    break;
            }
        }
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String label = "";
                try {
                    label = xLabels.get((int) value);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.d("GlycemicTrendsActivity", "getFormattedValue: no matching entry for this value. " + e.getMessage());
                }

                return label;
            }
        });

    }

    private ArrayList<Entry> extractDataForChart(List<DiabeticDay> diabeticDays) {
        //this method inserts entries for the chart, starting with bedtime and going backwards (since this is the chart "direction")
        //also saves into a HashMap a match between the entry index and a descriptive content. We will show this description in a Snackbar when clicking a point in the chart

        ArrayList<Entry> entries = new ArrayList<>();
        Log.d("GlycemicTrends", "extractDataForChart days: " + diabeticDays.size());
        List<DiabeticDay> diabeticDaysNoGaps = Utils.insertMockDays(diabeticDays);
        Log.d("GlycemicTrends", "extractDataForChart days no gaps: " + diabeticDaysNoGaps.size());

        for (int i = 0; i < diabeticDaysNoGaps.size(); i++) {
            DiabeticDay currentDiabeticDay = diabeticDaysNoGaps.get(i);
            int entryIndex = i * MEASURES_PER_DAY;
            if (currentDiabeticDay.getGlycemiaBedtime() == 0) {     //no data to display on chart, just increment the x-axis entry index
                entryIndex++;
            } else {    //populate the HashMap with a description for this entry, then insert this entry and increment the entry index
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getResources().getString(R.string.bedtime));
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaBedtime()));
            }
            if (currentDiabeticDay.getGlycemiaAfterDinner() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getString(R.string.after_dinner));
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaAfterDinner()));
            }
            if (currentDiabeticDay.getGlycemiaBeforeDinner() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getString(R.string.before_dinner));
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaBeforeDinner()));
            }
            if (currentDiabeticDay.getGlycemiaAfterLunch() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getString(R.string.after_lunch));
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaAfterLunch()));
            }
            if (currentDiabeticDay.getGlycemiaBeforeLunch() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getString(R.string.before_lunch));
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaBeforeLunch()));
            }
            if (currentDiabeticDay.getGlycemiaAfterBreakfast() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getString(R.string.after_breakfast));
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaAfterBreakfast()));
            }
            if (currentDiabeticDay.getGlycemiaBeforeBreakfast() != 0) {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) + " " + getString(R.string.before_breakfast));
                entries.add(new Entry(entryIndex, currentDiabeticDay.getGlycemiaBeforeBreakfast()));
            }


        }
        return entries;
    }


}
