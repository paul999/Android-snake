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

@SuppressWarnings("deprecation")
public class main extends Activity{
	private homeView mhomeView;
	private boolean DEBUG = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		AdView ad = (AdView) findViewById(R.id.adver);
		ad.bringToFront();
		if (DEBUG) {
			AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR, // Android
																				// emulator
					"0BD8378A2247D33B57762EB03AF750D7", // My T-Mobile G1 Test
														// Phone
			});
		}

		Button StartGameButton = (Button) findViewById(R.id.StartGame);
		StartGameButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent StartGameIntent = new
				// Intent(main.this,StartGame.class);
				// startActivity(StartGameIntent);
			}
		});

		Button HelpButton = (Button) findViewById(R.id.Help);
		HelpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent HelpIntent = new Intent(main.this,Help.class);
				// startActivity(HelpIntent);
			}
		});

		Button OptionsButton = (Button) findViewById(R.id.Options);
		OptionsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent OptionsIntent = new Intent(main.this,
						SettingsActivity.class);
				startActivity(OptionsIntent);
			}
		});

		Button AboutButton = (Button) findViewById(R.id.Credits);
		AboutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent AboutIntent = new Intent(main.this, AboutActivity.class);
				startActivity(AboutIntent);
			}
		});

/*		Button HighscoreButton = (Button) findViewById(R.id.Highscore);
		HighscoreButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent CreditsIntent= new
				// Intent(main.this,AboutActivity.class);
				// startActivity(CreditsIntent);
			}
		});*/

		Button ExitButton = (Button) findViewById(R.id.Exit);
		ExitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
				
		mhomeView = (homeView) findViewById(R.id.snake);

		mhomeView.update();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}