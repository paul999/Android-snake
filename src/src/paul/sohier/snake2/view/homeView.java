package paul.sohier.snake2.view;

import paul.sohier.snake2.R;
import paul.sohier.snake2.R.drawable;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

/**
 * homeView: implementation of a simple game of main
 * 
 * 
 */
public class homeView extends TileView {

	/**
	 * Labels for the drawables that will be loaded into the TileView class
	 */
	private static final int GREEN_STAR = 1;

	/**
	 * Create a simple handler that we can use to cause animation to happen. We
	 * set ourselves as a target and we can use the sleep() function to cause an
	 * update/invalidate to occur at a later date.
	 */
	private RefreshHandler mRedrawHandler = new RefreshHandler();

	private long mMoveDelay = 600;

	/**
	 * mLastMove: tracks the absolute time when the snake last moved, and is
	 * used to determine if a move should be made based on mMoveDelay.
	 */
	private long mLastMove;	

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
	 * Constructs a homeView based on inflation from XML
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

	public void initSnakeView() {
		setFocusable(true);
		Log.d("DEBUG","LOGGER");

		Resources r = this.getContext().getResources();

		resetTiles(2);
		loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
	}
	/**
	 * Handles the basic update loop, checking to see if we are in the running
	 * state, determining if a move should be made, updating the snake's
	 * location.
	 */
	public void update() {
			long now = System.currentTimeMillis();

			if (now - mLastMove > mMoveDelay) {
//				clearTiles();
				updateWalls();;
				mLastMove = now;
			}
			mRedrawHandler.sleep(mMoveDelay);
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
}