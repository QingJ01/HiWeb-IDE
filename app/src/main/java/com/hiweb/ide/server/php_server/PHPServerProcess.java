package com.hiweb.ide.server.php_server;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.hiweb.ide.*;
import com.hiweb.ide.server.*;
import java.io.*;
import com.hiweb.ide.edit.*;

public class PHPServerProcess extends Service {

	private ServerNotification serverNotification;

	@Override
	public IBinder onBind(Intent p1) {
		// TODO: Implement this method
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getBooleanExtra("startPHPWebsite", false)) {
			final int port = intent.getIntExtra("startPHPWebsitePort", -1);
			File projectDir = new File(intent.getStringExtra("startPHPDir"));
			File php = new File(getFilesDir(), "php");
			File lib = new File(getFilesDir(), "lib");
			File profitFile = new File(getFilesDir(), "php_profit");
			File errorFile = new File(getFilesDir(), "php_error");

			if (profitFile.exists())
				return Service.START_REDELIVER_INTENT;

			errorFile.delete();
			profitFile.delete();

			try {
				profitFile.createNewFile();
				Do.write(port + "//////////" + projectDir.getPath() + "//////////" + android.os.Process.myPid(),
						profitFile);
				serverNotification = new ServerNotification(this, 1);
				startForeground(1, serverNotification.notification);
				Log.i("PHP Web Server Service", "PHP Web Server Service is entering.");
				new Thread(() -> {
					lib.setExecutable(true);
					php.setExecutable(true);
					for (File i : lib.listFiles()) {
						i.setExecutable(true);
					}

					final String msg;
					try {
						msg = Do.runCommand(new String[] { "/system/bin/sh", "-c",
								"export LD_LIBRARY_PATH=\"" + lib.getPath() + "\" && \"" + php.getPath()
										+ "\" -S 0.0.0.0:" + port + " -t \"" + projectDir.getPath() + "\"" });
						if (msg.startsWith("Error: ")) {
							errorFile.createNewFile();
							Do.write(msg, errorFile);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					stopForeground(true);
					profitFile.delete();
				}).start();
			} catch (final Exception e) {
				try {
					Do.write(e.getMessage(), errorFile);
				} catch (IOException ioe) {
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
		Log.i("PHP Web Server Service", "PHP Web Server Service is exiting.");
		serverNotification.cancle(1);
		File errorFile = new File(getFilesDir(), "php_error");
		File profitFile = new File(getFilesDir(), "php_profit");

		errorFile.delete();
		profitFile.delete();
		super.onDestroy();
	}
}
