package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
/*
 * T103-17
 * Henry Conklin, hwc5gj
 * Wylie Wang, ww5ts	
 * Cornelius Nelson, cn3dh	
 * Ryan Montgomery, rmm4wf
 */
public class Battery extends GameObject {
	
	private static final float HEIGHT = 30; // Pixels
	private static final float WIDTH = 60;
	private Paint p;
	private Bitmap bmp;
	
	public Battery(World world, PointF pos) {
		super(world,pos);
		p = new Paint();
		p.setColor(Color.YELLOW);
		bmp = ResourceManager.getImage(R.drawable.battery);
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
//		c.drawRect(pos.x, pos.y, pos.x + WIDTH, pos.y + HEIGHT, p);
		c.drawBitmap(bmp, new Rect(0,0,bmp.getWidth(), bmp.getHeight()), getColBounds(), p);
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		if (g instanceof Player){
			world.removeObject(this);
			world.rechargeBattery();
		}
	}
}
