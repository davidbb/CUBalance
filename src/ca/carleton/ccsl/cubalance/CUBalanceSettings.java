package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CUBalanceSettings extends Activity 
{
  private final String TAG = getClass().getSimpleName();

  public static final String PREFS_NAME = "MyPrefsFile";
  public static final String USER_KEY   = "user";
  public static final String PIN_KEY    = "pin";
  public static final String BAL_KEY    = "lastBalance";
  public static final String DATE_KEY   = "lastUpdated";
  public static final String UPDATE_KEY = "autoUpdate";

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
    final CheckBox refreshbox    = (CheckBox) findViewById(R.id.refreshopt);
    
    btnSave.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v)
      {
        final SharedPreferences.Editor editor = settings.edit();

        Log.i(TAG, "Updating saved preferences.");
        editor.putString (USER_KEY,   txtStudentNum.getText().toString());
        editor.putString (PIN_KEY,    txtPinNum.getText().toString());
        editor.putBoolean(UPDATE_KEY, refreshbox.isChecked());
        editor.commit();
        
        Toast.makeText(v.getContext(), "Settings Updated", Toast.LENGTH_SHORT).show();
        
        Log.i(TAG, "Finishing CUBalanceSettings activity");
        finish(); //Close the activity and return
      }
    });
  }
  
  @Override
  public void onStart()
  {
    super.onStart();

    final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    final EditText txtStudentNum = (EditText) findViewById(R.id.userfield);
    final EditText txtPinNum     = (EditText) findViewById(R.id.pinfield);
    final CheckBox refreshbox    = (CheckBox) findViewById(R.id.refreshopt);
       
    String  prefsUser = settings.getString (USER_KEY,   "");
    String  prefsPin  = settings.getString (PIN_KEY,    "");
    Boolean autoUp    = settings.getBoolean(UPDATE_KEY, false);
  
    txtStudentNum.setText(prefsUser);
    txtPinNum.setText(prefsPin);
    refreshbox.setChecked(autoUp);
  }
  @Override
  public void onBackPressed()
  {
	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	String  prefsUser = settings.getString (USER_KEY,   "");
	String  prefsPin  = settings.getString (PIN_KEY,    "");
	// if the user presses back and hasn't saved credentials, dump back to home screen
	if(prefsUser.equals("") || prefsPin.equals("")){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}
	else{
		// go back to the main app without saving
		finish();
	}
  }
}
