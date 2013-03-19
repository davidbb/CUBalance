package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
  private final String       TAG      = getClass().getSimpleName();
  private BroadcastReceiver  myReceiver = new BroadcastReceiver() {        
      @Override
      public void onReceive(Context context, Intent intent) {
          if (intent.getAction().equals("ca.carleton.ccsl.cubalance.FETCH_FINISHED")){
        	  updateBalance();
          }
      }
  };  

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    final Button button = (Button) findViewById(R.id.updateBalanceBtn);
    button.setOnClickListener(new View.OnClickListener() 
    {
      @Override
	public void onClick(View v)
      {
        fetchBalance();
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

    Boolean autoUp   = settings.getBoolean(CUBalanceSettings.UPDATE_KEY, false);
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
    if(!lastBal.equals("")) {
		balance.setText(lastBal);
	}
    
    if(!lastUp.equals("")) {
		updatedAt.setText("Last Updated: "+ lastUp);
	}
    
    Log.i(TAG,"Registering BroadcastReceiver");
    registerReceiver(myReceiver, new IntentFilter("ca.carleton.ccsl.cubalance.FETCH_FINISHED"));
    
    if(autoUp) {
		fetchBalance();
	}
  }
  
  @Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,"Unregistering BroadcastReceiver");
		unregisterReceiver(myReceiver);
	}
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
    // Handle item selection
    switch (item.getItemId()) 
    {
      case R.id.menu_settings_item:
        Intent myIntent = new Intent(getBaseContext(), CUBalanceSettings.class);
        startActivityForResult(myIntent, 0);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  
  private void fetchBalance()
  {
    Log.i(TAG, "Spawning a CUBalanceFetcher task.");
    
    try {
      Intent fetchBalanceIntent = new Intent(this, CUBalanceFetcher.class);
      startService(fetchBalanceIntent);
      Toast.makeText(this, "Updating Balance...", Toast.LENGTH_SHORT).show();
    } catch(Exception e) {
      Log.e(TAG, "Unable to instantiate CUBalanceFetcher");
      Log.e(TAG, e.getMessage());
      Log.e(TAG, e.toString());
      
      Toast.makeText(this, "Error updating balance.", Toast.LENGTH_SHORT).show();
    }
  }

  public void updateBalance()
  {
    final TextView  balance   = (TextView) findViewById(R.id.balanceTxt);
    final TextView  updatedAt = (TextView) findViewById(R.id.updatedAtTxt);

    final SharedPreferences settings = getSharedPreferences(CUBalanceSettings.PREFS_NAME, MODE_PRIVATE);
    
    String balanceStr = settings.getString(CUBalanceSettings.BAL_KEY, getResources().getString(R.string.unknown_bal_text));
    String dateStr    = settings.getString(CUBalanceSettings.DATE_KEY, getResources().getString(R.string.unknown_date_text));
    
    balance.setText(balanceStr);
    updatedAt.setText("Last Updated: "+ dateStr);
    
    Toast.makeText(this, "Balance updated.", Toast.LENGTH_SHORT).show();
  }
}
