package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Bomb extends GameObject {
	
	private static final float SIZE = 40; // Pixels
	private Paint p;
	private Bitmap bmp;
	
	public Bomb(World world, PointF pos) {
		super(world,pos);
		p = new Paint();
		p.setColor(Color.BLUE);
		bmp = ResourceManager.getImage(R.drawable.bomb);
	}

	@Override
	public RectF getColBounds() {
		return new RectF(this.pos.x, this.pos.y,
				this.pos.x + SIZE, this.pos.y + SIZE);
	}

	@Override
	public void update(float timePassed) {
		//Stay Still
	}

	@Override
	public void render(Canvas c) {
//		c.drawCircle(pos.x + SIZE, pos.y + SIZE, SIZE, p);
		c.drawBitmap(bmp, new Rect(0,0,bmp.getWidth(), bmp.getHeight()), getColBounds(), p);
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		if (g instanceof Player){
			world.removeObject(this);
			Player p = (Player)g;
			p.addBomb();
		}
	}
}
