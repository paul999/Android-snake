package paul.sohier.snake2.general;

import paul.sohier.snake2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class Beheer {
	private String url = "http://bit.ly/aDLfDS";

	static private Activity act;

	private static final boolean DEBUG = true;	

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

	public static void showAlertDialog(Context context, int messageId,
			String string) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(messageId);
		builder.setTitle(string);
		builder.setNeutralButton(android.R.string.ok, null);
		builder.create().show();
	}

	public static boolean isOnline(Context c) {
		try {
			ConnectivityManager cm = (ConnectivityManager) c
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
}
