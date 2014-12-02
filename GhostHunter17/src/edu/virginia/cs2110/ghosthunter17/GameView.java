package edu.virginia.cs2110.ghosthunter17;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class GameView extends View {

	private long lastTime;
	private World w;

	public GameView(Context context) {
		super(context);
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		Log.v("GameView", "initialize");
		// Initialize things
		lastTime = System.currentTimeMillis();
		
		setLayerType(LAYER_TYPE_SOFTWARE, null);

		// Set up GameObject world
		w = new World();
	
	}

	public void onPause() {
		w.pause();
	}

	public void onResume() {
		lastTime = System.currentTimeMillis();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Ghost.boundX = w;
		Ghost.boundY = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Update the state of the game
		update();

		// Draw everything to the screen
		render(canvas);

		// Redraw the view (call onDraw again)
		this.invalidate();
	
	}

	private void render(Canvas c) {
		w.render(c);
	}

	private void update() {
		// Update time
		long thisTime = System.currentTimeMillis();
		float timePassed = (thisTime - lastTime) / 1000.0f;
		lastTime = thisTime;

		w.update(timePassed);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (w.onTouchEvent(event)) {
			performClick();
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {

		return super.performClick();
	}

}
