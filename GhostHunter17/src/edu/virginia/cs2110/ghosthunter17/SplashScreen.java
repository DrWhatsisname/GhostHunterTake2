package edu.virginia.cs2110.ghosthunter17;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class SplashScreen extends Activity {
	
	private static final long SPLASH_TIME = 10000;

//	private Thread mSplashThread;
	private Handler handler;
	private Runnable launchMain;
	
	MediaPlayer ourSong;
	
	public SplashScreen() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);
		ourSong = MediaPlayer.create(this, R.raw.gameboy);
		ourSong.start();
		
		handler = new Handler();
		launchMain = new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashScreen.this, MainMenuActivity.class);
				startActivity(intent);
				ourSong.release();
				finish();
			}
		};
		
		handler.postDelayed(launchMain, SPLASH_TIME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(launchMain);
		launchMain.run();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			handler.removeCallbacks(launchMain);
			launchMain.run();
			return true;
		}
		else
			return super.onTouchEvent(evt);
	}
}
