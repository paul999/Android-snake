package paul.sohier.snake2.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import paul.sohier.snake2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class Beheer {
	private String url = "http://bit.ly/aDLfDS";

	static private Activity act;

	private static final boolean DEBUG = true;
	
	private static SharedPreferences settings;
	
	private static Context context;

	static public void setAct(Activity a) {
		act = a;
	}

	static public void setAd() {
		AdView ad = (AdView) act.findViewById(R.id.adver);
		ad.bringToFront();
		if (getDebug()) {
			AdManager.setTestDevices(new String[] { AdManager.TEST_EMULATOR, // Android
																				// emulator
					"0BD8378A2247D33B57762EB03AF750D7", // My T-Mobile G1 Test
														// Phone
			});
		}
		AdManager.setAllowUseOfLocation(true);
	}

	public static void showAlertDialog(int messageId,
			String string) {
		if (getContext()  == null)
		{
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage(messageId);
		builder.setTitle(string);
		builder.setNeutralButton(android.R.string.ok, null);
		builder.create().show();
	}

	public static boolean isOnline() {
		if (getContext()  == null)
		{
			return false;
		}
		try {
			ConnectivityManager cm = (ConnectivityManager) getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (Exception e) {
			Log.d("error!", "error!", e);
			return false;
		}
	}

	/**
	 * @return the debug
	 */
	public static boolean getDebug() {
		return DEBUG;
	}

	@SuppressWarnings("unused")
	public static boolean makeHttpRequest(long mScore2) {
		try {
			if (getContext()  == null)
			{
				return false;
			}			
			if (!isOnline())
			{
				return false;
			}
			// Create a URL for the desired page URL

			String Android_ID = android.provider.Settings.System.getString(getContext().getContentResolver(),
					android.provider.Settings.System.ANDROID_ID);

			int usesensor = Integer
					.parseInt(getSettings().getString("sensor", "1"));

			URL url = new URL(
					"http://android.paulsohier.nl/savehigh.php?score="
							+ mScore2 + "&user="
							+ settings.getString("highName", "") + "&id="
							+ Android_ID + "&sensor=" + usesensor + "&version="
							+ settings.getInt("versionCurrent", 0));
			
			if (DEBUG)Log.d("URL:", url.toString());
			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String str = "";
			while ((str = in.readLine()) != null) {
				// str is one line of text; readLine() strips the newline
				// character(s)
			}
			in.close();
			if (str == "")
				return false;
			
		} catch (MalformedURLException e) {
			if (DEBUG)
				Log.d("ERROR", "exc", e);
			return false;
		} catch (IOException e) {
			if (DEBUG)
				Log.d("ERROR", "exc", e);
			return false;
		}
		return true;
	}

	/**
	 * @param settings the settings to set
	 */
	public static void setSettings(SharedPreferences settings) {
		Beheer.settings = settings;
	}

	/**
	 * @return the settings
	 */
	public static SharedPreferences getSettings() {
		return settings;
	}

	/**
	 * @param context the context to set
	 */
	public static void setContext(Context context) {
		Beheer.context = context;
	}

	/**
	 * @return the context
	 */
	public static Context getContext() {
		return context;
	}
}
