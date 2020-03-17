package com.example.calorietracker;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SQLiteDatabase.ReportC;
import com.example.SQLiteDatabase.ReportDatabase;
import com.example.entities.Report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Welcome_fragment extends Fragment {
    View vWelcome;
    ReportDatabase db = null;
    TextView tv_goals = null;
    EditText et_goal;
    TextView tv_steps;
    TextView tv_calorieConsumed;
    TextView tv_calorieBurned;
    Button btn_reset;
    TextClock textClock;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vWelcome = inflater.inflate(R.layout.welcome_fragment, container, false);

        TextView calTracker = (TextView) vWelcome.findViewById(R.id.tv_welcome);
        TextView time = (TextView) vWelcome.findViewById(R.id.tv_time);
        textClock = (TextClock) vWelcome.findViewById(R.id.tc_clock);
        TextView user = (TextView) vWelcome.findViewById(R.id.tv_user);
        tv_goals = (TextView) vWelcome.findViewById(R.id.tv_goal);
        et_goal = (EditText) vWelcome.findViewById(R.id.et_goal);
        tv_steps = (TextView) vWelcome.findViewById(R.id.tv_steps);
        tv_calorieBurned = (TextView) vWelcome.findViewById(R.id.tv_calorie_burned);
        tv_calorieConsumed = (TextView) vWelcome.findViewById(R.id.tv_calorie_consumed);
        btn_reset = (Button) vWelcome.findViewById(R.id.btn_reset);

        time.setText("Time :     " + textClock.getText());

        Bundle bundle = getActivity().getIntent().getExtras();
        String username = bundle.getString("username");

        db = Room.databaseBuilder(getActivity().getApplicationContext(), ReportDatabase.class, "ReportDatabase").fallbackToDestructiveMigration().build();

        InsertDatabase insertDatabase = new InsertDatabase();
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        CredentialAsyncTask credentialAsyncTask = new CredentialAsyncTask();
        String firstName = "";
        try {
            firstName = credentialAsyncTask.execute("findNameByUsername", username).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user.setText("Welcome, " + firstName);

        SharedPreferences spMyUnits = getActivity().getSharedPreferences("goal",
                Context.MODE_PRIVATE);
        String message = spMyUnits.getString("goal", null);
        if (message == null || message.isEmpty()) {
            tv_goals.setText("You have not set your goal today");
            et_goal.setVisibility(View.VISIBLE);
        } else {
            et_goal.setVisibility(View.GONE);
            tv_goals.setText("Your goal today is: " + message + " calories");
        }
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_goals.setVisibility(View.GONE);
                et_goal.setVisibility(View.VISIBLE);
                if (!et_goal.getText().toString().matches("^[0-9]+$")) {
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = "The goal must be numbers";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    et_goal.setVisibility(View.GONE);
                    String setGoal = et_goal.getText().toString();
                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(
                            "goal", Context.MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                    spEditor.putString("goal", setGoal);
                    spEditor.apply();
                    tv_goals.setText("Your goal today is to burn " + setGoal + " calories");
                    tv_goals.setVisibility(View.VISIBLE);
                }
            }
        });

        SharedPreferences spSteps = getActivity().getSharedPreferences("steps", Context.MODE_PRIVATE);
        String stepStr = spSteps.getString("steps", null);
        if (stepStr == null) {
            stepStr = "0";
        }
        tv_steps.setText("Total steps today: " + stepStr + "\n" + "You could record your steps taken for today in Step Counter.");

        ConsumptionAsyncTask consumptionAsyncTask = new ConsumptionAsyncTask();
        CredentialAsyncTask1 credentialAsyncTask1 = new CredentialAsyncTask1();
        String userid = "";
        try {
            userid = credentialAsyncTask1.execute("findUseridByUsername", username).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String today = sdf.format(date);
        consumptionAsyncTask.execute("totalCaloriesConsumedForTheDay", userid, today + "/");

        UserAsyncTask userAsyncTask = new UserAsyncTask();
        String result = "";
        try {
            result = userAsyncTask.execute("caculateCaloriesBurnedPerStep", userid).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Double totalCalorieBurned = Double.parseDouble(result) * Integer.parseInt(stepStr);
        DecimalFormat df = new DecimalFormat("0.00");
        tv_calorieBurned.setText("You have burned " + df.format(totalCalorieBurned) + " calories today.");

        return vWelcome;
    }

    /*
        All the AsyncTask classes tried to get information from the server
     */
    private class CredentialAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return RestClient.findWithStringParams(params[0], "credential", params[1]);
        }
    }

    private class CredentialAsyncTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return RestClient.findWithStringParamReturnPlainText(params[0], "credential", params[1]);
        }
    }

    private class ConsumptionAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithTwoStringReturnPlainText(strings[0], "consumption", strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            tv_calorieConsumed.setText("You have consumed " + s + " calories today.");
        }
    }

    private class UserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithStringParamReturnPlainText(strings[0], "users", strings[1]);
        }
    }

    private class InsertDatabase extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ReportC report = new ReportC();
            report.setSetcaloriegoal(Integer.parseInt(params[0]));
            return params[0];
        }
    }

    private class ReadDatabase extends AsyncTask<Void, Void, List<ReportC>> {

        @Override
        protected List<ReportC> doInBackground(Void... voids) {
            List<ReportC> reports = db.reportDao().getAll();
            return reports;
        }


    }
}
