package paul.sohier.snake2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class HighActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.high);

		Beheer.setAct(this);
		Beheer.setAd();

		homeView mhomeView = (homeView) findViewById(R.id.snake);

		mhomeView.update();

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();

		TextView tv = (TextView) findViewById(R.id.highText);

		tv.setWidth(width - 75);
	}
}
