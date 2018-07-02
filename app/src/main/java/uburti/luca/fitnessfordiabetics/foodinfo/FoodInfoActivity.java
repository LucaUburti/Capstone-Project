package uburti.luca.fitnessfordiabetics.foodinfo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.utils.NetworkUtils;

public class FoodInfoActivity extends AppCompatActivity implements NetworkUtils.FoodInfoAsyncTask.AsyncResponseListener {
    private static final String FITNESSFORDIABETICS_URL = "https://fitnessfordiabetics.firebaseio.com/food_info.json";
    private static final int MIN_SEARCH_CHARS = 2;
    @BindView(R.id.food_info_results_rv)
    RecyclerView resultsRv;
    @BindView(R.id.food_info_search_et)
    EditText foodInfoSearchEt;

    private FoodInfoAdapter foodInfoAdapter;
    private ArrayList<FoodInfoPOJO> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //https://stackoverflow.com/questions/9732761/prevent-the-keyboard-from-displaying-on-activity-start


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsRv.setLayoutManager(layoutManager);
        foodInfoAdapter = new FoodInfoAdapter(this);
        if (!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, R.string.check_connectivity, Toast.LENGTH_LONG).show();
        } else {
            NetworkUtils.FoodInfoAsyncTask foodInfoAsyncTask = new NetworkUtils.FoodInfoAsyncTask(); //call the AsyncTask
            foodInfoAsyncTask.callback = this;
            foodInfoAsyncTask.execute(FITNESSFORDIABETICS_URL);
        }


        foodInfoSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (foodList != null) { //ensure AsyncTask returned full food list
                    ArrayList<FoodInfoPOJO> foodFound = searchFood(foodInfoSearchEt.getText().toString());
                    UpdateUIResults(foodFound);
                } //if we don't have the food list do nothing for now: the AsyncTask callback will update the UI if the search EditText isn't empty
            }
        });


    }


    private ArrayList<FoodInfoPOJO> searchFood(String searchString) {
        Log.d("FoodInfo", "Searchstring: " + searchString);
        if (searchString.length() < MIN_SEARCH_CHARS) {
            return null; //wait for a few chars before doing a look up
        }

        ArrayList<FoodInfoPOJO> foodFound = new ArrayList<>();
        for (int i = 0; i < foodList.size(); i++) {
            String currentFood = foodList.get(i).getName();
            if (currentFood.toLowerCase().contains(searchString.toLowerCase())) { //check if the current food matches the search string, ignoring Uppercases

                foodFound.add(foodList.get(i));
            }
        }
        return foodFound;

    }

    private void UpdateUIResults(ArrayList<FoodInfoPOJO> foodFound) {
        foodInfoAdapter.setFoodInfoAdapterContent(foodFound);
        resultsRv.setAdapter(foodInfoAdapter);
    }

    @Override
    public void processFinish(ArrayList<FoodInfoPOJO> foodList) {   //Asynctask callback
        if (foodList != null) {
            this.foodList = foodList;
            if (foodInfoSearchEt.getText().toString().length() > 0) { //if the search Edittext isn't empty, do a search now and update the UI
                ArrayList<FoodInfoPOJO> foodFound = searchFood(foodInfoSearchEt.getText().toString());
                UpdateUIResults(foodFound);
            }
        }
    }
}
