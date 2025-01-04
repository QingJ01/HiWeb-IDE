package com.hiweb.ide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hiweb.ide.add.addViewWidget.ButtonLayout;
import com.hiweb.ide.add.addViewWidget.ChoiceLayout;
import com.hiweb.ide.add.addViewWidget.ObjectLayout;
import com.hiweb.ide.add.addViewWidget.TextLayout;
import com.hiweb.ide.edit.Do;
import com.venter.jssrunner.JssRunner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import android.text.InputType;
import android.widget.ImageButton;
import android.widget.EditText;
import java.io.IOException;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class SettingDoor {
	static String SChars = "";
	static String SNames = "";
	static File qsWebsite;
	static AlertDialog adSet;

	public static void show() {
		LinearLayout Ly;
		ScrollView Sv = new ScrollView(MainActivity.main);
		Sv.setFillViewport(true);
		RelativeLayout Rl = new RelativeLayout(MainActivity.main);
		Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
		Ly = new LinearLayout(MainActivity.main);
		Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
		Ly.setOrientation(LinearLayout.VERTICAL);
		Ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10));
		Sv.addView(Rl);
		Rl.addView(Ly);

		adSet = null;

		SChars = "";
		SNames = "";

		for (int i = 0; i < SettingsClass.SaChars.length; i++) {
			SChars += SettingsClass.SaChars[i] + " ";
		}
		SChars = SChars.trim();

		for (int i = 0; i < SettingsClass.SaNames.length; i++) {
			SNames += SettingsClass.SaNames[i] + " ";
		}
		SNames = SNames.trim();

		final TextLayout TlChars = new TextLayout(MainActivity.main);
		TlChars.build(-1, MainActivity.main.getString(R.string.set_chars), R.string.set_describle, null);
		TlChars.Acet.setText(SChars);

		final TextLayout TlNames = new TextLayout(MainActivity.main);
		TlNames.build(-1, MainActivity.main.getString(R.string.set_name), R.string.set_describle, null);
		TlNames.Acet.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1f));
		TlNames.Acet.setText(SNames);
		TlNames.Acet.setInputType(InputType.TYPE_CLASS_TEXT);
		TlNames.Acet.setMaxLines(1);
		ImageButton Acib = new ImageButton(MainActivity.main, null, android.R.attr.buttonBarButtonStyle);
		Acib.setLayoutParams(
				new LinearLayout.LayoutParams(Do.dp2px(MainActivity.main, 44), Do.dp2px(MainActivity.main, 44)));
		Acib.setImageDrawable(Vers.i.getAddIconDrawable());
		Acib.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				final EditText editText = new EditText(MainActivity.main);
				editText.setBackground(null);
				editText.setText(TlNames.Acet.getText());
				Dl dl = new Dl(MainActivity.main);
				dl.builder.setView(editText);
				dl.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2) {
						TlNames.Acet.setText(editText.getText());
					}
				});
				dl.show();
			}
		});
		TlNames.vLyText.addView(Acib);

		final ChoiceLayout ClTheme = new ChoiceLayout(MainActivity.main);
		ClTheme.build(true, -1, MainActivity.main.getString(R.string.set_theme), R.string.need_reboot,
				MainActivity.main.getString(R.string.set_theme_blue) + "|"
						+ MainActivity.main.getString(R.string.set_theme_black) + "|"
						+ MainActivity.main.getString(R.string.set_theme_green) + "|"
						+ MainActivity.main.getString(R.string.set_theme_light) + "|"
						+ MainActivity.main.getString(R.string.set_theme_white));
		ClTheme.Acs.setSelection(SettingsClass.IVirtualTheme + 1, false);

		final ChoiceLayout ClDarktheme = new ChoiceLayout(MainActivity.main);
		ClDarktheme.build(false, -1, MainActivity.main.getString(R.string.set_darktheme), R.string.need_reboot,
				MainActivity.main.getString(R.string.enable) + "|" + MainActivity.main.getString(R.string.disable));
		ClDarktheme.Acs.setSelection(SettingsClass.BVirtualIsDarktheme ? 0 : 1, false);

		final ObjectLayout OlJsRes = new ObjectLayout(MainActivity.main);
		LinearLayout LyJsRes = new LinearLayout(MainActivity.main);
		LyJsRes.setOrientation(LinearLayout.VERTICAL);
		LyJsRes.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		LyJsRes.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
		Button getBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		getBtn.setText(R.string.js_res_network);
		getBtn.setTextColor(Color.parseColor("#1593E5"));
		getBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		getBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		getBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				getJsRes();
			}
		});
		Button managerBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		managerBtn.setText(R.string.js_res_manager);
		managerBtn.setTextColor(Color.parseColor("#1593E5"));
		managerBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		managerBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		managerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				managerJsRes();
			}
		});
		Button helpBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		helpBtn.setText(R.string.js_res_help);
		helpBtn.setTextColor(Color.parseColor("#1593E5"));
		helpBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		helpBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		helpBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				helpJsRes();
			}
		});
		LyJsRes.addView(getBtn);
		LyJsRes.addView(managerBtn);
		LyJsRes.addView(helpBtn);
		OlJsRes.build(-1, MainActivity.main.getString(R.string.js_res), R.string.js_res_describle, LyJsRes);

		final ObjectLayout OlPhpIni = new ObjectLayout(MainActivity.main);
		LinearLayout LyPhpIni = new LinearLayout(MainActivity.main);
		LyPhpIni.setOrientation(LinearLayout.VERTICAL);
		LyPhpIni.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		LyPhpIni.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
		Button openIniBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		openIniBtn.setText(R.string.open_file);
		openIniBtn.setTextColor(Color.parseColor("#1593E5"));
		openIniBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		openIniBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		openIniBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				adSet.dismiss();

				File phpINI = new File(MainActivity.main.getFilesDir(), "php.ini");
				if (!phpINI.exists()) {
					try {
						phpINI.createNewFile();
					} catch (IOException e) {
						Do.showErrDialog(MainActivity.main, e);
						return;
					}
				}

				MainActivity.main.OpenFile(phpINI);
			}
		});

		LyPhpIni.addView(openIniBtn);
		OlPhpIni.build(-1, null, R.string.set_php_ini, LyPhpIni);

		final TextLayout TlBackup = new TextLayout(MainActivity.main);
		TlBackup.build(-1, MainActivity.main.getString(R.string.backup_frequency), R.string.backup_frequency_describe,
				null);
		TlBackup.Acet.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
		TlBackup.Acet.setText(SettingsClass.vIBackup + "");

		final ChoiceLayout ClWordWarp = new ChoiceLayout(MainActivity.main);
		ClWordWarp.build(false, -1, null, R.string.wordwarp,
				MainActivity.main.getString(R.string.enable) + "|" + MainActivity.main.getString(R.string.disable));
		ClWordWarp.Acs.setSelection(SettingsClass.vBIsWordWarp ? 0 : 1, false);

		final ChoiceLayout ClAllowAbsolute = new ChoiceLayout(MainActivity.main);
		ClAllowAbsolute.build(false, -1, MainActivity.main.getString(R.string.allow_use_absolute_path),
				R.string.allow_use_absolute_path_des,
				MainActivity.main.getString(R.string.enable) + "|" + MainActivity.main.getString(R.string.disable));
		ClAllowAbsolute.Acs.setSelection(SettingsClass.isAllowAbsolute ? 0 : 1, false);

		qsWebsite = SettingsClass.vQsWebsite;
		final ObjectLayout OlQs = new ObjectLayout(MainActivity.main);
		LinearLayout LyQs = new LinearLayout(MainActivity.main);
		LyQs.setOrientation(LinearLayout.VERTICAL);
		LyQs.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		LyQs.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
		final TextView msgTv = new TextView(MainActivity.main);
		msgTv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		msgTv.setGravity(Gravity.LEFT);
		msgTv.setText(qsWebsite == null ? R.string.quick_start_default : R.string.quick_start_custom);
		Button importBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		importBtn.setText(R.string.quick_start_choose_zip);
		importBtn.setTextColor(Color.parseColor("#1593E5"));
		importBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		importBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		importBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				FileChooseClass fileChooseClass = new FileChooseClass();
				fileChooseClass.Type(true);
				fileChooseClass.setOpenPath(Environment.getExternalStorageDirectory());
				fileChooseClass.setOnFileClickListener(new OnFileClickListener() {
					@Override
					public void onClick(final File ChooseFile, AlertDialog dialog) {
						if (ChooseFile.getName().toLowerCase().endsWith(".zip")) {
							dialog.dismiss();
							Do.showWaitAndRunInThread(false, new Runnable() {

								@Override
								public void run() {
									try {
										final File website = new File(MainActivity.main.getFilesDir(),
												"custom_quickstart");
										if (website.exists()) {
											Do.delete(website.getPath());
										}
										website.mkdirs();
										if (!Do.upZipFileDir(ChooseFile, website)) {
											throw new Exception(
													"The selected ZIP file can't be used. Maybe there are some errors in this file.");
										}
										MainActivity.main.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												qsWebsite = website;
												msgTv.setText(R.string.quick_start_custom);
												Do.finishWaiting();
											}
										});
									} catch (final Exception e) {
										MainActivity.main.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Do.finishWaiting();
												Do.showErrDialog(MainActivity.main, e);
											}
										});
									}
								}
							});
						} else {
							MainActivity.main.toast(R.string.not_zip);
						}
					}
				});
				fileChooseClass.Show(MainActivity.main);
			}
		});
		Button recoverBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		recoverBtn.setText(R.string.quick_start_recover);
		recoverBtn.setTextColor(Color.parseColor("#1593E5"));
		recoverBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		recoverBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		recoverBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				qsWebsite = null;
				msgTv.setText(R.string.quick_start_default);
			}
		});
		Button helpHomeBtn = new Button(MainActivity.main, null, R.attr.buttonBarButtonStyle);
		helpHomeBtn.setText(R.string.js_res_help);
		helpHomeBtn.setTextColor(Color.parseColor("#1593E5"));
		helpHomeBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		helpHomeBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		helpHomeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				Do.openUrl(Vers.i.supportHost + "easywebide/article/#5", MainActivity.main);
			}
		});
		LyQs.addView(msgTv);
		LyQs.addView(importBtn);
		LyQs.addView(recoverBtn);
		LyQs.addView(helpHomeBtn);
		OlQs.build(-1, null, R.string.quick_start, LyQs);

		final ChoiceLayout ClAutoPreviewFull = new ChoiceLayout(MainActivity.main);
		ClAutoPreviewFull.build(false, -1, null, R.string.auto_preview_full,
				MainActivity.main.getString(R.string.enable) + "|" + MainActivity.main.getString(R.string.disable));
		ClAutoPreviewFull.Acs.setSelection(SettingsClass.isAutoPreviewFull ? 0 : 1, false);

		final ChoiceLayout ClHideHW = new ChoiceLayout(MainActivity.main);
		ClHideHW.build(false, -1, MainActivity.main.getString(R.string.hide_hopweb), R.string.apply_in_a_few_time,
				MainActivity.main.getString(R.string.enable) + "|" + MainActivity.main.getString(R.string.disable));
		ClHideHW.Acs.setSelection(SettingsClass.isHideHW ? 0 : 1, false);

		final ChoiceLayout ClShowWebViewAlert = new ChoiceLayout(MainActivity.main);
		ClShowWebViewAlert.build(false, -1, null, R.string.show_webview_alert,
				MainActivity.main.getString(R.string.enable) + "|" + MainActivity.main.getString(R.string.disable));
		ClShowWebViewAlert.Acs.setSelection(SettingsClass.isShowWebViewAlert ? 0 : 1, false);

		Ly.addView(TlChars);
		Ly.addView(TlNames);
		Ly.addView(ClTheme);
		Ly.addView(ClDarktheme);
		Ly.addView(OlJsRes);
		// Ly.addView(OlPhpIni);
		Ly.addView(TlBackup);
		// Ly.addView(ClWordWarp);
		Ly.addView(OlQs);
		Ly.addView(ClAutoPreviewFull);
		Ly.addView(ClAllowAbsolute);
		Ly.addView(ClHideHW);
		if (!Vers.i.isInstalledHopWeb)
			ClHideHW.setVisibility(View.GONE);
		Ly.addView(ClShowWebViewAlert);

		Dl AdbSet = new Dl(MainActivity.main);
		AdbSet.builder.setTitle(R.string.setting_title);
		AdbSet.builder.setView(Sv);
		AdbSet.builder.setCancelable(false);
		AdbSet.builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				try {
					{
						String[] SaChars;
						String[] SaNames;
						if (TlChars.getText("$") != null && TlChars.getText("$").trim().indexOf(" ") == -1) {
							SaChars = new String[] { TlChars.getText("$") };
						} else {
							SaChars = TlChars.getText("$") == null ? new String[] {}
									: TlChars.getText("$").trim().split(" ");
						}
						if (TlNames.getText("$") != null && TlNames.getText("$").trim().indexOf(" ") == -1) {
							SaNames = new String[] { TlNames.getText("$") };
						} else {
							SaNames = TlNames.getText("$") == null ? new String[] {}
									: TlNames.getText("$").trim().split(" ");
						}

						JsonObject JoAll = new JsonObject();

						JsonArray JaChars = new JsonArray();
						JsonArray JaNames = new JsonArray();
						for (int i = 0; i < SaChars.length; i++) {
							JaChars.add(SaChars[i]);
						}
						for (int i = 0; i < SaNames.length; i++) {
							JaNames.add(SaNames[i]);
						}

						Arrays.sort(SaNames);

						SettingsClass.SaChars = SaChars.length == 0 ? Vers.i.SaDefChars : SaChars;
						SettingsClass.SaNames = SaNames.length == 0 ? Vers.i.SaDefNames : SaNames;

						String SChoice = ClTheme.getText("$");
						if (SChoice == null)
							SettingsClass.IVirtualTheme = -1;
						else if (SChoice.equals(MainActivity.main.getString(R.string.set_theme_blue)))
							SettingsClass.IVirtualTheme = 0;
						else if (SChoice.equals(MainActivity.main.getString(R.string.set_theme_black)))
							SettingsClass.IVirtualTheme = 1;
						else if (SChoice.equals(MainActivity.main.getString(R.string.set_theme_green)))
							SettingsClass.IVirtualTheme = 2;
						else if (SChoice.equals(MainActivity.main.getString(R.string.set_theme_light)))
							SettingsClass.IVirtualTheme = 3;
						else if (SChoice.equals(MainActivity.main.getString(R.string.set_theme_white)))
							SettingsClass.IVirtualTheme = 4;

						boolean vBIsDarktheme = ClDarktheme.getText("$")
								.equals(MainActivity.main.getString(R.string.enable));
						SettingsClass.BVirtualIsDarktheme = vBIsDarktheme;

						String vSBackup = TlBackup.getText("$");
						int vIBackup = 60;
						if (vSBackup != null) {
							try {
								vIBackup = Integer.parseInt(vSBackup) < 10 ? 10 : Integer.parseInt(vSBackup);
							} catch (Exception e) {

							}
						}
						SettingsClass.vIBackup = vIBackup;

						SettingsClass.vQsWebsite = qsWebsite;

						SettingsClass.isAutoPreviewFull = ClAutoPreviewFull.getText("$")
								.equals(MainActivity.main.getString(R.string.enable));

						SettingsClass.isAllowAbsolute = ClAllowAbsolute.getText("$")
								.equals(MainActivity.main.getString(R.string.enable));

						SettingsClass.isHideHW = ClHideHW.getText("$")
								.equals(MainActivity.main.getString(R.string.enable));

						SettingsClass.isShowWebViewAlert = ClShowWebViewAlert.getText("$")
								.equals(MainActivity.main.getString(R.string.enable));

						JoAll.add("chars", JaChars);
						JoAll.add("names", JaNames);
						JoAll.addProperty("theme", SettingsClass.IVirtualTheme);
						JoAll.addProperty("isDarkTheme", vBIsDarktheme);
						JoAll.addProperty("backup_frequency", vIBackup);
						if (qsWebsite != null)
							JoAll.addProperty("custom_quickstart", qsWebsite.getPath());
						JoAll.addProperty("isAutoPreviewFull", SettingsClass.isAutoPreviewFull);
						JoAll.addProperty("isAllowAbsolutePath", SettingsClass.isAllowAbsolute);
						JoAll.addProperty("isHideHopWeb", SettingsClass.isHideHW);
						JoAll.addProperty("isShowWebViewAlert", SettingsClass.isShowWebViewAlert);
						if (SettingsClass.bgFile != null)
							JoAll.addProperty("bg", SettingsClass.bgFile.getPath());
						if (SettingsClass.bgScale != null)
							JoAll.addProperty("bg_scale", SettingsClass.bgScale);
						JoAll.addProperty("bg_alpha", SettingsClass.bgAlpha);

						if (!Vers.i.FSetting.exists()) {
							Vers.i.FSetting.createNewFile();
						}
						Do.write(JoAll.toString(), Vers.i.FSetting);

						MainActivity.main.binding.wwvWelcome.loadWhenShowWelcome();

						MainActivity.main.toast(R.string.done);
					}

				} catch (Exception e) {
					Do.showErrDialog(MainActivity.main, e);
				}
			}
		});
		AdbSet.builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				adSet.dismiss();
			}
		});
		AdbSet.builder.setNeutralButton(R.string.main_about, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				showAbout();
			}
		});
		adSet = AdbSet.show();
	}

	public static final Object[][] jsLinkArr = new Object[][] {
			{ Vers.i.serverHost + "server/hiweb/jsres/jquery/latest_ver.txt",
					new String[] { Vers.i.serverHost + "server/hiweb/jsres/jquery/prod.js",
							Vers.i.serverHost + "server/hiweb/jsres/jquery/dev.js" },
					MainActivity.main.getString(R.string.jquery),
					MainActivity.main.getResources().getDrawable(R.drawable.jquery),
					MainActivity.main.getString(R.string.jquery_ad),
					"https://jquery.com", MainActivity.main.getString(R.string.installation_ad) }, // jQuery 0
			{ Vers.i.serverHost + "server/hiweb/jsres/vuejs/latest_ver.txt",
					new String[] { Vers.i.serverHost + "server/hiweb/jsres/vuejs/prod.js", null },
					MainActivity.main.getString(R.string.vuejs),
					MainActivity.main.getResources().getDrawable(R.drawable.vuejs),
					MainActivity.main.getString(R.string.vue_ad),
					"https://cn.vuejs.org", MainActivity.main.getString(R.string.installation_ad) }, // Vue.js 1
			{ Vers.i.serverHost + "server/hiweb/jsres/mui/latest_ver.txt", null,
					MainActivity.main.getString(R.string.mui),
					MainActivity.main.getResources().getDrawable(R.drawable.mui),
					MainActivity.main.getString(R.string.mui_ad),
					"https://dev.dcloud.net.cn/mui", MainActivity.main.getString(R.string.installation_ad_lib),
					MUIManager.class }, // MUI 2
			{ Vers.i.serverHost + "server/hiweb/jsres/vconsole/latest_ver.txt",
					new String[] { Vers.i.serverHost + "server/hiweb/jsres/vconsole/prod.js", null },
					MainActivity.main.getString(R.string.vconsole),
					MainActivity.main.getResources().getDrawable(R.drawable.vconsole),
					MainActivity.main.getString(R.string.vconsole_ad),
					"https://github.com/Tencent/vConsole", MainActivity.main.getString(R.string.installation_ad) }, // vConsole
																													// 3
			{ Vers.i.serverHost + "server/hiweb/jsres/mdui/latest_ver.txt", null,
					MainActivity.main.getString(R.string.mdui),
					MainActivity.main.getResources().getDrawable(R.drawable.layers),
					MainActivity.main.getString(R.string.mdui_ad),
					"https://www.mdui.org/", MainActivity.main.getString(R.string.installation_ad_lib),
					MDUIManager.class }, // MDUI 4
			{ Vers.i.serverHost + "server/hiweb/jsres/bootstrap/latest_ver.txt", null,
					MainActivity.main.getString(R.string.bootstrap),
					MainActivity.main.getResources().getDrawable(R.drawable.bootstrap),
					MainActivity.main.getString(R.string.bootstrap_ad),
					"https://getbootstrap.com/", MainActivity.main.getString(R.string.installation_ad_lib),
					BootstrapManager.class }, // Bootstrap 5
			{ Vers.i.serverHost + "server/hiweb/jsres/art-template/latest_ver.txt",
					new String[] { Vers.i.serverHost + "server/hiweb/jsres/art-template/prod.js", null },
					MainActivity.main.getString(R.string.art_template),
					MainActivity.main.getResources().getDrawable(R.drawable.art_template),
					MainActivity.main.getString(R.string.art_template_ad),
					"http://aui.github.io/art-template/", MainActivity.main.getString(R.string.installation_ad) }, // art-template
																													// 6
			{ Vers.i.serverHost + "server/hiweb/jsres/layui/latest_ver.txt", null,
					MainActivity.main.getString(R.string.layui),
					MainActivity.main.getResources().getDrawable(R.drawable.layui),
					MainActivity.main.getString(R.string.layui_ad),
					"https://www.layuiweb.com/", MainActivity.main.getString(R.string.installation_ad_lib),
					LayuiManager.class }// Layui 7

	};

	public static void getJsRes() {

		// ↑ {版本URL,new String[]{压缩（生产）版本URL,开发版本URL}}
		Do.showWaitAndRunInThread(true, new Runnable() {

			@Override
			public void run() {
				// final EditFileClass.Webpage webpage=new EditFileClass.Webpage();
				final String[] verArr = new String[jsLinkArr.length];
				for (int i = 0; i < verArr.length; i++) {
					// webpage.setPageUrl(jsLinkArr[i][0].toString());
					verArr[i] = Do.testGetHtml(jsLinkArr[i][0].toString(), 6);
					if (verArr[i] == null) {
						final int index = i;
						MainActivity.main.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Do.finishWaiting();
								Dl dlErr = new Dl(MainActivity.main);
								dlErr.builder
										.setTitle(R.string.error)
										.setMessage(MainActivity.main.getString(R.string.download_error))
										.setPositiveButton(R.string.ok, null)
										.setNeutralButton(R.string.solve_proj, new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface p1, int p2) {
												Do.openUrl(Vers.i.supportHost + "easywebide/article/#6",
														MainActivity.main);
											}
										});
								dlErr.show();
							}
						});
						return;
					}
				}

				MainActivity.main.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Do.finishWaiting();
						ScrollView Sv = new ScrollView(MainActivity.main);
						Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
						Sv.setFillViewport(true);
						RelativeLayout Rl = new RelativeLayout(MainActivity.main);
						Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
						LinearLayout Ly = new LinearLayout(MainActivity.main);
						Ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
						Ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
								Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
						Ly.setOrientation(LinearLayout.VERTICAL);
						Sv.addView(Rl);
						Rl.addView(Ly);

						for (int i = 0; i < jsLinkArr.length; i++) {
							final String name = jsLinkArr[i][2].toString();
							final int index = i;
							ButtonLayout bl = new ButtonLayout(MainActivity.main);
							bl.setId(i);

							Drawable icon = (Drawable) jsLinkArr[i][3];
							icon.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));
							bl.leftImg.setImageDrawable(icon);
							bl.titleTv.setText(name);
							bl.descriptionTv.setText(MainActivity.main.getString(R.string.latest_ver) + verArr[i]);
							bl.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View p1) {
									// 下载框架
									ScrollView Sv = new ScrollView(MainActivity.main);
									Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
									Sv.setFillViewport(true);
									RelativeLayout Rl = new RelativeLayout(MainActivity.main);
									Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
									LinearLayout ly = new LinearLayout(MainActivity.main);
									ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
									ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
											Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
									ly.setOrientation(LinearLayout.VERTICAL);
									Rl.addView(ly);
									Sv.addView(Rl);
									LinearLayout jsResLy = new LinearLayout(MainActivity.main);
									jsResLy.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
									jsResLy.setBackground(MainActivity.main.getResources()
											.getDrawable(R.drawable.shape_gray_background));
									jsResLy.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
											Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));

									TextView actv = new TextView(MainActivity.main);
									actv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
									actv.setText(jsLinkArr[index][4].toString());
									actv.setTextSize(13);
									actv.setTextColor(ContextCompat.getColor(MainActivity.main, R.color.opposition));
									actv.setTypeface(null, Typeface.ITALIC);

									jsResLy.addView(actv);

									TextView actvInstallation = new TextView(MainActivity.main);
									actvInstallation.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
									actvInstallation.setTextSize(10);
									actvInstallation.setPadding(0, 15, 0, 0);
									actvInstallation.setText(jsLinkArr[index][6].toString());

									ly.addView(jsResLy);
									ly.addView(actvInstallation);

									Dl Adb = new Dl(MainActivity.main);
									Adb.builder.setTitle(MainActivity.main.getString(R.string.get) + " " + name);
									Adb.builder.setView(Sv);
									Adb.builder.setPositiveButton(R.string.download_or_update, (p11, p2) -> {
										if (jsLinkArr[index][1] == null) {
											// 下载库
											try {
												((LibraryManager) ((Class) jsLinkArr[index][7]).newInstance())
														.download(verArr[index]);
											} catch (IllegalAccessException | InstantiationException e) {
												Do.showErrDialog(MainActivity.main, e);
											}

											return;
										}

										ScrollView Sv1 = new ScrollView(MainActivity.main);
										Sv1.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
										Sv1.setFillViewport(true);
										RelativeLayout Rl1 = new RelativeLayout(MainActivity.main);
										Rl1.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
										LinearLayout ly1 = new LinearLayout(MainActivity.main);
										ly1.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
										ly1.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
												Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
										ly1.setOrientation(LinearLayout.VERTICAL);
										Rl1.addView(ly1);
										Sv1.addView(Rl1);

										Drawable dDown = MainActivity.main.getResources()
												.getDrawable(R.drawable.download);
										dDown.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));

										if ((((String[]) (jsLinkArr[index][1]))[0]) != null) {
											ButtonLayout blProd = new ButtonLayout(MainActivity.main);
											blProd.leftImg.setImageDrawable(dDown);
											blProd.titleTv.setText(R.string.prod_build);
											final String prodUrl = (((String[]) (jsLinkArr[index][1]))[0]);
											blProd.descriptionTv.setText(R.string.prod_build_des);
											blProd.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View p11) {
													downloadJsToRes(prodUrl, name, verArr[index], true);
												}
											});

											ly1.addView(blProd);
										}
										if ((((String[]) (jsLinkArr[index][1]))[1]) != null) {
											ButtonLayout blDev = new ButtonLayout(MainActivity.main);
											blDev.leftImg.setImageDrawable(dDown);
											blDev.titleTv.setText(R.string.dev_build);
											final String devUrl = (((String[]) (jsLinkArr[index][1]))[1]);
											blDev.descriptionTv.setText(R.string.dev_build_des);
											blDev.setOnClickListener(new View.OnClickListener() {

												@Override
												public void onClick(View p11) {
													downloadJsToRes(devUrl, name, verArr[index], false);
												}
											});
											ly1.addView(blDev);
										}

										Dl AdbDown = new Dl(MainActivity.main);
										AdbDown.builder
												.setTitle(MainActivity.main.getString(R.string.download) + " " + name);
										AdbDown.builder.setView(Sv1);
										AdbDown.builder.setPositiveButton(R.string.cancel, null);
										AdbDown.show();
									});
									Adb.builder.setNeutralButton(R.string.js_website,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface p1, int p2) {
													Uri uri = Uri.parse(jsLinkArr[index][5].toString());
													Intent intent = new Intent(Intent.ACTION_VIEW, uri);
													MainActivity.main.startActivity(intent);
												}
											});
									Adb.show();
								}
							});
							Ly.addView(bl);
						}
						Dl Adb = new Dl(MainActivity.main);
						Adb.builder.setTitle(R.string.js_res_network);
						Adb.builder.setView(Sv);
						Adb.show();
					}
				});
			}
		});
	}

	public static void downloadJsToRes(final String url, final String name, final String ver, final boolean isMin) {
		Do.showWaitAndRunInThread(true, new Runnable() {

			@Override
			public void run() {
				try {
					String dirName = "";
					if (isMin)
						dirName = name + "/prod";
					else
						dirName = name + "/dev";
					File saveDir = new File(Vers.i.FVenter, ".jsres/" + dirName);
					File versionFile = new File(saveDir, ".version.txt");
					File jsFile = new File(saveDir, ".main.js");
					saveDir.mkdirs();
					versionFile.createNewFile();
					jsFile.createNewFile();
					Do.downloadNet(url, jsFile.getPath());
					Do.write(ver, versionFile);
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							MainActivity.main.toast(R.string.jsres_download_done);
						}
					});
				} catch (final Exception e) {
					MainActivity.main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							Dl dlErr = new Dl(MainActivity.main);
							dlErr.builder
									.setTitle(R.string.error)
									.setMessage(MainActivity.main.getString(R.string.jsres_download_error) + "\n"
											+ MainActivity.main.getString(R.string.download_error))
									.setPositiveButton(R.string.ok, null)
									.setNeutralButton(R.string.solve_proj, new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface p1, int p2) {
											Do.openUrl(Vers.i.supportHost + "easywebide/article/#6", MainActivity.main);
										}
									});
							dlErr.show();
						}
					});
				}
			}
		});
	}

	public static void managerJsRes() {
		try {
			ScrollView Sv = new ScrollView(MainActivity.main);
			Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
			Sv.setFillViewport(true);
			RelativeLayout Rl = new RelativeLayout(MainActivity.main);
			Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
			LinearLayout ly = new LinearLayout(MainActivity.main);
			ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
			ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
					Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
			ly.setOrientation(LinearLayout.VERTICAL);
			Rl.addView(ly);
			Sv.addView(Rl);
			JsonObject myJsresJsonObject = getMyJsres();
			if (myJsresJsonObject == null) {
				MainActivity.main.toast(R.string.no_jsres);
				return;
			}
			Dl Adb = new Dl(MainActivity.main);
			Adb.builder.setTitle(R.string.js_res_manager);
			Adb.builder.setView(Sv);
			final AlertDialog Ad = Adb.create();
			for (int i = 0; i < jsLinkArr.length; i++) {
				if (myJsresJsonObject.has(jsLinkArr[i][2].toString())) {
					final JsonObject nowJsresVers = (JsonObject) myJsresJsonObject.get(jsLinkArr[i][2].toString());
					final String name = jsLinkArr[i][2].toString();
					StringBuilder sbVers = new StringBuilder();
					/*
					 * sbVers:
					 * 生产环境版本：xxx
					 * 开发环境版本：xxx
					 */
					if (nowJsresVers.has("prod"))
						sbVers.append(
								MainActivity.main.getString(R.string.prod) + nowJsresVers.get("prod").getAsString());
					if (nowJsresVers.has("dev"))
						sbVers.append((nowJsresVers.has("prod") ? "\n" : "") + MainActivity.main.getString(R.string.dev)
								+ nowJsresVers.get("dev").getAsString());
					ButtonLayout bl = new ButtonLayout(MainActivity.main);

					Drawable icon = (Drawable) jsLinkArr[i][3];
					icon.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));

					bl.leftImg.setImageDrawable(icon);
					bl.titleTv.setText(jsLinkArr[i][2].toString());
					bl.descriptionTv.setText(sbVers.toString());
					bl.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View p1) {
							Ad.dismiss();
							Dl AdbManagerSelectedJsres = new Dl(MainActivity.main);
							AdbManagerSelectedJsres.builder.setTitle(name);
							final AlertDialog Ad = AdbManagerSelectedJsres.create();

							ScrollView Sv = new ScrollView(MainActivity.main);
							Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
							Sv.setFillViewport(true);
							RelativeLayout Rl = new RelativeLayout(MainActivity.main);
							Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
							LinearLayout ly = new LinearLayout(MainActivity.main);
							ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
							ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
									Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
							ly.setOrientation(LinearLayout.VERTICAL);
							Rl.addView(ly);
							Sv.addView(Rl);
							if (nowJsresVers.has("prod")) {
								ButtonLayout bl = new ButtonLayout(MainActivity.main);
								bl.leftImg.setVisibility(View.GONE);
								bl.titleTv.setText(MainActivity.main.getString(R.string.prod)
										+ nowJsresVers.get("prod").getAsString());
								bl.descriptionTv.setText(R.string.jsres_touch_del);
								bl.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										Ad.dismiss();
										File targetDir = new File(Vers.i.FVenter, ".jsres/" + name + "/prod");
										Do.delete(targetDir.getPath());
										MainActivity.main.toast(R.string.done);
									}
								});
								ly.addView(bl);
							}
							if (nowJsresVers.has("dev")) {
								ButtonLayout bl = new ButtonLayout(MainActivity.main);
								bl.leftImg.setVisibility(View.GONE);
								bl.titleTv.setText(MainActivity.main.getString(R.string.dev)
										+ nowJsresVers.get("dev").getAsString());
								bl.descriptionTv.setText(R.string.jsres_touch_del);
								bl.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View p1) {
										Ad.dismiss();
										File targetDir = new File(Vers.i.FVenter, ".jsres/" + name + "/dev");
										Do.delete(targetDir.getPath());
										MainActivity.main.toast(R.string.done);
									}
								});
								ly.addView(bl);
							}
							Ad.setView(Sv);
							Ad.show();
						}
					});

					ly.addView(bl);
				}
			}
			Ad.show();
		} catch (Exception e) {
			Do.showErrDialog(MainActivity.main, e);
		}
	}

	public static JsonObject getMyJsres() throws Exception {
		/*
		 * myJsresJsonObject:{
		 * "jQuery":{
		 * "prod":"1.x"
		 * "dev":"1.x"
		 * },
		 * "Vue.js":{
		 * "prod":"2.x"
		 * },
		 * "MUI":{
		 * "prod":"1.x"
		 * }
		 * }
		 */
		JsonObject myJsresJsonObject = new JsonObject();
		String[] jsresNames = new String[jsLinkArr.length];
		File[] jsresDirs = new File[jsLinkArr.length];
		for (int i = 0; i < jsLinkArr.length; i++) {
			jsresNames[i] = jsLinkArr[i][2].toString();
			jsresDirs[i] = new File(Vers.i.FVenter, ".jsres/" + jsresNames[i]);
			boolean isProd = false;
			boolean isDev = false;
			if (new File(jsresDirs[i], "prod").exists())
				isProd = true;
			if (new File(jsresDirs[i], "dev").exists())
				isDev = true;
			if (myJsresJsonObject.has(jsresNames[i]))
				myJsresJsonObject.remove(jsresNames[i]);
			if (isProd || isDev) {
				JsonObject nowJsresVers = new JsonObject();
				if (isProd) {
					nowJsresVers.addProperty("prod", Do.getText(new File(jsresDirs[i], "prod/.version.txt")));
				}
				if (isDev) {
					nowJsresVers.addProperty("dev", Do.getText(new File(jsresDirs[i], "dev/.version.txt")));
				}
				myJsresJsonObject.add(jsresNames[i], nowJsresVers);
			}
		}
		return myJsresJsonObject.size() == 0 ? null : myJsresJsonObject;
	}

	public static void helpJsRes() {
		Do.showImgDialogCust(MainActivity.main, MainActivity.main.getString(R.string.jsres_help1_title),
				R.string.jsres_help1_msg, R.drawable.jsres_1, R.string.next, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2) {
						Do.showImgDialog(MainActivity.main, R.string.jsres_help2_title, R.string.jsres_help2_msg,
								R.drawable.jsres_2);
					}
				}, false);
	}

	public static void chooseJsres(final onChooseJsresListener onClick, onChooseJsresListener onNull) {
		try {

			{
				ScrollView Sv = new ScrollView(MainActivity.main);
				Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
				Sv.setFillViewport(true);
				RelativeLayout Rl = new RelativeLayout(MainActivity.main);
				Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
				LinearLayout ly = new LinearLayout(MainActivity.main);
				ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
				ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
						Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
				ly.setOrientation(LinearLayout.VERTICAL);
				Rl.addView(ly);
				Sv.addView(Rl);
				JsonObject myJsresJsonObject = getMyJsres();
				if (myJsresJsonObject == null) {
					onNull.onClick(null, null, -1);
					return;
				}
				Dl Adb = new Dl(MainActivity.main);
				Adb.builder.setTitle(R.string.choose_jsres);
				Adb.builder.setView(Sv);
				final AlertDialog Ad = Adb.create();
				for (int i = 0; i < jsLinkArr.length; i++) {
					if (myJsresJsonObject.has(jsLinkArr[i][2].toString())) {
						final JsonObject nowJsresVers = (JsonObject) myJsresJsonObject.get(jsLinkArr[i][2].toString());
						final String name = jsLinkArr[i][2].toString();
						StringBuilder sbVers = new StringBuilder();
						/*
						 * sbVers:
						 * 生产环境版本：xxx
						 * 开发环境版本：xxx
						 */
						if (nowJsresVers.has("prod"))
							sbVers.append(MainActivity.main.getString(R.string.prod)
									+ nowJsresVers.get("prod").getAsString());
						if (nowJsresVers.has("dev"))
							sbVers.append(
									(nowJsresVers.has("prod") ? "\n" : "") + MainActivity.main.getString(R.string.dev)
											+ nowJsresVers.get("dev").getAsString());
						ButtonLayout bl = new ButtonLayout(MainActivity.main);

						Drawable icon = (Drawable) jsLinkArr[i][3];
						icon.setTint(ContextCompat.getColor(MainActivity.main, R.color.opposition));
						bl.leftImg.setImageDrawable(icon);
						bl.titleTv.setText(jsLinkArr[i][2].toString());
						bl.descriptionTv.setText(sbVers.toString());
						int finalI = i;
						bl.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View p1) {
								Ad.dismiss();
								Dl AdbManagerSelectedJsres = new Dl(MainActivity.main);
								AdbManagerSelectedJsres.builder
										.setTitle(MainActivity.main.getString(R.string.choose) + " " + name);
								final AlertDialog Ad = AdbManagerSelectedJsres.create();

								ScrollView Sv = new ScrollView(MainActivity.main);
								Sv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
								Sv.setFillViewport(true);
								RelativeLayout Rl = new RelativeLayout(MainActivity.main);
								Rl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
								LinearLayout ly = new LinearLayout(MainActivity.main);
								ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
								ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
										Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
								ly.setOrientation(LinearLayout.VERTICAL);
								Rl.addView(ly);
								Sv.addView(Rl);
								if (nowJsresVers.has("prod")) {
									ButtonLayout bl = new ButtonLayout(MainActivity.main);
									bl.leftImg.setVisibility(View.GONE);
									bl.titleTv.setText(MainActivity.main.getString(R.string.prod)
											+ nowJsresVers.get("prod").getAsString());
									bl.descriptionTv.setText(R.string.jsres_touch_choose);
									bl.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View p1) {
											Ad.dismiss();
											File target = new File(Vers.i.FVenter, ".jsres/" + name + "/prod/.main.js");
											onClick.onClick(target, name, finalI);
										}
									});
									ly.addView(bl);
								}
								if (nowJsresVers.has("dev")) {
									ButtonLayout bl = new ButtonLayout(MainActivity.main);
									bl.leftImg.setVisibility(View.GONE);
									bl.titleTv.setText(MainActivity.main.getString(R.string.dev)
											+ nowJsresVers.get("dev").getAsString());
									bl.descriptionTv.setText(R.string.jsres_touch_choose);
									bl.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View p1) {
											Ad.dismiss();
											File target = new File(Vers.i.FVenter, ".jsres/" + name + "/dev/.main.js");
											onClick.onClick(target, name, finalI);
										}
									});
									ly.addView(bl);
								}
								Ad.setView(Sv);
								Ad.show();
							}
						});

						ly.addView(bl);
					}
				}
				Ad.show();
			}
		} catch (Exception e) {
			Do.showErrDialog(MainActivity.main, e);
		}
	}

	public static void regJsresProject() {
		final Map vMOpenFile = new HashMap();
		if (MainActivity.main.binding.Ep.MSID.size() > 0) {
			for (Object IKey : MainActivity.main.binding.Ep.MSID.keySet()) {
				if (((EditorPage.EPItem) (((Object[]) (MainActivity.main.binding.Ep.MSID.get(IKey)))[0])).getFile()
						.getName().toLowerCase().endsWith(".html")) {
					vMOpenFile.put(
							((EditorPage.EPItem) (((Object[]) (MainActivity.main.binding.Ep.MSID.get(IKey)))[0]))
									.getFile(),
							(Editor) (((Object[]) (MainActivity.main.binding.Ep.MSID.get(IKey)))[1]));
				}
			}
		}
		final File rootDir = Vers.i.ProjectDir;
		chooseJsres(new onChooseJsresListener() {

			@Override
			public void onClick(final File chooseFile, final String name, int jsIndex) {
				if (jsLinkArr[jsIndex][1] == null) {
					if (name.equals(MainActivity.main.getString(R.string.bootstrap))) {
						// 注册Bootstrap
						Dl dl = new Dl(MainActivity.main);
						dl.builder
								.setTitle(R.string.inf)
								.setMessage(R.string.reg_bootstrap_tip)
								.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface p1, int p2) {
										new BootstrapManager().reg(vMOpenFile);
									}
								});
						dl.show();
						return;
					} else {
						try {
							((LibraryManager) ((Class) jsLinkArr[jsIndex][7]).newInstance()).reg(vMOpenFile);
						} catch (IllegalAccessException | InstantiationException e) {
							Do.showErrDialog(MainActivity.main, e);
						}
						return;
					}
				}

				Do.showWaitAndRunInThread(false, new Runnable() {
					List allHTMLFileList;

					@Override
					public void run() {
						try {
							Do.copySdcardFile(chooseFile.getPath(), rootDir.getPath() + "/" + name + "_main.js");
							allHTMLFileList = new ArrayList();
							openDir(rootDir);
							for (final Object now : allHTMLFileList) {
								String html;
								if (vMOpenFile.containsKey(now)) {
									html = ((Editor) (vMOpenFile.get(now))).getString();
								} else {
									html = Do.getText((File) now);
								}

								Document doc = Jsoup.parse(html);
								doc.outputSettings().indentAmount(4);
								String path = new File(
										((File) now).getPath().replace(Vers.i.ProjectDir.getPath() + "/", ""))
										.getPath();
								if (path.indexOf("/") == -1) {
									path = path.replace(((File) now).getName(), name + "_main.js");
								} else {
									String[] names = path.split("/");
									StringBuilder sbPath = new StringBuilder();
									for (int i = 0; i < names.length - 1; i++) {
										sbPath.append("../");
									}
									sbPath.append(name + "_main.js");
									path = sbPath.toString();
								}

								boolean isHas = false;
								for (Element nowScript : doc.head().getElementsByTag("script")) {
									if (!nowScript.attributes().hasKey("src"))
										continue;
									else {
										String link = nowScript.attr("src");
										if (link.equals(path))
											isHas = true;
									}
								}
								if (!isHas) {
									doc.head().appendElement("script").attr("src", path);
									final String newHtml = doc.html();
									if (vMOpenFile.containsKey(now)) {
										MainActivity.main.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												((Editor) (vMOpenFile.get(now))).replaceAll(newHtml);
											}
										});
									} else {
										Do.write(newHtml, (File) now);
									}
								}
							}
							MainActivity.main.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Do.finishWaiting();
									MainActivity.main.binding.elvMainExplorerFile
											.toExplorer(new File(Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath));
									Dl dl = new Dl(MainActivity.main);
									dl.builder.setTitle(R.string.done)
											.setMessage(R.string.reg_jsres_done)
											.setPositiveButton(R.string.ok, null);
									dl.show();
								}
							});
						} catch (final Exception e) {
							MainActivity.main.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Do.finishWaiting();
									Do.showErrDialog(MainActivity.main, e);
								}
							});

						}
					}

					private void openDir(File dir) {
						File[] files = dir.listFiles();
						for (File now : files) {
							if (now.isDirectory()) {
								openDir(now);
								continue;
							}
							if (now.getName().toLowerCase().endsWith(".html")) {
								allHTMLFileList.add(now);
							}
						}
					}
				});
			}
		}, new onChooseJsresListener() {

			@Override
			public void onClick(File chooseFile, String name, int jsIndex) {
				MainActivity.main.toast(R.string.no_jsres);
			}
		});
	}

	public static void showAbout() {
		Dl AboutBuilder = new Dl(MainActivity.main);
		final AlertDialog AboutDialog = AboutBuilder.create();
		View dialogView = LayoutInflater.from(MainActivity.main)
				.inflate(R.layout.dialog_about, null);
		Button loveBtn = dialogView.findViewById(R.id.btn_main_about_love);
		Button demoBtn = dialogView.findViewById(R.id.btn_main_about_demo);
		Button feedbackBtn = dialogView.findViewById(R.id.btn_main_about_feedback);
		Button updateBtn = dialogView.findViewById(R.id.btn_main_about_findupdate);
		Button joinBtn = dialogView.findViewById(R.id.btn_main_about_joinchat);
		Button copyrightBtn = dialogView.findViewById(R.id.btn_main_about_copyright);
		Button getHopWebBtn = dialogView.findViewById(R.id.btn_main_about_get_hopweb);
		TextView TvVer = dialogView.findViewById(R.id.tv_ver);
		TextView TvVerJsr = dialogView.findViewById(R.id.tv_ver_jsr);
		TextView TvVerEasyApp = dialogView.findViewById(R.id.tv_ver_easyapp);

		try {
			TvVer.setText(MainActivity.main.getPackageManager().getPackageInfo(MainActivity.main.getPackageName(),
					0).versionName);
			TvVerJsr.setText(MainActivity.main.getString(R.string.jsr_ver) + JssRunner.getVersionName());
		} catch (Exception e) {
			TvVer.setText(R.string.ver_err);
		}

		loveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AboutDialog.dismiss();
				new LoveClass().GiveLove(MainActivity.main);
			}
		});
		demoBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AboutDialog.dismiss();
				Uri uri = Uri.parse(Vers.i.supportHost + "easywebide");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				MainActivity.main.startActivity(intent);
			}
		});
		feedbackBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				Toast.makeText(MainActivity.main, R.string.feedback_msg, Toast.LENGTH_SHORT).show();
				Uri uri = Uri.parse("https://www.coolapk.com/apk/com.hiweb.ide");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				MainActivity.main.startActivity(intent);
				AboutDialog.dismiss();
			}
		});
		updateBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AboutDialog.dismiss();
				MainActivity.main.findUpdate(true);
				AboutDialog.dismiss();
			}
		});
		joinBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				AboutDialog.dismiss();
				Do.showGroup(MainActivity.main);
			}
		});
		copyrightBtn.setOnClickListener((view) -> {
			Dl dlCopy = new Dl(MainActivity.main);
			dlCopy.builder
					.setTitle(R.string.copyright)
					.setMessage(R.string.copyright_msg)
					.setPositiveButton(R.string.ok, null);
			dlCopy.show();
		});

		File libVersion = new File(MainActivity.main.getFilesDir(), "packingLibrary/EasyApp_version.txt");
		String version = null;
		try {
			version = Do.getText(libVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (version != null) {
			TvVerEasyApp.setVisibility(View.VISIBLE);
			TvVerEasyApp.setText(MainActivity.main.getText(R.string.easyapp_ver) + version);
		}

		if (Vers.i.isInstalledHopWeb) {
			getHopWebBtn.setVisibility(View.GONE);
		}
		getHopWebBtn.setOnClickListener(p1 -> Do.showHopWebDialog());
		AboutDialog.setView(dialogView);
		AboutDialog.show();
	}

	public abstract interface onChooseJsresListener {
		public abstract void onClick(File chooseFile, String name, int jsIndex);
	}
}
