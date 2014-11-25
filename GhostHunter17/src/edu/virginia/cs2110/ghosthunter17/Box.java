package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Box extends GameObject {
	public static int boundX, boundY;

	private PointF vel, size;
	private Paint p;

	public Box(World w, PointF pos, PointF vel) {
		super(w, pos);
		this.vel = vel;
		this.size = new PointF(100, 100);

		// Initialize a paint object to red
		this.p = new Paint();
		p.setColor(0xffff0000);

	}

	@Override
	public void update(float timePassed) {
		// Update the position
		this.pos.x += vel.x * timePassed;
		this.pos.y += vel.y * timePassed;

		// Check and resolve moving out of bounds
		if (pos.x + size.x > boundX) {
			pos.x = boundX - size.x;
			vel.x = -vel.x;
		} else if (pos.x < 0) {
			pos.x = 0;
			vel.x = -vel.x;
		}
		if (pos.y + size.y > boundY) {
			pos.y = boundY - size.y;
			vel.y = -vel.y;
		} else if (pos.y < 0) {
			pos.y = 0;
			vel.y = -vel.y;
		}

	}

	@Override
	public void render(Canvas c) {
		// Draw a rectangle at the box's position
		c.drawRect(pos.x, pos.y, pos.x + size.x, pos.y + size.y, p);
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		// Set color to a random color if collide with another box
		if (g instanceof Box) {
			p.setARGB(255, (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255));
		}
		// Reset to red if collide with a circle or the player
		else if (g instanceof Circle || g instanceof Player) {
			p.setColor(0xffff0000);
		}

		else if (g instanceof Wall) {
			if (dir == Direction.NORTH || dir == Direction.SOUTH) {
				vel.y = -vel.y;
			}
			else {
				vel.x = -vel.x;
			}
		}
	}

	@Override
	public RectF getColBounds() {
		return new RectF(pos.x, pos.y, pos.x + size.x, pos.y + size.y);
	}

}
