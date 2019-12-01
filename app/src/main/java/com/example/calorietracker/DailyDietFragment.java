package com.example.calorietracker;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entities.Consumption;
import com.example.entities.Credential;
import com.example.entities.Food;
import com.example.entities.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DailyDietFragment extends Fragment implements View.OnClickListener {
    View vDailyDiet;
    Spinner sp_category = null;
    Spinner sp_foods = null;
    String selectedCategory = "";
    TextView tv_foodDest = null;
    Button btn_consume = null;
    Button btn_add = null;
    EditText et_foodname = null;
    Button btn_search = null;
    TextView tv_searchedFood = null;
    TextView tv_addFood = null;
    EditText et_quantity = null;
    Food food = null;
    Users user = null;
    Button btn_addfood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        vDailyDiet = inflater.inflate(R.layout.daily_diet_fragment, container, false);

        sp_category = (Spinner) vDailyDiet.findViewById(R.id.sp_category);
        sp_foods = (Spinner) vDailyDiet.findViewById(R.id.sp_food);
        tv_foodDest = (TextView) vDailyDiet.findViewById(R.id.tv_fooddesc);
        btn_add = (Button) vDailyDiet.findViewById(R.id.btn_add);
        et_foodname = (EditText)vDailyDiet.findViewById(R.id.et_foodname);
        btn_search = (Button) vDailyDiet.findViewById(R.id.btn_search);
        tv_searchedFood = (TextView) vDailyDiet.findViewById(R.id.tv_searchedFood);
        tv_addFood = (TextView) vDailyDiet.findViewById(R.id.tv_addFood);
        btn_consume = (Button) vDailyDiet.findViewById(R.id.btn_consume);
        et_quantity = (EditText) vDailyDiet.findViewById(R.id.et_quantity);
        btn_addfood = (Button)vDailyDiet.findViewById(R.id.btn_addFood);
        getUser();

        String category = "";
        String[] categoriesArray = {};
        try {
            FoodAsyncTask foodAsyncTask = new FoodAsyncTask();
            category = foodAsyncTask.execute("findAllCategories","").get();
            categoriesArray = category.split(",");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> categories = Arrays.asList(categoriesArray);

        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(spinnerAdapter1);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                try {
                    FoodAsyncTask foodAsyncTask = new FoodAsyncTask();
                    String foodJson = foodAsyncTask.execute("findByCategory", selectedCategory).get();
                    final JSONArray jsonArray = new JSONArray(foodJson);
                    final List<Food> foods = new ArrayList<>();
                    List<String> foodNames = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject food = jsonArray.getJSONObject(i);
                        Food newFood = new Food(food.getInt("foodid"), food.getString("name"), food.getString("category"),
                                food.getInt("calorieamount"), food.getString("servingunit"), food.getString("servingamount"), food.getInt("fat"));
                        foods.add(newFood);
                    }
                    for (Food food: foods){
                        foodNames.add(food.getName());
                    }

                    final List<Food> finalFoods = foods;
                    ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, foodNames);
                    spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_foods.setAdapter(spinnerAdapter2);
                    sp_foods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedFoodName = parent.getItemAtPosition(position).toString();
                            String tv_foods = "";
                            Food selectedFood = null;
                            for (Food food : finalFoods) {
                                if (food.getName().equals(selectedFoodName)) {
                                    selectedFood = food;
                                }
                            }
                            tv_foods = tv_foods + "Name: " + selectedFood.getName() + "\n" + "Category: " + selectedFood.getCategory() + "\n" +
                                    "Serving Unit: " + selectedFood.getServingunit() + "\n" + "Serving Amount: " + selectedFood.getServingamount() + "\n" +
                                    "Calorie: " + selectedFood.getCalorieamount() + "\n" + "Fat: " + selectedFood.getFat();
                            tv_foodDest.setText(tv_foods);

                            food = selectedFood;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "selectedCategory";
            }
        });

        btn_consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = et_quantity.getText().toString();
                if (quantityStr.equals("")) {
                    ConsumptionAsyncTask consumptionAsyncTask = new ConsumptionAsyncTask();
                    consumptionAsyncTask.execute(quantityStr);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please insert the quantity", Toast.LENGTH_SHORT);
                }
            }
        });

        btn_add.setOnClickListener(this);
        return vDailyDiet;
    }

    String keyword = "";
    @Override
    public void onClick(View v) {
        tv_addFood.setVisibility(View.INVISIBLE);
        btn_add.setVisibility(View.INVISIBLE);
        et_foodname.setVisibility(View.VISIBLE);
        btn_search.setVisibility(View.VISIBLE);
        tv_searchedFood.setVisibility(View.VISIBLE);
        btn_addfood.setVisibility(View.VISIBLE);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = et_foodname.getText().toString();
                NutrientAsyncTask nutrientAsyncTask = new NutrientAsyncTask();
                nutrientAsyncTask.execute(keyword);
            }
        });

        btn_addfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFoodAsync createFoodAsync = new CreateFoodAsync();
                createFoodAsync.execute(keyword);
            }
        });
    }

    private class CreateFoodAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Food food = null;
            try {
                String result = SearchGoogleAPI.search(keyword, null, null, 2);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("parsed");
                JSONObject foodObject = jsonArray.getJSONObject(0);
                JSONObject foodObj = foodObject.getJSONObject("food");
                JSONObject nutrition = foodObj.getJSONObject("nutrients");
                food = new Food(1, foodObj.getString("label"), foodObj.getString("category"), nutrition.getInt("ENERC_KCAL"), "g", "100",nutrition.getInt("FAT"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RestClient.createFood(food);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Context context = getActivity().getApplicationContext();
            CharSequence text = "The user does not exist";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    private class NutrientAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = SearchGoogleAPI.search(strings[0], null, null, 2);
            return SearchGoogleAPI.getNutritionInfo(result);
        }

        @Override
        protected void onPostExecute(String s) {
            tv_searchedFood.setText(s);
        }


    }

    private class FoodAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return RestClient.findWithStringParams(params[0], "food", params[1]);
        }
    }

    private class ConsumptionAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Date date = Calendar.getInstance().getTime();
            Consumption c = new Consumption(date, Integer.parseInt(strings[0]), food, user);
            Log.e("test", "test");
            RestClient.CreateConsumption(c);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Consume successfully";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private class CredentialAsynTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            Bundle bundle = getActivity().getIntent().getExtras();
            String username = bundle.getString("username");
            return RestClient.findWithStringParams("findByUsername", "credential", username);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONObject jsonUser = jsonObject.getJSONObject("userid");
                String date = jsonUser.getString("dob");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date dob = null;
                dob = sdf.parse(date);
                user = new Users(Integer.parseInt(jsonUser.getString("userid")), jsonUser.getString("name"), jsonUser.getString("surname"),
                        jsonUser.getString("email"), dob, jsonUser.getInt("height"), jsonUser.getInt("weight"), jsonUser.getString("gender").charAt(0),
                        jsonUser.getString("address"), jsonUser.getString("postcode"), jsonUser.getInt("levelofactivity"), jsonUser.getInt("stepspermile"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void getUser(){
        CredentialAsynTask credentialAsynTask = new CredentialAsynTask();
        credentialAsynTask.execute();
    }

}
