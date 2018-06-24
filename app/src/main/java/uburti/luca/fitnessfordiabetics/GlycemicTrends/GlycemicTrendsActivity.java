package uburti.luca.fitnessfordiabetics.GlycemicTrends;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
import uburti.luca.fitnessfordiabetics.ViewModel.GlycemicTrendsViewModel;
import uburti.luca.fitnessfordiabetics.ViewModel.GlycemicTrendsViewModelFactory;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;

import static uburti.luca.fitnessfordiabetics.utils.Utils.getNumericDate;

public class GlycemicTrendsActivity extends AppCompatActivity {
    public static final int MEASURES_PER_DAY = 7;
    LineChart chart;
    HashMap<Integer, String> xAxisToDateMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_trends);
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
                setupChart(entries, maxPossibleEntries); //TODO prefs
            }
        });


    }

    private void setupChart(ArrayList<Entry> entries, int maxPossibleEntries) {

        setupXAxis(entries, maxPossibleEntries);

        setupYAxis();

        LineData lineData = setupLineData(entries);

        setupChartOptions(lineData);

        chart.invalidate(); //redraw the chart
    }

    private void setupChartOptions(LineData lineData) {
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setData(lineData);
        chart.animateX(2500);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
//        chart.setScaleMinima(10f,1f);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String msg= xAxisToDateMap.get((int)e.getX())+ "\n"+ getString(R.string.glycemia_header) + e.getY();
                Snackbar.make(findViewById(R.id.glycemic_trends_cl), msg, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        chart.setVisibleXRangeMinimum(3);
//        chart.setVisibleXRangeMaximum(49);
//        chart.setVisibleYRangeMinimum(150, YAxis.AxisDependency.LEFT);
        chart.getDescription().setEnabled(false);


    }

    @NonNull
    private LineData setupLineData(ArrayList<Entry> entries) {
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

    private void setupXAxis(ArrayList<Entry> entries, int maxPossibleEntries) {
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
        for (int i = 0; i < maxPossibleEntries; i++) {
            switch (i % MEASURES_PER_DAY) {
                case 0:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.before_breakfast_abbrv));
                    break;
                case 1:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.after_breakfast_abbrv));
                    break;
                case 2:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.before_lunch_abbrv));
                    break;
                case 3:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.after_lunch_abbrv));
                    break;
                case 4:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.before_dinner_abbrv));
                    break;
                case 5:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.after_dinner_abbrv));
                    break;
                case 6:
                    xLabels.add(getNumericDate(cal.getTimeInMillis(), this) + " " + getString(R.string.bedtime));
                    cal.add(Calendar.DATE, -1);
                    break;
            }
        }
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabels.get((int) value);
            }
        });
        xAxis.setGranularity(1f);

    }

    private ArrayList<Entry> extractDataForChart(List<DiabeticDay> diabeticDays) {
        ArrayList<Entry> entries = new ArrayList<>();
        Log.d("GlycemicTrends", "extractDataForChart days: " + diabeticDays.size());
        List<DiabeticDay> diabeticDaysNoGaps = Utils.insertMockDays(diabeticDays);
        Log.d("GlycemicTrends", "extractDataForChart days no gaps: " + diabeticDaysNoGaps.size());

        for (int i = 0; i < diabeticDaysNoGaps.size(); i++) {
            DiabeticDay currentDiabeticDay = diabeticDaysNoGaps.get(i);
            int entryIndex = i * MEASURES_PER_DAY;
            if (currentDiabeticDay.getGlycemiaBeforeBreakfast() == 0) {
                entryIndex++;   //no data to display on chart, just increment the x-axis entry index
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ "Before breakfast");
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaBeforeBreakfast()));
            }
            if (currentDiabeticDay.getGlycemiaAfterBreakfast() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ "After breakfast");
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaAfterBreakfast()));
            }
            if (currentDiabeticDay.getGlycemiaBeforeLunch() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ "Before lunch");
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaBeforeLunch()));
            }
            if (currentDiabeticDay.getGlycemiaAfterLunch() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ "After breakfast");
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaAfterLunch()));
            }
            if (currentDiabeticDay.getGlycemiaBeforeDinner() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ "Before dinner");
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaBeforeDinner()));
            }
            if (currentDiabeticDay.getGlycemiaAfterDinner() == 0) {
                entryIndex++;
            } else {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ "After breakfast");
                entries.add(new Entry(entryIndex++, currentDiabeticDay.getGlycemiaAfterDinner()));
            }
            if (currentDiabeticDay.getGlycemiaBedtime() != 0) {
                xAxisToDateMap.put(entryIndex, Utils.getReadableDate(currentDiabeticDay.getDate()) +" "+ getResources().getString(R.string.bedtime));
                entries.add(new Entry(entryIndex, currentDiabeticDay.getGlycemiaBedtime()));
            }
        }
        return entries;
    }


}
