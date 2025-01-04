package com.hiweb.ide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hiweb.ide.add.addViewWidget.ChoiceLayout;
import com.hiweb.ide.add.addViewWidget.ColorLayout;
import com.hiweb.ide.add.addViewWidget.TextLayout;
import com.hiweb.ide.edit.Do;
import com.hiweb.ide.edit.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kellinwood.security.zipsigner.ZipSigner;

public class PackMachine {
    public File packDir = new File(Vers.i.FVenter, "packing_cache");
    public File packLibDir = new File(MainActivity.main.getFilesDir(), "packingLibrary");
    public File packLibZip = new File(packDir, "packLib.zip");// 下载的打包库压缩包
    public File aapt = new File(packLibDir, "aapt");
    public File androidJar = new File(packLibDir, "android.jar");
    public File apk_ = new File(packLibDir, "apk_.zip");
    public File libVersion = new File(packLibDir, "EasyApp_version.txt");

    public File apk_res = new File(packDir, "apk_res");

    public File res = new File(apk_res, "res");
    public File icon = new File(res, "drawable/ic_launcher.png");
    public File manifest = new File(apk_res, "AndroidManifest.xml");
    public File strings = new File(res, "values/strings.xml");

    public File classes = new File(apk_res, "classes.dex");

    public String titlebarColor = "#3F51B5";
    public boolean titlebarVisible = true;
    public boolean isLongClick = true;
    public boolean isShowWait = true;
    public boolean isAllowZoom = true;
    public boolean isZoomAsPC = false;
    public boolean isAllowEasyApp = false;

    public String appName = "MyWebApp";
    public String packName = "com.mycompany.mywebapp";
    public String verName = "1.0.0";
    public String verCode = "1";
    public String iconPath = null;

    public File favicon = new File(packDir, "favicon.png");

    public File makeAPKEasyweb;

    public void show(boolean isCreatingProj) {
        if (isCreatingProj)
            isAllowEasyApp = true;
        try {
            if (checkAndDownload()) {
                Do.showWaitAndRunInThread(false, () -> {
                    try {
                        Do.delete(packDir.getPath());
                        apk_res.mkdirs();
                        if (!Do.upZipFileDir(apk_, apk_res))
                            throw new Exception("Unzip failed.");

                        MainActivity.main.runOnUiThread(() -> {
                            Do.finishWaiting();

                            try {
                                final LinearLayout ly = new LinearLayout(MainActivity.main);
                                ly.setOrientation(LinearLayout.HORIZONTAL);
                                ly.setGravity(Gravity.CENTER);
                                ly.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                                ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
                                        Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));

                                final ImageView iv = new ImageView(MainActivity.main);
                                iv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(MainActivity.main, 50),
                                        Do.dp2px(MainActivity.main, 50)));
                                if (iconPath == null || !new File(iconPath).exists()) {
                                    Do.copySdcardFile(icon.getPath(), favicon.getPath());
                                    iconPath = null;
                                }

                                Bitmap bitmap = BitmapFactory
                                        .decodeFile(iconPath == null ? favicon.getPath() : iconPath);
                                iv.setImageBitmap(bitmap);
                                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                iv.setOnClickListener(p1 -> {
                                    FileChooseClass fileChooseClass = new FileChooseClass();
                                    fileChooseClass.Type(true);
                                    if (Vers.i.IsOpenProject) {
                                        fileChooseClass.setOpenPath(Vers.i.ProjectDir);
                                    } else {
                                        fileChooseClass.setOpenPath(Environment.getExternalStorageDirectory());
                                    }
                                    fileChooseClass.setOnFileClickListener(new OnFileClickListener() {

                                        @Override
                                        public void onClick(File ChooseFile, AlertDialog dialog) {
                                            if (ChooseFile.getName().toLowerCase().endsWith(".png")) {
                                                if (!ChooseFile.getPath().startsWith(Vers.i.ProjectDir.getPath())) {
                                                    Dl dl = new Dl(MainActivity.main);
                                                    dl.builder
                                                            .setTitle(R.string.inf)
                                                            .setMessage(R.string.icon_need_to_be_moven)
                                                            .setPositiveButton(R.string.yes, (dialog1, which) -> {
                                                                File projIcon = new File(Vers.i.ProjectDir,
                                                                        ChooseFile.getName());
                                                                try {
                                                                    Do.copySdcardFile(ChooseFile.getPath(),
                                                                            projIcon.getPath());
                                                                    MainActivity.main.binding.elvMainExplorerFile
                                                                            .toExplorer(new File(Vers.i.ProjectDir
                                                                                    + Vers.i.ExplorerPath));
                                                                    Bitmap bitmap1 = BitmapFactory
                                                                            .decodeFile(projIcon.getPath());
                                                                    dialog.dismiss();
                                                                    iv.setImageBitmap(bitmap1);
                                                                    iconPath = projIcon.getPath();
                                                                } catch (Exception e) {
                                                                    MainActivity.main.toast(R.string.error);
                                                                }

                                                            });
                                                    dl.show();
                                                    return;
                                                }
                                                dialog.dismiss();
                                                try {
                                                    Bitmap bitmap1 = BitmapFactory.decodeFile(ChooseFile.getPath());
                                                    iv.setImageBitmap(bitmap1);
                                                    iconPath = ChooseFile.getPath();
                                                } catch (Exception e) {
                                                    Do.showErrDialog(MainActivity.main, e);
                                                }
                                            } else {
                                                Toast.makeText(MainActivity.main, R.string.pack_apk_choose_icon,
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    });
                                    fileChooseClass.Show(MainActivity.main);
                                });
                                ly.addView(iv);

                                LinearLayout lySet = new LinearLayout(MainActivity.main);
                                lySet.setOrientation(LinearLayout.VERTICAL);
                                lySet.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1));
                                lySet.setGravity(Gravity.CENTER);
                                lySet.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
                                        Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
                                ly.addView(lySet);

                                final TextLayout tlName = new TextLayout(MainActivity.main);
                                tlName.build(-1, null, R.string.pack_apk_name, null);
                                tlName.Acet.setText(appName);
                                lySet.addView(tlName);

                                final TextLayout tlPackName = new TextLayout(MainActivity.main);
                                tlPackName.build(-1, null, R.string.pack_apk_packname, null);
                                tlPackName.Acet.setText(packName);
                                lySet.addView(tlPackName);

                                final TextLayout tlVersionName = new TextLayout(MainActivity.main);
                                tlVersionName.build(-1, null, R.string.pack_apk_version, null);
                                tlVersionName.Acet.setText(verName);
                                lySet.addView(tlVersionName);

                                final TextLayout tlVersionCode = new TextLayout(MainActivity.main);
                                tlVersionCode.build(-1, null, R.string.pack_apk_versionCode, null);
                                tlVersionCode.Acet.setText(verCode);
                                Do.onlyCanEdit(tlVersionCode.Acet, "1234567890", -1);
                                lySet.addView(tlVersionCode);

                                final TextView tvIcon = new TextView(MainActivity.main);
                                tvIcon.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                                tvIcon.setText(R.string.pack_apk_icon_tip);
                                lySet.addView(tvIcon);

                                Dl dl = new Dl(MainActivity.main);
                                dl.builder.setTitle(isCreatingProj ? R.string.set_this_proj : R.string.pack_apk);
                                dl.builder.setView(ly);
                                if (!isCreatingProj)
                                    dl.builder.setPositiveButton(R.string.start_convert, null);
                                dl.builder.setNegativeButton(R.string.save_config, null);
                                dl.builder.setNeutralButton(R.string.super_settings, null);
                                if (isCreatingProj)
                                    dl.builder.setCancelable(false);
                                final AlertDialog ad = dl.show();
                                ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(p1 -> {
                                    final String name = tlName.getText("$");
                                    final String packname = tlPackName.getText("$");
                                    final String versionName = tlVersionName.getText("$");
                                    final String versionCode = tlVersionCode.getText("$");

                                    if (name == null || packname == null || versionName == null
                                            || versionCode == null) {
                                        MainActivity.main.toast(R.string.add_table_err);
                                        return;
                                    }

                                    String config = getManifestText(name, packname, versionName, versionCode);

                                    final FileChooseClass fileChooseClass = new FileChooseClass();
                                    fileChooseClass.Type(false);
                                    if (Vers.i.IsOpenProject) {
                                        fileChooseClass.setOpenPath(Vers.i.ProjectDir);
                                    } else {
                                        fileChooseClass.setOpenPath(Environment.getExternalStorageDirectory());
                                    }
                                    fileChooseClass.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View p1) {
                                            fileChooseClass.dialog.dismiss();
                                            ad.dismiss();

                                            produceAPK(config, new File(fileChooseClass.nowPath, appName + ".apk"));
                                        }
                                    });
                                    fileChooseClass.Show(MainActivity.main);
                                });
                                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(p1 -> {
                                    final String name = tlName.getText("$");
                                    final String packname = tlPackName.getText("$");
                                    final String versionName = tlVersionName.getText("$");
                                    final String versionCode = tlVersionCode.getText("$");

                                    if (name == null || packname == null || versionName == null
                                            || versionCode == null) {
                                        MainActivity.main.toast(R.string.add_table_err);
                                        return;
                                    }

                                    Dl dlAsk = new Dl(MainActivity.main);
                                    dlAsk.builder
                                            .setTitle(R.string.save_config_as_exe)
                                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                                String config = getManifestText(name, packname, versionName,
                                                        versionCode);
                                                try {
                                                    makeAPKEasyweb = new File(Vers.i.ProjectDir, "MakeAPK.hiweb");
                                                    makeAPKEasyweb.createNewFile();
                                                    Do.write(config, makeAPKEasyweb);
                                                    MainActivity.main.binding.elvMainExplorerFile.toExplorer(
                                                            new File(Vers.i.ProjectDir + Vers.i.ExplorerPath));
                                                    new Dl(MainActivity.main, R.string.done,
                                                            R.string.save_config_successfully, R.string.ok);
                                                } catch (IOException e) {
                                                    MainActivity.main.toast(R.string.error);
                                                }

                                                ad.dismiss();
                                            })
                                            .setNegativeButton(R.string.cancel, null);
                                    dlAsk.show();
                                });
                                ad.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(p1 -> {
                                    ScrollView Sv = new ScrollView(MainActivity.main);
                                    Sv.setFillViewport(true);
                                    RelativeLayout Rl = new RelativeLayout(MainActivity.main);
                                    Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
                                    LinearLayout Ly = new LinearLayout(MainActivity.main);
                                    Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
                                    Ly.setOrientation(LinearLayout.VERTICAL);
                                    Ly.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
                                            Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));
                                    Sv.addView(Rl);
                                    Rl.addView(Ly);

                                    final ColorLayout colorLy = new ColorLayout(MainActivity.main);
                                    colorLy.build(-1, null, R.string.titlebar_color, null);
                                    colorLy.Acet.setText(titlebarColor.substring(1));
                                    Ly.addView(colorLy);
                                    final ChoiceLayout visibleCl = new ChoiceLayout(MainActivity.main);
                                    visibleCl.build(false, -1, null, R.string.titlebar_visible,
                                            MainActivity.main.getString(R.string.enable) + "|"
                                                    + MainActivity.main.getString(R.string.disable));
                                    visibleCl.Acs.setSelection(titlebarVisible ? 1 : 0, false);
                                    Ly.addView(visibleCl);
                                    final ChoiceLayout longCliclCl = new ChoiceLayout(MainActivity.main);
                                    longCliclCl.build(false, -1, null, R.string.long_onclick,
                                            MainActivity.main.getString(R.string.enable) + "|"
                                                    + MainActivity.main.getString(R.string.disable));
                                    longCliclCl.Acs.setSelection(isLongClick ? 0 : 1, false);
                                    Ly.addView(longCliclCl);
                                    final ChoiceLayout waitCl = new ChoiceLayout(MainActivity.main);
                                    waitCl.build(false, -1, null, R.string.show_wait_dialog,
                                            MainActivity.main.getString(R.string.enable) + "|"
                                                    + MainActivity.main.getString(R.string.disable));
                                    waitCl.Acs.setSelection(isShowWait ? 0 : 1, false);
                                    Ly.addView(waitCl);
                                    final ChoiceLayout allowZoomCl = new ChoiceLayout(MainActivity.main);
                                    allowZoomCl.build(false, -1, null, R.string.allow_zoom,
                                            MainActivity.main.getString(R.string.enable) + "|"
                                                    + MainActivity.main.getString(R.string.disable));
                                    allowZoomCl.Acs.setSelection(isAllowZoom ? 0 : 1, false);
                                    Ly.addView(allowZoomCl);
                                    final ChoiceLayout zoomAsPCCl = new ChoiceLayout(MainActivity.main);
                                    zoomAsPCCl.build(false, -1, null, R.string.zoom_as_pc,
                                            MainActivity.main.getString(R.string.enable) + "|"
                                                    + MainActivity.main.getString(R.string.disable));
                                    zoomAsPCCl.Acs.setSelection(isZoomAsPC ? 0 : 1, false);
                                    Ly.addView(zoomAsPCCl);

                                    final ChoiceLayout allowJSAppActionsCl = new ChoiceLayout(MainActivity.main);
                                    allowJSAppActionsCl.build(false, -1,
                                            MainActivity.main.getString(R.string.allow_easyapps),
                                            R.string.allow_easyapps_description,
                                            MainActivity.main.getString(R.string.enable) + "|"
                                                    + MainActivity.main.getString(R.string.disable));
                                    allowJSAppActionsCl.Acs.setSelection(isAllowEasyApp ? 0 : 1, false);
                                    Button jsAppActionsInfBtn = new Button(MainActivity.main, null,
                                            R.attr.buttonBarButtonStyle);
                                    jsAppActionsInfBtn.setText(R.string.easyapps_inf);
                                    jsAppActionsInfBtn.setTextColor(Color.parseColor("#1593E5"));
                                    jsAppActionsInfBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                                    jsAppActionsInfBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                                    jsAppActionsInfBtn.setAllCaps(false);
                                    jsAppActionsInfBtn.setOnClickListener(v -> {
                                        EasyAppInformation.showInfDialog();
                                    });
                                    allowJSAppActionsCl.setGravity(Gravity.LEFT);
                                    allowJSAppActionsCl.addView(jsAppActionsInfBtn);

                                    Ly.addView(allowJSAppActionsCl);

                                    Dl dl1 = new Dl(MainActivity.main);
                                    dl1.builder.setTitle(R.string.super_settings);
                                    dl1.builder.setView(Sv);
                                    dl1.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface p1, int p2) {
                                            String color = colorLy.getText("$");
                                            try {
                                                Color.parseColor(color);
                                            } catch (Exception e) {
                                                MainActivity.main.toast(R.string.color_err);
                                                return;
                                            }
                                            titlebarColor = color;
                                            titlebarVisible = visibleCl.getText("$")
                                                    .equals(MainActivity.main.getString(R.string.disable));
                                            isLongClick = longCliclCl.getText("$")
                                                    .equals(MainActivity.main.getString(R.string.enable));
                                            isShowWait = waitCl.getText("$")
                                                    .equals(MainActivity.main.getString(R.string.enable));
                                            isAllowZoom = allowZoomCl.getText("$")
                                                    .equals(MainActivity.main.getString(R.string.enable));
                                            isZoomAsPC = zoomAsPCCl.getText("$")
                                                    .equals(MainActivity.main.getString(R.string.enable));
                                            isAllowEasyApp = allowJSAppActionsCl.getText("$")
                                                    .equals(MainActivity.main.getString(R.string.enable));
                                        }
                                    });
                                    dl1.show();
                                });
                            } catch (Exception e) {
                                MainActivity.main.toast(R.string.error);
                            }
                        });
                    } catch (Exception e) {
                        MainActivity.main.runOnUiThread(() -> {
                            Do.finishWaiting();
                            MainActivity.main.toast(R.string.error);
                        });
                    }
                });
            }
        } catch (Exception e) {
            Do.showErrDialog(MainActivity.main, e);
        }

    }

    public boolean checkAndDownload() throws Exception {
        if (Vers.i.isDownloadingPacks) {
            MainActivity.main.toast(R.string.wait);
            return false;
        }

        Dl dl = new Dl(MainActivity.main);
        dl.builder.setTitle(R.string.inf);
        if (libVersion.exists()) {
            String versionName = Do.getText(libVersion);
            if (Vers.i.easyAppNewestVersion == null)
                return true;
            else {
                if (Do.compareVersion(Vers.i.easyAppNewestVersion, versionName) > 0) {
                    dl.builder.setMessage(R.string.pack_apk_update);
                } else
                    return true;
            }
        } else {
            dl.builder.setMessage(R.string.pack_apk_download);
        }
        dl.builder.setPositiveButton(R.string.download, (p1, p2) -> Do.showWaitAndRunInThread(true, new Runnable() {

            @Override
            public void run() {
                try {
                    Vers.i.isDownloadingPacks = true;
                    packDir.mkdirs();
                    Do.downloadNet(Vers.i.serverHost + "server/hiweb/easyapp/packLib.zip", packLibZip.getPath());
                    packLibDir.mkdirs();
                    if (!Do.upZipFileDir(packLibZip, packLibDir))
                        throw new Exception("Unzip failed.");
                    libVersion.createNewFile();
                    Do.write(Vers.i.easyAppNewestVersion, libVersion);
                    EasyAppInformation.loadFunctions(MainActivity.main.getFilesDir());
                    MainActivity.main.runOnUiThread(() -> {
                        Do.finishWaiting();

                        Vers.i.isDownloadingPacks = false;
                        Dl dlDone = new Dl(MainActivity.main);
                        dlDone.builder.setTitle(R.string.done);
                        dlDone.builder.setMessage(R.string.pack_apk_download_msg);
                        dlDone.builder.setPositiveButton(R.string.ok, null);
                        dlDone.show();
                    });
                } catch (final Exception e) {
                    packLibZip.delete();
                    MainActivity.main.runOnUiThread(() -> {
                        Do.finishWaiting();

                        Vers.i.isDownloadingPacks = false;
                        Dl dlDone = new Dl(MainActivity.main);
                        dlDone.builder.setTitle(R.string.error);
                        dlDone.builder.setMessage(MainActivity.main.getString(R.string.pack_apk_download_error) + "\n"
                                + MainActivity.main.getString(R.string.download_error));
                        dlDone.builder.setPositiveButton(R.string.ok, null);
                        dlDone.builder.setNeutralButton(R.string.solve_proj, (p11, p21) -> Do
                                .openUrl(Vers.i.supportHost + "easywebide/article/#6", MainActivity.main));
                        dlDone.show();
                    });
                }
            }
        }));
        dl.show();
        return false;
    }

    private String getManifestText(String name, String pack, String versionName, String versionCode) {
        makeAPKEasyweb = new File(Vers.i.ProjectDir, "MakeAPK.hiweb");

        appName = name;
        packName = pack;
        verName = versionName;
        verCode = versionCode;

        JsonObject baseConfig = new JsonObject();
        baseConfig.addProperty("icon",
                iconPath == null ? null : Do.getRelativePath(makeAPKEasyweb.getPath(), iconPath));
        baseConfig.addProperty("appName", appName);
        baseConfig.addProperty("packName", packName);
        baseConfig.addProperty("versionName", verName);
        baseConfig.addProperty("versionCode", verCode);

        return titlebarColor + "|"
                + titlebarVisible + "|"
                + isLongClick + "|"
                + isShowWait + "|"
                + isAllowZoom + "|"
                + isZoomAsPC + "|"
                + isAllowEasyApp + "|"
                + baseConfig;
    }

    private void applyManifest(String config) throws Exception {
        String[] array = config.split("\\|");
        String json = array[array.length - 1];
        JsonObject object = (JsonObject) JsonParser.parseString(json);

        if (object.get("icon").isJsonNull()) {
            iconPath = null;
            Do.copySdcardFile(icon.getPath(), favicon.getPath());
        } else {
            iconPath = Vers.i.ProjectDir.getPath() + "/" + object.get("icon").getAsString();
        }
        appName = object.get("appName").getAsString();
        packName = object.get("packName").getAsString();
        verName = object.get("versionName").getAsString();
        verCode = object.get("versionCode").getAsString();

        titlebarColor = array[0];
        titlebarVisible = Boolean.parseBoolean(array[1]);
        isLongClick = Boolean.parseBoolean(array[2]);
        isShowWait = Boolean.parseBoolean(array[3]);
        isAllowZoom = Boolean.parseBoolean(array[4]);
        isZoomAsPC = Boolean.parseBoolean(array[5]);
        isAllowEasyApp = Boolean.parseBoolean(array[6]);
    }

    private void checkEasyAppEventsFile(DialogInterface.OnClickListener listener) {
        File easyAppEvents = new File(packLibDir, "easyapp-events.js");
        File easyAppEventsSite = new File(Vers.i.ProjectDir, "easyapp-events.js");
        if (!easyAppEventsSite.exists() && isAllowEasyApp) {
            Dl dl = new Dl(MainActivity.main);
            dl.builder
                    .setTitle(R.string.need_notic)
                    .setMessage(R.string.easyapp_events_not_exist)
                    .setPositiveButton(R.string.yes, (p1, p2) -> {
                        try {
                            Do.copySdcardFile(easyAppEvents.getPath(), easyAppEventsSite.getPath());
                            listener.onClick(null, -1);
                        } catch (Exception e) {
                            Do.showErrDialog(MainActivity.main, e);
                        }
                    });
            dl.show();
        } else
            listener.onClick(null, -1);
    }

    private void produceAPK(String config, File outAPK) {
        checkEasyAppEventsFile((dialogInterface, i) -> {
            final File apk = new File(packDir, appName + "_unsigned.apk");
            Do.showWaitAndRunInThread(false, () -> {
                try {
                    outAPK.delete();
                    outAPK.getParentFile().mkdirs();

                    Do.copySdcardFile(iconPath == null ? favicon.getPath() : iconPath, icon.getPath());

                    String strStrings = Do.getText(strings);
                    strStrings = strStrings.replace("$appName$", appName);
                    Do.write(strStrings, strings);

                    String strManifest = Do.getText(manifest);
                    strManifest = strManifest
                            .replace("$packName$", packName)
                            .replace("$versionName$", verName)
                            .replace("$versionCode$", verCode);
                    Do.write(strManifest, manifest);

                    if (!aapt.setExecutable(true)) {
                        Do.finishWaiting();
                        MainActivity.main.runOnUiThread(() -> {
                            MainActivity.main.toast(R.string.aapt_failed);
                        });
                        return;
                    }

                    String aaptMsg = Do.runCommand(new String[] {
                            aapt.getPath(),
                            "package",
                            "-f",
                            "-M",
                            manifest.getPath(),
                            "-S",
                            res.getPath(),
                            "-I",
                            androidJar.getPath(),
                            "-F",
                            apk.getPath()
                    });

                    if (!apk.exists()) {
                        Do.finishWaiting();
                        MainActivity.main.runOnUiThread(() -> {
                            new Dl(MainActivity.main, MainActivity.main.getString(R.string.error_msg), aaptMsg,
                                    MainActivity.main.getString(R.string.ok));
                        });
                        return;
                    }

                    File apkUnzipDir = new File(packDir, appName);
                    apkUnzipDir.mkdirs();
                    if (!Do.upZipFileDir(apk, apkUnzipDir))
                        throw new Exception("Unzip failed.");
                    Do.copySdcardFile(classes.getPath(), new File(apkUnzipDir, "classes.dex").getPath());
                    File assets = new File(apkUnzipDir, "assets");
                    assets.mkdirs();
                    for (File now : Vers.i.ProjectDir.listFiles()) {
                        if (now.isFile()) {
                            Do.copySdcardFile(now.getPath(), new File(assets, now.getName()).getPath());
                        } else {
                            Do.copyDir(now.getPath() + "/", new File(assets, now.getName()).getPath() + "/");
                        }
                    }
                    new File(assets, "manifest.json").delete();
                    File manifestJSONFile = new File(assets, "manifest");
                    manifestJSONFile.createNewFile();
                    Do.write(config, manifestJSONFile);
                    apk.delete();
                    {
                        FileOutputStream fos1 = new FileOutputStream(apk);
                        ZipUtil.toZip(apkUnzipDir.getPath(), fos1, true);
                        // sign
                        ZipSigner zip = new ZipSigner();
                        // zip.setKeys("test",oCert,getPrivateKey(jks.getPath(),"123456","testkeystore","123456"),null);
                        zip.setKeymode("testkey");
                        zip.signZip(apk.getPath(), outAPK.getPath());
                    }

                    MainActivity.main.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Do.finishWaiting();
                            MainActivity.main.binding.elvMainExplorerFile
                                    .toExplorer(new File(Vers.i.ProjectDir.getPath() + Vers.i.ExplorerPath));
                            MainActivity.main.toast(R.string.done);
                            Do.installApkFile(MainActivity.main, outAPK.getPath());
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
            });
        });
    }

    public void runMakeAPK(File file, boolean isSilence) {
        try {
            if (!checkAndDownload())
                return;
        } catch (Exception e) {
            MainActivity.main.toast(R.string.error);
            return;
        }
        Do.showWaitAndRunInThread(false, () -> {
            try {
                Do.delete(packDir.getPath());
                apk_res.mkdirs();
                if (!Do.upZipFileDir(apk_, apk_res))
                    throw new Exception("Unzip failed.");

                String config = Do.getText(file);

                applyManifest(config);

                MainActivity.main.runOnUiThread(() -> {
                    Do.finishWaiting();

                    if (!isSilence) {
                        Dl dl = new Dl(MainActivity.main);
                        dl.builder
                                .setTitle(R.string.inf)
                                .setMessage(R.string.convert_to_app_in_config)
                                .setPositiveButton(R.string.ok, (dialog, which) -> {
                                    produceAPK(config, new File(Vers.i.ProjectDir, appName + ".apk"));
                                });
                        dl.show();
                    }

                });
            } catch (Exception e) {
                MainActivity.main.runOnUiThread(() -> {
                    Do.finishWaiting();
                    Do.showErrDialog(MainActivity.main, e);
                });
            }
        });
    }
}
