package com.bendaf.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by bendaf on 2018. 08. 20. BakingApp.
 */

public class IngredientsWidgetProvider extends AppWidgetProvider {

    @Override public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            RemoteViews rw = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

            Intent startMainActivity = new Intent(context, MainActivity.class);
            rw.setOnClickPendingIntent(R.id.fl_container,
                    PendingIntent.getActivity(context, 0, startMainActivity, 0));

            rw.setRemoteAdapter(R.id.lv_ingredients, new Intent(context, IngredientsWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_ingredients);
            appWidgetManager.updateAppWidget(appWidgetId, rw);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
