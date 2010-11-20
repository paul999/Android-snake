/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package paul.sohier.snake2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.admob.android.ads.*;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * SnakeView: implementation of a simple game of Snake
 * 
 * 
 */
public class homeView extends TileView {

	private static final String TAG = "SnakeView";

	public static final String PREFS_NAME = "Snake2";

	/**
	 * Current mode of application: READY to run, RUNNING, or you have already
	 * lost. static final ints are used instead of an enum for performance
	 * reasons.
	 */
	private int mMode = READY;
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;

	/**
	 * Current direction the snake is headed.
	 */
	private int mDirection = NORTH;
	private int mNextDirection = NORTH;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;

	/**
	 * Labels for the drawables that will be loaded into the TileView class
	 */
	private static final int RED_STAR = 1;
	private static final int YELLOW_STAR = 2;
	private static final int GREEN_STAR = 3;
	private static final int YELLOW_HEAD = 4;

	/**
	 * mScore: used to track the number of apples captured mMoveDelay: number of
	 * milliseconds between snake movements. This will decrease as apples are
	 * captured.
	 */
	private long mScore = 0;
	private long mMoveDelay = 600;
	private long mMoveDelayStart;

	/**
	 * mLastMove: tracks the absolute time when the snake last moved, and is
	 * used to determine if a move should be made based on mMoveDelay.
	 */
	private long mLastMove;

	/**
	 * mStatusText: text shows to the user in some run states
	 */
	private TextView mStatusText;
	private TextView mScoreText;

	/**
	 * mSnakeTrail: a list of Coordinates that make up the snake's body
	 * mAppleList: the secret location of the juicy apples the snake craves.
	 */
	private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();

	/**
	 * Everyone needs a little randomness in their life
	 */
	private static final Random RNG = new Random();
	private boolean disp = true;

	// private SharedPreferences settings;
	private SharedPreferences settings2;

	private long highscore;

	private int data[];

	private int MoveDelayOptions[];

	private boolean DEBUG = false; // @TODO: Add settings?

	/**
	 * Create a simple handler that we can use to cause animation to happen. We
	 * set ourselves as a target and we can use the sleep() function to cause an
	 * update/invalidate to occur at a later date.
	 */
	private RefreshHandler mRedrawHandler = new RefreshHandler();

	private AdView mAd;

	private ProgressDialog dialog;

	private String xstr;
	private boolean done;

	private String url = "http://bit.ly/aDLfDS";

	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			homeView.this.update();
			homeView.this.invalidate();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	/**
	 * Constructs a SnakeView based on inflation from XML
	 * 
	 * @param context
	 * @param attrs
	 */
	public homeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSnakeView();

	}

	public homeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initSnakeView();
	}

	private void initSnakeView() {
		setFocusable(true);

		Resources r = this.getContext().getResources();

		resetTiles(5);
		loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
		loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
		loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		loadTile(YELLOW_HEAD, r.getDrawable(R.drawable.head));

		MoveDelayOptions = new int[6];
		MoveDelayOptions[0] = 800;
		MoveDelayOptions[1] = 600;
		MoveDelayOptions[2] = 400;
		MoveDelayOptions[3] = 200;
		MoveDelayOptions[4] = 100;
	}

	void initNewGame() {
		mSnakeTrail.clear();
		mAppleList.clear();

		// For now we're just going to load up a short default eastbound snake
		// that's just turned north

		mSnakeTrail.add(new Coordinate(7, 7));
		mSnakeTrail.add(new Coordinate(6, 7));
		mSnakeTrail.add(new Coordinate(5, 7));
		mSnakeTrail.add(new Coordinate(4, 7));
		mSnakeTrail.add(new Coordinate(3, 7));
		mSnakeTrail.add(new Coordinate(2, 7));
		mSnakeTrail.add(new Coordinate(1, 7));
		mNextDirection = EAST;

		// Two apples to start with
		addRandomApple();
		addRandomApple();
		addRandomApple();

		int teller = Integer.parseInt(settings2.getString("delay", "0"));

		if (teller < 0 || teller > MoveDelayOptions.length) {
			teller = 0;
		}
		mMoveDelay = MoveDelayOptions[teller];
		mMoveDelayStart = MoveDelayOptions[teller];
		if (DEBUG)
			Log.d("SNAKE", "Speed: " + mMoveDelay);
		mScore = 0;
		setHighscore();
		setScore();

		long start = mMoveDelayStart / 10;

		data = new int[10];

		for (int i = 0; i < 10; i++) {
			data[i] = (int) (i * start);

			if (DEBUG)
				Log.d("SNAKE", "data[" + i + "] = " + data[i]);
		}

	}

	public void setAd(AdView ad) {
		mAd = ad;
	}

	/**
	 * Given a ArrayList of coordinates, we need to flatten them into an array
	 * of ints before we can stuff them into a map for flattening and storage.
	 * 
	 * @param cvec
	 *            : a ArrayList of Coordinate objects
	 * @return : a simple array containing the x/y values of the coordinates as
	 *         [x1,y1,x2,y2,x3,y3...]
	 */
	private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
		int count = cvec.size();
		int[] rawArray = new int[count * 2];
		for (int index = 0; index < count; index++) {
			Coordinate c = cvec.get(index);
			rawArray[2 * index] = c.x;
			rawArray[2 * index + 1] = c.y;
		}
		return rawArray;
	}

	/**
	 * Save game state so that the user does not lose anything if the game
	 * process is killed while we are in the background.
	 * 
	 * @return a Bundle with this view's state
	 */
	public Bundle saveState() {
		Bundle map = new Bundle();

		map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
		map.putInt("mDirection", Integer.valueOf(mDirection));
		map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
		map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
		map.putLong("mScore", Long.valueOf(mScore));
		map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

		return map;
	}

	/**
	 * Given a flattened array of ordinate pairs, we reconstitute them into a
	 * ArrayList of Coordinate objects
	 * 
	 * @param rawArray
	 *            : [x1,y1,x2,y2,...]
	 * @return a ArrayList of Coordinates
	 */
	private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
		ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 2) {
			Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
			coordArrayList.add(c);
		}
		return coordArrayList;
	}

	/**
	 * Restore game state if our process is being relaunched
	 * 
	 * @param icicle
	 *            a Bundle containing the game state
	 */
	public void restoreState(Bundle icicle) {
		setMode(PAUSE);

		mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
		mDirection = icicle.getInt("mDirection");
		mNextDirection = icicle.getInt("mNextDirection");
		mMoveDelay = icicle.getLong("mMoveDelay");
		mScore = icicle.getLong("mScore");
		setScore();
		mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
	}

	public void setDirection(int dir) {
		mNextDirection = dir;
		if (DEBUG)
			Log.d("SNAKE", "New Direction: " + dir);
	}

	public int getDirection() {
		return mNextDirection;
	}

	/*
	 * handles key events in the game. Update the direction our snake is
	 * traveling based on the DPAD. Ignore events that would cause the snake to
	 * immediately turn back on itself.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		int usesensor = Integer.parseInt(settings2.getString("sensor", "1"));
		if (DEBUG)
			Log.d("SNAKE", "Sensor: " + usesensor);

		if (usesensor != 1 || getMode() != RUNNING) {
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP && mDirection != SOUTH) {
			mNextDirection = NORTH;
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && mDirection != NORTH) {
			mNextDirection = SOUTH;
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && mDirection != EAST) {
			mNextDirection = WEST;
			return (true);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && mDirection != WEST) {
			mNextDirection = EAST;
			return (true);
		}

		return super.onKeyDown(keyCode, msg);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// Log.d("SNake touch", "TEST!");

		int usesensor = Integer.parseInt(settings2.getString("sensor", "1"));

		if (usesensor != 3) {
			return false;
		}
		else if (getMode() != RUNNING)
		{
			return false;
		}

		float x = event.getX();
		float y = event.getY();

		// Try to get the values the field should be in.

		int hoogte = (Hsize / 4);
		int breedte = (Wsize / 4);

		// Left:

		int leftHstart = hoogte;
		int leftHend = Hsize - hoogte;
		int leftWstart = 0;
		int leftWend = breedte;

		// Right
		int rightHstart = leftHstart;
		int rightHend = leftHend;
		int rightWstart = Wsize - breedte;
		int rightWend = Wsize;

		// Top
		hoogte = (Hsize / 4);
		breedte = (Wsize / 4);

		int topWstart = breedte;
		int topWend = Wsize - breedte;
		int topHstart = 0;
		int topHend = hoogte;

		// bottom
		int bottomWstart = breedte;
		int bottomWend = Wsize - breedte;
		int bottomHstart = Hsize - hoogte;
		int bottomHend = Hsize;

		// Left
		if (x > leftWstart && x < leftWend && y > leftHstart && y < leftHend
				&& mDirection != EAST) {
			if (DEBUG)
				Log.d("Touch:", "LEFT!");
			mNextDirection = WEST;
		}
		// Right
		if (x > rightWstart && x < rightWend && y > rightHstart
				&& y < rightHend && mDirection != WEST) {
			if (DEBUG)
				Log.d("Touch:", "RIGHT!");
			mNextDirection = EAST;
		}
		// Top
		if (x > topWstart && x < topWend && y > topHstart && y < topHend
				&& mDirection != SOUTH) {
			if (DEBUG)
				Log.d("Touch:", "TOP!");
			mNextDirection = NORTH;
		}
		// Bottom
		if (x > bottomWstart && x < bottomWend && y > bottomHstart
				&& y < bottomHend && mDirection != NORTH) {
			if (DEBUG)
				Log.d("Touch:", "BOTTOM!");
			mNextDirection = SOUTH;
		}

		return true;
	}

	/**
	 * Sets the TextView that will be used to give information (such as "Game
	 * Over" to the user.
	 * 
	 * @param newView
	 */
	public void setTextView(TextView newView, TextView newView2) {
		mStatusText = newView;
		mScoreText = newView2;
	}

	public void setSettings(/* SharedPreferences setting, */SharedPreferences sp) {
		// settings = setting;
		settings2 = sp;
	}

	public int getMode() {
		return mMode;
	}

	/**
	 * Updates the current mode of the application (RUNNING or PAUSED or the
	 * like) as well as sets the visibility of textview for notification
	 * 
	 * @param newMode
	 */
	public void setMode(int newMode) {
		int oldMode = mMode;
		mMode = newMode;

		// MotionEvent e;
		// e.

		if (newMode == RUNNING & oldMode != RUNNING) {
			dispAds(false);
			mStatusText.setVisibility(View.INVISIBLE);
			update();
			return;
		}

		Resources res = getContext().getResources();
		CharSequence str = "";
		if (newMode == PAUSE) {
			dispAds(true);
			str = res.getText(R.string.mode_pause);
		}
		if (newMode == READY) {
			dispAds(true);
			str = res.getText(R.string.mode_ready);
		}
		if (newMode == LOSE) {
			clearTiles();
			dispAds(true);
			str = res.getString(R.string.mode_lose_prefix) + mScore
					+ res.getString(R.string.mode_lose_suffix);

			if (mScore > highscore) {
				updateHighscore(mScore);
				str = str + "\n\n";
				str = str + res.getString(R.string.new_highscore);

				if (settings2.getBoolean("sendTwitter", true)) {
					String tmp = getContext().getResources().getString(
							R.string.sendingTwitter)
							+ " "
							+ mScore
							+ " "
							+ getContext().getResources().getString(
									R.string.sendingTwitter2) + " " + url;
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("text/plain");
					sendIntent.putExtra(Intent.EXTRA_TEXT, tmp);
					// Intent.
					// sendIntent.setType("application/twitter");
					getContext().startActivity(
							Intent.createChooser(sendIntent, null));
				}
			}
			xstr = "";
			done = false;

			if (settings2.getBoolean("sendHigh", true)) // @TODO: Check config!
			{

				String tmp = getContext().getResources().getString(
						R.string.sendingHigh);
				dialog = new ProgressDialog(this.getContext());
				dialog.setTitle(R.string.busy);
				dialog.setMessage(tmp);
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);

				dialog.show();

				if (!isOnline()) {
					str = str + "\n\n" + res.getString(R.string.no_internet);
					dialog.dismiss();
					if (DEBUG)
						Log.d("Snake", "no_internet");
				} else {
					if (DEBUG)
						Log.d("Snake", "internet");
				}

				new Thread(new Runnable() {
					public void run() {
						makeHttpRequest(mScore);

						dialog.dismiss();
						done = true;
					}
				}).start();

			} else {
				done = true;
			}

			str = str + xstr;
		}

		mStatusText.setText(str);
		mStatusText.setVisibility(View.VISIBLE);
	}

	private void showAlertDialog(int messageId, String string) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
		builder.setMessage(messageId);
		builder.setTitle(string);
		builder.setNeutralButton(android.R.string.ok, null);
		builder.create().show();
	}

	public boolean isOnline() {
		try {
			ConnectivityManager cm = (ConnectivityManager) this.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (Exception e) {
			Log.d("error!", "error!", e);
			return false;
		}
	}

	private boolean makeHttpRequest(long mScore2) {
		try { // Create a URL for the desired page URL
			// TelephonyManager mTelephonyMgr =
			// (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			// String imei = mTelephonyMgr.getDeviceId();

			// android.provider.Settings.Secure
			String Android_ID = android.provider.Settings.System.getString(this
					.getContext().getContentResolver(),
					android.provider.Settings.System.ANDROID_ID);

			int usesensor = Integer
					.parseInt(settings2.getString("sensor", "1"));

			URL url = new URL(
					"http://www.paulsohier.nl/android/savehigh.php?score="
							+ mScore2 + "&user="
							+ settings2.getString("highName", "") + "&id="
							+ Android_ID + "&sensor=" + usesensor + "&version="
							+ settings2.getInt("versionCurrent", 0));
			
			if (DEBUG)Log.d("URL:", url.toString());
			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				// str is one line of text; readLine() strips the newline
				// character(s)
			}
			in.close();
		} catch (MalformedURLException e) {
			xstr = e.toString();
			if (DEBUG)
				Log.d("ERROR", xstr);
			return false;
		} catch (IOException e) {
			xstr = e.toString();
			if (DEBUG)
				Log.d("ERROR", xstr);
			return false;
		}
		return true;
	}

	private void dispAds(boolean disp2) {
		if (disp == disp2)
			return;
		else
			// return;

			try {

				int arg0;
				int arg1;

				if (disp2) {
					arg0 = View.VISIBLE;
					arg1 = View.INVISIBLE;
				} else {
					arg0 = View.INVISIBLE;
					arg1 = View.VISIBLE;
				}

				mAd.setVisibility(arg0);
				setVisibility(arg1);
				
				disp = disp2;

			} catch (Exception e) {
				if (DEBUG)
					Log.d("ERROR", "Epp", e);
			}

	}

	/**
	 * Selects a random location within the garden that is not currently covered
	 * by the snake. Currently _could_ go into an infinite loop if the snake
	 * currently fills the garden, but we'll leave discovery of this prize to a
	 * truly excellent snake-player.
	 * 
	 */
	private void addRandomApple() {
		Coordinate newCoord = null;
		boolean found = false;
		while (!found) {
			// Choose a new location for our apple
			int newX = 1 + RNG.nextInt(mXTileCount - 2);
			int newY = 1 + RNG.nextInt(mYTileCount - 2);
			newCoord = new Coordinate(newX, newY);

			// Make sure it's not already under the snake
			boolean collision = false;
			int snakelength = mSnakeTrail.size();
			for (int index = 0; index < snakelength; index++) {
				if (mSnakeTrail.get(index).equals(newCoord)) {
					collision = true;
				}
			}
			// if we're here and there's been no collision, then we have
			// a good location for an apple. Otherwise, we'll circle back
			// and try again
			found = !collision;
		}
		if (newCoord == null) {
			Log.e(TAG, "Somehow ended up with a null newCoord!");
		}
		mAppleList.add(newCoord);
	}

	/**
	 * Handles the basic update loop, checking to see if we are in the running
	 * state, determining if a move should be made, updating the snake's
	 * location.
	 */
	public void update() {
		if (mMode == RUNNING) {
			long now = System.currentTimeMillis();

			if (now - mLastMove > mMoveDelay) {
				clearTiles();
				updateWalls();
			}
			mRedrawHandler.sleep(mMoveDelay);
		}

	}

	/**
	 * Draws some walls.
	 * 
	 */
	private void updateWalls() {
		for (int x = 0; x < mXTileCount; x++) {
			setTile(GREEN_STAR, x, 0);
			setTile(GREEN_STAR, x, mYTileCount - 1);
		}
		for (int y = 1; y < mYTileCount - 1; y++) {
			setTile(GREEN_STAR, 0, y);
			setTile(GREEN_STAR, mXTileCount - 1, y);
		}
	}

	/**
	 * Draws some apples.
	 * 
	 */
	private void updateApples() {
		for (Coordinate c : mAppleList) {
			setTile(YELLOW_STAR, c.x, c.y);
		}
	}

	/**
	 * Figure out which way the snake is going, see if he's run into anything
	 * (the walls, himself, or an apple). If he's not going to die, we then add
	 * to the front and subtract from the rear in order to simulate motion. If
	 * we want to grow him, we don't subtract from the rear.
	 * 
	 */
	private void updateSnake() {
		boolean growSnake = false;

		// grab the snake by the head
		Coordinate head = mSnakeTrail.get(0);
		Coordinate newHead = new Coordinate(1, 1);

		mDirection = mNextDirection;

		switch (mDirection) {
		case EAST: {
			newHead = new Coordinate(head.x + 1, head.y);
			break;
		}
		case WEST: {
			newHead = new Coordinate(head.x - 1, head.y);
			break;
		}
		case NORTH: {
			newHead = new Coordinate(head.x, head.y - 1);
			break;
		}
		case SOUTH: {
			newHead = new Coordinate(head.x, head.y + 1);
			break;
		}
		}

		// Collision detection
		// For now we have a 1-square wall around the entire arena
		if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > mXTileCount - 2)
				|| (newHead.y > mYTileCount - 2)) {
			setMode(LOSE);
			return;

		}

		// Look for collisions with itself
		int snakelength = mSnakeTrail.size();
		for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
			Coordinate c = mSnakeTrail.get(snakeindex);
			if (c.equals(newHead)) {
				if (DEBUG)
					Log.d("SNAKE", "head dead");
				setMode(LOSE);
				return;
			}
		}

		// Look for apples
		int applecount = mAppleList.size();
		for (int appleindex = 0; appleindex < applecount; appleindex++) {
			Coordinate c = mAppleList.get(appleindex);
			if (c.equals(newHead)) {
				mAppleList.remove(c);

				addRandomApple();

				long diff = mMoveDelayStart - mMoveDelay;

				for (int i = 9; i >= 0; i--) {
					if (DEBUG)
						Log.d("SNAKE", "Try " + i);
					if (diff > data[i]) {
						diff = i + 1;
						if (DEBUG)
							Log.d("SNAKE", "ADD: " + diff);
						break;
					}
				}
				if (diff < 1) {
					diff = 1;
				}

				mScore += diff;
				mMoveDelay *= 0.9;
				setScore();

				growSnake = true;

			}
		}

		// push a new head onto the ArrayList and pull off the tail
		mSnakeTrail.add(0, newHead);
		// except if we want the snake to grow
		if (!growSnake) {
			mSnakeTrail.remove(mSnakeTrail.size() - 1);
		}

		int index = 0;
		for (Coordinate c : mSnakeTrail) {
			if (index == 0) {
				setTile(YELLOW_HEAD, c.x, c.y);
			} else {
				setTile(RED_STAR, c.x, c.y);
			}
			index++;

		}

	}

	/**
	 * Simple class containing two integer values and a comparison function.
	 * There's probably something I should use instead, but this was quick and
	 * easy to build.
	 * 
	 */
	private class Coordinate {
		public int x;
		public int y;

		public Coordinate(int newX, int newY) {
			x = newX;
			y = newY;
		}

		public boolean equals(Coordinate other) {
			if (x == other.x && y == other.y) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return "Coordinate: [" + x + "," + y + "]";
		}
	}

	private void setScore() {
		String str;
		Resources res = getContext().getResources();
		str = res.getString(R.string.score) + ": " + mScore;
		str += "\t" + res.getString(R.string.highscore) + ": " + highscore;

		if (mScore > highscore) {
			str += "\t";
			str += res.getString(R.string.new_highscore);
		}

		mScoreText.setText(str);
	}

	private void updateHighscore(long hs) {
		SharedPreferences.Editor editor = settings2.edit();
		editor.putLong("highscore", hs);
		editor.commit();

		setHighscore();
	}

	private void setHighscore() {
		highscore = settings2.getLong("highscore", 0);
	}
}
