package com.stackbase.mobapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Android key = 000000
 * */
public class HomePage extends Activity {

	private ImageButton camera = null;
	private ImageButton manage = null;
	private ImageButton settings = null;

	private boolean isExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		camera = (ImageButton) findViewById(R.id.cameraBtn);
		manage = (ImageButton) findViewById(R.id.manageBtn);
		settings = (ImageButton) findViewById(R.id.settingsBtn);

		camera.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(HomePage.this, CollectActivity.class);
				startActivity(intent);
				// HomePage.this.finish();
			}

			
		});
		
		manage.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(HomePage.this, ManageActivity.class);
				startActivity(intent);
				// HomePage.this.finish();
			}
		});

		settings.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(HomePage.this, SettingsActivity.class);
				startActivity(intent);
				// HomePage.this.finish();
			}
		});
	}

	protected void toast() {
		// TODO Auto-generated method stub
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitByDoubleClick();
		}
		return false;
	}

	private void exitByDoubleClick() {
		Timer timer = null;
		if (isExit == false) {
			isExit = true; // Prepare to exit
			Toast.makeText(this, R.string.pressAgain, Toast.LENGTH_LONG).show();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
}
