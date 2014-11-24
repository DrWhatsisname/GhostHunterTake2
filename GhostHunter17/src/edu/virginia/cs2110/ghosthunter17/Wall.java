package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Wall extends GameObject {

	private static Paint wallPaint;

	private RectF bounds;

	public Wall(World world, RectF bounds) {
		super(world, bounds.left, bounds.top);
		this.bounds = bounds;
		if (wallPaint == null) {
			wallPaint = new Paint();
			wallPaint.setColor(0xff000000);
		}
	}

	@Override
	public RectF getColBounds() {
		return bounds;
	}

	@Override
	public void update(float timePassed) {
		// Do nothing
	}

	@Override
	public void render(Canvas c) {
		c.drawRect(bounds, wallPaint);
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		
	}

}
