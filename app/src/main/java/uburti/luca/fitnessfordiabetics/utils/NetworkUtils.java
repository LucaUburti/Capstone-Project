package uburti.luca.fitnessfordiabetics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

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

import uburti.luca.fitnessfordiabetics.foodinfo.FoodInfoPOJO;

public class NetworkUtils {

    private static final String JSON_NAME = "name";
    private static final String JSON_EN_US = "en_us";
    private static final String JSON_KCAL = "kcal";
    private static final String JSON_CARBS = "carbs";
    private static final String JSON_PROTEIN = "protein";
    private static final String JSON_FAT = "fat";
    private static final String JSON_GI_MIN_VALUE = "gi_min_value";
    private static final String JSON_GI_MAX_VALUE = "gi_max_value";
    private static final String JSON_GI_AVERAGE = "gi_average";
    private static final String JSON_PHOTO = "photo";

    private static String getResponseFromHttpUrl(URL url) throws IOException {
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
        //AsyncTask which calls an external URL, decodes the JSON and returns a List of food

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
            callback.processFinish(result); //callback implemented in FoodInfoActivity
        }

    }

    private static ArrayList<FoodInfoPOJO> extractFoodInfoFromJson(String stringResults) throws JSONException {
        //decode a JSON input string into a list of FoodInfo objects

        if (stringResults == null) {
            return null;
        }
        ArrayList<FoodInfoPOJO> results = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(stringResults);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentOjbect = jsonArray.getJSONObject(i);
            String name = currentOjbect.getJSONObject(JSON_NAME).getString(JSON_EN_US);
            String kcal = currentOjbect.optString(JSON_KCAL);
            String carbs = currentOjbect.optString(JSON_CARBS);
            String protein = currentOjbect.optString(JSON_PROTEIN);
            String fat = currentOjbect.optString(JSON_FAT);
            String giMinValue = currentOjbect.optString(JSON_GI_MIN_VALUE);
            String giMaxValue = currentOjbect.optString(JSON_GI_MAX_VALUE);
            String giAverage = currentOjbect.optString(JSON_GI_AVERAGE);
            String photo = currentOjbect.optString(JSON_PHOTO);
            FoodInfoPOJO foodInfoPOJO = new FoodInfoPOJO(name, giMinValue, giMaxValue, giAverage, photo, kcal, carbs, protein, fat);
            results.add(foodInfoPOJO);
        }
        return results;
    }


    public static boolean isOnline(Context context) { //check if we have connectivity
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
