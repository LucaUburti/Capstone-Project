package uburti.luca.fitnessfordiabetics.FoodInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.utils.NetworkUtils;

public class FoodInfoActivity extends AppCompatActivity implements NetworkUtils.FoodInfoAsyncTask.AsyncResponseListener {
    public static final String FITNESSFORDIABETICS_URL = "https://fitnessfordiabetics.firebaseio.com/food_info.json";
    public static String UNSPLASH_BASE_URL = "https://api.unsplash.com/search/photos?page=1&per_page=1&orientation=landscape&order_by=popular";
    @BindView(R.id.food_info_results_rv)
    RecyclerView resultsRv;

    FoodInfoAdapter foodInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsRv.setLayoutManager(layoutManager);

        NetworkUtils.FoodInfoAsyncTask foodInfoAsyncTask = new NetworkUtils.FoodInfoAsyncTask();
        foodInfoAsyncTask.callback = this;
        foodInfoAsyncTask.execute(FITNESSFORDIABETICS_URL);


//        String food="pizza";
//        String unsplashApiKey="dab357e62a5a079e47e7162b950648f9a047d3c184e2af80ade91dc8b7628eff";
//        Uri unsplashUri = Uri.parse(UNSPLASH_BASE_URL).buildUpon().appendQueryParameter("query", food).appendQueryParameter("client_id", unsplashApiKey).build();
//        NetworkUtils.FoodInfoAsyncTask networkAsyncTask2 =new NetworkUtils.FoodInfoAsyncTask();
//        networkAsyncTask2.callback = this;
//        networkAsyncTask2.execute(unsplashUri.toString());

    }

    @Override
    public void processFinish(ArrayList<FoodInfoPOJO> foodList) {
        Log.d("FoodInfo", "processFinish: " + foodList);
        if (foodList!=null) {
//            resultsTv.setText(output);
            foodInfoAdapter = new FoodInfoAdapter(foodList);
            resultsRv.setAdapter(foodInfoAdapter);

        }
    }
}
