package com.hiweb.ide;

import com.hiweb.ide.edit.*;
import com.google.gson.*;
import android.widget.*;
import com.hiweb.ide.add.addViewWidget.*;
import android.app.*;
import android.view.*;
import java.io.*;
import android.content.*;

public class ExamplesClass {
	public static MainActivity main;
	public static String jsUrl = Vers.i.serverHost + "server/hiweb/examples/.examples.js";
	private static ScrollView Sv;

	public static void showMainDialog() {
		main = MainActivity.main;
		Sv = new ScrollView(main);
		Dl dl = new Dl(main);
		dl.builder.setTitle(R.string.main_File_example);
		dl.builder.setView(Sv);
		dl.builder.setNeutralButton(R.string.contribution_plan, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				Do.openUrl(Vers.i.supportHost + "easywebide/article/#8", main);
			}
		});
		final AlertDialog ad = dl.create();
		Do.showWaitAndRunInThread(true, new Runnable() {

			@Override
			public void run() {
				try {
					String jsText = Do.testGetHtml(jsUrl, 6);
					if (jsText == null) {
						throw new Exception();
					}
					String jsonText = jsText.split("=")[1];
					JsonArray jsonArray = (JsonArray) new JsonParser().parse(jsonText);

					// 遍历 jsonArray ，循环创建 ButtonLayout 并加入到 LinearLayout 里。
					LinearLayout Ly;
					Sv.setFillViewport(true);
					RelativeLayout Rl = new RelativeLayout(main);
					Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
					Ly = new LinearLayout(main);
					Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
					Ly.setOrientation(LinearLayout.VERTICAL);
					Ly.setPadding(Do.dp2px(main, 10), Do.dp2px(main, 10), Do.dp2px(main, 10), Do.dp2px(main, 10));
					Sv.addView(Rl);
					Rl.addView(Ly);

					for (int i = jsonArray.size() - 1; i > -1; i--) {
						JsonObject item = (JsonObject) jsonArray.get(i);
						final ExampleButton btn = new ExampleButton(main, item);
						Ly.addView(btn);

						btn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View p1) {
								ad.dismiss();

								String name = btn.realName;
								final String url = Vers.i.serverHost + "server/hiweb/examples/" + btn.name;
								final File zipFile = new File(main.getFilesDir(), btn.name);
								final File exampleFile = new File(Vers.i.FVenter, "example/" + name);
								Do.showWaitAndRunInThread(false, new Runnable() {

									@Override
									public void run() {
										zipFile.delete();
										try {
											Do.downloadNet(url, zipFile.getPath());
											if (exampleFile.exists()) {
												Do.delete(exampleFile.getPath());
											}
											exampleFile.mkdirs();
											Do.upZipFileDir(zipFile, exampleFile);

											main.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													Do.finishWaiting();
													main.OpenWeb(exampleFile, true);
												}
											});
										} catch (final Exception e) {
											main.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													Do.finishWaiting();
													Do.showErrDialog(main, e);
												}
											});
										}
									}
								});
							}
						});
					}
					main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							ad.show();
						}
					});
				} catch (final Exception e) {
					main.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Do.finishWaiting();
							Do.showErrDialog(main, e);
						}
					});
				}
			}
		});
	}
}

class ExampleButton extends ButtonLayout {
	MainActivity main;
	public String name;
	public String realName;
	public String typeName;
	public String time;

	public ExampleButton(MainActivity main, JsonObject exampleObject) {
		super(main);
		this.main = main;
		name = exampleObject.get("name").getAsString();
		realName = name.substring(0, name.length() - 4);
		typeName = getTypeName(exampleObject.get("type").getAsInt());
		time = exampleObject.get("time").getAsString();

		titleTv.setText(realName);
		descriptionTv.setText(time + " • " + typeName);

	}

	private String getTypeName(int type) {
		switch (type) {
			case -1:
				return main.getString(R.string.html_website);
			case 0:
				return main.getString(R.string.ewserver_website);
			case 1:
				return main.getString(R.string.php_website);
			case 2:
				return main.getString(R.string.easyapps_website);
			default:
				return main.getString(R.string.other_website);
		}
	}
}
