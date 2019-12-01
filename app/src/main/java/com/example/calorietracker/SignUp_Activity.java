package com.example.calorietracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entities.Credential;
import com.example.entities.Users;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class SignUp_Activity extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    EditText et_firstName;
    EditText et_lastName;
    EditText et_email;
    EditText et_height;
    EditText et_weight;
    RadioGroup rd_gender;
    EditText et_address;
    EditText et_postcode;
    Spinner sp_level;
    TextView tv_date;
    EditText et_steps;
    DatePickerDialog.OnDateSetListener dateListener;
    String postcode = "";
    int levelOfActivity = 0;
    String date = "";
    String signupDate = "";
    private static final String TAG = "SignUp_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_firstName = (EditText)findViewById(R.id.et_firstName);
        et_lastName = (EditText) findViewById(R.id.et_lastName);
        et_email = (EditText)findViewById(R.id.et_email);
        et_height = (EditText) findViewById(R.id.et_height);
        et_weight = (EditText) findViewById(R.id.et_weight);
        rd_gender = (RadioGroup) findViewById(R.id.radio_gender);
        et_address = (EditText) findViewById(R.id.et_address);
//        sp_Postcode = (Spinner) findViewById(R.id.sp_postcode);
        et_postcode = (EditText)findViewById(R.id.et_postcode);
        sp_level = (Spinner) findViewById(R.id.sp_levelOfActivity);
//        UsersAsyncTask usersAsyncTask = new UsersAsyncTask();
//        String[] postcodes = null;
//        try {
//            String postcodeList = usersAsyncTask.execute("users", "findAllPostcode").get();
//            postcodes = postcodeList.split(",");
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, postcodes);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_Postcode.setAdapter(spinnerAdapter);
//        sp_Postcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                postcode = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                postcode = "0000";
//            }
//        });
        tv_date = (TextView) findViewById(R.id.tv_selectDate);
        postcode = et_postcode.getText().toString();

        List<Integer> levels = new ArrayList<>();
        levels.add(0,1);levels.add(1,2);levels.add(2,3);levels.add(3,4);levels.add(4,5);
        ArrayAdapter<Integer> spinnerAdapter2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, levels);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_level.setAdapter(spinnerAdapter2);
        sp_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                levelOfActivity = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Context context = getApplicationContext();
                CharSequence text = "Please select your level of activity";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        et_steps = (EditText)findViewById(R.id.et_stepsPMile);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SignUp_Activity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, dateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String receivedDate = month + "-" + dayOfMonth + "-" + year;
                Log.d(TAG, "onDateSet: MM/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);
                tv_date.setText(receivedDate);
                date = receivedDate;
            }
        };

        Button btnsign = (Button)findViewById(R.id.btnSignUp);
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();

                String password = et_password.getText().toString();

                String firstName = et_firstName.getText().toString();
                String lastName = et_lastName.getText().toString();
                String email = et_email.getText().toString();
//                String dob = et_dob.getText().toString();//change this to calendar
//                Date date = new Date();
//                try {
//                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                String strHeight = et_height.getText().toString();
                int height = 0;


                String strWeight = et_weight.getText().toString();
                int weight = 0;

                int radioButtonID = rd_gender.getCheckedRadioButtonId();
                View radioButton = rd_gender.findViewById(radioButtonID);
                int idx = rd_gender.indexOfChild(radioButton);
                Character gender = null;
                if (idx == 0){
                    gender = 'M';
                }
                else
                    gender = 'F';
                String address = et_address.getText().toString();
                String strSteps = et_steps.getText().toString();
                int steps = 0;

                signupDate = Calendar.YEAR + "/" + Calendar.MONTH + "/" + Calendar.DAY_OF_MONTH;

                if (!checkUserName(username)){
                    Context context = getApplicationContext();
                    CharSequence text = "Your Username has been used or its length is not long enough, please insert another one";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if (!checkPassword(password)){
                    Context context = getApplicationContext();
                    CharSequence text = "Your password must be longer than 8 characters, please insert again";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if (!checkName(firstName) && checkName(lastName)) {
                    Context context = getApplicationContext();
                    CharSequence text = "First name and Last name must be alphabetic based";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if (!checkSteps(strSteps)){
                    Context context = getApplicationContext();
                    CharSequence text = "Please insert steps per mile greater than 0";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if(!checkHeight(strHeight)){
                    Context context = getApplicationContext();
                    CharSequence text = "Please insert a height greater than 0";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if (!checkWeight(strWeight)){
                    Context context = getApplicationContext();
                    CharSequence text = "Please insert a weight greater than 0";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if (!checkSteps(strSteps)){
                    Context context = getApplicationContext();
                    CharSequence text = "Please insert a steps greater than 0";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else{
                    height = Integer.parseInt(strHeight);
                    weight = Integer.parseInt(strWeight);
                    steps = Integer.parseInt(strSteps);
                    createUsersAndCredentials(firstName, lastName, email, date, height, weight, gender, address, postcode, levelOfActivity, steps, username, password, signupDate);
                }

//                Intent intent = new Intent(SignUp_Activity.this, NavigationDrawer.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("username", username);
//                intent.putExtras(bundle);
//                startActivity(intent);
                }
            });
    }

    private void createUsersAndCredentials(String name, String surname, String email, String dob, int height, int weight, Character gender, String address, String postcode, Integer levelofactivity, Integer stepspermile, String username, String passwordhash, String signupdate){
        PostAsyncTask postAsyncTask = new PostAsyncTask();
        postAsyncTask.execute(name, surname, email, dob, height + "", weight + "", gender.toString(), address, postcode, levelofactivity + "", stepspermile + "", username, passwordhash, signupdate);
    }

    private List<String> getAllUsername(){
        List<String> list = new ArrayList<>();
        String result = "";
        CredentialAsyncTask cre = new CredentialAsyncTask();
        try {
            result = cre.execute().get();
            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String username = jsonArray.getJSONObject(i).getString("username");
                    list.add(username);
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private boolean checkSteps(String stepsStr){
        int steps = 0;
        try {
            steps = Integer.parseInt(stepsStr);
        }
        catch (Exception e){
            return false;
        }
        return steps > 0;
    }

    private boolean checkPassword(String password){
        if (password.trim().length() < 8)
            return false;
        return true;
    }

    private boolean checkName(String name){
        for (int i = 0; i < name.length(); i++){
            if (!Character.isLetter(name.charAt(i))){
                return false;
            }
        }
        return true;
    }

    private boolean checkHeight(String strHeight){
        int height = 0;
        try {
            height = Integer.parseInt(strHeight);
        }
        catch (Exception e){
            return false;
        }
        return height > 0;
    }

    private boolean checkWeight(String strWeight){
        int weight = 0;
        try {
            weight = Integer.parseInt(strWeight);
        }
        catch (Exception e){
            return false;
        }
        return weight > 0;
    }

    private boolean checkUserName(String username){
        List<String> list = getAllUsername();
        for(String str: list){
            if (str.equals(username)){
                return false;
            }
        }
        if (username.length() < 5){
            return false;
        }
        return true;
    }

    private class CredentialAsyncTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
           return RestClient.findAll("credential", "");
        }

//        @Override
//        protected void onPostExecute(String result){
//            try{
//                JSONArray jsonArray = new JSONArray(result);
//
//                if (jsonArray.length() > 0){
//                    for (int i = 0; i<jsonArray.length(); i++){
//                        Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(jsonArray.getJSONObject(i).getString("signupdate"));
//                        int userid = Integer.parseInt(jsonArray.getJSONObject(i).getString("userid"));
//                        Credential credential = new Credential(jsonArray.getJSONObject(i).getString("username"),
//                                jsonArray.getJSONObject(i).getString("passwordhash"), date, userid);
//                        list.add(credential);
//                    }
//                }
//            }catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private class PostAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Date dob = null;
            Date signDate = null;
            try {
                dob = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(params[3]);
                signDate = Calendar.getInstance().getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Users user = new Users(1, params[0], params[1], params[2], dob, Integer.parseInt(params[4]), Integer.parseInt(params[5]), params[6].charAt(0), params[7], params[8], Integer.parseInt(params[9]), Integer.parseInt(params[10]));
            RestClient.createUser(user);
            int userid = Integer.parseInt(RestClient.findWithStringParamReturnPlainText("findRecentUserid", "users", ""));
            user.setUserid(userid);
            Credential credential = new Credential(params[11], params[12], signDate, user);
            credential.hashPassword();
            RestClient.createCredential(credential);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Context context = getApplicationContext();
            CharSequence text = "Signed up successful!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
