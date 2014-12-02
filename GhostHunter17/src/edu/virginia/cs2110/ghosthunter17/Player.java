package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class Player extends GameObject {

	private static final float V = 500; // Pixels per second
	private static final float ROT_V = 180; // Degrees per second
	private static final float SIZE = 150; // Pixels

	private PointF target;
	private float rot;
	private Paint p;
	private int lives;
	private int numBombs;
	
	public Player(World world, PointF pos, int difficulty) {
		super(world, pos);
		p = new Paint();
		p.setColor(0xffff0000);
		target = null;
		lives = 3-difficulty;
		numBombs = 0;
	}

	@Override
	public void update(float timePassed) {
		if (target != null) {
			if (!target.equals(pos)) {
				rot = rotateTo(
						(float) (Math.atan2(target.y - pos.y, target.x - pos.x) * 180 / Math.PI),
						rot, timePassed, ROT_V);
			}
			moveTo(target, timePassed, V);
		}
	}

	@Override
	public void render(Canvas c) {
		if (lives == 3) {
			p.setColor(Color.GREEN);
		}

		else if (lives == 2) {
			p.setColor(Color.YELLOW);
		}

		else {
			p.setColor(Color.BLACK);
		}
		c.save();

		c.rotate(rot, pos.x, pos.y);
		c.drawRect(pos.x - SIZE / 2, pos.y - SIZE / 2, pos.x + SIZE / 2, pos.y
				+ SIZE / 2, p);
		c.restore();
	}

	public void setTarget(PointF target) {
		this.target = target;
	}

	@Override
	public void collide(GameObject g, Direction dir) {
		if (g instanceof Ghost) {
			

			lives--;
		}
	}

	@Override
	public RectF getColBounds() {
		return new RectF(this.pos.x - SIZE / 2, this.pos.y - SIZE / 2,
				(this.pos.x + SIZE / 2), (this.pos.y + SIZE / 2));
	}
	
	public float getRot() {
		return rot;
	}
	
	public void addBomb(){
		numBombs++;
		Log.d("WORLD", "Bombs: " +numBombs);
	}
	
	public int getNumBombs(){
		return numBombs;
	}
	
	public void useBomb(){
		if (numBombs > 0){
			numBombs--;
			world.addObject(new BombEffect(world,new PointF(pos.x,pos.y)));
		}
		else{
			Log.d("WORLD", "No bombs left");
		}
	}

}
