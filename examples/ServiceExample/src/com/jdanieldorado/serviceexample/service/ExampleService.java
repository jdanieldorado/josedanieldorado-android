package com.jdanieldorado.serviceexample.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class ExampleService extends Service {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG = ExampleService.class.getSimpleName();

	public static final String SERVICE_THREAD_NAME = "ExampleServiceThread";

	public static final String EXTRA_KEEP_SERVICE_ALIVE = "keepAlive";

	public static final int MSG_TYPE_DONT_KEEP_ALIVE = 0;
	public static final int MSG_TYPE_KEEP_ALIVE = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;

	private static Service thisService;

	// ===========================================================
	// Constructors & Initialization
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread(SERVICE_THREAD_NAME,
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Static reference to this service. To Stop it when we leave the app.
		thisService = this;

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "[+] Service Starting...");

		// Indicates if the service keeps alive after handling the message.
		int keepAlive = intent.getIntExtra(EXTRA_KEEP_SERVICE_ALIVE, 0);

		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job

		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = keepAlive;

		msg.what = keepAlive; // Message type

		// Remove previous messages with current type.
		mServiceHandler.removeMessages(msg.what);

		// Send a new message with a little delay.
		mServiceHandler.sendMessageDelayed(msg, 2 * 1000);

		// If we get killed, after returning from here, dont restart
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
		// Destroy the service.
		Log.d(TAG, "[-] Service destroyed");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static void stop() {
		// Stops the service.
		if (thisService != null) {
			thisService.stopSelf();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "[>] [Service] What: " + msg.what + " , StartId: "
					+ msg.arg1);

			if (msg.arg2 == MSG_TYPE_DONT_KEEP_ALIVE) {
				// Stop the service using the startId, so that we don't stop
				// the service in the middle of handling another job
				stopSelf(msg.arg1);
			}
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

}
