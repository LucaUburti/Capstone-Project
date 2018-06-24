package uburti.luca.fitnessfordiabetics.AppWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import uburti.luca.fitnessfordiabetics.MainActivity;
import uburti.luca.fitnessfordiabetics.R;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String widgetMsg,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetMsg);

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        views.setOnClickPendingIntent(R.id.appwidget_rl, pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }

        AppWidgetService.startActionSetMsg(context);
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
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, widgetMsg, appWidgetId);

        }
    }
}

