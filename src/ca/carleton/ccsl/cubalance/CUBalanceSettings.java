package ca.carleton.ccsl.cubalance;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class CUBalanceSettings extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	/** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings);
	    
	    final EditText studentnumberfield  = (EditText) findViewById(R.id.userfield);
	    final EditText pinfield = (EditText) findViewById(R.id.pinfield);
	    final CheckBox refreshbox = (CheckBox) findViewById(R.id.refreshopt);
	    
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	
	    String prefsUser=settings.getString("user", "nouser");
    	String prefsPin=settings.getString("pin", "nopin");
    	
    	if((prefsUser!="nouser") && (prefsPin!="nopin")){
    		//there are saved values, fill them in
    		studentnumberfield.setText(prefsUser);
    		pinfield.setText(prefsPin);
    	}
	  }
}
