package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class GameObject {

	protected PointF pos;
	protected World world;
	protected RectF colBounds;
	
	public GameObject(World world) {
		this.pos = new PointF();
		this.world = world;
	}

	public GameObject(World world, float x, float y) {
		this.pos = new PointF(x, y);
		this.world = world;
	}

	public GameObject(World world, PointF pos) {
		this.world = world;
		this.pos = pos;
	}
	
	public RectF getRectF(){
		return colBounds;
	}
	
	public abstract void update(float timePassed);

	public abstract void render(Canvas c);
	
	public abstract void collide();
}
