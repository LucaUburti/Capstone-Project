package uburti.luca.fitnessfordiabetics.FoodInfo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.R;

public class FoodInfoAdapter extends RecyclerView.Adapter<FoodInfoAdapter.FoodInfoHolder> {

    private ArrayList<FoodInfoPOJO> foodList;

    FoodInfoAdapter(ArrayList<FoodInfoPOJO> foodList) {
        this.foodList = foodList;
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
    }

    @Override
    public int getItemCount() {
        if (foodList.isEmpty()) {
            return 0;
        } else {
            return foodList.size();
        }
    }

    class FoodInfoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.food_item_name_tv)
        TextView food_item_name_tv;

        FoodInfoHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
