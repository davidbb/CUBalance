package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CUCampusCardBalanceActivity extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    String user = "YOUR_USER_ID_NUMBER";
    String pin  = "YOUR_PIN_NUMBER";

    final Button    button  = (Button)   findViewById(R.id.updateBalanceBtn);
    final TextView  balance = (TextView) findViewById(R.id.balanceTxt);
    
    final CUBalanceFetcher fetchTask = new CUBalanceFetcher(user, pin, balance);
    
    button.setOnClickListener(new View.OnClickListener() 
    {
      @Override
      public void onClick(View v)
      {
        fetchTask.execute();
        Toast.makeText(v.getContext(), "Updating Balance...", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
