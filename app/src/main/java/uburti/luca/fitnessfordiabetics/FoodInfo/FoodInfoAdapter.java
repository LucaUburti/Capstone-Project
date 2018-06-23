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
//                context.getString(R.string.glycemic_index);
        if (currentFood.getMinValue().length() > 0) {
            glycemicInfo += context.getString(R.string.min_value_header) + currentFood.getMinValue() +"\n";
        }
        if (currentFood.getMaxValue().length() > 0) {
            glycemicInfo += context.getString(R.string.max_value_header) + currentFood.getMaxValue() +"\n";
        }
        if (currentFood.getAverage().length() > 0) {
            glycemicInfo += context.getString(R.string.average_value_header) + currentFood.getAverage();
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
