package paul.sohier.snake2;

import java.util.Formatter;
import java.util.Locale;

import paul.sohier.snake2.general.Beheer;
import paul.sohier.snake2.view.homeView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends Activity {
	private static ProgressDialog dialog;
	private SharedPreferences settings;
	
	StringBuilder sb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.score);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		int mScore = (int) settings.getLong("tmpscore", 0);

		sb = new StringBuilder();
		// Send all output to the Appendable object sb
		Formatter formatter = new Formatter(sb, Locale.getDefault());

		formatter.format(getString(R.string.score_done), mScore);
		
		if (mScore < 5)
		{
			sb.append(getString(R.string.low_score));
		}

		Beheer.setAct(this);
		Beheer.setAd();

		homeView mhomeView = (homeView) findViewById(R.id.snake);

		mhomeView.update();

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();

		TextView tv = (TextView) findViewById(R.id.scoreText);

		tv.setWidth(width - 75);
		
		if (settings.getLong("tmpscore", 0) > settings.getInt("highscore", 0))
		{
			formatter.format(getString(R.string.localHigh));
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("highscore", (int)settings.getLong("tmpscore", 0));
			editor.commit();
			
		}		

		if (settings.getBoolean("savescore", false)) {
			Log.d("DEBUG", "SCORE SAVE!");

			String tmp = getResources().getString(R.string.sendingHigh);
			dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.busy);
			dialog.setMessage(tmp);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);

			dialog.show();

			if (!Beheer.isOnline()) {
				sb.append("\n\n"
						+ getResources().getString(R.string.no_internet));
				dialog.dismiss();
				if (Beheer.getDebug())
					Log.d("Snake", "no_internet");
			} else {
				if (Beheer.getDebug())
					Log.d("Snake", "internet");
			}

			new Thread(new Runnable() {
				public void run() {

					int mScore = (int) settings.getLong("tmpscore", 0);

					SharedPreferences.Editor editor = settings.edit();
					editor.putLong("tmpscore", 0);
					editor.commit();

					Beheer.makeHttpRequest(mScore);
					dialog.dismiss();
					
					sb.append(getString(R.string.score_saved));
				}
			}).start();

			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("savescore", false);

			editor.commit();
			editor = null;
		}
		
		TextView t = (TextView) findViewById(R.id.scoreText);
		t.setText(sb);
		
		Button RetryButton = (Button) findViewById(R.id.retry);

		RetryButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
				Intent StartGameIntent = new Intent(ScoreActivity.this,PlayActivity.class);
				startActivity(StartGameIntent);
			}
		});

		Button homeButton = (Button) findViewById(R.id.home);

		homeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});		
	}
}
