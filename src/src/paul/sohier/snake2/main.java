package paul.sohier.snake2;

import com.admob.android.ads.AdView;

import android.app.Activity;
import android.os.Bundle;

public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		AdView ad = (AdView) findViewById(R.id.adver);
		ad.bringToFront();
		
//		  AdManager.setTestDevices( new String[] {                 
//				     AdManager.TEST_EMULATOR,             // Android emulator
//				     "0BD8378A2247D33B57762EB03AF750D7",  // My T-Mobile G1 Test Phone
//				     } );          
    }
}