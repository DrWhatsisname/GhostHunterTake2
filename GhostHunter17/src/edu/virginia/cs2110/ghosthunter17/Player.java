package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Player extends GameObject {

	private static final float V = 200;

	private PointF target;
	private Paint p;
	private int lives;
	
	public Player(World world, PointF pos) {
		super(world, pos);
		p = new Paint();
		// Initialize collision bounds as a rectangle
		this.colBounds = new RectF(pos.x-50,pos.y-50,pos.x+50,pos.y + 50);
		p.setColor(0xffff0000);
		target = null;
		lives = 3;
	}

	@Override
	public void update(float timePassed) {
		if (target != null) {
			PointF diff = new PointF(target.x - pos.x, target.y - pos.y);
			float len = diff.length();
			diff.x *= V/len;
			diff.y *= V/len;
			pos.x += diff.x * timePassed;
			pos.y += diff.y * timePassed;
		}
		
		colBounds.set(this.pos.x-50, this.pos.y-50, this.pos.x + 50 , this.pos.y + 50);
	}

	@Override
	public void render(Canvas c) {
		c.drawRect(pos.x-50, pos.y-50, pos.x + 50, pos.y + 50, p);

	}

	public void setTarget(PointF target) {
		this.target = target;
	}

	@Override
	public void collide() {
		if (lives == 3){
			p.setColor(Color.YELLOW);
		}
		
		else if (lives == 2){
			p.setColor(Color.RED);
		}
		
		else{
			p.setColor(Color.BLACK);
		}
		
		lives--;
	}

}
