package com.hiweb.ide;

import android.app.*;
import android.os.*;
import android.content.*;
import android.net.*;
import java.io.*;

public class FileOpenActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String action = intent.getAction();
		if (intent.ACTION_VIEW.equals(action)) {
			Uri uri = intent.getData();
			String file = Uri.decode(uri.getEncodedPath());

			File openFile = new File(file);
			if (openFile.exists()) {
				Intent i = new Intent();
				i.setClass(this, PreActivity.class);
				i.putExtra("openFile", file);
				startActivity(i);
			}
		}
		finish();
	}

}
