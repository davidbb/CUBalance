package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CUCampusCardBalanceActivity extends Activity
{
  private final String TAG = getClass().getSimpleName();

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    final Button    button  = (Button)   findViewById(R.id.updateBalanceBtn);
    final TextView  balance = (TextView) findViewById(R.id.balanceTxt);

    final SharedPreferences settings 
      = getSharedPreferences(CUBalanceSettings.PREFS_NAME, MODE_PRIVATE);
 
    button.setOnClickListener(new View.OnClickListener() 
    {
      public void onClick(View v)
      {
        String prefsUser = settings.getString(CUBalanceSettings.USER_KEY, "");
        String prefsPin  = settings.getString(CUBalanceSettings.PIN_KEY,  "");

        Log.i(TAG, "Spawning a CUBalanceFetcher task.");
        final CUBalanceFetcher fetchTask = new CUBalanceFetcher(prefsUser, prefsPin, balance);
        fetchTask.execute();

        Toast.makeText(v.getContext(), "Updating Balance...", Toast.LENGTH_SHORT).show();
      }
    });
  }
  
  @Override
  public void onStart()
  {
    super.onStart();
    
    final SharedPreferences settings 
      = getSharedPreferences(CUBalanceSettings.PREFS_NAME, MODE_PRIVATE);
       
    String prefsUser = settings.getString("user", "");
    String prefsPin  = settings.getString("pin",  "");
    
    //If the user or pin haven't been changed from the default, we need to show
    //the settings screen before anything meaningful can happen in this Activity
    if(prefsUser.equals("") || prefsPin.equals(""))
    {
      Log.i(TAG, "No first setup done. Launching CUBalanceSettings activity.");
      Intent myIntent = new Intent(getBaseContext(), CUBalanceSettings.class);
      startActivityForResult(myIntent, 0);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.item1:
        setContentView(R.layout.settings);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
