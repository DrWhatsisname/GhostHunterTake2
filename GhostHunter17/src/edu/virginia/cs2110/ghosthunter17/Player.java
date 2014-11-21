package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Player extends GameObject {

	private static final float V = 500; // Pixels per second
	private static final float ROT_V = 180; // Degrees per second
	private static final float SIZE = 150; // Pixels

	private PointF target;
	private float rot;
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
			
			//Rotate towards target
			float targetRot = (float) (Math.atan2(dist.y, dist.x) * 180 / Math.PI);
			float frameRot = targetRot - rot;
			if (Math.abs(targetRot + 360 - rot) < Math.abs(frameRot)) {
				frameRot = targetRot + 360 - rot;
			} else if (Math.abs(targetRot - 360 - rot) < Math.abs(frameRot)) {
				frameRot = targetRot - 360 - rot;
			}
			
			if (frameRot!=0){
				float normalFrameRot = frameRot / Math.abs(frameRot);
				normalFrameRot *= ROT_V * timePassed;
	
				if (Math.abs(frameRot) < Math.abs(normalFrameRot)) {
					rot += frameRot;
				} else {
					rot += normalFrameRot;
				}
				rot%=360;
			}

			//Move towards target
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
		c.save();

		c.rotate(rot, pos.x, pos.y);
		c.drawRect(pos.x - SIZE / 2, pos.y - SIZE / 2, pos.x + SIZE / 2, pos.y
				+ SIZE / 2, p);
		c.restore();
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
