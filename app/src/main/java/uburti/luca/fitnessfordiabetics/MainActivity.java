package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.FoodInfo.FoodInfoActivity;
import uburti.luca.fitnessfordiabetics.GlycemicTrends.GlycemicTrendsActivity;
import uburti.luca.fitnessfordiabetics.ViewModel.MainActivityViewModel;
import uburti.luca.fitnessfordiabetics.ViewModel.MainActivityViewModelFactory;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;

import static uburti.luca.fitnessfordiabetics.utils.Utils.getReadableDate;

public class MainActivity extends AppCompatActivity implements DayAdapter.DayClickHandler {
    public static final String DAY_ID_EXTRA = "DAY_ID";
    public static final String DATE_EXTRA = "DATE";
    public static final String ADMOB_ID = "ca-app-pub-3940256099942544~3347511713"; //test AdMob ID
    public static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; //test Ad Unit ID
    private InterstitialAd interstitialAd;
    @BindView(R.id.main_rv)
    RecyclerView mainRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MobileAds.initialize(this, ADMOB_ID);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(AD_UNIT_ID);

//        Toast.makeText(this, getResources().getConfiguration().locale.toString(), Toast.LENGTH_SHORT).show();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRv.setLayoutManager(layoutManager);

        long startDate = Utils.getStartDate();

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(appDatabase, startDate);    //retrieving all DiabeticDays starting from startDate
        MainActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        viewModel.getDiabeticDays().observe(this, new Observer<List<DiabeticDay>>() {
            @Override
            public void onChanged(@Nullable List<DiabeticDay> diabeticDays) {
                Log.d("MainActivity", "days retrieved: " + (diabeticDays == null ? 0 : diabeticDays.size()));
                List<DiabeticDay> diabeticDaysNoGaps = Utils.insertMockDays(diabeticDays); //before updating the UI, fill gaps in the retrieved list with mock days
                populateUI(diabeticDaysNoGaps);
            }
        });
    }


    private void populateUI(List<DiabeticDay> diabeticDays) {
        DayAdapter dayAdapter = new DayAdapter(diabeticDays, this, this);
        mainRv.setAdapter(dayAdapter);
        mainRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onDayClicked(long dayId, long date) {
        Log.d("MainActivity", "onDayClicked dayIdFromBundle: " + dayId + " dateFromBundle: " + getReadableDate(date));

        interstitialAd.loadAd(new AdRequest.Builder().build());

        Intent intent = new Intent(MainActivity.this, DayDetail.class);
        intent.putExtra(DAY_ID_EXTRA, dayId);
        intent.putExtra(DATE_EXTRA, date);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (new Random().nextBoolean()) {
            //on return from other Activities will display an Interstitial Ad 50% of the time
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) { //TODO replicate menu to other activities
        Intent intent;
        switch (item.getItemId()) {
            case R.id.food_info:
                intent = new Intent(this, FoodInfoActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.glycemic_trends:
                intent = new Intent(this, GlycemicTrendsActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

