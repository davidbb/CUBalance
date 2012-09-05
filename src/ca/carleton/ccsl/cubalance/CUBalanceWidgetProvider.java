package ca.carleton.ccsl.cubalance;

/*This is a placeholder for a widget that displays the current balance on the
 * home screen. 
 * TODO: make it refresh on tap, and refresh every X seconds.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class CUBalanceWidgetProvider extends AppWidgetProvider 
{

  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
  {
    super.onUpdate(context, appWidgetManager, appWidgetIds);

    final int N = appWidgetIds.length;

    // Perform this loop procedure for each App Widget that belongs to this provider
    for (int i = 0; i < N; i ++) 
    {
      int appWidgetId = appWidgetIds[i];

      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cubalance_widget);
            
      final SharedPreferences settings 
        = context.getSharedPreferences(CUBalanceSettings.PREFS_NAME, Activity.MODE_PRIVATE);

      //String lastUp    = settings.getString(CUBalanceSettings.DATE_KEY, "");      
      String lastBal  = settings.getString(CUBalanceSettings.BAL_KEY,  "");
      views.setTextViewText(R.id.widgetBalanceTxt, lastBal);
      
      Intent intent = new Intent(context, CUCampusCardBalanceActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
      
      //views.setOnClickPendingIntent(R.id.widgetUpdateButton, pendingIntent);
      
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }    
  }
}