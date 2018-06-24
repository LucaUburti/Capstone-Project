package uburti.luca.fitnessfordiabetics.AppWidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import uburti.luca.fitnessfordiabetics.DayDetail;

public class AppWidgetService extends IntentService { //TODO remove some logging
    private String textToBeDisplayedInWidget;
    private static final String ACTION_SET_MSG = "ACTION_SET_MSG";

    public AppWidgetService() {
        super("AppWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            Log.d("AppWidgetService", "onHandleIntent: intent is null");
            return;

        }
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.d("AppWidgetService", "onHandleIntent: bundle is not null");
            textToBeDisplayedInWidget = bundle.getString(DayDetail.WIDGET_TEXT);
            Log.d("AppWidgetService", "onHandleIntent: bundle contains: " + textToBeDisplayedInWidget);
        }
        if (action != null && action.equals(ACTION_SET_MSG)) {
            Log.d("AppWidgetService", "action isn't null: "+ACTION_SET_MSG );
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, AppWidget.class));
            AppWidget.updateAppWidgets(this, appWidgetManager, textToBeDisplayedInWidget, appWidgetIds);
        }
    }


    public static void startActionSetMsg(Context context, String textToBeDisplayedInWidget) {
        Log.d("AppWidgetService", "startActionSetMsg: about to start service with text " + textToBeDisplayedInWidget);
        Intent intent = new Intent(context, AppWidgetService.class);
        intent.setAction(ACTION_SET_MSG);
        Bundle bundle = new Bundle();
        bundle.putString(DayDetail.WIDGET_TEXT, textToBeDisplayedInWidget);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
