package uburti.luca.fitnessfordiabetics.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import uburti.luca.fitnessfordiabetics.FoodInfo.FoodInfoPOJO;

public class NetworkUtils {

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String output = scanner.next();
                Log.d("NetworkUtils", "getResponseFromHttpUrl: " + output);
                return output;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static class FoodInfoAsyncTask extends AsyncTask<String, Void, ArrayList<FoodInfoPOJO>> {
        public interface AsyncResponseListener {
            void processFinish(ArrayList<FoodInfoPOJO> output);
        }

        public AsyncResponseListener callback = null;

        @Override
        protected ArrayList<FoodInfoPOJO> doInBackground(String... urls) {
            URL myUrl = null;
            String stringResults = null;
            try {
                myUrl = new URL(urls[0]);
            } catch (MalformedURLException e) {
                Log.e("URL", "Malformed url " + e);
            }
            try {
                if (myUrl != null) {
                    stringResults = NetworkUtils.getResponseFromHttpUrl(myUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<FoodInfoPOJO> results = null;
            try {
                results = extractFoodInfoFromJson(stringResults);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<FoodInfoPOJO> result) {
            callback.processFinish(result);
        }

    }

    private static ArrayList<FoodInfoPOJO> extractFoodInfoFromJson(String stringResults) throws JSONException {
        ArrayList<FoodInfoPOJO> results = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(stringResults);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentOjbect = jsonArray.getJSONObject(i);
            String name = currentOjbect.getJSONObject("name").getString("en_us");
            String minValue = currentOjbect.getString("min_value");
            String maxValue = currentOjbect.getString("max_value");
            String average = currentOjbect.getString("average");
            FoodInfoPOJO foodInfoPOJO = new FoodInfoPOJO(name, minValue, maxValue, average);
            results.add(foodInfoPOJO);
        }
        return results;
    }



}
