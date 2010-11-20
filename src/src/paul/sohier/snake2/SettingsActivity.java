package paul.sohier.snake2;
import paul.sohier.snake2.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Window;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity{
    
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	super.onCreate(savedInstanceState);
        
        
        
        addPreferencesFromResource(R.xml.preferences);
    }
}
