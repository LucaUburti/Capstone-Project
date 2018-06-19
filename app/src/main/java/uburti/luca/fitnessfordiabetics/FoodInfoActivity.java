package uburti.luca.fitnessfordiabetics;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.utils.NetworkUtils;

public class FoodInfoActivity extends AppCompatActivity implements NetworkUtils.FoodAsyncTask.AsyncResponseListener{
    public static final String FITNESSFORDIABETICS_URL = "https://fitnessfordiabetics.firebaseio.com/food_info.json";
    @BindView(R.id.food_info_results_tv)
    TextView resultsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        ButterKnife.bind(this);

        NetworkUtils.FoodAsyncTask foodAsyncTask = new NetworkUtils.FoodAsyncTask();
        foodAsyncTask.callback=this;
        foodAsyncTask.execute(FITNESSFORDIABETICS_URL);

    }

    @Override
    public void processFinish(String output) {
        Log.d("FoodInfo", "processFinish: "+output);
        if (!output.isEmpty()) {
            resultsTv.setText(output);
        }
    }
}
