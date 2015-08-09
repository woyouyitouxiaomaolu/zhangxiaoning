package org.cos.watch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private WatchView mWatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mWatch = (WatchView) findViewById(R.id.wv);
	}

	public void clickStart(View v) {
		mWatch.start();
	}

	public void clickStop(View v) {
		mWatch.stop();
	}

}
