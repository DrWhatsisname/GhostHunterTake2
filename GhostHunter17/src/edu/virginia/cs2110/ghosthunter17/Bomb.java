package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Bomb extends GameObject {
	
	private static final float SIZE = 80; // Pixels
	private Paint p;
	
	public Bomb(World world, PointF pos) {
		super(world,pos);
		p = new Paint();
		p.setColor(Color.BLUE);
	}

	@Override
	public RectF getColBounds() {
		return new RectF(this.pos.x, this.pos.y,
				this.pos.x + SIZE, this.pos.y + SIZE);
	}

	@Override
	public void update(float timePassed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Canvas c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		// TODO Auto-generated method stub
		
	}

}
