package paul.sohier.snake2;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity {

	private static final boolean DEBUG = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);			
		
		setContentView(R.layout.about);

		AdView ad = (AdView) findViewById(R.id.adver);
		ad.bringToFront();
		if (DEBUG) {
			AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR, // Android
											 									// emulator
					"0BD8378A2247D33B57762EB03AF750D7", // My T-Mobile G1 Test
														// Phone
			});
		}
		homeView mhomeView = (homeView) findViewById(R.id.snake);

		mhomeView.update();
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		
		TextView tv = (TextView) findViewById(R.id.aboutText);
		
		tv.setWidth(width - 75);
	}
}
