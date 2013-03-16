package ca.carleton.ccsl.cubalance;

/*
 * This is a placeholder for a widget that displays the current balance on the
 * home screen. Implementation is not complete.
 *  
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

public class CUBalanceWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cubalance_widget);

			final SharedPreferences settings = context.getSharedPreferences(CUBalanceSettings.PREFS_NAME, Activity.MODE_PRIVATE);

			String lastBal = settings.getString(CUBalanceSettings.BAL_KEY, context.getResources().getString(R.string.unknown_bal_text));
			String lastUp = settings.getString(CUBalanceSettings.DATE_KEY, context.getResources().getString(R.string.unknown_last_up));

			views.setTextViewText(R.id.tvWidgetBalance, lastBal);
			views.setTextViewText(R.id.tvWidgetLastUp, lastUp);

			Intent intent = new Intent(context, CUCampusCardBalanceActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.iWidgetLogo, pendingIntent);

			Intent refreshIntent = new Intent(context, CUBalanceFetcherService.class);
			PendingIntent pendingRefreshIntent = PendingIntent.getService(context, 0, refreshIntent, 0);
			views.setOnClickPendingIntent(R.id.tvWidgetBalance, pendingRefreshIntent);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}