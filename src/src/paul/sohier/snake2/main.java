package paul.sohier.snake2;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
		AdView ad = (AdView) findViewById(R.id.adver);
		ad.bringToFront();
		
		  AdManager.setTestDevices( new String[] {                 
				     AdManager.TEST_EMULATOR,             // Android emulator
				     "0BD8378A2247D33B57762EB03AF750D7",  // My T-Mobile G1 Test Phone
				     } );
		  
	        Button StartGameButton = (Button)findViewById(R.id.StartGame);
	        StartGameButton.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		//Intent StartGameIntent = new Intent(main.this,StartGame.class);
	        		//startActivity(StartGameIntent);
	        	}
	        });
	        
	        Button HelpButton = (Button)findViewById(R.id.Help);
	        HelpButton.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		//Intent HelpIntent = new Intent(main.this,Help.class);
	        		//startActivity(HelpIntent);
	        	}
	        });
	        
	        Button OptionsButton = (Button)findViewById(R.id.Options);
	        OptionsButton.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		Intent OptionsIntent = new Intent(main.this,SettingsActivity.class);
	        		startActivity(OptionsIntent);
	        	}
	        });
	        
	        Button HighscoreButton = (Button)findViewById(R.id.Highscore);
	        HighscoreButton.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		//Intent CreditsIntent= new Intent(main.this,AboutActivity.class);
	        		//startActivity(CreditsIntent);
	        	}
	        });
	        
	        Button ExitButton = (Button)findViewById(R.id.Exit);
	        ExitButton.setOnClickListener(new OnClickListener() {
	        	
	        	public void onClick(View v) {
	        		finish();
	        	}
	        });	        
		  
    }
}