package edu.virginia.cs2110.ghosthunter17;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MotionEvent.PointerCoords;

public class GameView extends View {

	private long lastTime;
	private World w;
	private Rect bombButton, lightButton;
	private Paint buttonPaint;

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
		
		bombButton = new Rect();
		lightButton = new Rect();
		
		buttonPaint = new Paint();
		buttonPaint.setTextAlign(Align.CENTER);
		buttonPaint.setTextSize(40);
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
		
		bombButton.left = 65;
		bombButton.right = 365;
		bombButton.top = h - 90;
		bombButton.bottom = h - 10;
		
		lightButton.left = 415;
		lightButton.right = 715;
		lightButton.top = h - 90;
		lightButton.bottom = h - 10;
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
		
		buttonPaint.setColor(Color.GRAY);
		c.drawRect(bombButton, buttonPaint);
		c.drawRect(lightButton, buttonPaint);
		
		buttonPaint.setColor(Color.BLACK);
		c.drawText("Drop Bomb", bombButton.centerX(), bombButton.centerY(), buttonPaint);
		c.drawText("Toggle Light", lightButton.centerX(), lightButton.centerY(), buttonPaint);
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
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			PointerCoords coord = new PointerCoords();
			event.getPointerCoords(event.getActionIndex(), coord);
			if (bombButton.contains((int)coord.x, (int)coord.y)) {
				w.dropBomb();
			}
			else if (lightButton.contains((int)coord.x, (int)coord.y)){
				w.toggleLight();
			}
		}
		
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
