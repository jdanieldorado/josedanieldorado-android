package com.jdanieldorado.serviceexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jdanieldorado.serviceexample.service.ExampleService;

public class ServiceExampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_example);

		Button btOne = (Button) findViewById(R.id.btOne);
		btOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ServiceExampleActivity.this,
						ExampleService.class);
				intent.putExtra(ExampleService.EXTRA_KEEP_SERVICE_ALIVE,
						ExampleService.MSG_TYPE_KEEP_ALIVE);
				startService(intent);
			}
		});

		Button btTwo = (Button) findViewById(R.id.btTwo);
		btTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ServiceExampleActivity.this,
						ExampleService.class);
				intent.putExtra(ExampleService.EXTRA_KEEP_SERVICE_ALIVE,
						ExampleService.MSG_TYPE_DONT_KEEP_ALIVE);
				startService(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_service_example, menu);
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();

		ExampleService.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		ExampleService.stop();
	}

}
