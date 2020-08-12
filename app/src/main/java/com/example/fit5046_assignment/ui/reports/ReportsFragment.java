package com.example.fit5046_assignment.ui.reports;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fit5046_assignment.NetworkConnection;
import com.example.fit5046_assignment.R;
import com.example.fit5046_assignment.SignUpActivity;
import com.example.fit5046_assignment.ui.home.HomeFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    NetworkConnection networkConnection=null;
    private Button bt_start;
    private Button bt_end;
    private Button bt_go;
    private Button bt_go2;
    private TextView tv_start;
    private TextView tv_end;
    private PieChart piechart;
    private View root;
    private int personId;
    private Spinner spinner_year;
    private String year;
    private BarChart barChart;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        root = inflater.inflate(R.layout.fragment_reports, container, false);
        networkConnection = new NetworkConnection();
        bt_start = root.findViewById(R.id.bt_start);
        bt_end = root.findViewById(R.id.bt_end);
        bt_go = root.findViewById(R.id.bt_go);
        bt_go2 = root.findViewById(R.id.bt_go2);
        tv_start = root.findViewById(R.id.tv_start);
        tv_end = root.findViewById(R.id.tv_end);
        piechart = root.findViewById(R.id.pie_chart);
        barChart = root.findViewById(R.id.bar_chart);
        spinner_year = root.findViewById(R.id.spinner_year);
        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        personId = sp.getInt("personId",0);

        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("personID -------" + personId);
                System.out.println("start -----------" +tv_start.getText());
                System.out.println("end -----------" +tv_end.getText());
                MoviesPerPostcode moviesPerPostcode = new MoviesPerPostcode();
                String id = Integer.toString(personId);
                moviesPerPostcode.execute(id,tv_start.getText().toString(),tv_end.getText().toString());
            }
        });

        bt_go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("go2 -------");
                MoviesPerMonth moviesPerMonth = new MoviesPerMonth();
                String id = Integer.toString(personId);
                moviesPerMonth.execute(id,year);
            }
        });

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer date = new StringBuffer();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1,
                                          int day1) {
                        StringBuffer date = new StringBuffer();
                        month1 = month1 + 1;
                        date.append(year1);
                        date.append("-");
                        if (month1 < 10) {
                            date.append("0");
                            date.append(month1);
                        } else {
                            date.append(month1);
                        }
                        date.append("-");
                        if (day1 < 10) {
                            date.append(0);
                            date.append(day1);
                        } else {
                            date.append(day1);
                        }

                        tv_start.setText(date.toString());
                        System.out.println(tv_start.getText().toString());
                    }
                }, year, month, day);
                DatePicker dp = datePicker.getDatePicker();
                dp.setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        bt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer date = new StringBuffer();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                date.append(year);
                date.append("-");

                if (month < 10) {
                    date.append("0");
                    date.append(month);
                } else {
                    date.append(month);
                }

                date.append("-");

                if (day < 10) {
                    date.append(0);
                    date.append(day);
                } else {
                    date.append(day);
                }
                DatePickerDialog datePicker = new DatePickerDialog(root.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1,
                                          int day1) {
                        StringBuffer date = new StringBuffer();
                        month1 = month1 + 1;
                        date.append(year1);
                        date.append("-");
                        if (month1 < 10) {
                            date.append("0");
                            date.append(month1);
                        } else {
                            date.append(month1);
                        }
                        date.append("-");
                        if (day1 < 10) {
                            date.append(0);
                            date.append(day1);
                        } else {
                            date.append(day1);
                        }

                        tv_end.setText(date.toString());
                        //System.out.println(tv_date.getText().toString());
                    }
                }, year, month, day);
                DatePicker dp = datePicker.getDatePicker();
                dp.setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        year = (String) spinner_year.getSelectedItem();
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = spinner_year.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = "2015";
            }
        });


        return root;
    }


    private class MoviesPerPostcode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.moviesPerPostcode(params[0],params[1],params[2]);
        }
        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            List<PieEntry> strings = new ArrayList<>();
            JSONArray jsonArray = null;
            try{
                jsonArray = new JSONArray(record);
                for (int i=0; i<jsonArray.length();i++){
                   JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    strings.add(new PieEntry(jsonObject.getInt("totalNumber"),jsonObject.getString("postcode")));
                }

            } catch(JSONException e){
                e.printStackTrace();
            }


            PieDataSet dataSet = new PieDataSet(strings, "Postcode");
            piechart.setUsePercentValues(true);
            piechart.setDrawHoleEnabled(false);
            piechart.getDescription().setText("No. of movies watched per postcode");
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);
            PieData pieData = new PieData();
            pieData.setDataSet(dataSet);

            //各个饼状图所占比例数字的设置
            pieData.setValueFormatter(new PercentFormatter());//设置%
            pieData.setValueTextSize(12f);
            pieData.setValueTextColor(Color.BLUE);

            piechart.setData(pieData);
            // undo all highlights
            piechart.highlightValues(null);
            piechart.invalidate();


        }
    }

    private class MoviesPerMonth extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.moviesPerMonth(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
            ArrayList<String> labels = new ArrayList<String> ();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(record);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    barEntries.add(new BarEntry(i,jsonObject.getInt("totalNumber")));
                    labels.add(jsonObject.getString("monthName"));


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarDataSet barDataSet = new BarDataSet(barEntries, "Month");
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            //        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            barDataSet.setColors(colors);

            BarData barData = new BarData(barDataSet);

            barChart.getDescription().setText("No. of movies watched per month");
            barChart.getDescription().setTextSize(12);
            barChart.setDrawMarkers(true);


            barChart.getAxisLeft().setAxisMinimum(0);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.animateY(1000);

            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setGranularity(1.0f);
            barChart.getXAxis().setLabelCount(barDataSet.getEntryCount());

            barChart.setData(barData);
        }
    }

}
