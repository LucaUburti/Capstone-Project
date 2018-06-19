package uburti.luca.fitnessfordiabetics.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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

    public static class FoodAsyncTask extends AsyncTask<String, Void, String> {
        public interface AsyncResponseListener {
            void processFinish(String output);
        }

        public AsyncResponseListener callback = null;

        @Override
        protected String doInBackground(String... urls) {
            URL myUrl = null;
            String results = null;
            try {
                myUrl = new URL(urls[0]);
            } catch (MalformedURLException e) {
                Log.e("URL", "Malformed url " + e);
            }
            try {
                if (myUrl != null) {
                    results = NetworkUtils.getResponseFromHttpUrl(myUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String result) {
            callback.processFinish(result);
        }

    }


}
