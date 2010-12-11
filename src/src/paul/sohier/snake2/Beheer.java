package paul.sohier.snake2;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;

public class Beheer {
	static private Activity act;
	
	static protected final boolean DEBUG = true;
	
	static public void setAct(Activity a)
	{
		act = a;
	}
	
	static public void setAd()
	{
		AdView ad = (AdView) act.findViewById(R.id.adver);
		ad.bringToFront();
		if (DEBUG) {
			AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR, // Android
											 									// emulator
					"0BD8378A2247D33B57762EB03AF750D7", // My T-Mobile G1 Test
														// Phone
			});
		}	
	}
}
