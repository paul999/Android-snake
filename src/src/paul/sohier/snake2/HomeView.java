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

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;

import paul.sohier.snake2.R;

/**
 * HomeView: implementation of a simple game of Snake
 * 
 * 
 */
public class HomeView extends TileView {

	private static final String TAG = "HomeView";

	public static final String PREFS_NAME = "Snake2";

	private static final int RED_STAR = 1;
	private static final int YELLOW_STAR = 2;
	private static final int GREEN_STAR = 3;
	private static final int YELLOW_HEAD = 4;	
	
	/**
	 * Constructs a HomeView based on inflation from XML
	 * 
	 * @param context
	 * @param attrs
	 */
	public HomeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSnakeView();

	}

	public HomeView(Context context, AttributeSet attrs, int defStyle) {
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
		updateWalls();
	}

	/**
	 * Draws some walls.
	 * 
	 */
	public void updateWalls() {
		for (int x = 0; x < mXTileCount; x++) {
			setTile(GREEN_STAR, x, 0);
			setTile(GREEN_STAR, x, mYTileCount - 1);
		}
		for (int y = 1; y < mYTileCount - 1; y++) {
			setTile(GREEN_STAR, 0, y);
			setTile(GREEN_STAR, mXTileCount - 1, y);
		}
		Log.d("debug", "updatewalls");
	}
}
