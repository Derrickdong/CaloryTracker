package com.example.calorietracker;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchGoogleAPI {
    private static final String Google_API_KEY = "AIzaSyCp7J-jiNO2JXxu8WYIjFkbD3apeJ7vUrM";
    private static final String Google_ID_cx = "004417283233849037788:k5i7yz-onuw";
    private static final String Food_API_KEY = "95a410ba1174d5c0577cd9f0d10394d7";
    private static final String Food_ID_cx = "004417283233849037788:roozqxsyhvc";

    public static String search(String keyword, String[] params, String values[], int searchType){
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter = "";

        if (params != null && values != null){
            for(int i = 0; i< params.length; i++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }

        try {
            if (searchType == 1)
            url = new URL("https://www.googleapis.com/customsearch/v1?key=" +
                    Google_API_KEY + "&cx=" + Google_ID_cx + "&q=" + keyword + query_parameter);
            else {
                url = new URL("https://api.edamam.com/api/food-database/parser?" + "app_id=ce7f3da0&" + "app_key=" +
                        Food_API_KEY + "&ingr=" + keyword);}
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }
        return textResult;
    }

    public static String getSnippet(String result){
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if(jsonArray != null && jsonArray.length() >0 ){
                snippet = jsonArray.getJSONObject(0).getString("snippet");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    public static String getImage(String result){
        String image = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if(jsonArray != null && jsonArray.length() >0 ){
                image = jsonArray.getJSONObject(0).getString("link");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public static String getNutritionInfo(String result){
        String info = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("parsed");
            JSONObject foodObject = jsonArray.getJSONObject(0);
            JSONObject food = foodObject.getJSONObject("food");
            JSONObject nutrition = food.getJSONObject("nutrients");
            info = "Name: " + food.getString("label") + "\n" + "Calorie: " + nutrition.getDouble("ENERC_KCAL") +
                    "\n" + "Fat: " + nutrition.getDouble("FAT") + "\n" + "Serving Unit: 100g";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }
}
