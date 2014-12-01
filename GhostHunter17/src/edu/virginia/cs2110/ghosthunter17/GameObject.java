package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class GameObject {

	public enum Direction{NORTH,EAST,SOUTH,WEST}
	
	protected PointF pos;
	protected World world;

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

	public abstract RectF getColBounds();

	public abstract void update(float timePassed);

	public abstract void render(Canvas c);

	public abstract void collide(GameObject g, Direction dir);

	public void inLight(){}
	
	protected void moveTo(PointF target, float timePassed, float velocity) {
		PointF dist = new PointF(target.x - pos.x, target.y - pos.y);
		
		//Move towards target
		float len = dist.length();
		float scale = velocity / len * timePassed;
		PointF diff = new PointF(dist.x * scale, dist.y * scale);
		// Move towards the target if it is further than the player can
		// travel in a frame
		if (diff.length() < len) {
			pos.x += diff.x;
			pos.y += diff.y;
		}
		// Otherwise move to the target
		else {
			pos.x += dist.x;
			pos.y += dist.y;
		}
	}
	
	protected float rotateTo(float angleTo, float angleFrom, float timePassed, float velocity) {
		//Rotate towards target
		float frameRot = angleTo - angleFrom;
		if (Math.abs(angleTo + 360 - angleFrom) < Math.abs(frameRot)) {
			frameRot = angleTo + 360 - angleFrom;
		} else if (Math.abs(angleTo - 360 - angleFrom) < Math.abs(frameRot)) {
			frameRot = angleTo - 360 - angleFrom;
		}
		
		if (frameRot!=0){
			float normalFrameRot = frameRot / Math.abs(frameRot);
			normalFrameRot *= velocity * timePassed;

			if (Math.abs(frameRot) < Math.abs(normalFrameRot)) {
				angleFrom += frameRot;
			} else {
				angleFrom += normalFrameRot;
			}
			angleFrom%=360;
		}
		return angleFrom;
	}	
}
