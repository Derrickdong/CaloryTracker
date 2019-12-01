package com.example.calorietracker;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class ReportFragment extends Fragment {
    View vReport;
    DatePickerDialog.OnDateSetListener dateListener1;
    DatePickerDialog.OnDateSetListener dateListener2;
    DatePickerDialog.OnDateSetListener dateListener3;
    String date;
    String receivedDate;
    String fromDateStr;
    String toDateStr;
    TextView tv_date;
    TextView fromDate;
    TextView toDate;
    Button btn_bar;
    Button btn_pie;
    BarChart barChart;
    PieChart pieChart;
    ArrayList<BarEntry> barEntries1;
    ArrayList<BarEntry> barEntries2;
    ArrayList<PieEntry> pieEntries;
    ArrayList<String> xAxisLabel;
    YAxis leftAxis;
    YAxis rightAxis;
    XAxis xAxis;
    Legend legend;
    private static final String TAG = "NavigationDrawer";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.report_fragment, container, false);
        tv_date = (TextView)vReport.findViewById(R.id.tv_selectDate);
        fromDate = (TextView) vReport.findViewById(R.id.fromDate);
        toDate = (TextView)vReport.findViewById(R.id.toDate);
        btn_bar = (Button)vReport.findViewById(R.id.btn_bar);
        btn_pie = (Button)vReport.findViewById(R.id.btn_pie);
        barChart = (BarChart)vReport.findViewById(R.id.barchart);
        pieChart = (PieChart)vReport.findViewById(R.id.piechart);
        barEntries1 = new ArrayList<>();
        barEntries2 = new ArrayList<>();
        pieEntries = new ArrayList<>();
        xAxisLabel = new ArrayList<>();

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, dateListener1, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                receivedDate = year + "-" + month + "-" + dayOfMonth;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);
                tv_date.setText(receivedDate);
                DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    date = format.format(format.parse(receivedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        btn_pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.clear();
                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5, 10, 5, 5);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.WHITE);
                pieChart.setTransparentCircleRadius(61f);

                ReportAsyncTask reportAsycTask = new ReportAsyncTask();
                String result = "";
                int totalCalConsumed = 0;
                int totalCalBurned = 0;
                int goal = 0;

                try {
                    if (date == null){
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Please Select a Date";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else {
                        result = reportAsycTask.execute("findByDate", receivedDate).get();
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        totalCalConsumed = jsonObject.getInt("totalcaloriesconsumed");
                        totalCalBurned = jsonObject.getInt("totalcaloriesburrend");
                        goal = jsonObject.getInt("setcaloriegoal");
                        int calRemain = goal - totalCalBurned;
                        pieEntries.add(new PieEntry(totalCalConsumed, "Total Calorie Consumed"));
                        pieEntries.add(new PieEntry(totalCalBurned, "Total Calorei Burned"));
                        pieEntries.add(new PieEntry(calRemain, "Remaining Calorie"));
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "Calorie situations");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                PieData data = new PieData((dataSet));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.BLACK);
                pieChart.getLegend().setTextColor(Color.BLACK);

                pieChart.setData(data);
                pieChart.setVisibility(View.VISIBLE);
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, dateListener2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                fromDateStr = year + "-" + month + "-" + dayOfMonth;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);
                fromDate.setText("From: " + fromDateStr);
            }
        };

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, dateListener3, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateListener3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                toDateStr = year + "-" + month + "-" + dayOfMonth;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);
                toDate.setText("To: " + toDateStr);
                DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            }
        };

        btn_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChart.clear();
                barChart.setBackgroundColor(Color.WHITE);
                barChart.setDrawBarShadow(false);
                barChart.setDrawValueAboveBar(true);
                barChart.setMaxVisibleValueCount(50);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(true);

                xAxis = barChart.getXAxis();

                leftAxis = barChart.getAxisLeft();
                leftAxis.setAxisMinimum(0f);
                rightAxis = barChart.getAxisRight();
                rightAxis.setAxisMinimum(0f);

                legend = barChart.getLegend();
                legend.setForm(Legend.LegendForm.LINE);
                legend.setTextSize(11f);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);

                String result = "";
                Bundle bundle = getActivity().getIntent().getExtras();
                String username = bundle.getString("username");
                String userid = "";
                CredentialAsyncTask credentialAsyncTask = new CredentialAsyncTask();
                try {
                    userid = credentialAsyncTask.execute(username).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ReportAsyncTaskA reportAsycTaska = new ReportAsyncTaskA();
                try {
                    result = reportAsycTaska.execute("generatePeriod",userid, fromDateStr, toDateStr).get();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int totalCalConsumed = jsonObject.getInt("totalcaloriesconsumed");
                        int totalCalBurned = jsonObject.getInt("totalcaloriesburrend");
                        String searchDate = jsonObject.getString("date").substring(0,9);
                        barEntries1.add(new BarEntry(i, totalCalBurned));
                        barEntries2.add(new BarEntry(i, totalCalConsumed));
                        xAxisLabel.add(i, searchDate);
                    }
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setCenterAxisLabels(true);
                xAxis.setAxisMinimum(0f);
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Total Calories Burned");
                barDataSet1.setColor(Color.GRAY);
                BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Total Calories Consumed");
                barDataSet2.setColor(Color.LTGRAY);

                BarData data = new BarData(barDataSet1, barDataSet2);
                data.setBarWidth(0.1f);
                barChart.setData(data);
                barChart.groupBars(-0.3f, 0.6f, 0.1f);
                barChart.getDescription().setEnabled(false);
                barChart.invalidate();
                barChart.setVisibility(View.VISIBLE);
            }
        });
        return vReport;
    }

    private class ReportAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithStringParams(strings[0], "report", strings[1]);
        }
    }

    private class ReportAsyncTaskA extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithMoreThanOneString(strings[0], "report", strings[1],strings[2] + "/", strings[3],"");
        }
    }

    private class CredentialAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithStringParamReturnPlainText("findUseridByUsername", "credential", strings[0]);
        }
    }
}
