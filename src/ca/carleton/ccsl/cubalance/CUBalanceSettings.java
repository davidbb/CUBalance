package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CUBalanceSettings extends Activity 
{
  private final String TAG = getClass().getSimpleName();

  public static final String PREFS_NAME = "MyPrefsFile";
  public static final String USER_KEY   = "user";
  public static final String PIN_KEY    = "pin";

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings);
  
    final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

    final EditText txtStudentNum = (EditText) findViewById(R.id.userfield);
    final EditText txtPinNum     = (EditText) findViewById(R.id.pinfield);
    final Button   btnSave       = (Button)   findViewById(R.id.saveprefs);
    
    //TODO: Code refresh functionality...
    //final CheckBox refreshbox = (CheckBox) findViewById(R.id.refreshopt);

    String prefsUser = settings.getString(USER_KEY, "");
    String prefsPin  = settings.getString(PIN_KEY,  "");
  
    txtStudentNum.setText(prefsUser);
    txtPinNum.setText(prefsPin);
    
    btnSave.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v)
      {
        final SharedPreferences.Editor editor = settings.edit();

        Log.i(TAG, "Updating saved preferences.");
        editor.putString("user", txtStudentNum.getText().toString());
        editor.putString("pin",  txtPinNum.getText().toString());
        editor.commit();
        
        Toast.makeText(v.getContext(), "Settings Updated", Toast.LENGTH_SHORT).show();
        
        Log.i(TAG, "Finishing CUBalanceSettings activity");
        finish(); //Close the activity and return
      }
    });
  }
}