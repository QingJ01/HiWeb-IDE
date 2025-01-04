package com.hiweb.ide;
import android.content.*;
import java.text.*;
import java.util.*;
import android.graphics.*;
import android.text.*;
import android.text.style.*;
import android.webkit.*;

public class JSRunWebView extends WebView
{
	Context ctx;
	public JSRunWebView(Context c)
	{
		super(c);
		ctx = c;
		init();
	}
	
	private void init()
	{
		WebSettings webSettings = getSettings();

		// 设置与Js交互的权限
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setAllowContentAccess(true);
		
		setWebChromeClient(MainActivity.main.binding.termux.wcc);
	}
}
