package uburti.luca.fitnessfordiabetics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<DiabeticDay> diabeticDays;
    private Context context;
    private boolean hypoglycemiaWarning = false;
    private boolean hyperglycemiaWarning = false;

    private final DayClickHandler dayClickHandler;

    public interface DayClickHandler {
        void onDayClicked(long dayId, long date);
    }

    DayAdapter(List<DiabeticDay> diabeticDays, Context context, DayClickHandler dayClickHandler) {
        this.diabeticDays = diabeticDays;
        this.context = context;
        this.dayClickHandler = dayClickHandler;
        notifyDataSetChanged();
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
            displayBlankDay(holder, date);
        } else {
            displayFilledOutDay(holder, diabeticDay, date);
        }
    }

    private void displayFilledOutDay(@NonNull DayViewHolder holder, DiabeticDay diabeticDay, String date) {
        hypoglycemiaWarning = false;
        hyperglycemiaWarning = false;
        holder.dayItemCl.setBackgroundColor(Color.parseColor(context.getString(R.string.edited_day_bg)));
        holder.dateTv.setText(date);
        holder.beforeBreakfastGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBeforeBreakfast()));
        checkForGlycemicWarnings(holder.beforeBreakfastGlycemiaTv, diabeticDay.getGlycemiaBeforeBreakfast());
        holder.afterBreakfastGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaAfterBreakfast()));
        checkForGlycemicWarnings(holder.afterBreakfastGlycemiaTv, diabeticDay.getGlycemiaAfterBreakfast());
        holder.beforeLunchGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBeforeLunch()));
        checkForGlycemicWarnings(holder.beforeLunchGlycemiaTv, diabeticDay.getGlycemiaBeforeLunch());
        holder.afterLunchGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaAfterLunch()));
        checkForGlycemicWarnings(holder.afterLunchGlycemiaTv, diabeticDay.getGlycemiaAfterLunch());
        holder.beforeDinnerGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBeforeDinner()));
        checkForGlycemicWarnings(holder.beforeDinnerGlycemiaTv, diabeticDay.getGlycemiaBeforeDinner());
        holder.afterDinnerGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaAfterDinner()));
        checkForGlycemicWarnings(holder.afterDinnerGlycemiaTv, diabeticDay.getGlycemiaAfterDinner());
        holder.bedtimeGlycemiaTv.setText(Utils.valueOfIntWithoutZeroSetDash(diabeticDay.getGlycemiaBedtime()));
        checkForGlycemicWarnings(holder.bedtimeGlycemiaTv, diabeticDay.getGlycemiaBedtime());
        holder.workoutTv.setText(diabeticDay.getWorkouts());
        if (hypoglycemiaWarning && !hyperglycemiaWarning) {
            holder.warningTv.setText(context.getResources().getString(R.string.hypoglycemia));
            holder.warningTv.setVisibility(View.VISIBLE);
            holder.warningIv.setVisibility(View.VISIBLE);
        } else if (!hypoglycemiaWarning && hyperglycemiaWarning) {
            holder.warningTv.setText(context.getResources().getString(R.string.hyperglycemia));
            holder.warningTv.setVisibility(View.VISIBLE);
            holder.warningIv.setVisibility(View.VISIBLE);
        } else if (hypoglycemiaWarning && hyperglycemiaWarning) {
            holder.warningTv.setText(context.getResources().getString(R.string.hypohyperglycemia));
            holder.warningTv.setVisibility(View.VISIBLE);
            holder.warningIv.setVisibility(View.VISIBLE);
        } else {
            holder.warningTv.setText("");
            holder.warningTv.setVisibility(View.GONE);
            holder.warningIv.setVisibility(View.GONE);
        }
    }

    private void displayBlankDay(@NonNull DayViewHolder holder, String date) {
        holder.dateTv.setText(date);    //no data in DB, just populate the mock day with the date
        holder.dayItemCl.setBackgroundColor(Color.parseColor(context.getString(R.string.blank_day_bg)));
        holder.beforeBreakfastGlycemiaTv.setText("-");
        holder.beforeBreakfastGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.beforeBreakfastGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.afterBreakfastGlycemiaTv.setText("-");
        holder.afterBreakfastGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.afterBreakfastGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.beforeLunchGlycemiaTv.setText("-");
        holder.beforeLunchGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.beforeLunchGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.afterLunchGlycemiaTv.setText("-");
        holder.afterLunchGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.afterLunchGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.beforeDinnerGlycemiaTv.setText("-");
        holder.beforeDinnerGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.beforeDinnerGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.afterDinnerGlycemiaTv.setText("-");
        holder.afterDinnerGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.afterDinnerGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.bedtimeGlycemiaTv.setText("-");
        holder.bedtimeGlycemiaTv.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        holder.bedtimeGlycemiaTv.setTypeface(null, Typeface.NORMAL);
        holder.workoutTv.setText("");
        holder.warningTv.setText("");
        holder.warningTv.setVisibility(View.GONE);
        holder.warningIv.setVisibility(View.GONE);
    }


    private void checkForGlycemicWarnings(TextView textView, int glycemia) {
        if ((glycemia > 0) && (glycemia < context.getResources().getInteger(R.integer.low_glycemia_threshold))) {
            textView.setTextColor(context.getResources().getColor(R.color.hypoglycemia));
            textView.setTypeface(null, Typeface.BOLD);
            hypoglycemiaWarning = true;

        } else if (glycemia > context.getResources().getInteger(R.integer.high_glycemia_threshold)) {
            textView.setTextColor(context.getResources().getColor(R.color.hyperglycemia));
            textView.setTypeface(null, Typeface.BOLD);
            hyperglycemiaWarning = true;

        } else {
            textView.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
            textView.setTypeface(null, Typeface.NORMAL);
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
