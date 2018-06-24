package uburti.luca.fitnessfordiabetics.FoodInfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.R;

public class FoodInfoAdapter extends RecyclerView.Adapter<FoodInfoAdapter.FoodInfoHolder> {

    private ArrayList<FoodInfoPOJO> foodList;
    private Context context;

    FoodInfoAdapter(Context context) {
        this.context = context;
    }

    void setFoodInfoAdapterContent(ArrayList<FoodInfoPOJO> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodInfoAdapter.FoodInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodInfoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodInfoAdapter.FoodInfoHolder holder, int position) {
        FoodInfoPOJO currentFood = foodList.get(position);
        holder.food_item_name_tv.setText(currentFood.getName());

        String glycemicInfo = "";
        if (currentFood.getKcal()!= null && currentFood.getKcal().length() > 0) {
            glycemicInfo += context.getString(R.string.kcal_header) + currentFood.getKcal() +"\n";
        }
        if (currentFood.getCarbs()!= null && currentFood.getCarbs().length() > 0) {
            glycemicInfo += context.getString(R.string.carbs_header) + currentFood.getCarbs() +context.getString(R.string.grams_abbrv)+"\n";
        }
        if (currentFood.getProtein()!= null && currentFood.getProtein().length() > 0) {
            glycemicInfo += context.getString(R.string.proteins_header) + currentFood.getProtein() +context.getString(R.string.grams_abbrv)+"\n";
        }
        if (currentFood.getFat()!= null && currentFood.getFat().length() > 0) {
            glycemicInfo += context.getString(R.string.fat_header) + currentFood.getFat() +context.getString(R.string.grams_abbrv)+"\n";
        }


        if (currentFood.getGiMinValue()!= null && currentFood.getGiMinValue().length() > 0) { //TODO polish glycemic index value in the UI
            glycemicInfo += context.getString(R.string.gi_min_header) + currentFood.getGiMinValue() +"\n";
        }
        if (currentFood.getGiMaxValue()!= null && currentFood.getGiMaxValue().length() > 0) {
            glycemicInfo += context.getString(R.string.gi_max_header) + currentFood.getGiMaxValue() +"\n";
        }
        if (currentFood.getGiAverage()!= null && currentFood.getGiAverage().length() > 0) {
            glycemicInfo += context.getString(R.string.gi_average_header) + currentFood.getGiAverage();
        }
        holder.food_item_glycemic_info_tv.setText(glycemicInfo);

        Picasso.get()
                .load(currentFood.getPhoto())
                .placeholder(R.drawable.genericfood)
                .error(R.drawable.genericfood)
                .into(holder.food_item_iv);
        holder.food_item_iv.setContentDescription(currentFood.getName());
    }

    @Override
    public int getItemCount() {
        if (foodList == null) {
            return 0;
        } else {
            return foodList.size();
        }
    }

    class FoodInfoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.food_item_name_tv)
        TextView food_item_name_tv;
        @BindView(R.id.food_item_iv)
        ImageView food_item_iv;
        @BindView(R.id.food_item_glycemic_info_tv)
        TextView food_item_glycemic_info_tv;

        FoodInfoHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
