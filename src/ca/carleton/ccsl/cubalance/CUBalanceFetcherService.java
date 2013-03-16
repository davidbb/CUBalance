package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CUBalanceFetcherService extends Service {// Service to update balance from widget
	private final String TAG = getClass().getSimpleName();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final SharedPreferences settings = getSharedPreferences(CUBalanceSettings.PREFS_NAME, Activity.MODE_PRIVATE);

		String prefsUser = settings.getString(CUBalanceSettings.USER_KEY, "");
		String prefsPin = settings.getString(CUBalanceSettings.PIN_KEY, "");

		Log.i(TAG, "Spawning a CUBalanceFetcher task from widget.");
		try {
			new CUBalanceFetcher(getApplicationContext(), prefsUser, prefsPin).execute();
			Toast.makeText(getApplicationContext(), "Updating Balance from widget...", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(TAG, "Unable to instantiate CUBalanceFetcher from widget");
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());

			Toast.makeText(getApplicationContext(), "Error updating balance from widget.", Toast.LENGTH_SHORT).show();
		}

		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
