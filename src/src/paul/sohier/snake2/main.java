package paul.sohier.snake2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class main extends Activity {
	private homeView mhomeView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		Beheer.setAct(this);
		Beheer.setAd();

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth() - 75;

		Button StartGameButton = (Button) findViewById(R.id.StartGame);
		StartGameButton.setWidth(width);
		StartGameButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent StartGameIntent = new
				// Intent(main.this,StartGame.class);
				// startActivity(StartGameIntent);
			}
		});

		Button HelpButton = (Button) findViewById(R.id.Help);
		HelpButton.setWidth(width);
		HelpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent HelpIntent = new Intent(main.this, HelpActivity.class);
				startActivity(HelpIntent);
			}
		});

		Button OptionsButton = (Button) findViewById(R.id.Options);
		OptionsButton.setWidth(width);
		OptionsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent OptionsIntent = new Intent(main.this,
						SettingsActivity.class);
				startActivity(OptionsIntent);
			}
		});

		Button AboutButton = (Button) findViewById(R.id.Credits);
		AboutButton.setWidth(width);
		AboutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent AboutIntent = new Intent(main.this, AboutActivity.class);
				startActivity(AboutIntent);
			}
		});

		Button HighscoreButton = (Button) findViewById(R.id.Highscore);
		HighscoreButton.setWidth(width);
		HighscoreButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent CreditsIntent= new
				// Intent(main.this,AboutActivity.class);
				// startActivity(CreditsIntent);
			}
		});

		Button ExitButton = (Button) findViewById(R.id.Exit);
		ExitButton.setWidth(width);
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