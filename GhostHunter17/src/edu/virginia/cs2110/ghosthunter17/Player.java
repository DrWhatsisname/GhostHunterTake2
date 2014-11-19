package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Player extends GameObject {

	private static final float V = 2000;
	private static final float SIZE = 150;

	private PointF target;
	private Paint p;
	private int lives;

	public Player(World world, PointF pos) {
		super(world, pos);
		p = new Paint();
		// Initialize collision bounds as a rectangle
		p.setColor(0xffff0000);
		target = null;
		lives = 3;
	}

	@Override
	public void update(float timePassed) {
		if (target != null) {
			PointF dist = new PointF(target.x - pos.x, target.y - pos.y);
			float len = dist.length();
			float scale = V / len * timePassed;
			PointF diff = new PointF(dist.x * scale, dist.y * scale);
			// Move towards the target if it is further than the player can
			// travel in a frame
			if (diff.length() < len) {
				pos.x += diff.x;
				pos.y += diff.y;
			}
			// Otherwise move to the target
			else {
				pos.x += dist.x;
				pos.y += dist.y;
			}
		}
	}

	@Override
	public void render(Canvas c) {
		c.drawRect(pos.x - SIZE / 2, pos.y - SIZE / 2, pos.x + SIZE / 2, pos.y
				+ SIZE / 2, p);

	}

	public void setTarget(PointF target) {
		this.target = target;
	}

	@Override
	public void collide(GameObject g) {
		if (g instanceof Circle) {
			if (lives == 3) {
				p.setColor(Color.YELLOW);
			}

			else if (lives == 2) {
				p.setColor(Color.RED);
			}

			else {
				p.setColor(Color.BLACK);
			}

			lives--;
		}
	}

	@Override
	public RectF getColBounds() {
		return new RectF(this.pos.x - SIZE / 2, this.pos.y - SIZE / 2,
				(this.pos.x + SIZE / 2), (this.pos.y + SIZE / 2));
	}

}
