package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

public class Circle extends GameObject {
	public static int boundX, boundY;

	private PointF vel, size;
	private Paint p;
	private float radius;
	
	public Circle(World w, PointF pos, PointF vel, float radius) {
		super(w, pos);
		this.vel = vel;
		this.size = new PointF(2*radius, 2*radius);
		this.radius = radius;
		
		// Initialize collision bounds as a rectangle
		this.colBounds = new Rect((int)pos.x,(int)pos.y,(int)(pos.x+size.x),(int)(pos.y + size.y));
		
		// Initialize a paint object to red
		this.p = new Paint();
		p.setColor(0xff4b0082);
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
		
		colBounds.set((int)this.pos.x, (int)this.pos.y, (int)(this.pos.x + size.x) , (int)(this.pos.y + size.y));
	}

	@Override
	public void render(Canvas c) {
		// Draw a rectangle at the box's position
		c.drawCircle(pos.x+radius, pos.y+radius, radius, p);
	}

	@Override
	public void collide() {
		// TODO Auto-generated method stub
		
	}

}
