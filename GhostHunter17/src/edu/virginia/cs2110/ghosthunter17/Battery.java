package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Battery extends GameObject {
	
	private static final float HEIGHT = 60; // Pixels
	private static final float WIDTH = 30;
	private Paint p;
	
	public Battery(World world, PointF pos) {
		super(world,pos);
		p = new Paint();
		p.setColor(Color.YELLOW);
	}

	@Override
	public RectF getColBounds() {
		return new RectF(this.pos.x, this.pos.y,
				this.pos.x + HEIGHT, this.pos.y + WIDTH);
	}

	@Override
	public void update(float timePassed) {
		//Stay Still
	}

	@Override
	public void render(Canvas c) {
		c.drawRect(pos.x, pos.y, pos.x + WIDTH, pos.y + HEIGHT, p);
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		if (g instanceof Player){
			world.removeObject(this);
			Player p = (Player)g;
		}
	}
}
