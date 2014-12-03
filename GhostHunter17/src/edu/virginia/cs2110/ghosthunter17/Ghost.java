package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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

public class Ghost extends GameObject {
	public static int boundX, boundY;
	
	private static float LIGHT_DAMAGE = 2f;

	private PointF vel, size;
	private Paint p;
	private float radius;
	private float health;
	private boolean light;
	private Bitmap bmp;


	
	//ignore this comment
	public Ghost(World w, PointF pos, PointF vel, float radius) {
		super(w, pos);
		this.vel = vel;
		this.size = new PointF(2 * radius, 2 * radius);
		this.radius = radius;
		this.health = 1;
		this.light = false;
		bmp = ResourceManager.getImage(R.drawable.ghastly_sprite);
		
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
		
		if (light) {
			health -= LIGHT_DAMAGE * timePassed;
		}
		
		if (health <= 0) {
			die();
		}
		
		light = false;
	}

	@Override
	public void render(Canvas c) {
		// Draw a rectangle at the box's position
		p.setAlpha((int)(255*health));
//		c.drawCircle(pos.x + radius, pos.y + radius, radius, p);
		c.drawBitmap(bmp, new Rect(0,0,bmp.getWidth(), bmp.getHeight()), getColBounds(), p);
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		if (g instanceof Player){
			die();
		}
	}
	
	public void die(){
		world.removeObject(this);
		world.addKill();
		
		//15% chance to spawn a bomb on death of Ghost
		int rand = (int)(Math.random()*100);
		if (rand<=15){
			world.spawnBomb(pos);
		}
	}

	@Override
	public RectF getColBounds() {
		return new RectF(pos.x, pos.y, pos.x+size.x, pos.y+size.y);
	}
	
	@Override
	public void inLight() {
		light = true;
	}

}
