package com.hiweb.ide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hiweb.ide.edit.Do;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WelcomeWebView extends WebView {
	public WelcomeWebView(android.content.Context context) {
		super(context);
		init();
	}

	public WelcomeWebView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WelcomeWebView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public WelcomeWebView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	WebViewClient wvc = new WebViewClient() {
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			hide();
		}

		public void onPageFinished(WebView view, String url) {
			setVisibility(View.VISIBLE);
		}
	};

	private void init() {
		WebSettings webSettings = getSettings();
		webSettings.setJavaScriptEnabled(true);// 允许使用js
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		/**
		 * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
		 * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
		 * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
		 * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
		 */
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存，只从网络获取数据.

		// 支持屏幕缩放
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setUseWideViewPort(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);
		// 不显示webview缩放按钮
		webSettings.setDisplayZoomControls(false);

		webSettings.setLoadsImagesAutomatically(true);

		webSettings.setAllowFileAccess(true);
		webSettings.setAllowContentAccess(true);

		setWebViewClient(wvc);
		setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(android.webkit.WebView view, java.lang.String url, final java.lang.String message,
					android.webkit.JsResult result) {
				Dl dl = new Dl(getContext());
				dl.builder.setMessage(message);
				dl.show();
				result.confirm();
				return true;
			}
		});
		setLongClickable(true);
		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		addJavascriptInterface(new EasyWeb(), "EasyWeb");
	}

	public void loadWhenShowWelcome() {
		if ((getQsUrl() == null && !WelcomeClean.isHide()) || getVisibility() == View.VISIBLE) {
			load();
		}
	}

	public void load() {
		if (getQsUrl() == null) {
			setVisibility(View.GONE);
			WelcomeClean.update();
			MainActivity.main.binding.toolbar.setNavigationIcon(null);
			MainActivity.main.binding.toolbar.setSubtitle(null);
			return;
		}
		loadUrl(getQsUrl());
		setVisibility(View.VISIBLE);
		WelcomeClean.hide();
		MainActivity.main.showUser();
	}

	public String getQsUrl() {
		if (SettingsClass.vQsWebsite == null) {
			return null;
		} else {
			return "file://" + SettingsClass.vQsWebsite.getPath() + "/index.html";
		}
	}

	public void hide() {
		setVisibility(View.GONE);
	}

	public JsonArray getRecent() {
		try {
			JsonArray JaProjects = new JsonArray();
			File FTemp = new File(MainActivity.main.getFilesDir(), "htx.json");
			if (!FTemp.exists())
				throw new Exception();
			String code = Do.getText(FTemp);

			JsonArray Ja = (JsonArray) new JsonParser().parse(code);

			for (int i = 0; i < Ja.size(); i++) {
				JaProjects.add(((JsonObject) Ja.get(i)).get("name").getAsString());
			}

			return JaProjects;
		} catch (Exception e) {
			return null;
		}
	}

	public void delRecent(int index) {
		try {

			File FTemp = new File(MainActivity.main.getFilesDir(), "htx.json");
			if (!FTemp.exists())
				throw new Exception();
			String code = Do.getText(FTemp);
			JsonArray Ja = (JsonArray) new JsonParser().parse(code);
			Ja.remove(index);

			Do.write(Ja.toString(), FTemp);
		} catch (Exception e) {

		}
	}

	public JsonArray getRecentArray() {
		try {

			File FTemp = new File(MainActivity.main.getFilesDir(), "htx.json");
			if (!FTemp.exists())
				throw new Exception();
			String code = Do.getText(FTemp);

			JsonArray Ja = (JsonArray) new JsonParser().parse(code);

			return Ja;
		} catch (Exception e) {
			return null;
		}
	}

	public void openRecent(final int index) {
		try {
			File FTemp = new File(MainActivity.main.getFilesDir(), "htx.json");
			if (!FTemp.exists())
				throw new Exception();
			String code = Do.getText(FTemp);

			final JsonArray Ja = (JsonArray) new JsonParser().parse(code);

			final File website = new File(((JsonObject) Ja.get(index)).get("path").getAsString());

			if (!website.exists()) {
				MainActivity.main.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MainActivity.main.toast(R.string.open_website_error);
					}
				});
				return;
			}

			MainActivity.main.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					MainActivity.main.OpenWeb(website, true);
					loadWhenShowWelcome();
				}
			});

		} catch (final Exception e) {
			MainActivity.main.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Do.showErrDialog(MainActivity.main, e);
				}
			});
		}
	}

	public void showLoginDialog() {
		UserLoginer.showLogin("");
	}

	public void startRecover() {
		final JsonObject vJaObject = (JsonObject) Vers.i.vOaBackup[1];
		final boolean isOpenedWebsite = vJaObject.get("isopenedwebsite").getAsBoolean();

		final JsonArray vJaOpenedFiles = (JsonArray) vJaObject.get("files");
		MainActivity.main.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (isOpenedWebsite) {
						File dir = new File(vJaObject.get("websitepath").getAsString());
						MainActivity.main.OpenWeb(dir, true);
					}
				} catch (Exception e) {

				}
				try {
					for (int i = 0; i < vJaOpenedFiles.size(); i++) {
						JsonObject nowObject = (JsonObject) vJaOpenedFiles.get(i);
						boolean isStar = nowObject.get("isstar").getAsBoolean();
						File file = new File(nowObject.get("path").getAsString());

						if (isStar) {
							String text = nowObject.get("edittext").getAsString();

							if (!MainActivity.main.binding.Ep.MSID.isEmpty()) {
								int IOpenSID = Do.getMFileKey(
										(HashMap<Integer, File>) MainActivity.main.binding.Ep.MAllFiles, file);
								if (!(IOpenSID != -1 && MainActivity.main.binding.Ep.MSID.containsKey(IOpenSID))) {
									MainActivity.main.prepareOpenFile(file, text, true);
									MainActivity.main.getNowEpi().star();
								}
							} else {
								MainActivity.main.prepareOpenFile(file, text, true);
								MainActivity.main.getNowEpi().star();
							}
						} else {
							MainActivity.main.OpenFile(file);
						}
					}
				} catch (final Exception e) {
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.showErrDialog(MainActivity.main, e);
						}
					});
				}
			}
		});
	}

	public class EasyWeb {
		@JavascriptInterface
		public void createWebsite() {
			MainActivity.main.createProject();
		}

		@JavascriptInterface
		public void createFileType() {
			MainActivity.main.createNewFile();
		}

		@JavascriptInterface
		public void openURL(String url) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			MainActivity.main.startActivity(intent);
		}

		@JavascriptInterface
		public void openHandlerManagerHelp() {
			try {
				Do.showLocalServerHelp();
			} catch (IOException e) {
			}
		}

		@JavascriptInterface
		public void openEasyWebServerHelp() {
			Do.showImgDialog(getContext(), R.string.server_import_help_title, R.string.server_import_help_msg,
					R.drawable.server_help);
		}

		@JavascriptInterface
		public void openJsresHelp() {
			SettingDoor.helpJsRes();
		}

		@JavascriptInterface
		public int getTheme() {
			return SettingsClass.ITheme;
		}

		@JavascriptInterface
		public void createTempWebsite() {
			MainActivity.main.CreateTempProject();
		}

		@JavascriptInterface
		public String getRecentProjects() {
			JsonArray ja = getRecent();
			if (ja == null) {
				return "null";
			} else {
				return ja.toString();
			}
		}

		@JavascriptInterface
		public void openRecentWebsite(final int index) {
			openRecent(index);
		}

		@JavascriptInterface
		public void setExp() {
			final String url = "https://gloriouspast.gitee.io/server/hiweb/example/newest.html";
			MainActivity.main.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					final WebView vWv = new WebView(getContext());
					vWv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
					vWv.getSettings().setJavaScriptEnabled(true);
					vWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
					vWv.setWebChromeClient(new WebChromeClient() {
						@Override
						public boolean onJsAlert(android.webkit.WebView view, java.lang.String url,
								final java.lang.String message, android.webkit.JsResult result) {
							WelcomeWebView.this.post(new Runnable() {

								@Override
								public void run() {
									WelcomeWebView.this.evaluateJavascript("javascript:addExp('"
											+ message.split("<thenext>")[0] + "','" + message.split("<thenext>")[1]
											+ "','" + message.split("<thenext>")[2] + "')", null);
								}
							});
							result.confirm();
							return true;
						}
					});
					vWv.loadUrl(url);
				}
			});
		}

		@JavascriptInterface
		public boolean isLogined() {
			return Vers.i.userName != null;
		}

		@JavascriptInterface
		public void showLogin() {
			MainActivity.main.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showLoginDialog();
				}
			});
		}

		@JavascriptInterface
		public boolean hasRecover() {
			return Vers.i.vOaBackup != null;
		}

		@JavascriptInterface
		public void delRecover() {
			new File(MainActivity.main.getFilesDir(), "recover.json").delete();
			Vers.i.vOaBackup = null;
			MainActivity.main.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadWhenShowWelcome();
				}
			});
		}

		@JavascriptInterface
		public String getRecoverTime() {
			return Vers.i.vOaBackup[0] + "";
		}

		@JavascriptInterface
		public void recover() {
			startRecover();
		}

		public String getUsername() {
			return Vers.i.userName;
		}
	}
}
