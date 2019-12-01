package com.example.calorietracker;

import android.util.Log;

import com.example.entities.Consumption;
import com.example.entities.Credential;
import com.example.entities.Food;
import com.example.entities.Report;
import com.example.entities.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.net.URL;
import java.net.HttpURLConnection;

public class RestClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/CalorieTracker/webresources/";

    public static String findAll(String className, String param){
        String str = "";
        if (!param.isEmpty()){
            str = param + "/";
        }
        final String methodPath = "trakcer." + className + "/" + str;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";

        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()){
                textResult += inStream.nextLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textResult;
    }

    public static String findWithStringParamReturnPlainText(String methodName, String className, String param){
        final String methodPath = "trakcer." + className + "/" + methodName + "/" + param;
        HttpURLConnection conn = null;
        String textResult = "";
        URL url = null;
        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");

            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()){
                textResult += inStream.nextLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textResult;
    }

    public static String findWithStringParams(String methodName, String className, String param){
        final String methodPath = "trakcer." + className + "/" + methodName + "/" + param;
        HttpURLConnection conn = null;
        String textResult = "";
        URL url = null;
        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()){
                textResult += inStream.nextLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textResult;
    }

    public static String findWithTwoStringReturnPlainText(String methodName, String className, String str1, String str2){
        final String methodPath = "trakcer." + className + "/" + methodName + "/" + str1 + "/" + str2;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";

        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Accept", "text/plain");

            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()){
                textResult += inStream.nextLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textResult;
    }

    public static String findWithMoreThanOneString(String methodName, String className, String str1, String str2, String str3, String str4){
        final String methodPath = "trakcer." + className + "/" + methodName + "/" + str1 + "/" + str2 + str3 + str4;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";

        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Scanner inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()){
                textResult += inStream.nextLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textResult;
    }

    public static void createUser(Users user){
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "trakcer.users/";
        try{
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonUser = gson.toJson(user);
            System.out.println(jsonUser);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(jsonUser.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(jsonUser);
            out.close();
            Log.i("error", conn.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    public static void CreateConsumption(Consumption consumption){
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "trakcer.consumption/";
        try{
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonConsumption = gson.toJson(consumption);
            System.out.println(jsonConsumption);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(jsonConsumption.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(jsonConsumption);
            out.close();
            Log.i("error", conn.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    public static void createCredential(Credential credential){
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "trakcer.credential/";
        try{
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonCredential = gson.toJson(credential);
            System.out.println(jsonCredential);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(jsonCredential.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(jsonCredential);
            out.close();
            Log.i("error",conn.getResponseMessage() + conn.getResponseCode());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    public static void createFood(Food food){
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "trakcer.food/";
        try{
            Gson gson = new Gson();
            String foodJson = gson.toJson(food);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(foodJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(foodJson);
            out.close();
            Log.i("error", conn.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    public static void createReport(Report report){
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath = "trakcer.report/";
        try{
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
            String jsonReport = gson.toJson(report);
            System.out.println(jsonReport);
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(jsonReport.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(jsonReport);
            out.close();
            Log.i("error", conn.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }
}
