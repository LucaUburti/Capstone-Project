package uburti.luca.fitnessfordiabetics;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;

import static uburti.luca.fitnessfordiabetics.utils.Utils.valueOfIntWithoutZero;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<DiabeticDay> diabeticDays;
    private Context context;

    private final DayClickHandler dayClickHandler;

    public interface DayClickHandler {
        void onDayClicked(long dayId, long date);
    }

    DayAdapter(List<DiabeticDay> diabeticDays, Context context, DayClickHandler dayClickHandler) {
        this.diabeticDays = diabeticDays;
        this.context = context;
        this.dayClickHandler = dayClickHandler;
    }

    @NonNull
    @Override
    public DayAdapter.DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.DayViewHolder holder, int position) {
        DiabeticDay diabeticDay = diabeticDays.get(position);
        String date = Utils.getReadableDate(diabeticDay.getDate());

        if (diabeticDay.isBlankDay()) {
            holder.dateTv.setText(date);    //no data in DB, just populate the mock day with the date
            holder.dayItemCl.setBackgroundColor(Color.parseColor(context.getString(R.string.blank_day_bg)));
            holder.warningIv.setVisibility(View.GONE);
            holder.warningTv.setVisibility(View.GONE);
            holder.beforeBreakfastGlycemiaTv.setText("-");
            holder.afterBreakfastGlycemiaTv.setText("-");
            holder.beforeLunchGlycemiaTv.setText("-");
            holder.afterLunchGlycemiaTv.setText("-");
            holder.beforeDinnerGlycemiaTv.setText("-");
            holder.afterDinnerGlycemiaTv.setText("-");
            holder.bedtimeGlycemiaTv.setText("-");
            holder.workoutTv.setText("");


        } else {
            holder.dayItemCl.setBackgroundColor(Color.parseColor(context.getString(R.string.edited_day_bg)));
            holder.dateTv.setText(date);
            holder.beforeBreakfastGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBeforeBreakfast()));
            holder.afterBreakfastGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaAfterBreakfast()));
            holder.beforeLunchGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBeforeLunch()));
            holder.afterLunchGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaAfterLunch()));
            holder.beforeDinnerGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBeforeDinner()));
            holder.afterDinnerGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaAfterDinner()));
            holder.bedtimeGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBedtime()));
            holder.workoutTv.setText(diabeticDay.getWorkouts());
        }
    }

    @Override
    public int getItemCount() {
        if (diabeticDays == null) return 0;
        return diabeticDays.size();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.day_item_cl)
        ConstraintLayout dayItemCl;
        @BindView(R.id.rv_item_warning_iv)
        ImageView warningIv;
        @BindView(R.id.rv_item_warning_tv)
        TextView warningTv;
        @BindView(R.id.rv_item_date_tv)
        TextView dateTv;
        @BindView(R.id.rv_item_beforebreakfast_glycemia_tv)
        TextView beforeBreakfastGlycemiaTv;
        @BindView(R.id.rv_item_afterbreakfast_glycemia_tv)
        TextView afterBreakfastGlycemiaTv;
        @BindView(R.id.rv_item_beforelunch_glycemia_tv)
        TextView beforeLunchGlycemiaTv;
        @BindView(R.id.rv_item_afterlunch_glycemia_tv)
        TextView afterLunchGlycemiaTv;
        @BindView(R.id.rv_item_beforedinner_glycemia_tv)
        TextView beforeDinnerGlycemiaTv;
        @BindView(R.id.rv_item_afterdinner_glycemia_tv)
        TextView afterDinnerGlycemiaTv;
        @BindView(R.id.rv_item_bedtime_glycemia_tv)
        TextView bedtimeGlycemiaTv;
        @BindView(R.id.rv_item_workout)
        TextView workoutTv;

        DayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DiabeticDay diabeticDay = diabeticDays.get(getAdapterPosition());
            long dayId = diabeticDay.getDayId();
            long date = diabeticDay.getDate();

            dayClickHandler.onDayClicked(dayId, date);
        }
    }
}
