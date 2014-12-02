package edu.virginia.cs2110.ghosthunter17;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private GameView game;
	private MediaPlayer bgm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bgm = MediaPlayer.create(this, R.raw.castletheme);
		bgm.setLooping(true);
		bgm.start();
		setContentView(R.layout.activity_main);
		game = (GameView) findViewById(R.id.gameView);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		game.onPause();
		bgm.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		game.onResume();
		bgm.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bgm.release();
		Log.v("MainActivity", "destroyed");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_pause) {
			game.onPause();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
