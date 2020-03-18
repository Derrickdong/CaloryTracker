package com.example.calorietracker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SQLiteDatabase.ReportC;
import com.example.SQLiteDatabase.ReportDatabase;
import com.example.entities.Users;
import com.example.entities.WelcomeListItem;
import com.example.tools.WelcomeListAdaptor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Welcome_fragment extends Fragment implements AdapterView.OnItemClickListener {
    View vWelcome;
    List<WelcomeListItem> list;
    WelcomeListAdaptor adaptor;
    Users loginUser;
    ReportDatabase db = null;
    TextClock textClock;
    ListView lv_welcome;
    String stepStr;
    WelcomeListItem goal;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vWelcome = inflater.inflate(R.layout.welcome_fragment, container, false);

        TextView calTracker = (TextView) vWelcome.findViewById(R.id.tv_welcome);
        TextView time = (TextView) vWelcome.findViewById(R.id.tv_time);
        textClock = (TextClock) vWelcome.findViewById(R.id.tc_clock);
        TextView user = (TextView) vWelcome.findViewById(R.id.tv_user);

        list = new ArrayList<>();

        lv_welcome = (ListView) vWelcome.findViewById(R.id.lv_welcome);

        time.setText("Time :     " + textClock.getText());

        Bundle bundle = getActivity().getIntent().getExtras();
        loginUser = (Users)bundle.getSerializable("user");

        db = Room.databaseBuilder(getActivity().getApplicationContext(), ReportDatabase.class, "ReportDatabase").fallbackToDestructiveMigration().build();
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        String firstName = "";
        firstName = loginUser.getName();
        user.setText("Welcome, " + firstName);

        SharedPreferences spSteps = getActivity().getSharedPreferences("steps", Context.MODE_PRIVATE);
        stepStr = spSteps.getString("steps", null);
        if (stepStr == null) {
            stepStr = "0";
        }

        ConsumptionAsyncTask consumptionAsyncTask = new ConsumptionAsyncTask();
        String userid = "";
        userid = loginUser.getUserid().toString();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String today = sdf.format(date);
        consumptionAsyncTask.execute("totalCaloriesConsumedForTheDay", userid, today + "/");

        UserAsyncTask userAsyncTask = new UserAsyncTask();
        try {
            String s = userAsyncTask.execute("caculateCaloriesBurnedPerStep", userid).get();
            Double totalCalorieBurned = Double.parseDouble(s) * Integer.parseInt(stepStr);
            DecimalFormat df = new DecimalFormat("0.00");
            WelcomeListItem burned = new WelcomeListItem("Total Burned: ", df.format(totalCalorieBurned));
            list.add(burned);
            setItems();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adaptor = new WelcomeListAdaptor(getActivity().getApplicationContext(), list);
        lv_welcome.setAdapter(adaptor);
        lv_welcome.setOnItemClickListener(this);

        return vWelcome;
    }

    private void setItems() {
        goal = new WelcomeListItem("Your goal: ", "");
        SharedPreferences spMyUnits = getActivity().getSharedPreferences("goal",
                Context.MODE_PRIVATE);
        String message = spMyUnits.getString("goal", null);
        if (message == null || message.isEmpty()) {
            goal.setTitle("Please set a goal for today");
        } else {
            goal.setData(message);
        }
        list.add(goal);

        SharedPreferences spSteps = getActivity().getSharedPreferences("steps", Context.MODE_PRIVATE);
        String stepStr = spSteps.getString("steps", null);
        if (stepStr == null) {
            stepStr = "0";
        }
        WelcomeListItem steps = new WelcomeListItem("Your steps today: ", stepStr);
        list.add(steps);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1){
            final EditText editText = new EditText(getActivity());
            new AlertDialog.Builder(getActivity())
                    .setTitle("Please insert your new goal")
                    .setView(editText)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!editText.getText().toString().matches("^[0-9]+$")) {
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "The goal must be numbers";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            } else {
                                String setGoal = editText.getText().toString();
                                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(
                                        "goal", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                                spEditor.putString("goal", setGoal);
                                spEditor.apply();
                                goal.setData(setGoal);
                            }
                            adaptor.notifyDataSetChanged();
                        }
                    }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private class ConsumptionAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithTwoStringReturnPlainText(strings[0], "consumption", strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            WelcomeListItem consumption = new WelcomeListItem("Total Concume: ", s);
            list.add(consumption);
        }
    }

    private class UserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithStringParamReturnPlainText(strings[0], "users", strings[1]);
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
