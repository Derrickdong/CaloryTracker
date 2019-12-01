package com.example.calorietracker;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SearchFood_fragment extends Fragment implements View.OnClickListener{
    private View vSearchFood;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vSearchFood = inflater.inflate(R.layout.fragment_search_food, container, false);

        final EditText editText = (EditText) vSearchFood.findViewById(R.id.editText);
        Button btnSearch = (Button) vSearchFood.findViewById(R.id.search_btn);
        final Button btnImage = (Button) vSearchFood.findViewById(R.id.show_image);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText.getText().toString();
                SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                searchAsyncTask.execute(keyword);
                SearchNutrition searchNutrition = new SearchNutrition();
                searchNutrition.execute(keyword);
                btnImage.setVisibility(View.VISIBLE);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText.getText().toString();
                SearchImage searchImage = new SearchImage();
                searchImage.execute(keyword);
            }
        });
        return vSearchFood;
    }

    @Override
    public void onClick(View v) {

    }

    private class SearchAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return SearchGoogleAPI.search(params[0], new String[] {"num"}, new String[] {"1"}, 1);
        }

        @Override
        protected void onPostExecute(String result){
            TextView textView = (TextView) vSearchFood.findViewById(R.id.search_tv);
            textView.setText(SearchGoogleAPI.getSnippet(result));
        }
    }

    private class SearchNutrition extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            return SearchGoogleAPI.search(strings[0], null,null,  2);
        }

        @Override
        protected void onPostExecute(String s) {
            TextView tv_nutrition = (TextView) vSearchFood.findViewById(R.id.tv_foodNutrition);
            tv_nutrition.setText(SearchGoogleAPI.getNutritionInfo(s));
        }
    }

    private class SearchImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            String result = SearchGoogleAPI.search(params[0], new String[] {"num", "searchType"}, new String[] {"1", "image"}, 1);
            String url = SearchGoogleAPI.getImage(result);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            ImageView imageView = (ImageView) vSearchFood.findViewById(R.id.food_image);
            imageView.setImageBitmap(result);
        }
    }
}
