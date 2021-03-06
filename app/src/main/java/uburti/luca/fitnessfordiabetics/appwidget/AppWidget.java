package uburti.luca.fitnessfordiabetics.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import uburti.luca.fitnessfordiabetics.MainActivity;
import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.utils.Utils;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String widgetMsg,
                                        int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetMsg);
        views.setInt(R.id.appwidget_iv, "setAlpha", 150);//60% transparent

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        views.setOnClickPendingIntent(R.id.appwidget_rl, pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //launch the Widget Service with data from Shared Preferences

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String textToBeDisplayedInWidget = sharedPrefs.getString(Utils.WIDGET_TEXT, context.getString(R.string.empty_widget_text));
        AppWidgetService.startActionSetMsg(context, textToBeDisplayedInWidget);
        Log.d("AppWidget", "onUpdate: periodic widget data update with text from from shared prefs: " + textToBeDisplayedInWidget);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, String widgetMsg, int[] appWidgetIds) {
        Log.d("AppWidget", "updateAppWidgets, text: " + widgetMsg);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetMsg, appWidgetId);

        }
    }
}

