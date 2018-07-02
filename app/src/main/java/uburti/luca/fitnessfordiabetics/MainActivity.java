package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.foodinfo.FoodInfoActivity;
import uburti.luca.fitnessfordiabetics.glycemictrends.GlycemicTrendsActivity;
import uburti.luca.fitnessfordiabetics.utils.RandomDBDataGenerator;
import uburti.luca.fitnessfordiabetics.utils.Utils;
import uburti.luca.fitnessfordiabetics.viewmodel.MainActivityViewModel;
import uburti.luca.fitnessfordiabetics.viewmodel.MainActivityViewModelFactory;

import static uburti.luca.fitnessfordiabetics.TOSActivity.TOS_ACCEPTED;
import static uburti.luca.fitnessfordiabetics.utils.Utils.getReadableDate;

public class MainActivity extends AppCompatActivity implements DayAdapter.DayClickHandler {
    public static final String DAY_ID_EXTRA = "DAY_ID";
    public static final String DATE_EXTRA = "DATE";
    private static final String ADMOB_ID = "ca-app-pub-3940256099942544~3347511713"; //test AdMob ID
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; //test Ad Unit ID
    private InterstitialAd interstitialAd;
    private FirebaseAnalytics firebaseAnalytics;
    @BindView(R.id.main_rv)
    RecyclerView mainRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        checkTOSAccepted();

        MobileAds.initialize(this, ADMOB_ID);   //setting up AdMob...
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(AD_UNIT_ID);


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

    private void checkTOSAccepted() {
        //if Terms of service not yet accepted go to the TOS Activity

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean TOSAccepted = sharedPrefs.getBoolean(TOS_ACCEPTED, false);
        if (!TOSAccepted) { //TOS not yet accepted: go straight to TOS Activity
            Intent intent = new Intent(this, TOSActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //clear backstack: we don't want the users to be able to use the app by pressing the back button until they accept the TOS
            startActivity(intent);
        }
    }


    private void populateUI(List<DiabeticDay> diabeticDays) {
        DayAdapter dayAdapter = new DayAdapter(diabeticDays, this, this);
        mainRv.setAdapter(dayAdapter);
        mainRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onDayClicked(long dayId, long date) {   //callback method when clicking a RecyclerView item

        Log.d("MainActivity", "onDayClicked dayIdFromBundle: " + dayId + " dateFromBundle: " + getReadableDate(date));

        interstitialAd.loadAd(new AdRequest.Builder().build()); //prepare an Ad to display when we return here

        Intent intent = new Intent(MainActivity.this, DayDetail.class);
        intent.putExtra(DAY_ID_EXTRA, dayId);
        intent.putExtra(DATE_EXTRA, date);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //on return from any other Activities will display an Interstitial Ad 50% of the time
        if (new Random().nextInt(100) > 50) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        interstitialAd.loadAd(new AdRequest.Builder().build()); //prepares an Ad to display for when we return
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
            case R.id.my_therapy:
                intent = new Intent(this, MyTherapyActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.terms_of_service:
                intent = new Intent(this, TOSActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.populate_db_with_random_data:
                RandomDBDataGenerator randomDBDataGenerator = new RandomDBDataGenerator(this);
                randomDBDataGenerator.startDBReset();
                return true;
            case R.id.delete_everything:
                (new RandomDBDataGenerator(this)).startDBDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

