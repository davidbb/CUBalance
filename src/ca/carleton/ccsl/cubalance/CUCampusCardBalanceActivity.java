package ca.carleton.ccsl.cubalance;

import java.text.DateFormat;

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
  private final String     TAG      = getClass().getSimpleName();
  private final DateFormat DATE_FMT = DateFormat.getTimeInstance(DateFormat.DEFAULT);
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    final CUCampusCardBalanceActivity mainUI = this;
  
    final SharedPreferences settings 
      = getSharedPreferences(CUBalanceSettings.PREFS_NAME, MODE_PRIVATE);
    
    final Button button = (Button) findViewById(R.id.updateBalanceBtn);
    button.setOnClickListener(new View.OnClickListener() 
    {
      public void onClick(View v)
      {
        String prefsUser = settings.getString(CUBalanceSettings.USER_KEY, "");
        String prefsPin  = settings.getString(CUBalanceSettings.PIN_KEY,  "");

        Log.i(TAG, "Spawning a CUBalanceFetcher task.");
        final CUBalanceFetcher fetchTask = new CUBalanceFetcher(prefsUser, prefsPin, mainUI);
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
    
    final TextView balance   = (TextView) findViewById(R.id.balanceTxt);
    final TextView updatedAt = (TextView) findViewById(R.id.updatedAtTxt);
     
    String prefsUser = settings.getString(CUBalanceSettings.USER_KEY, "");
    String prefsPin  = settings.getString(CUBalanceSettings.PIN_KEY,  "");
    String lastBal   = settings.getString(CUBalanceSettings.BAL_KEY,  "");
    String lastUp    = settings.getString(CUBalanceSettings.DATE_KEY, "");
    
    //If the user or pin haven't been changed from the default, we need to show
    //the settings screen before anything meaningful can happen in this Activity
    if(prefsUser.equals("") || prefsPin.equals(""))
    {
      Log.i(TAG, "No first setup done. Launching CUBalanceSettings activity.");
      Intent myIntent = new Intent(getBaseContext(), CUBalanceSettings.class);
      startActivityForResult(myIntent, 0);
    }
    
    //There is a 'cached' balance, set the text box to that value.
    if(!lastBal.equals(""))
      balance.setText("Balance: "+ lastBal);
    
    if(!lastUp.equals(""))
      updatedAt.setText("Last Updated: "+ lastUp);
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

  public void updateBalance(String result)
  {
    final TextView  balance   = (TextView) findViewById(R.id.balanceTxt);
    final TextView  updatedAt = (TextView) findViewById(R.id.updatedAtTxt);

    final SharedPreferences settings 
      = getSharedPreferences(CUBalanceSettings.PREFS_NAME, MODE_PRIVATE);
    final SharedPreferences.Editor editor = settings.edit();
    
    String dateStr = DATE_FMT.format(new java.util.Date());

    Log.i(TAG, "Updating cached balance to "+ result);
    editor.putString(CUBalanceSettings.BAL_KEY, result);
    editor.putString(CUBalanceSettings.DATE_KEY, dateStr);
    editor.commit();
    
    balance.setText("Balance: "+ result);
    updatedAt.setText("Last Updated: "+ dateStr);
  }
}
