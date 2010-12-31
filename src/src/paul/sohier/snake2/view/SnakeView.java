package paul.sohier.snake2.view;


import java.util.ArrayList;
import java.util.Random;

import android.os.*;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.*;
import android.view.*;

import paul.sohier.snake2.R;
import paul.sohier.snake2.general.Beheer;

public class SnakeView extends SurfaceView implements SurfaceHolder.Callback {
	private static final int RED_STAR = 1;
	private static final int YELLOW_STAR = 2;
	private static final int GREEN_STAR = 3;
	private static final int YELLOW_HEAD = 4;	
	
	private int mDirection = NORTH;
	private int mNextDirection = NORTH;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;
	
	private long mMoveDelay = 600;
	private long mMoveDelayStart;

	/**
	 * mLastMove: tracks the absolute time when the snake last moved, and is
	 * used to determine if a move should be made based on mMoveDelay.
	 */
	private long mLastMove;
	
	private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();

	/**
	 * Everyone needs a little randomness in their life
	 */
	private static final Random RNG = new Random();
	
	// private SharedPreferences settings;
	private SharedPreferences settings;	
	
	private int score = 0;
	
	private SnakeThread mThread = null;
	
	/**
	 * Parameters controlling the size of the tiles and their range within view.
	 * Width/Height are in pixels, and Drawables will be scaled to fit to these
	 * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
	 */

	protected static int mTileSize = 12;

	protected static int mXTileCount;
	protected static int mYTileCount;

	private static int Wsize;
	private static int Hsize;

	private static int mXOffset;
	private static int mYOffset;

	/**
	 * A hash that maps integer handles specified by the subclasser to the
	 * drawable that will be used for that reference
	 */
	private Bitmap[] mTileArray;

	/**
	 * A two-dimensional array of integers in which the number represents the
	 * index of the tile that should be drawn at that locations
	 */
	private int[][] mTileGrid;

	/**
	 * Rests the internal array of Bitmaps used for drawing tiles, and sets the
	 * maximum index of tiles to be inserted
	 * 
	 * @param tilecount
	 */	
	
	private int MoveDelayOptions[];
	private int data[];
	
	private boolean DEBUG;
	private Activity activity;

	public SnakeView(Context context, Activity act) {
		super(context);
		
		DEBUG = Beheer.getDebug();
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mThread = new SnakeThread(holder, context, new Handler());
		activity = act;
//		setFocusable(true); // need to get the key events

		Resources r = this.getContext().getResources();

		resetTiles(5);
		loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
		loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
		loadTile(YELLOW_HEAD, r.getDrawable(R.drawable.head));
		
		MoveDelayOptions = new int[6];
		MoveDelayOptions[0] = 800;
		MoveDelayOptions[1] = 600;
		MoveDelayOptions[2] = 400;
		MoveDelayOptions[3] = 200;
		MoveDelayOptions[4] = 100;		

		updateWalls();		
		
		setSettings(PreferenceManager.getDefaultSharedPreferences(this.getContext()));
	}	
	
	private void initGame()
	{
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

		int teller = Integer.parseInt(getSettings().getString("delay", "0"));

		if (teller < 0 || teller > MoveDelayOptions.length) {
			teller = 0;
		}
		mMoveDelay = MoveDelayOptions[teller];
		mMoveDelayStart = MoveDelayOptions[teller];
		//if (DEBUG)
			Log.d("SNAKE", "Speed: " + mMoveDelay);
		score = 0;
		//setHighscore();
		//setScore();

		long start = mMoveDelayStart / 10;

		data = new int[10];

		for (int i = 0; i < 10; i++) {
			data[i] = (int) (i * start);

			if (DEBUG)
				Log.d("SNAKE", "data[" + i + "] = " + data[i]);
		}
	}
	
	public void setDirection(int dir)
	{
		// TODO: Validate value;
		mNextDirection = dir;
	}

	public SnakeThread getThread() {
		return mThread;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		initGame();
		
		mThread.setRunning(true);

		try {
			if (!mThread.isAlive())
				mThread.start();
		} catch (Exception e) {

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		mThread.setRunning(false);
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// mThread.doKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// mThread.doKeyUp(keyCode, event);
		return super.onKeyUp(keyCode, event);
	}

	public void resetTiles(int tilecount) {
		mTileArray = new Bitmap[tilecount];
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mXTileCount = (int) Math.floor((w / mTileSize));
		mYTileCount = (int) Math.floor((h / mTileSize) - 1);

		mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
		mYOffset = (((h - (mTileSize * mYTileCount)) / 2) + 1 * mTileSize);

		setWsize(w);
		setHsize(h);

		mTileGrid = new int[mXTileCount][mYTileCount];
		clearTiles();

		updateWalls();
	}

	/**
	 * Function to set the specified Drawable as the tile for a particular
	 * integer key.
	 * 
	 * @param key
	 * @param tile
	 */
	public void loadTile(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, mTileSize, mTileSize);
		tile.draw(canvas);

		mTileArray[key] = bitmap;
	}

	/**
	 * Resets all tiles to 0 (empty)
	 * 
	 */
	public void clearTiles() {
		for (int x = 0; x < mXTileCount; x++) {
			for (int y = 0; y < mYTileCount; y++) {
				setTile(0, x, y);
			}
		}
	}

	/**
	 * Used to indicate that a particular tile (set with loadTile and referenced
	 * by an integer) should be drawn at the given x/y coordinates during the
	 * next invalidate/draw cycle.
	 * 
	 * @param tileindex
	 * @param x
	 * @param y
	 */
	public void setTile(int tileindex, int x, int y) {
		try {
			mTileGrid[x][y] = tileindex;
		} catch (Exception e) {
		}
	}

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
			//Log.e(TAG, "Somehow ended up with a null newCoord!");
		}
		mAppleList.add(newCoord);
	}	
	
	private void updateApples() {
		for (Coordinate c : mAppleList) {
			setTile(YELLOW_STAR, c.x, c.y);
		}
	}	
	private boolean growSnake;
	private Coordinate head;
	private Coordinate newHead;
	private int snakelength;
	private int applecount;
	private int index = 0;
	
	private void updateSnake() {
		growSnake = false;

		// grab the snake by the head
		head = mSnakeTrail.get(0);
		newHead = new Coordinate(1, 1);

		setmDirection(mNextDirection);

		switch (getmDirection()) {
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
			mThread.setRunning(false);
			activity.finish();
			return;

		}

		// Look for collisions with itself
		snakelength = mSnakeTrail.size();
		for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
			Coordinate c = mSnakeTrail.get(snakeindex);
			if (c.equals(newHead)) {
				if (DEBUG)
					Log.d("SNAKE", "head dead");
				mThread.setRunning(false);
				activity.finish();
				return;
			}
		}

		// Look for apples
		applecount = mAppleList.size();
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

				score += diff;
				mMoveDelay *= 0.9;
				// setScore();

				growSnake = true;

			}
		}

		// push a new head onto the ArrayList and pull off the tail
		mSnakeTrail.add(0, newHead);
		// except if we want the snake to grow
		if (!growSnake) {
			mSnakeTrail.remove(mSnakeTrail.size() - 1);
		}

		index = 0;
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
	 * @param settings the settings to set
	 */
	public void setSettings(SharedPreferences settings) {
		this.settings = settings;
		
		Beheer.setSettings(settings);
	}

	/**
	 * @return the settings
	 */
	public SharedPreferences getSettings() {
		return settings;
	}

	/**
	 * @param mDirection the mDirection to set
	 */
	public void setmDirection(int mDirection) {
		this.mDirection = mDirection;
	}

	/**
	 * @return the mDirection
	 */
	public int getmDirection() {
		return mDirection;
	}

	/**
	 * @param hsize the hsize to set
	 */
	public static void setHsize(int hsize) {
		Hsize = hsize;
	}

	/**
	 * @return the hsize
	 */
	public static int getHsize() {
		return Hsize;
	}

	/**
	 * @param wsize the wsize to set
	 */
	public static void setWsize(int wsize) {
		Wsize = wsize;
	}

	/**
	 * @return the wsize
	 */
	public static int getWsize() {
		return Wsize;
	}

	class FPSTimer {
		private int mFPS;
		private double mSecPerFrame;
		private double mSecTiming;
		private long mCur;
		private long next;
		private long passage_time;

		public FPSTimer(int fps) {
			mFPS = fps;
			reset();
		}

		public void reset() {
			mSecPerFrame = 1.0 / mFPS;
			mCur = System.currentTimeMillis();
			mSecTiming = 0.0;
		}

		public boolean elapsed() {
			next = System.currentTimeMillis();
			passage_time = next - mCur;
			mCur = next;
			mSecTiming += (passage_time / 1000.0);
			mSecTiming -= mSecPerFrame;
			if (mSecTiming > 0) {
				if (mSecTiming > mSecPerFrame) {
					reset();
					return true; // force redraw
				}
				return false;
			}
			try {
				Thread.sleep((long) (-mSecTiming * 1000.0));
			} catch (InterruptedException e) {
			}
			return true;
		}
	}

	class SnakeThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private boolean mRun = false;
		private Paint paint;
		private long now;

		private double rfps = 0.00d;

		public SnakeThread(SurfaceHolder holder, Context context,
				Handler handler) {
			mSurfaceHolder = holder;
			paint = new Paint();
		}

		public void setRunning(boolean b) {
			mRun = b;
		}

		public void run() {
			int fps = 0;
			long cur = System.currentTimeMillis();
			boolean isdraw = true;
			FPSTimer timer = new FPSTimer(120);
			long now;
			Canvas c = null;
			while (mRun) {
				c = null;
				if (isdraw) {
					try {
						c = mSurfaceHolder.lockCanvas(null);
						synchronized (mSurfaceHolder) {
							doDraw(c);
						}
						fps++;
					} finally {
						if (c != null)
							mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
				isdraw = timer.elapsed();
				now = System.currentTimeMillis();
				if (now - cur > 1000) {
					rfps = (fps * 1000 / ((double) now - cur));
					Log.d("KZK", "FPS=" + rfps);
					fps = 0;
					cur = now;
				}
			}
		}

		
		protected void doDraw(Canvas canvas) {
			// Clear stuff
			
			now = System.currentTimeMillis();

			if (now - mLastMove > mMoveDelay) {
				clearTiles();
				updateWalls();
				updateSnake();
				updateApples();
				mLastMove = now;
			}			
			
			paint.setColor(Color.argb(255, 0, 0, 0));
			canvas.drawRect(
					new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
					paint);
			// paint.setAntiAlias(true);
			// canvas.drawBitmap(image, 0, 0, null);

			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.argb(255, 0, 0, 255));

			canvas.drawText("FPS: " + rfps, 80, 60, paint);
			
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.argb(255, 0, 0, 255));			
			canvas.drawText("Score: " + score, 0, 10, paint);

			for (int x = 0; x < mXTileCount; x += 1) {
				for (int y = 0; y < mYTileCount; y += 1) {
					if (mTileGrid[x][y] > 0) {
						canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset
								+ x * mTileSize, mYOffset + y * mTileSize,
								paint);
					}
				}
			}
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
}
