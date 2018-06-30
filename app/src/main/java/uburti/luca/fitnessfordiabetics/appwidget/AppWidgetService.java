package uburti.luca.fitnessfordiabetics.appwidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import uburti.luca.fitnessfordiabetics.utils.Utils;

public class AppWidgetService extends IntentService {
    private String textToBeDisplayedInWidget;
    private static final String ACTION_SET_MSG = "ACTION_SET_MSG";

    public AppWidgetService() {
        super("AppWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            textToBeDisplayedInWidget = bundle.getString(Utils.WIDGET_TEXT);
        }
        if (action != null && action.equals(ACTION_SET_MSG)) {
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
        bundle.putString(Utils.WIDGET_TEXT, textToBeDisplayedInWidget);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
