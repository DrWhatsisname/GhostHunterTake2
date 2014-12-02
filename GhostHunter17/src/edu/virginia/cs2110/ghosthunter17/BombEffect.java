package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class BombEffect extends GameObject {
	
	private float size; // Pixels
	private boolean explode;
	private Paint p;
	private float time;
	
	public BombEffect(World world, PointF pos) {
		super(world,pos);
		p = new Paint();
		p.setColor(Color.BLUE);
		size = 40;
		explode = false;
		time = 3;
	}

	@Override
	public RectF getColBounds() {
		return new RectF(this.pos.x, this.pos.y,
				this.pos.x + size, this.pos.y + size);
	}

	@Override
	public void update(float timePassed) {
		//Wait 2 seconds, then blow up
		time-= timePassed;
		if (time<=0){
			world.removeObject(this);
		}
		else if (time <1){
			explode = true;
			size = 160;
			p.setColor(Color.RED);
			world.checkCollision();
			explode = false;
		}
	}

	@Override
	public void render(Canvas c) {
		if (time<1){
			c.drawCircle(pos.x, pos.y, size, p);
		}
		else{
			c.drawCircle(pos.x + size, pos.y + size, size, p);
		}
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		if (explode && g instanceof Ghost){
				Ghost cir = (Ghost)g;
				cir.die();
		}
	}
}
