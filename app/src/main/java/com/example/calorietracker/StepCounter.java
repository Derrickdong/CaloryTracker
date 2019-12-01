package com.example.calorietracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.entities.Report;
import com.example.entities.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StepCounter extends Fragment implements View.OnClickListener{

    View vStepCounter;
    TextView tv_text;
    EditText et_steps;
    Button btn_add;
    TextView tv_records;
    Button btn_nextDay;
    int steps = 0;
    String text = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vStepCounter = inflater.inflate(R.layout.fragment_stepcounter, container, false);
        tv_text = (TextView) vStepCounter.findViewById(R.id.tv_text);
        et_steps = (EditText) vStepCounter.findViewById(R.id.et_steps);
        btn_add = (Button) vStepCounter.findViewById(R.id.btn_add);
        tv_records = (TextView) vStepCounter.findViewById(R.id.tv_records);
        btn_nextDay = (Button) vStepCounter.findViewById(R.id.btn_nextDay);

        SharedPreferences spMyUnits = getActivity().getSharedPreferences("steps", Context.MODE_PRIVATE);
        SharedPreferences spRecord = getActivity().getSharedPreferences("record", Context.MODE_PRIVATE);
        String message = spMyUnits.getString("steps",null);
        String records = spRecord.getString("record", null);
        if(records != null){
            tv_records.setText(records);
        }
        if (message == null) {
            message = "0";
        }
        tv_text.setText("Total steps today: " + message);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newSteps = Integer.parseInt(et_steps.getText().toString());
                steps = steps + newSteps;

                SharedPreferences sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences(
                        "steps", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString("steps", steps + "");
                spEditor.apply();

                tv_text.setText("Total steps today: " + steps);

                TextAsyncTask textAsyncTask =  new TextAsyncTask();
                textAsyncTask.execute(steps + "");
            }
        });
        btn_nextDay.setOnClickListener(this);
        return vStepCounter;
    }

    private void post(){
        ReportAsyncTask reportAsyncTask = new ReportAsyncTask();
        reportAsyncTask.execute();
    }

    private void deleteAll(){
        SharedPreferences sp1 = getActivity().getSharedPreferences("goal",  Context.MODE_PRIVATE);
        SharedPreferences sp2 = getActivity().getSharedPreferences("steps", Context.MODE_PRIVATE);
        SharedPreferences sp3 = getActivity().getSharedPreferences("record", Context.MODE_PRIVATE);
        sp1.edit().remove("goal").apply();
        sp2.edit().remove("steps").apply();
        sp3.edit().remove("record").apply();
    }

    @Override
    public void onClick(View v) {
        post();
        deleteAll();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new Welcome_fragment()).commit();
    }

    private class ReportAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("goal", Context.MODE_PRIVATE);
            int goal = 0;
            if (!(sharedPreferences.getString("goal", null) == null)){
                goal = Integer.parseInt(sharedPreferences.getString("goal", null));
            }
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
            String today = sdf.format(date);
            Bundle bundle = getActivity().getIntent().getExtras();
            String username = bundle.getString("username");
            String userResult = RestClient.findWithStringParams("findByUsername", "credential", username);
            String userid = "";
            Users user = null;
            try {
                JSONArray jsonArray = new JSONArray(userResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONObject userObj = jsonObject.getJSONObject("userid");
                userid = userObj.getString("userid");
                String dateStr = userObj.getString("dob");
                Date date1 = new Date(dateStr.substring(0,10).replace("-", "/"));
                user = new Users(Integer.parseInt(userid), userObj.getString("name"), userObj.getString("surname"), userObj.getString("email"), date1,
                        userObj.getInt("height"), userObj.getInt("weight"), userObj.getString("gender").charAt(0), userObj.getString("address"),
                        userObj.getString("postcode"), userObj.getInt("levelofactivity"), userObj.getInt("stepspermile"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            double calPerStep = Double.parseDouble(RestClient.findWithStringParamReturnPlainText("caculateCaloriesBurnedPerStep", "users",userid));
            String totalConsumed = RestClient.findWithTwoStringReturnPlainText("totalCaloriesConsumedForTheDay", "consumption", userid, today);
            Report report = new Report(1, date, (int)Double.parseDouble(totalConsumed), (int)calPerStep*steps, (int)steps, goal, user);
            RestClient.createReport(report);
            return null;
        }
    }

    private class TextAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String addText = "You have walked " + params[0] + "steps at " + dateFormat.format(date) + "\n";
            return addText;
        }

        @Override
        protected void onPostExecute(String result){
            text = text + result;
            SharedPreferences sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences(
                    "record", Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("record", text);
            spEditor.apply();
            tv_records.setText(text);
        }
    }
}
