package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText etUsername;
    EditText etPassword;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.username_et);
        etPassword = (EditText) findViewById(R.id.password_et);

        Button login = (Button) findViewById(R.id.signin_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (checkUserExist(username)){
                    if (checkPassword(username, password)){
                        Intent intent = new Intent(MainActivity.this, NavigationDrawer.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        Context context = getApplicationContext();
                        CharSequence text = "Incorrect password";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "The user does not exist";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });

        Button signup = (Button) findViewById(R.id.signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp_Activity.class);
                startActivity(intent);
            }
        });
    }

    public String hashPassword(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean checkUserExist(String username){
        CredentialUsernameAsyncTask credentialAsyncTask = new CredentialUsernameAsyncTask();
        credentialAsyncTask.execute(username);
        String result = null;
        try {
            result = credentialAsyncTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.equals("[]")){
            return false;
        }
        return true;
    }

    public boolean checkPassword(String username, String password){
        CredentialAsyncTask credentialAsyncTask = new CredentialAsyncTask();
        password = hashPassword(password);
        credentialAsyncTask.execute(username, password);
        String result = null;
        try {
            result = credentialAsyncTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.equals("[]")){
            return false;
        }
        return true;
    }

    private class CredentialAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return RestClient.findWithMoreThanOneString("findByUsernameAndPassword", "credential", params[0], params[1],"","");
        }
    }

    private class CredentialUsernameAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return RestClient.findWithStringParams("findByUsername", "credential", params[0]);
        }
    }
}
