package paul.sohier.snake2;

import paul.sohier.snake2.general.Beheer;
import paul.sohier.snake2.view.SnakeView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;


public class PlayActivity extends Activity {

    SnakeView view = null;  
    private int mNextDirection = 0;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        if (view == null)  
            view = new SnakeView(this);  
        
        setContentView(view);  
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

		int usesensor = Integer.parseInt(view.getSettings().getString("sensor", "1"));
		mNextDirection = 0;
		if (Beheer.getDebug())
			Log.d("SNAKE", "Sensor: " + usesensor);

		if (usesensor != 1) {
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP && view.getmDirection() != SnakeView.SOUTH) {
			mNextDirection = SnakeView.NORTH;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && view.getmDirection() != SnakeView.NORTH) {
			mNextDirection = SnakeView.SOUTH;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && view.getmDirection() != SnakeView.EAST) {
			mNextDirection = SnakeView.WEST;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && view.getmDirection() != SnakeView.WEST) {
			mNextDirection = SnakeView.EAST;
		}
		
		if (mNextDirection != 0)
		{
			view.setDirection(mNextDirection);
		}

		return super.onKeyDown(keyCode, msg);
	}   
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// Log.d("SNake touch", "TEST!");

		int usesensor = Integer.parseInt(view.getSettings().getString("sensor", "1"));

		if (usesensor != 3) {
			return false;
		}
		//else if (getMode() != RUNNING)
		{
			//return false;
		}

		float x = event.getX();
		float y = event.getY();

		// Try to get the values the field should be in.

		int hoogte = (SnakeView.getHsize() / 4);
		int breedte = (SnakeView.getWsize() / 4);

		// Left:

		int leftHstart = hoogte;
		int leftHend = SnakeView.getHsize() - hoogte;
		int leftWstart = 0;
		int leftWend = breedte;

		// Right
		int rightHstart = leftHstart;
		int rightHend = leftHend;
		int rightWstart = SnakeView.getWsize() - breedte;
		int rightWend = SnakeView.getWsize();

		// Top
		hoogte = (SnakeView.getHsize() / 4);
		breedte = (SnakeView.getWsize() / 4);

		int topWstart = breedte;
		int topWend = SnakeView.getWsize() - breedte;
		int topHstart = 0;
		int topHend = hoogte;

		// bottom
		int bottomWstart = breedte;
		int bottomWend = SnakeView.getWsize() - breedte;
		int bottomHstart = SnakeView.getHsize() - hoogte;
		int bottomHend = SnakeView.getHsize();
		
		mNextDirection = 0;

		// Left
		if (x > leftWstart && x < leftWend && y > leftHstart && y < leftHend
				&& view.getmDirection() != SnakeView.EAST) {
			if (Beheer.getDebug())
				Log.d("Touch:", "LEFT!");
			mNextDirection = SnakeView.WEST;
		}
		// Right
		if (x > rightWstart && x < rightWend && y > rightHstart
				&& y < rightHend && view.getmDirection() != SnakeView.WEST) {
			if (Beheer.getDebug())
				Log.d("Touch:", "RIGHT!");
			mNextDirection = SnakeView.EAST;
		}
		// Top
		if (x > topWstart && x < topWend && y > topHstart && y < topHend
				&& view.getmDirection() != SnakeView.SOUTH) {
			if (Beheer.getDebug())
				Log.d("Touch:", "TOP!");
			mNextDirection = SnakeView.NORTH;
		}
		// Bottom
		if (x > bottomWstart && x < bottomWend && y > bottomHstart
				&& y < bottomHend && view.getmDirection() != SnakeView.NORTH) {
			if (Beheer.getDebug())
				Log.d("Touch:", "BOTTOM!");
			mNextDirection = SnakeView.SOUTH;
		}
		
		if (mNextDirection != 0)
		{
			view.setDirection(mNextDirection);
		}

		return true;
	}	
}
