package edu.virginia.cs2110.ghosthunter17;

import java.util.ArrayList;
import java.util.Collection;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class World {

	private ArrayList<GameObject> gameObjects;
	private ArrayList<GameObject> addQueue;
	private ArrayList<GameObject> removeQueue;

	private Player p;
	private boolean paused;
	private Paint pausePaint;

	public World() {
		this(new ArrayList<GameObject>());
	}

	public World(Collection<GameObject> gameObjects) {

		this.gameObjects = new ArrayList<GameObject>();
		gameObjects.addAll(gameObjects);

		this.addQueue = new ArrayList<GameObject>();

		this.removeQueue = new ArrayList<GameObject>();

		paused = false;
		pausePaint = new Paint();
		pausePaint.setColor(0x10111111);

		p = new Player(this, new PointF(200, 200));
		this.gameObjects.add(p);

		this.gameObjects.add(new Circle(this, new PointF(0, 0), new PointF(100,
				100), 50));
	}

	public void update(float timePassed) {
		if (!paused){
			// Add/remove objects added/removed outside update
			gameObjects.addAll(addQueue);
			addQueue.clear();
			gameObjects.removeAll(removeQueue);
			removeQueue.clear();
	
			// Update GameObjects
			for (GameObject g : gameObjects) {
				g.update(timePassed);
			}
			
			checkCollision();
	
			// Add/remove objects added/removed during update loop
			gameObjects.addAll(addQueue);
			gameObjects.removeAll(removeQueue);
		
		}
	}

	public void checkCollision() {
		for (GameObject g : gameObjects) {
			for (GameObject gTarget : gameObjects) {
				if (g != gTarget && g.getRect().intersect(gTarget.getRect())) {
					if (g instanceof Player && gTarget instanceof Circle) {
						g.collide();
						removeQueue.add(gTarget);
						// gTarget.collide();
					}
				}
			}
		}

	}

	public void render(Canvas c) {

		// Render GameObjects
		for (GameObject g : gameObjects) {
			g.render(c);
		}
		
		if (paused) {
			pausePaint.setColor(0x88111111);
			c.drawRect(0, 0, c.getWidth(), c.getHeight(), pausePaint);
			pausePaint.setColor(0xFFFFFFFF);
			pausePaint.setTextSize(200);
			pausePaint.setTextAlign(Align.CENTER);
			c.drawText("Paused", c.getWidth()/2, c.getHeight()/2, pausePaint);
		}
	}

	public void addObject(GameObject g) {
		addQueue.add(g);
	}

	public void removeObject(GameObject g) {
		removeQueue.remove(g);
	}

	public void pause() {
		paused = true;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE
				|| event.getAction() == MotionEvent.ACTION_DOWN) {
			if (paused){
				paused = false;
				return true;
			}
			int index = event.getActionIndex();
			PointerCoords coord = new PointerCoords();
			event.getPointerCoords(index, coord);
			p.setTarget(new PointF(coord.x, coord.y));
			// addObject(new Box(this, new PointF(coord.x, coord.y), new PointF(
			// 500 * (float) Math.random() - 250,
			// 500 * (float) Math.random() - 250)));
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			p.setTarget(null);
			return true;
		} else
			return false;
	}

}
