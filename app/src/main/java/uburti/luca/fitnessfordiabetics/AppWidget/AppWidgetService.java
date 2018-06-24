package uburti.luca.fitnessfordiabetics.AppWidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class AppWidgetService extends IntentService {

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
        if (action != null && action.equals(ACTION_SET_MSG)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, AppWidget.class));

            String widgetMsg = "ciaone";
            AppWidget.updateAppWidgets(this, appWidgetManager, widgetMsg, appWidgetIds);
        }
    }


    public static void startActionSetMsg(Context context) {
        Intent intent = new Intent(context, AppWidgetService.class);
        intent.setAction(ACTION_SET_MSG);
        context.startService(intent);
    }
}
