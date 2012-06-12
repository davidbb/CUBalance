package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CUCampusCardBalanceActivity extends Activity
{
  public static final String PREFS_NAME = "MyPrefsFile";
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    String user = "YOUR_STUDENT_ID";
    String pin  = "YOUR_PIN_NUMBER";
    
    final Button    button  = (Button)   findViewById(R.id.updateBalanceBtn);
    final TextView  balance = (TextView) findViewById(R.id.balanceTxt);
    
    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("user", user);
    editor.putString("pin", pin);
    editor.commit();
    
    //final CUBalanceFetcher fetchTask = new CUBalanceFetcher(user, pin, balance);
    
    button.setOnClickListener(new View.OnClickListener() 
    {
      @Override
      public void onClick(View v)
      {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	String prefsUser=settings.getString("user", "000000000");
    	String prefsPin=settings.getString("pin", "000000");
    	
    	final CUBalanceFetcher fetchTask = new CUBalanceFetcher(prefsUser,prefsPin,balance);
    	fetchTask.execute();
        Toast.makeText(v.getContext(), "Updating Balance...", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
