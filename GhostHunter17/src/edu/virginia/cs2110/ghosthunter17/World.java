package edu.virginia.cs2110.ghosthunter17;

import java.util.ArrayList;
import java.util.Collection;

import edu.virginia.cs2110.ghosthunter17.GameObject.Direction;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class World {
	private static final float LIGHT_WIDTH = 120; // Degrees
	private static final int SHADOW_COLOR = 0xff000000;
	private static final float VIEW_CIRCLE = 200;

	private ArrayList<GameObject> gameObjects;
	private ArrayList<GameObject> addQueue;
	private ArrayList<GameObject> removeQueue;

	private Player p;
	private int kills;
	private boolean paused;
	private Paint pausePaint;
	private Paint screenText;
	private float ghostSpawnTimer;
	private float ghostSpawnReset;
	private float batterySpawnTimer;
	private float batteryLife;
	private boolean light;
	
	private Paint gameOverPaint;
	private boolean gameOver;

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
		pausePaint.setTextSize(200);
		pausePaint.setTextAlign(Align.CENTER);
		
		gameOver = false;
		gameOverPaint = new Paint();
		gameOverPaint.setTextSize(150);
		gameOverPaint.setTextAlign(Align.CENTER);
		
		screenText = new Paint();
		screenText.setTextSize(50);
		screenText.setTextAlign(Align.CENTER);
		screenText.setColor(Color.WHITE);
		screenText.setShadowLayer(5, 0, 0, Color.BLACK);
		
		
		light = true;

		ghostSpawnReset = 5;
		ghostSpawnTimer = 5;
		batterySpawnTimer = 25;
		batteryLife = 30;
		kills = 0;
		p = new Player(this, new PointF(500, 500), MainMenuActivity.difficulty);
		this.gameObjects.add(p);

		//Walls
		this.gameObjects.add(new Wall(this, new RectF(390, 0, 410, 600)));
		this.gameObjects.add(new Wall(this, new RectF(0, 590, 100, 610)));
		this.gameObjects.add(new Wall(this, new RectF(250, 590, 410, 610)));
		this.gameObjects.add(new Wall(this, new RectF(410, 590, 510, 610)));
		this.gameObjects.add(new Wall(this, new RectF(670, 590, 800, 610)));	
	
		//Remove this line later
		testCode();

	}

	public void update(float timePassed) {

		if (!paused && !gameOver) {
			
			// Add/remove objects added/removed outside update
			gameObjects.addAll(addQueue);
			addQueue.clear();
			gameObjects.removeAll(removeQueue);
			removeQueue.clear();
			ghostSpawnTimer -= timePassed;
			batterySpawnTimer -= timePassed;
			
			if (light){
				batteryLife -= timePassed;
			}
			
			if (ghostSpawnTimer <= 0) {
				spawnGhost();
				ghostSpawnTimer = ghostSpawnReset;
				ghostSpawnReset -= ghostSpawnReset/50;
				if (ghostSpawnReset < .5) {
					ghostSpawnReset = .5f;
				}
			}
			
			if (batterySpawnTimer <= 0) {
				spawnBattery();
				batterySpawnTimer = 25;
			}
			
			// Update GameObjects
			for (GameObject g : gameObjects) {
				g.update(timePassed);
			}

			checkCollision();
			
			if (p.getLives() <=0){
				gameOver = true;
			}
		}
	}

	public void checkCollision() {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject g1 = gameObjects.get(i);
			for (int j = i + 1; j < gameObjects.size(); j++) {
				GameObject g2 = gameObjects.get(j);
				RectF r1 = g1.getColBounds();
				RectF r2 = g2.getColBounds();
				if (r1.intersect(r2)) {
					float top = Math.abs(r2.bottom - r1.top);
					float bot = Math.abs(r2.top - r1.bottom);
					float left = Math.abs(r2.right - r1.left);
					float right = Math.abs(r2.left - r1.right);

					Direction d1 = Direction.NORTH, d2 = Direction.SOUTH;
					float min = top;

					if (bot < min) {
						min = bot;
						d1 = Direction.SOUTH;
						d2 = Direction.NORTH;
					}
					if (left < min) {
						min = left;
						d1 = Direction.WEST;
						d2 = Direction.EAST;
					}
					if (right < min) {
						d1 = Direction.EAST;
						d2 = Direction.WEST;
					}

					g1.collide(g2, d1);
					g2.collide(g1, d2);
				}
			}
		}
	}

	public void render(Canvas c) {	
		
		// Render GameObjects
		for (GameObject g : gameObjects) {
			g.render(c);
		}
		if (light && batteryLife>0) {
		doLighting(c);
		}
		else {
			Path path = new Path();
			path.addOval(new RectF(p.pos.x-VIEW_CIRCLE, p.pos.y-VIEW_CIRCLE, p.pos.x+VIEW_CIRCLE, p.pos.y+VIEW_CIRCLE), Path.Direction.CCW);
			path.toggleInverseFillType();
			c.save();
			c.clipPath(path);
			c.drawColor(SHADOW_COLOR);
			c.restore();
		}
			
		showKills(c);
		showBombs(c);
		
		if (paused) {
			pausePaint.setColor(0x88111111);
			c.drawRect(0, 0, c.getWidth(), c.getHeight(), pausePaint);
			pausePaint.setColor(0xFFFFFFFF);
			c.drawText("Paused", c.getWidth() / 2, c.getHeight() / 2,
					pausePaint);
		}
		
		if (gameOver){
			gameOverPaint.setColor(0x88111111);
			c.drawRect(0, 0, c.getWidth(), c.getHeight(), gameOverPaint);
			gameOverPaint.setColor(0xFFFFFFFF);
			c.drawText("Game Over", c.getWidth() / 2, c.getHeight() / 2,
					gameOverPaint);
			
		}
	}

	private void doLighting(Canvas c) {
		ArrayList<Segment> walls = new ArrayList<Segment>();
		for (GameObject go : gameObjects) {
			if (go instanceof Wall) {
				walls.addAll(((Wall) go).getEdges());
			}
		}
		RectF bounds = new RectF(0, 0, c.getWidth(), c.getHeight());
		walls.addAll(new Wall(null,	bounds).getEdges());
		
		ArrayList<Segment> edges = new ArrayList<Segment>();
		edges.addAll(new Wall(null,	bounds).getEdges());
		
		ArrayList<PointF> lightPts = new ArrayList<PointF>();
		
		lightPts.add(new PointF(p.pos.x + 1000*(float) Math.cos((p.getRot() + LIGHT_WIDTH/2) * Math.PI / 180),
				p.pos.y + 1000*(float) Math.sin((p.getRot() + LIGHT_WIDTH/2) * Math.PI	/ 180)));
		lightPts.add(new PointF(p.pos.x + 1000*(float) Math.cos((p.getRot() - LIGHT_WIDTH/2) * Math.PI / 180),
				p.pos.y	+ 1000*(float) Math.sin((p.getRot() - LIGHT_WIDTH/2) * Math.PI / 180)));
		
		lightPts.set(0,Segment.raycast(new Segment(p.pos, lightPts.get(0)), edges));
		lightPts.set(1,Segment.raycast(new Segment(p.pos, lightPts.get(1)), edges));
		
		PointF v1 = new PointF(lightPts.get(0).x - p.pos.x, lightPts.get(0).y - p.pos.y);
		PointF v2 = new PointF(p.pos.x - lightPts.get(1).x, p.pos.y - lightPts.get(1).y);
		for (int i = 0; i < edges.size(); i++) { 
			float cross1 = Segment.cross(v1, new PointF(edges.get(i).p1.x - p.pos.x, edges.get(i).p1.y - p.pos.y));
			float cross2 = Segment.cross(v2, new PointF(edges.get(i).p1.x - p.pos.x, edges.get(i).p1.y - p.pos.y));
			if ((cross1 < 0 && cross2 < 0)) {
				lightPts.add((i==3&&lightPts.contains(edges.get(0).p1))?lightPts.size()-1:1 ,edges.get(i).p1);
			}
		}
		
		Path path = new Path();
		path.moveTo(p.pos.x, p.pos.y);
		for (int i = 0; i < lightPts.size(); i++) {
			path.lineTo(lightPts.get(i).x, lightPts.get(i).y);
		}
		path.close();
		path.toggleInverseFillType();
		
		c.save();
		c.clipPath(path);
		c.drawColor(SHADOW_COLOR);
		c.restore();
		path.toggleInverseFillType();
		
		ArrayList<GameObject> inLight = new ArrayList<GameObject>();
		Region r = new Region();
		Region screen = new Region((int)bounds.left, (int)bounds.top, (int)bounds.right, (int)bounds.bottom);
		r.setPath(path, screen);
		for (GameObject g : gameObjects) { 
			if (r.contains((int)g.pos.x, (int)g.pos.y)) {
				inLight.add(g);
			}
		}
		
		
		for (int i = 0; i < walls.size(); i++) {
			lightPts.clear();
			path.rewind();
			
			lightPts.add(walls.get(i).p1);
			lightPts.add(walls.get(i).p2);
			lightPts.add(Segment.raycast(new Segment(p.pos, lightPts.get(1)), edges));
			lightPts.add(Segment.raycast(new Segment(p.pos, lightPts.get(0)), edges));
			
			v1 = new PointF(p.pos.x - lightPts.get(2).x, p.pos.y - lightPts.get(2).y);
			v2 = new PointF(lightPts.get(3).x - p.pos.x, lightPts.get(3).y - p.pos.y);
			if (Segment.cross(v1, v2) > 0) {
				for (int j = 0; j < edges.size(); j++) { 
					float cross1 = Segment.cross(v1, new PointF(edges.get(j).p1.x - p.pos.x, edges.get(j).p1.y - p.pos.y));
					float cross2 = Segment.cross(v2, new PointF(edges.get(j).p1.x - p.pos.x, edges.get(j).p1.y - p.pos.y));
					if ((cross1 > 0 && cross2 > 0)) {
						lightPts.add((i==3&&lightPts.contains(edges.get(0).p1))?lightPts.size()-1:3 ,edges.get(j).p1);
					}
				}
			}
			else { 
				for (int j = 0; j < edges.size(); j++) { 
					float cross1 = Segment.cross(v1, new PointF(edges.get(j).p1.x - p.pos.x, edges.get(j).p1.y - p.pos.y));
					float cross2 = Segment.cross(v2, new PointF(edges.get(j).p1.x - p.pos.x, edges.get(j).p1.y - p.pos.y));
					if ((cross1 < 0 && cross2 < 0)) {
						lightPts.add(lightPts.size()-1 ,edges.get(j).p1);
					}
				}
			}
			
			path.moveTo(lightPts.get(0).x, lightPts.get(0).y);
			for (int j = 1; j < lightPts.size(); j++) {
				path.lineTo(lightPts.get(j).x, lightPts.get(j).y);
			}
			path.close();
		
			c.save();
			c.clipPath(path);
			c.drawColor(SHADOW_COLOR);
			c.restore();
			
			r.setPath(path, screen);
			for (int g = inLight.size()-1; g >= 0; g--) {
				if (r.contains((int)inLight.get(g).pos.x, (int)inLight.get(g).pos.y)) {
					inLight.remove(g);
				}
			}
		}
		
		for (GameObject g : inLight) {
			g.inLight();
		}
		

	}

	public void addObject(GameObject g) {
		addQueue.add(g);
	}

	public void removeObject(GameObject g) {
		removeQueue.add(g);
	}

	public void pause() {
		if (!gameOver){
			paused = true;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE
				|| event.getAction() == MotionEvent.ACTION_DOWN) {
			if (paused) {
				paused = false;
				return true;
			}
			int index = event.getActionIndex();
			PointerCoords coord = new PointerCoords();
			event.getPointerCoords(index, coord);
			p.setTarget(new PointF(coord.x, coord.y));
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			p.setTarget(null);
			return true;
		} else
			return false;
	}
	
	public void addKill(){
		kills++;
		Log.d("WORLD", "Kills: " +kills);
	}
	
	public void showKills(Canvas c){
		String text = "Kills: " + kills;
		c.drawText(text, c.getWidth()-300, 50, screenText);
	}
	
	public void showBombs(Canvas c){
		String text = "Bombs: " + p.getNumBombs();
		c.drawText(text, 150, 50, screenText);
	}
	
	public void spawnGhost(){
		RectF safeZone = new RectF(p.pos.x -Player.SIZE, p.pos.y -Player.SIZE, p.pos.x + Player.SIZE, p.pos.y + Player.SIZE);
		Ghost g;
		do{
			int x = (int)(Math.random()*1200);
			int y = (int)(Math.random()*1100);
			double theta = Math.random()*Math.PI*2;
			g = new Ghost(this, new PointF(x, y), new PointF(100 * (float)Math.cos(theta),
					100 * (float)Math.sin(theta)), 50);
		}
		while(g.getColBounds().intersect(safeZone));
		addObject(g);
	}
	
	public void spawnBomb(PointF pos){
		addObject(new Bomb(this,pos));
	}
	
	public void spawnBattery(){
		int x = (int)(Math.random()*1200);
		int y = (int)(Math.random()*1100);
		addObject(new Battery(this, new PointF(x, y)));
	}

	public void dropBomb() {
		p.useBomb();	
	}
	
	public void testCode(){
		dropBomb();
	}

	public void toggleLight() {
		light = !light;
		
	}
	
	public void rechargeBattery(){
		batteryLife = 30;
	}
	
}
