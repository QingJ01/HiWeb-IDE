package com.venter.easyweb;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.webkit.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import android.widget.*;
import com.venter.easyweb.edit.*;
import android.graphics.drawable.*;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.venter.easyweb.add.addViewWidget.*;

public class HopWebWebView extends WebView
{
    public String STitle=null;
    public Bitmap BIcon=null;
    public String SUrl=null;
	public HopWebWebView(Context context)
	{
		super(context);
		init();
	}

    public HopWebWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

    public HopWebWebView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return false;
	}
	public WebChromeClient wcc=new WebChromeClient()
	{
		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			super.onReceivedTitle(view, title);
			if (title.trim().equals("about:blank"))
			{
				title = null;
			}
			STitle = title;
		}
		@Override
		public void onReceivedIcon(WebView view, Bitmap B)
		{
			super.onReceivedIcon(view, B);
			BIcon = B;
		}
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage)
		{
			Spannable text = null;

			if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.DEBUG)
			{
				String title="Debug(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());		
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR)
			{
				String title="Error(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());		
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.LOG)
			{
				String title="Log(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());		
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.TIP)
			{
				String title="Tip(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());		
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.WARNING)
			{
				String title="Warning(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());		
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}

			showFloat(text);
			return MainActivity.main.binding.termux.wcc.onConsoleMessage(consoleMessage);
		}
		@Override
		public boolean onJsAlert(WebView webView, String url, String message, JsResult result)
		{
			String title="Alert:";
			Spannable text=new SpannableString(title + message);		
			text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			showFloat(text);
			return MainActivity.main.binding.termux.wcc.onJsAlert(webView, url, message, result);
		}
		@Override
		public boolean onJsConfirm(WebView view, String url, String message, final JsResult result)
		{
			ScrollView Sv=new ScrollView(view.getContext());
			Sv.setFillViewport(true);
			RelativeLayout Rl=new RelativeLayout(view.getContext());
			Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
			LinearLayout Ly=new LinearLayout(view.getContext());
			Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
			Ly.setOrientation(LinearLayout.VERTICAL);
			Ly.setPadding(Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10));
			Sv.addView(Rl);
			Rl.addView(Ly);

			TextView tvOrigin=new TextView(view.getContext());
			TextView tvMsg=new TextView(view.getContext());
			ObjectLayout olOrigin=new ObjectLayout(view.getContext());
			ObjectLayout olMsg=new ObjectLayout(view.getContext());

			Do.CanSelect(tvOrigin);
			String SUrl=view.getUrl();
			if ((SUrl == null || SUrl == null) || (SUrl.equals("about:blank") || SUrl.equals("about:blank")))
			{
				tvOrigin.setText("file://" + Do.URLEncode(Vers.i.OpenFile.getPath()));
			}
			else
			{
				tvOrigin.setText(SUrl);
			}

			olOrigin.build(-1, null, R.string.origin, tvOrigin);
			Ly.addView(olOrigin);

			Do.CanSelect(tvMsg);
			tvMsg.setText(message);
			olMsg.build(-1, null, R.string.message, tvMsg);
			Ly.addView(olMsg);

			Dl alert= new Dl(view.getContext());
			alert.builder.setTitle("Confirm")
				.setView(Sv)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						result.confirm();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						result.cancel();
					}
				})
				.setCancelable(false);
			alert.show();
			return true;
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result)
		{

			ScrollView Sv=new ScrollView(view.getContext());
			Sv.setFillViewport(true);
			RelativeLayout Rl=new RelativeLayout(view.getContext());
			Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
			LinearLayout Ly=new LinearLayout(view.getContext());
			Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
			Ly.setOrientation(LinearLayout.VERTICAL);
			Ly.setPadding(Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10));
			Sv.addView(Rl);
			Rl.addView(Ly);

			TextView tvOrigin=new TextView(view.getContext());
			ObjectLayout olOrigin=new ObjectLayout(view.getContext());
			final TextLayout tl=new TextLayout(view.getContext());

			Do.CanSelect(tvOrigin);
			String SUrl=view.getUrl();
			if ((SUrl == null || SUrl == null) || (SUrl.equals("about:blank") || SUrl.equals("about:blank")))
			{
				tvOrigin.setText("file://" + Do.URLEncode(Vers.i.OpenFile.getPath()));
			}
			else
			{
				tvOrigin.setText(SUrl);
			}

			olOrigin.build(-1, null, R.string.origin, tvOrigin);
			Ly.addView(olOrigin);

			tl.build(-1, null, R.string.message, null);
			tl.Tv.setText(message);
			tl.Acet.setText(defaultValue);
			Ly.addView(tl);

			Dl alert= new Dl(view.getContext());
			alert.builder.setTitle("Prompt")
				.setView(Sv)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						result.confirm(tl.Acet.getText().toString());
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						result.cancel();
					}
				})
				.setCancelable(false);
			alert.show();
			return true;
		}
		public boolean onShowFileChooser(WebView webView,
										 ValueCallback<Uri[]> filePathCallback,
										 WebChromeClient.FileChooserParams fileChooserParams) {
			FileChooseClass Fcc=new FileChooseClass();
			Fcc.Type(true);
			Fcc.setOpenPath(Environment.getExternalStorageDirectory());
			Fcc.setOnFileClickListener(new OnFileClickListener(){
				@Override
				public void onClick(File ChooseFile, AlertDialog dialog) {
					dialog.dismiss();
					filePathCallback.onReceiveValue(new Uri[]{Uri.fromFile(ChooseFile)});
				}
			});
			Fcc.Show(getContext());
			Fcc.dialog.setOnCancelListener(p1->{
				filePathCallback.onReceiveValue(null);
			});
			return true;
		}
	};
	public WebViewClient wvc=new WebViewClient(){
		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl)
		{
			//用javascript隐藏系统定义的404页面信息
			super.onReceivedError(view, errorCode, description, failingUrl);
//			view.removeAllViews();
//			String data = "<body style='text-align:center;'><div id='div'>"+
//				"<img width='40px' src='data:image/png;base64,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'/>"
//				+"<br><div style='font-size:x-large;'>"+getContext().getString(R.string.cant_open_page)+"</div><br><span style='font-size:small;'>"+getContext().getString(R.string.description)+": "+description+"</span></div></body>";
//			view.loadUrl("javascript:document.innerHTML=\"" + data + "\"",null);
			showError(description);
		}

		@Override
		public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
		{
			super.onReceivedHttpError(view, request, errorResponse);
			if (request.isForMainFrame())
			{
//				view.removeAllViews();
//				String data = "<body style='text-align:center;'><div id='div'>"+
//					"<img width='40px' src='data:image/png;base64,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'/>"
//					+"<br><div style='font-size:x-large;'>"+getContext().getString(R.string.cant_open_page)+"</div><br><span style='font-size:small;'>"+getContext().getString(R.string.description)+errorResponse.getReasonPhrase()+"</span></div></body>";
//				view.loadUrl("javascript:document.innerHTML=\"" + data + "\"",null);
				showError(errorResponse.getReasonPhrase());
			}
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
		{
			super.onReceivedError(view, request, error);
			if (request.isForMainFrame())
			{
//				view.removeAllViews();
//				String data = "<body style='text-align:center;'><div id='div'>"+
//					"<img width='40px' src='data:image/png;base64,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'/>"
//					+"<br><div style='font-size:x-large;'>"+getContext().getString(R.string.cant_open_page)+"</div><br><span style='font-size:small;'>"+getContext().getString(R.string.description)+error.getDescription()+"</span></div></body>";
//				view.loadUrl("javascript:document.innerHTML=\"" + data + "\"",null);
				if(Build.VERSION.SDK_INT >=23)
				{
					showError(error.getDescription().toString());
				}
				else
				{
					showError(error.toString());
				}
			}
		}


		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			Vers.i.MainPreviewIsDone = false;
			if (MainActivity.main.MoreMode == 1)
				MainActivity.main.binding.tvMainMoreSubtitle.setText(R.string.main_More_loading);
			MainActivity.main.binding.lyPreviewErrorBowl.removeAllViews();
		}

		public void onPageFinished(WebView view, String url)
		{
			Vers.i.MainPreviewIsDone = true;
			if (MainActivity.main.MoreMode == 1)
				MainActivity.main.binding.tvMainMoreSubtitle.setText(R.string.main_More_done);
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String newurl)
		{
			try
			{
				//处理intent协议
				if (newurl.startsWith("intent://"))
				{
					Intent intent;
					try
					{
						intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
						intent.addCategory("android.intent.category.BROWSABLE");
						intent.setComponent(null);
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
						{
							intent.setSelector(null);
						}
						List<ResolveInfo> resolves = getContext().getPackageManager().queryIntentActivities(intent, 0);
						if (resolves.size() > 0)
						{
							MainActivity.main.startActivityIfNeeded(intent, -1);
						}
						return true;
					}
					catch (URISyntaxException e)
					{
						e.printStackTrace();
					}
				}
				// 处理自定义scheme协议
				if (!newurl.startsWith("http://") && !newurl.startsWith("https://") && !newurl.startsWith("file://") && !newurl.startsWith("ftp://") && !newurl.startsWith("ftps://") && !newurl.startsWith("sftp://") && !newurl.startsWith("javascript:") && !newurl.equals("about:blank"))
				{

					try
					{
						// 以下固定写法
						final Intent intent = new Intent(Intent.ACTION_VIEW,
														 Uri.parse(newurl));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
										| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						MainActivity.main.startActivity(intent);
					}
					catch (Exception e)
					{
						// 防止没有安装的情况
						e.printStackTrace();
						wcc.onConsoleMessage(new ConsoleMessage(getContext().getString(R.string.start_intent_debug), newurl, 0, ConsoleMessage.MessageLevel.ERROR));
					}
					return true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			STitle = null;
			BIcon = null;
			SUrl = newurl;
			return false;
		}
	};
	@SuppressLint("SetJavaScriptEnabled")
	private void init()
	{
		WebSettings webSettings=getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

		//支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示 WebView 缩放按钮
        webSettings.setDisplayZoomControls(false);

		webSettings.setUseWideViewPort(Vers.i.isPreviewerZoomPC);

        webSettings.setLoadsImagesAutomatically(true);

		webSettings.setAllowFileAccess(true);
		webSettings.setAllowContentAccess(true);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
		{
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}

		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);

		webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要

		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存

		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);

		setWebChromeClient(wcc);
		setWebViewClient(wvc);

		setDownloadListener((String url, String userAgent,String contentDisposition, String mimetype, long contentLength) -> {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			intent.setData(Uri.parse(url));
			getContext().startActivity(intent);
		});
	}
	public void showError(String err)
	{
		MainActivity.main.binding.lyPreviewErrorBowl.removeAllViews();
		ScrollView sv=new ScrollView(getContext());
		sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		sv.setFillViewport(true);
		RelativeLayout rl=new RelativeLayout(getContext());
		rl.setLayoutParams(new ScrollView.LayoutParams(-1, -1));
		sv.addView(rl);
		LinearLayout ly=new LinearLayout(getContext());
		ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
		ly.setGravity(Gravity.CENTER);
		ly.setOrientation(LinearLayout.VERTICAL);
		ly.setBackgroundColor(Color.WHITE);
		ly.setPadding(Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10));
		ly.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
				}
			});
		rl.addView(ly);
		ImageView iv=new ImageView(getContext());
		iv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(getContext(), 50), Do.dp2px(getContext(), 50)));
		Drawable draw=getContext().getDrawable(R.drawable.alert);
		draw.setTint(Color.GRAY);
		iv.setImageDrawable(draw);
		ly.addView(iv);
		TextView title=new TextView(getContext());
		title.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		title.setGravity(Gravity.CENTER);
		title.setTextSize(18);
		title.setTextColor(Color.BLACK);
		title.setText(R.string.cant_open_page);
		title.setPadding(0, Do.dp2px(getContext(), 10), 0, Do.dp2px(getContext(), 10));
		ly.addView(title);
		TextView msg=new TextView(getContext());
		msg.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		msg.setGravity(Gravity.CENTER);
		msg.setTextSize(10);
		msg.setTextColor(Color.BLACK);
		msg.setText(getContext().getString(R.string.description) + err);
		msg.setPadding(0, Do.dp2px(getContext(), 10), 0, Do.dp2px(getContext(), 10));
		ly.addView(msg);
		MainActivity.main.binding.lyPreviewErrorBowl.addView(sv);
	}
	public void showFloat(Spanned msg)
	{
		MainActivity.main.binding.lyPreviewAlert.setVisibility(View.VISIBLE);
		MainActivity.main.binding.tvPreviewAlert.setText(msg);
		MainActivity.main.binding.lyPreviewAlert.setOnClickListener(new View.OnClickListener(){

				private final long DOUBLE_TIME = 3000;
				private long lastClickTime = 0;

				@Override
				public void onClick(View v)
				{
					long currentTimeMillis = System.currentTimeMillis();
					if (currentTimeMillis - lastClickTime < DOUBLE_TIME)
					{
						//双击事件
						if (Vers.i.isFullPreview)
							MainActivity.main.unFullScreenPreview();
						MainActivity.main.ViewOutput();
						return;
					}
					lastClickTime = currentTimeMillis;

					MainActivity.main.toast(R.string.double_click_skip_to_termux);
				}

			});
	}
    public void clearData()
    {
        WebStorage.getInstance().deleteAllData();
        stopLoading();
        clearHistory();
        clearCache(true);
        destroy();
    }
}
