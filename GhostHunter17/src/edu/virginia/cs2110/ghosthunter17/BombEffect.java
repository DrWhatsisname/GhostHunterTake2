package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;

/*
 * T103-17
 * Henry Conklin, hwc5gj
 * Wylie Wang, ww5ts	
 * Cornelius Nelson, cn3dh	
 * Ryan Montgomery, rmm4wf
 */

public class BombEffect extends GameObject {
	
	private float size; // Pixels
	private boolean explode;
	private boolean animation;
	private Paint p;
	private float time;
	private Bitmap bmp;
	
	MediaPlayer ourSong;
	
	public BombEffect(World world, PointF pos) {
		super(world,pos);
		p = new Paint();
		p.setColor(Color.BLUE);
		size = 40;
		explode = false;
		animation = false;

		bmp = ResourceManager.getImage(R.drawable.bomb);
		time = 2;

	}

	@Override
	public RectF getColBounds() {
		if (time<1){
			return new RectF(this.pos.x-size, this.pos.y-size, this.pos.x+size, this.pos.y+size);
		}
		return new RectF(this.pos.x, this.pos.y,
				this.pos.x + size, this.pos.y + size);
	}

	@Override
	public void update(float timePassed) {
		//Wait 2 seconds, then blow up
		//Allow 1 second extra for animation
		time-= timePassed;
		if (time<=0){
			world.removeObject(this);
		}
		else if (time <1 && !animation){
			explode = true;
			size = 160;
			p.setColor(Color.RED);
			ourSong = ResourceManager.loadSong(R.raw.bomb);
			ourSong.seekTo(0);
			ourSong.start();
			world.checkCollision();
			explode = false;
			animation = true;
		}
	}

	@Override
	public void render(Canvas c) {
		if (time<1){
			c.drawCircle(pos.x, pos.y, size, p);
		}
		else{
			//c.drawCircle(pos.x + size, pos.y + size, size, p);
			c.drawBitmap(bmp, new Rect(0,0,bmp.getWidth(),bmp.getHeight()), getColBounds(), p);
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
