package edu.virginia.cs2110.ghosthunter17;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Player extends GameObject {

	private static final float V = 200;

	private PointF target;
	private Paint p;

	public Player(World world, PointF pos) {
		super(world, pos);
		p = new Paint();
		p.setColor(0xffff0000);
		target = null;
	}

	@Override
	public void update(float timePassed) {
		if (target != null) {
			PointF diff = new PointF(target.x - pos.x, target.y - pos.y);
			float len = diff.length();
			diff.x *= V/len;
			diff.y *= V/len;
			pos.x += diff.x * timePassed;
			pos.y += diff.y * timePassed;
		}
	}

	@Override
	public void render(Canvas c) {
		c.drawRect(pos.x-50, pos.y-50, pos.x + 50, pos.y + 50, p);

	}

	public void setTarget(PointF target) {
		this.target = target;
	}

}
