package edu.virginia.cs2110.ghosthunter17;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

/*
 * T103-17
 * Henry Conklin, hwc5gj
 * Wylie Wang, ww5ts	
 * Cornelius Nelson, cn3dh	
 * Ryan Montgomery, rmm4wf
 */
public class MainMenuActivity extends Activity {
	
	public static int difficulty = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_menu);	
		
	}
	
	public void onNewGame(View v){
		difficulty = ((SeekBar)findViewById(R.id.seekBar1)).getProgress();
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
	}
}
