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

public class HelpActivity extends Activity {

	private static final boolean DEBUG = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);			
		
		setContentView(R.layout.help);
		
		Beheer.setAct(this);
		Beheer.setAd();
		
		homeView mhomeView = (homeView) findViewById(R.id.snake);

		mhomeView.update();
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		
		TextView tv = (TextView) findViewById(R.id.helpText);
		
		tv.setWidth(width - 75);
	}
}
