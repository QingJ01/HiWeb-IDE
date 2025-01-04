package com.hiweb.ide;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hiweb.ide.databinding.BootBinding;
import com.hiweb.ide.edit.Do;
import com.hiweb.ide.edit.StyleUtils;
import java.io.File;
import java.io.IOException;
import com.hiweb.ide.add.AddStyle;
import android.os.Environment;
import com.hiweb.ide.add.addViewWidget.ChoiceLayout;
import android.content.DialogInterface;
import android.webkit.*;
import java.util.*;

import android.graphics.drawable.*;
import com.hiweb.ide.add.addViewWidget.*;

public class BootActivity extends BaseActivity {
	private boolean isHor = false;

	File passwordFile;

	int statusBarHeight;

	private BootBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(Color.TRANSPARENT);

		// 获得状态栏高度
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		statusBarHeight = getResources().getDimensionPixelSize(resourceId);
		StyleUtils.setAndroidNativeLightStatusBar(this, SettingsClass.ITheme != 1);

		binding = BootBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		coordinatorLayout = binding.coordinator;

		{
			binding.cvFace.setRadius(Do.dp2px(this, 20));
			binding.cvFace.setContentPadding(Do.dp2px(this, 20), Do.dp2px(this, 20), Do.dp2px(this, 20),
					Do.dp2px(this, 20));
			binding.cvFace.setCardElevation(0);

			binding.lyBootMask.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					// TODO: Implement this method
				}
			});
		}

		binding.lyBootFace.setPadding(Do.dp2px(this, 10), statusBarHeight + Do.dp2px(this, 10), Do.dp2px(this, 10),
				Do.dp2px(this, 10));

		if (Do.isScreenHor(this)) {
			binding.lyBootFaceBtns.setOrientation(LinearLayout.HORIZONTAL);
		}

		binding.ibBootContinue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				Do.restartApplication(BootActivity.this);
			}
		});

		if (Build.VERSION.SDK_INT >= 31) {
			binding.iconLogo.setVisibility(View.GONE);
			binding.iconBand.setVisibility(View.VISIBLE);
		}

		switchScreenDirection();

		passwordFile = new File(Environment.getExternalStorageDirectory(), ".data/.pass");

		IsHasUser();

		if (isHaveAuthorities()) {
			new Thread(() -> {

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}

				AddStyle.StyleTable.sortAll();
				Vers.i.SaAllStyle = AddStyle.StyleTable.getAll();

				BootActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							if (!Vers.i.buildPassword.equals("")) {
								if (!passwordFile.exists()) {
									passwordFile.getParentFile().mkdirs();
									showPassword();
									return;
								} else {
									String text = Do.getText(passwordFile);
									String versionCode = BootActivity.this.getPackageManager()
											.getPackageInfo(BootActivity.this.getPackageName(), 0).versionCode + "";
									if (text.equals(versionCode)) {
										enter();
									} else if (text.equals("[SYSTEM FILE]")) {
										showBlack();
									} else {
										showPassword();
									}
								}
							} else {
								enter();
							}

						} catch (Exception e) {
							Do.showErrDialog(BootActivity.this, e);
						}

					}
				});
			}).start();
		} else {
			binding.ibBootContinue.setVisibility(View.VISIBLE);
			Do.showPermissions(this);
		}
	}

	private void showBlack() {
		Buttons btns = new Buttons();
		btns.putButton(R.string.join_chat, R.drawable.join_chat, new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				if (!Do.joinQQGroup(BootActivity.this, "jaugxRhdtXrOqLlangE02a8_qhoknr01")) {
					Do.showErrDialog(BootActivity.this, new Exception("Join QQ chat failed."));
				}
			}
		}, true);
		showFace(getString(R.string.black_title),
				getTextFaceView(getString(R.string.black_msg), Do.getColor(this, R.color.ash)), btns,
				Do.getColor(this, R.color.ash), R.drawable.ic_alert);

	}

	private void showPassword() {
		LinearLayout ly = getTextFaceView(getString(R.string.password_msg), Do.getColor(this, R.color.ash));

		final ChoiceLayout cl = new ChoiceLayout(this);
		cl.build(false, -1, null, R.string.password_choose,
				"A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|Unknow");
		cl.setPadding(0, 60, 0, 0);

		ly.addView(cl);

		Buttons btns = new Buttons();
		btns.putButton(R.string.join_chat, R.drawable.join_chat, new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				if (!Do.joinQQGroup(BootActivity.this, "jaugxRhdtXrOqLlangE02a8_qhoknr01")) {
					Do.showErrDialog(BootActivity.this, new Exception("Join QQ chat failed."));
				}
			}
		}, false);
		btns.putButton(R.string.continue_, R.drawable.ok, new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				Dl dl = new Dl(BootActivity.this);
				dl.builder.setTitle(cl.getText("$"));
				dl.builder.setMessage(R.string.password_ok);
				dl.builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2) {
						try {
							if (cl.getText("$").equals(Vers.i.buildPassword)) {
								String versionCode = BootActivity.this.getPackageManager()
										.getPackageInfo(BootActivity.this.getPackageName(), 0).versionCode + "";
								passwordFile.createNewFile();
								Do.write(versionCode, passwordFile);
								enter();
							} else {
								passwordFile.createNewFile();
								Do.write("[SYSTEM FILE]", passwordFile);
								showBlack();
							}
						} catch (Exception e) {
							Do.showErrDialog(BootActivity.this, e);
						}
					}
				});
				dl.show();
			}
		}, true);
		showFace(getString(R.string.password_title), ly, btns, Do.getColor(this, R.color.ash), R.drawable.key);
	}

	private void enter() {
		try {
			updateTip();
		} catch (Exception e) {
			Do.showErrDialog(this, e);
		}
	}

	private void updateTip() throws Exception {
		File versionFile = new File(getFilesDir(), "versionCode");

		boolean isShowUpdateTip = false;
		int oldVer = -1;
		try {
			int nowVer = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			{
				try {
					oldVer = Integer.parseInt(Do.getText(versionFile));
				} catch (Exception e) {
				}

				if (nowVer > oldVer)
					isShowUpdateTip = true;
				else
					isShowUpdateTip = false;
			}
		} catch (PackageManager.NameNotFoundException e) {
		}

		if (isShowUpdateTip) {
			new TipsManager(this).delete("join_chat");
			Do.copyDataToSD(this, "update_msg.txt", new File(getFilesDir(), "update_msg.txt").getPath());
			String updateMsg = Do.getText(new File(getFilesDir(), "update_msg.txt"));
			if (updateMsg.trim().equals("")) {
				updateMsg = getString(R.string.update_msg);
			}

			LinearLayout vLy = getTextFaceView(updateMsg, Do.getColor(this, R.color.ash));
			Buttons btns = new Buttons();
			btns.putButton(R.string.continue_, R.drawable.ok, new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					joinChat();
				}
			}, true);

			showFace(getString(R.string.explore_new_version_), vLy, btns, Do.getColor(this, R.color.ash),
					R.drawable.list_bulleted);

			versionFile.createNewFile();
			Do.write((getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) + "", versionFile);
		} else {
			skip();
		}
	}

	private void joinChat() {
		if (!new TipsManager(this).isExists("join_chat")) {
			LinearLayout vLy = getTextFaceView(getString(R.string.join_chat_ad), Do.getColor(this, R.color.ash));
			Buttons btns = new Buttons();
			btns.putButton(R.string.continue_, R.drawable.continue_right, new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					skip();
				}
			}, false);
			btns.putButton(R.string.join_chat, R.drawable.join_chat, new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					Do.showGroup(BootActivity.this);
				}
			}, true);

			showFace(getString(R.string.join_chat), vLy, btns, Do.getColor(this, R.color.ash), R.drawable.group);

			new TipsManager(this).create("join_chat");
		} else {
			skip();
		}
	}

	private boolean isHaveAuthorities() {
		try {
			// 试写
			File testFile = new File(Vers.i.FVenter, "temp.abc");
			testFile.getParentFile().mkdirs();
			testFile.createNewFile();
			Do.write("[EASYWEB] Do I have authorities?", testFile);
			Do.getText(testFile);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void showFace(final String title, final LinearLayout view, final Buttons btns, final int bgcolor,
			final int icon) {
		binding.lyBootMask.setVisibility(View.VISIBLE);
		Do.setAlphaAnimation(binding.rlRoot, true, 500, new Runnable() {

			@Override
			public void run() {
				binding.rlRoot.setVisibility(View.GONE);
				binding.lyBootFaceCup.removeAllViews();
				binding.lyBootFaceBtns.removeAllViews();
				binding.lyBoot.setVisibility(View.GONE);
				binding.lyBootFace.setVisibility(View.VISIBLE);
				binding.tvBootFaceTitle.setText(title);
				Drawable iconDrawable = getDrawable(icon);
				iconDrawable.setTint(Do.getColor(BootActivity.this, R.color.display_color));
				binding.ivBootFaceIcon.setImageDrawable(iconDrawable);
				view.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
				binding.lyBootFaceCup.addView(view);

				binding.cvFace.setCardBackgroundColor(bgcolor);

				for (int i = 0; i < btns.size(); i++) {
					CircleButton cbtn = new CircleButton(BootActivity.this, btns.getIcon(i), btns.getText(i),
							btns.isImportantBtn(i));
					if (isHor)
						cbtn.circleMode();
					cbtn.setOnClickListener(btns.getEvent(i));
					binding.lyBootFaceBtns.addView(cbtn);
				}

				binding.rlRoot.setVisibility(View.VISIBLE);
				Do.setAlphaAnimation(binding.rlRoot, false, 500, new Runnable() {

					@Override
					public void run() {
						binding.lyBootMask.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	private LinearLayout getTextFaceView(String text, int bgcolor) {
		TextView ActvMsg = new TextView(this);
		int textColor;
		textColor = Do.isLightColor(bgcolor) ? Color.BLACK : Color.WHITE;

		ActvMsg.setTextColor(textColor);
		ActvMsg.setText(text);
		ActvMsg.setTextSize(15);
		ActvMsg.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1));

		LinearLayout vLy = new LinearLayout(this);
		vLy.setOrientation(LinearLayout.VERTICAL);
		vLy.addView(ActvMsg);
		return vLy;
	}

	private void showLogo() {
		binding.lyBoot.setVisibility(View.VISIBLE);
		binding.lyBootFace.setVisibility(View.GONE);
	}

	private void skip() {
		showLogo();
		testWebView();
	}

	private void testWebView() {
		if (!SettingsClass.isShowWebViewAlert) {
			intentToMain();
			return;
		}
		int title = 0;
		int msg = 0;
		try {
			WebView webview = new WebView(BootActivity.this);
			try {
				String[] versions = webview.getSettings().getUserAgentString().split("/");
				String version = versions[versions.length - 1];
				String chromiumVersion = versions[versions.length - 2];

				if (Double.parseDouble(version) < 537.36d) {
					// WebView 版本过低
					title = R.string.webview_version_low;
					msg = R.string.webview_version_low_msg;
				} else {
					if (Integer.parseInt(chromiumVersion.split("\\.")[0]) < 80) {
						title = R.string.webview_version_low;
						msg = R.string.webview_version_low_msg;
					} else {
						intentToMain();
						return;
					}
				}
			} catch (Exception e) {
				// WebView 版本获取失败
				title = R.string.webview_version_get_err;
				msg = R.string.webview_version_get_err_msg;
			}
		} catch (Exception e) {
			// 无法使用 WebView
			title = R.string.cannot_use_webview;
			msg = R.string.cannot_use_webview_msg;
		}

		{
			LinearLayout vLy = getTextFaceView(getString(msg), Do.getColor(this, R.color.ash));
			Buttons btns = new Buttons();
			btns.putButton(R.string.ignore, R.drawable.continue_right, new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					intentToMain();
				}
			}, false);
			btns.putButton(R.string.solve_proj, R.drawable.lightbulb, new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					Do.openUrl(Vers.i.supportHost + "easywebide/article/#7", BootActivity.this);
				}
			}, true);

			showFace(getString(title), vLy, btns, Do.getColor(this, R.color.ash), R.drawable.settings);
		}
	}

	private void intentToMain() {
		showLogo();
		new Thread(new Runnable() {

			@Override
			public void run() {
				BootActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Intent i = new Intent(BootActivity.this, MainActivity.class);
						Vers.i.isEW = true;
						startActivity(i);
						finish();
					}
				});
			}
		}).start();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		switchScreenDirection();
		// 检测实体键盘的状态：推出或者合上
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			// 实体键盘处于推出状态，在此处添加额外的处理代码
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			// 实体键盘处于合上状态，在此处添加额外的处理代码
		}
	}

	private void switchScreenDirection() {
		// 检测屏幕的方向：纵向或横向
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 当前为横屏， 在此处添加额外的处理代码
			binding.lyBootFaceBtns.setOrientation(LinearLayout.HORIZONTAL);
			for (int i = 0; i < binding.lyBootFaceBtns.getChildCount(); i++) {
				((CircleButton) binding.lyBootFaceBtns.getChildAt(i)).circleMode();
			}

			isHor = true;
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 当前为竖屏， 在此处添加额外的处理代码
			binding.lyBootFaceBtns.setOrientation(LinearLayout.VERTICAL);
			for (int i = 0; i < binding.lyBootFaceBtns.getChildCount(); i++) {
				((CircleButton) binding.lyBootFaceBtns.getChildAt(i)).barMode();
			}

			isHor = false;
		}
	}

	private void IsHasUser() {

		if (Vers.i.userFile.exists()) {
			String userName;
			try {
				String JuserName = Do.getText(Vers.i.userFile);
				userName = ((JsonObject) new JsonParser().parse(JuserName)).get("name").getAsString();

				Vers.i.userName = userName;
			} catch (Exception e) {

			}
		} else {
			Vers.i.userName = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	public class Buttons {
		public List<Object[]> list;

		public Buttons() {
			list = new ArrayList<Object[]>();
		}

		public void putButton(int text, int icon, View.OnClickListener event, boolean isImportantBtn) {
			putButton(getString(text), icon, event, isImportantBtn);
		}

		public void putButton(String text, int icon, View.OnClickListener event, boolean isImportantBtn) {
			list.add(new Object[] { text, icon, event, isImportantBtn });
		}

		public int size() {
			return list.size();
		}

		public Drawable getIcon(int index) {
			Drawable drawable = BootActivity.this.getDrawable((Integer) list.get(index)[1]);
			return drawable;
		}

		public String getText(int index) {
			return (String) list.get(index)[0];
		}

		public boolean isImportantBtn(int index) {
			return (boolean) list.get(index)[3];
		}

		public View.OnClickListener getEvent(int index) {
			return (View.OnClickListener) (list.get(index)[2]);
		}
	}
}
