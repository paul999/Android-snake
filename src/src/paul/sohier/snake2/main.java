package paul.sohier.snake2;

import paul.sohier.snake2.general.Beheer;
import paul.sohier.snake2.view.homeView;
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
		Beheer.setContext(this);

		Button StartGameButton = (Button) findViewById(R.id.StartGame);
		StartGameButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 Intent StartGameIntent = new
				 Intent(main.this,PlayActivity.class);
				 startActivity(StartGameIntent);
			}
		});

		Button HelpButton = (Button) findViewById(R.id.Help);
		HelpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent HelpIntent = new Intent(main.this, HelpActivity.class);
				startActivity(HelpIntent);
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

		Button HighscoreButton = (Button) findViewById(R.id.Highscore);
		HighscoreButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				 Intent HighIntent= new
				 Intent(main.this,HighActivity.class);
				 startActivity(HighIntent);
			}
		});

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