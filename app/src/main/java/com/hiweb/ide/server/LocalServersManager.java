package com.venter.easyweb.server;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import com.google.gson.*;
import com.venter.easyweb.*;
import com.venter.easyweb.add.addViewWidget.*;
import com.venter.easyweb.edit.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.venter.easyweb.server.easyweb_server.*;
import android.app.*;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.venter.easyweb.server.php_server.*;
import com.venter.easyweb.server.other.*;

public class LocalServersManager {
    public void showDialog(final Context c) {
        if (Vers.i.hasShownServerDialog)
            return;
        Vers.i.hasShownServerDialog = true;
        LinearLayout Ly;
        ScrollView Sv = new ScrollView(c);
        Sv.setFillViewport(true);
        RelativeLayout Rl = new RelativeLayout(c);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
        Ly = new LinearLayout(c);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(30, 30, 30, 30);
        Sv.addView(Rl);
        Rl.addView(Ly);

        TextView ActvIP = new TextView(c);
        try {
            ActvIP.setText(c.getString(R.string.server_wifi_ip_title) + Do.getIpAddressString());
        } catch (SocketException e) {
            ActvIP.setText(c.getString(R.string.server_wifi_ip_title) + c.getString(R.string.error));
        }
        Do.CanSelect(ActvIP);
        Ly.addView(ActvIP);

        final Dl Adb = new Dl(c);

        Adb.builder.setTitle(R.string.server_manager);
        Adb.builder.setView(Sv);
        Adb.builder.setPositiveButton(R.string.refresh, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Vers.i.hasShownServerDialog = false;
                showDialog(c);
            }
        });
        Adb.builder.setNegativeButton(R.string.help, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Vers.i.hasShownServerDialog = false;
                Do.showImgDialog(c, R.string.server_import_help_title, R.string.server_import_help_msg, R.drawable.server_help);
            }
        });
        final AlertDialog ad = Adb.create();

        {
            //EasyWeb Server
            ObjectLayout ol = new ObjectLayout(c);
            ol.build(-1, null, R.string.easyweb_server, null);
            ol.Ly.setPadding(Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10));
            ol.Ly.setBackground(c.getDrawable(R.drawable.shape_gray_background));
            Do.setMargin(ol.Ly, Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10));
            Do.setMargin(ol, 0, Do.dp2px(c, 10), 0, Do.dp2px(c, 10));
            ListCup Lc = new ListCup(c);

            if (Vers.isServerOn || (Vers.easyWebServerWebsitesMap != null && !Vers.easyWebServerWebsitesMap.isEmpty())) {
                Switch sOpen = new Switch(c);
                sOpen.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                sOpen.setText(Vers.isServerOn ? c.getString(R.string.enable_easyweb_server) : c.getString(R.string.disable_easyweb_server));
                sOpen.setChecked(Vers.isServerOn);
                sOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton p1, boolean p2) {
                        Vers.i.hasShownServerDialog = false;
                        ad.dismiss();
                        if (p2) {
                            if ((Vers.easyWebServerWebsitesMap == null || Vers.easyWebServerWebsitesMap.isEmpty())) {
                                MainActivity.main.toast(R.string.service_no_website);

                            } else {
                                EasyWebServerReceiver.openServer(c);
                            }
                        } else {
                            EasyWebServerReceiver.shutdownServer(c);
                        }
                    }
                });
                Do.setMargin(ol, 0, Do.dp2px(c, 5), 0, Do.dp2px(c, 5));
                ol.Ly.addView(sOpen);
                Set S = Vers.easyWebServerWebsitesMap.keySet();
                for (Object i : S) {
                    try {
                        ((LinearLayout) (((ServerItem) (Vers.easyWebServerWebsitesMap.get(i)[3])).getParent())).removeView(((ServerItem) (Vers.easyWebServerWebsitesMap.get(i)[3])));
                    } catch (Exception e) {
                    }
                    Lc.addLyView((LocalServersManager.ServerItem) (Vers.easyWebServerWebsitesMap.get(i)[3]));
                }
            } else {
                TextView tv = new TextView(c);
                tv.setText(R.string.service_no_website);
                tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                tv.setGravity(Gravity.CENTER);
                Lc.addLyView(tv);
            }
            ol.Ly.addView(Lc);
            Ly.addView(ol);
        }

        {
            //PHP Web Server
            ObjectLayout ol = new ObjectLayout(c);
            ol.build(-1, null, R.string.php_web_server, null);
            ol.Ly.setPadding(Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10));
            ol.Ly.setBackground(c.getDrawable(R.drawable.shape_gray_background));
            Do.setMargin(ol.Ly, Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10), Do.dp2px(c, 10));
            ListCup Lc = new ListCup(c);

            if (Vers.phpServerWebsitesMap != null && !Vers.phpServerWebsitesMap.isEmpty()) {
                Set S = Vers.phpServerWebsitesMap.keySet();
                for (Object i : S) {
                    try {
                        ((LinearLayout) (((ServerItem) (Vers.phpServerWebsitesMap.get(i)[1])).getParent())).removeView(((ServerItem) (Vers.phpServerWebsitesMap.get(i)[1])));
                    } catch (Exception e) {
                    }
                    Lc.addLyView((LocalServersManager.ServerItem) (Vers.phpServerWebsitesMap.get(i)[1]));
                }
            } else {
                TextView tv = new TextView(c);
                tv.setText(R.string.service_no_website);
                tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                tv.setGravity(Gravity.CENTER);
                Lc.addLyView(tv);
            }

            ol.Ly.addView(Lc);
            Ly.addView(ol);
        }
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface p1) {
                Vers.i.hasShownServerDialog = false;
            }
        });
        ad.show();
    }

    public static void showWebsiteDialog(final Context c, int type, final int port) {
        if (type == 0) {
            showWebsiteDialog(c, (ServerItem) ((Vers.easyWebServerWebsitesMap.get(port))[3]), port, new Runnable() {

                @Override
                public void run() {
                    LinearLayout Ly;
                    ScrollView Sv = new ScrollView(c);
                    Sv.setFillViewport(true);
                    RelativeLayout Rl = new RelativeLayout(c);
                    Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
                    Ly = new LinearLayout(c);
                    Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
                    Ly.setOrientation(LinearLayout.VERTICAL);
                    Ly.setPadding(30, 30, 30, 30);
                    Sv.addView(Rl);
                    Rl.addView(Ly);

                    final NumLayout NlPort = new NumLayout(c);
                    NlPort.build(false, 0, null, R.string.server_wifi_port, R.string.server_wifi_port_8080);
                    NlPort.Acet.setText(port + "");
                    Ly.addView(NlPort);

                    final ChoiceLayout ClForm = new ChoiceLayout(c);
                    ClForm.build(false, 1, null, R.string.server_wifi_website_form, c.getString(R.string.server_wifi_website_form_page) + "|" + c.getString(R.string.server_wifi_website_form_explorer));
                    if ((boolean) (Vers.easyWebServerWebsitesMap.get(port)[0])) {
                        ClForm.Acs.setSelection(0, true);
                    } else {
                        ClForm.Acs.setSelection(1, true);
                    }
                    Ly.addView(ClForm);

                    Dl AdbManifest = new Dl(c);
                    AdbManifest.builder.setTitle(R.string.website_manifest);
                    AdbManifest.builder.setView(Sv);
                    AdbManifest.builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {

                            try {
                                int INewPort = -1;
                                if (NlPort.getText("$") == null) {
                                    INewPort = port;
                                } else {
                                    INewPort = Integer.parseInt(NlPort.getText("$"));
                                    if (INewPort < 0) {
                                        throw new Exception();
                                    }
                                }
                                if (INewPort != port && Do.isServersHasPort(INewPort)) {
                                    MainActivity.main.toast(R.string.server_wifi_port_has);
                                    return;
                                }

                                boolean BIsWebsite;
                                String SType = ClForm.getText("$");
                                if (SType.equals(c.getString(R.string.server_wifi_website_form_page))) {
                                    BIsWebsite = true;
                                } else if (SType.equals(c.getString(R.string.server_wifi_website_form_explorer))) {
                                    BIsWebsite = false;
                                } else {
                                    throw new Exception();
                                }

                                LinearLayout lySiBefore = null;
                                try {
                                    lySiBefore = ((LinearLayout) (((ViewGroup) (Vers.easyWebServerWebsitesMap.get(port)[3])).getParent()));
                                } catch (Exception e) {
                                }

                                Object[] old = Vers.easyWebServerWebsitesMap.get(port);

                                Map oldRegs = null;
                                if (Vers.easyWebServerRegsMap.containsKey(port))
                                    oldRegs = (Map) Vers.easyWebServerRegsMap.get(port);

                                if (INewPort != port) {
                                    boolean isNowWebsite=false;
                                    if (port == Vers.i.nowWebsitePort) {
                                        isNowWebsite=true;
                                    }
                                    EasyWebServerReceiver.deleteWebsite(true, port);

                                    if(isNowWebsite)
                                    {
                                        Vers.i.nowWebsitePort = port;
                                        Vers.i.nowProjectServerType = 0;
                                    }
                                } else {
                                    EasyWebServerReceiver.controlWebsite(MainActivity.main, false, INewPort);
                                    try {
                                        final LinearLayout parent = ((LinearLayout) (((ViewGroup) (Vers.easyWebServerWebsitesMap.get(port)[3])).getParent()));
                                        parent.removeView(((ViewGroup) (Vers.easyWebServerWebsitesMap.get(port)[3])));
                                    } catch (Exception e) {
                                    }
                                }

                                saveChangeManifest(port, INewPort, BIsWebsite, false, lySiBefore, old, oldRegs);

                                Do.finishWaiting();
                            } catch (final Exception e) {
                                Do.showErrDialog(MainActivity.main, e);
                            }
                        }
                    });
                    AdbManifest.show();
                }

                public void saveChangeManifest(int IOldPort, int INewPort, boolean BIsWebsite, boolean isHttps, LinearLayout lySiParent, Object[] old, Map oldRegs) throws IOException {
                    File FPortManifest = new File((File) old[1], "manifest.json");

                    if (!FPortManifest.exists()) {
                        if (!FPortManifest.createNewFile())
                            throw new IOException("Can't find manifest.json .");
                    }
                    {
                        try {
                            //保存到manifest.json
                            String SJson = Do.getText(FPortManifest);

                            JsonObject JoAll;
                            try {
                                JoAll = (JsonObject) JsonParser.parseString(SJson);
                            } catch (Exception e) {
                                SJson = "{}";
                                JoAll = (JsonObject) JsonParser.parseString(SJson);
                            }

                            final JsonObject JoServer = JoAll.get("server").getAsJsonObject();

                            JoServer.remove("port");
                            JoServer.remove("form");
                            //JoServer.remove("https");
                            JoServer.addProperty("port", INewPort);
                            JoServer.addProperty("form", BIsWebsite);
                            //JoServer.addProperty("https",isHttps);

                            JoAll.remove("server");
                            JoAll.add("server", JoServer);

                            Do.write(JoAll.toString(), FPortManifest);
                        } catch (Exception e) {
                            MainActivity.main.runOnUiThread(() -> MainActivity.main.toast(R.string.save_manifest_err));
                        }
                    }
                    old[0] = BIsWebsite;
                    old[2] = isHttps;
                    old[3] = getServerItem(0, INewPort, FPortManifest.getParentFile());
                    if (old.length == 5)
                        old[4] = Do.createServer(INewPort, BIsWebsite, ((File) old[1]).getPath(), isHttps);
                    Vers.easyWebServerWebsitesMap.put(INewPort, old);
                    if (oldRegs != null) {
                        Vers.easyWebServerRegsMap.put(INewPort, oldRegs);
                    }
                    if (Vers.i.nowWebsitePort == IOldPort) {
                        Vers.i.nowWebsitePort = INewPort;
                        Vers.i.nowProjectServerType = 0;
                    }
                    try {
                        lySiParent.addView((LinearLayout) (((ViewGroup) (Vers.easyWebServerWebsitesMap.get(INewPort)[3]))));
                    } catch (Exception e) {
                    }
                }
            }, (Runnable) () -> {
                Dl AdbCon = new Dl(MainActivity.main);
                String[] Sa;
                if (Vers.isServerOn) {
                    if (((ServerItem) (Vers.easyWebServerWebsitesMap.get(port)[3])).getIcon() == ServerItem.STATUS_OFF) {
                        Sa = new String[]{MainActivity.main.getString(R.string.show_register_handler), MainActivity.main.getString(R.string.server_delete_website), MainActivity.main.getString(R.string.server_on_website)};
                    } else {
                        Sa = new String[]{MainActivity.main.getString(R.string.show_register_handler), MainActivity.main.getString(R.string.server_delete_website), MainActivity.main.getString(R.string.server_off_website)};
                    }
                } else {
                    Sa = new String[]{MainActivity.main.getString(R.string.show_register_handler), MainActivity.main.getString(R.string.server_delete_website)};
                }
                AdbCon.builder.setItems(Sa, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        switch (p2) {
                            case 0:
                                //注册处理器
                            {
                                EasyWebServerReceiver.showRegManager(port);
                            }
                            break;
                            case 1:
                                EasyWebServerReceiver.deleteWebsite(true, port);
                                break;
                            case 2:
                                if (Vers.isServerOn) {
                                    if (((ServerItem) (Vers.easyWebServerWebsitesMap.get(port)[3])).getIcon() == ServerItem.STATUS_OFF) {
                                        //启用
                                        EasyWebServerReceiver.controlWebsite(MainActivity.main, true, port);
                                    } else {
                                        //停止
                                        EasyWebServerReceiver.controlWebsite(MainActivity.main, false, port);
                                    }
                                }
                                break;
                        }
                    }
                });
                AdbCon.show();
            });
        } else if (type == 1) {
            File errorFile = new File(MainActivity.main.getFilesDir(), "php_error");
            if (errorFile.exists()) {
                try {
                    String error = Do.getText(errorFile);
                    errorFile.delete();
                    ((LocalServersManager.ServerItem) (Vers.phpServerWebsitesMap.get(port)[1])).setIcon(LocalServersManager.ServerItem.STATUS_ALERT);
                    ((LocalServersManager.ServerItem) (Vers.phpServerWebsitesMap.get(port)[1])).SAlert = error.toString().replace("Error: ", "");
                } catch (IOException e) {
                }
            }
            showWebsiteDialog(c, (ServerItem) ((Vers.phpServerWebsitesMap.get(port))[1]), port, new Runnable() {

                @Override
                public void run() {
                    LinearLayout Ly;
                    ScrollView Sv = new ScrollView(c);
                    Sv.setFillViewport(true);
                    RelativeLayout Rl = new RelativeLayout(c);
                    Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
                    Ly = new LinearLayout(c);
                    Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
                    Ly.setOrientation(LinearLayout.VERTICAL);
                    Ly.setPadding(30, 30, 30, 30);
                    Sv.addView(Rl);
                    Rl.addView(Ly);

                    final NumLayout NlPort = new NumLayout(c);
                    NlPort.build(false, 0, null, R.string.server_wifi_port, R.string.server_wifi_port_8080);
                    NlPort.Acet.setText(port + "");
                    Ly.addView(NlPort);

                    Dl AdbManifest = new Dl(c);
                    AdbManifest.builder.setTitle(R.string.website_manifest);
                    AdbManifest.builder.setView(Sv);
                    AdbManifest.builder.setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {

                            try {
                                int INewPort = 0;
                                if (NlPort.getText("$") == null) {
                                    INewPort = port;
                                } else {
                                    INewPort = Integer.parseInt(NlPort.getText("$"));
                                    if (INewPort < 0) {
                                        throw new Exception();
                                    }
                                }
                                if (INewPort != port && Do.isServersHasPort(INewPort)) {
                                    MainActivity.main.toast(R.string.server_wifi_port_has);
                                    return;
                                }

                                LinearLayout lySiBefore = null;
                                try {
                                    lySiBefore = ((LinearLayout) (((ViewGroup) (Vers.phpServerWebsitesMap.get(port)[1])).getParent()));
                                } catch (Exception e) {
                                }

                                Object[] old = Vers.phpServerWebsitesMap.get(port);

                                PHPServerReceiver.stopServer(port);
                                if (INewPort != port) {
                                    boolean isNowWebsite=false;
                                    if (port == Vers.i.nowWebsitePort) {
                                        isNowWebsite=true;
                                    }
                                    PHPServerReceiver.deleteWebsite(port);

                                    if(isNowWebsite)
                                    {
                                        Vers.i.nowWebsitePort = port;
                                        Vers.i.nowProjectServerType = 1;
                                    }
                                }
                                saveChangeManifest(port, INewPort, lySiBefore, old);

                                Do.finishWaiting();
                            } catch (final Exception e) {
                                Do.showErrDialog(MainActivity.main, e);
                            }
                        }

                        public void saveChangeManifest(int IOldPort, int INewPort, LinearLayout lySiParent, Object[] old) throws IOException {
                            File FPortManifest = new File((File) old[0], "manifest.json");

                            if (!FPortManifest.exists()) {
                                if (!FPortManifest.createNewFile())
                                    throw new IOException();
                            }
                            {
                                try {
                                    //保存到manifest.json
                                    String SJson = Do.getText(FPortManifest);

                                    JsonObject JoAll;
                                    try {
                                        JoAll = (JsonObject) JsonParser.parseString(SJson);
                                    } catch (Exception e) {
                                        SJson = "{}";
                                        JoAll = (JsonObject) JsonParser.parseString(SJson);
                                    }

                                    final JsonObject JoServer = JoAll.get("server").getAsJsonObject();

                                    JoServer.remove("port");
                                    JoServer.addProperty("port", INewPort);

                                    JoAll.remove("server");
                                    JoAll.add("server", JoServer);

                                    Do.write(JoAll.toString(), FPortManifest);

                                } catch (Exception e) {
                                    MainActivity.main.toast(R.string.save_manifest_err);
                                }
                            }
                            old[1] = getServerItem(1, INewPort, FPortManifest.getParentFile());
                            Vers.phpServerWebsitesMap.put(INewPort, old);

                            if (Vers.i.nowWebsitePort == IOldPort) {
                                Vers.i.nowWebsitePort = INewPort;
                                Vers.i.nowProjectServerType = 1;
                            }
                            try {
                                if (Vers.phpServerWebsitesMap.size() == 1)
                                    lySiParent.removeAllViews();
                                lySiParent.addView((LinearLayout) (((ViewGroup) (Vers.phpServerWebsitesMap.get(INewPort)[1]))));
                            } catch (Exception e) {
                            }
                        }
                    });
                    AdbManifest.show();
                }
            }, new Runnable() {

                @Override
                public void run() {
                    Dl AdbCon = new Dl(MainActivity.main);
                    String[] Sa;
                    final boolean isWebsiteRunning = ((LocalServersManager.ServerItem) (Vers.phpServerWebsitesMap.get(port)[1])).getIcon() == LocalServersManager.ServerItem.STATUS_RUNNING;
                    if (isWebsiteRunning) {
                        Sa = new String[]{MainActivity.main.getString(R.string.server_delete_website), MainActivity.main.getString(R.string.server_off_website)};
                    } else {
                        Sa = new String[]{MainActivity.main.getString(R.string.server_delete_website), MainActivity.main.getString(R.string.server_on_website)};
                    }
                    AdbCon.builder.setItems(Sa, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            switch (p2) {
                                case 0:
                                    PHPServerReceiver.deleteWebsite(port);
                                    break;
                                case 1:
                                    if (isWebsiteRunning) {
                                        PHPServerReceiver.stopServer(port);
                                    } else {
                                        PHPServerReceiver.startServer(MainActivity.main,port,false);
                                    }
                                    break;
                            }
                        }
                    });
                    AdbCon.show();
                }
            });
        } else if (type == 2) {
            OtherReceiver.deploy(Vers.i.nowWebsitePort);
        }
    }

    public static ServerItem getServerItem(final int type, final int port, File dir) {
        final LocalServersManager.ServerItem Si = new LocalServersManager.ServerItem(MainActivity.main);
        Si.setTitle(MainActivity.main.getString(R.string.server_wifi_port_title) + port);
        Si.setPath(dir);
        Si.setIcon(LocalServersManager.ServerItem.STATUS_OFF);
        Si.setId(port);
        Si.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                LocalServersManager.showWebsiteDialog(MainActivity.main, type, port);
            }
        });
        return Si;
    }

    public static void showWebsiteDialog(final Context c, final LocalServersManager.ServerItem Si, final int port, final Runnable manifestRun, final Runnable controlRun) {
        LinearLayout Ly;
        ScrollView Sv = new ScrollView(c);
        Sv.setFillViewport(true);
        RelativeLayout Rl = new RelativeLayout(c);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
        Ly = new LinearLayout(c);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(30, 30, 30, 30);
        Sv.addView(Rl);
        Rl.addView(Ly);

        TextView Actv = new TextView(c);

        Do.CanSelect(Actv);

        Actv.setText(c.getString(R.string.website_note) + Do.getTime("HH:mm:ss"));
        Ly.addView(Actv);

        String SStatus = null;
        switch (Si.getIcon()) {
            case LocalServersManager.ServerItem.STATUS_RUNNING:
                SStatus = c.getString(R.string.website_running);
                break;
            case LocalServersManager.ServerItem.STATUS_OFF:
                SStatus = c.getString(R.string.website_off);
                break;
            case LocalServersManager.ServerItem.STATUS_ALERT:
                SStatus = c.getString(R.string.website_alert);
                break;
        }
        Actv = new TextView(c);
        Do.CanSelect(Actv);
        Actv.setText(c.getString(R.string.website_status) + SStatus);
        Ly.addView(Actv);

        if (SStatus.equals(c.getString(R.string.website_alert))) {
            Actv = new TextView(c);
            Do.CanSelect(Actv);
            Actv.setText(c.getString(R.string.website_alert_msg) + Si.SAlert);
            Ly.addView(Actv);
        }

        String SWebLink;
        try {
            SWebLink = "http://" + Do.getIpAddressString() + ":" + port + "/";
        } catch (SocketException e) {
            SWebLink = c.getString(R.string.error);
        }
        Actv = new TextView(c);
        Do.CanSelect(Actv);
        Actv.setText(c.getString(R.string.website_link) + SWebLink);
        Ly.addView(Actv);

        Dl Adb = new Dl(c);

        Adb.builder.setTitle(R.string.website_control);
        Adb.builder.setView(Sv);
        Adb.builder.setPositiveButton(R.string.refresh, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                showWebsiteDialog(c, Si, port, manifestRun, controlRun);
            }
        });
        Adb.builder.setNegativeButton(R.string.control, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                controlRun.run();
            }
        });
        Adb.builder.setNeutralButton(R.string.website_manifest, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                manifestRun.run();
            }
        });
        Adb.show();
    }

    public static class ServerItem extends LinearLayout {
        public static final int STATUS_RUNNING = R.drawable.website_running;
        public static final int STATUS_ALERT = R.drawable.website_alert;
        public static final int STATUS_OFF = R.drawable.website_off;
        public int STATUS = -1;
        public ImageView IvIcon;
        public TextView TvTitle;
        public TextView TvSub;
        public LinearLayout LyText;
        public String SAlert;

        public ServerItem(Context ctx) {
            super(ctx);

            setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            setBackground(ctx.getResources().getDrawable(R.drawable.shape_item));
            setGravity(Gravity.CENTER);
            setOrientation(LinearLayout.HORIZONTAL);
            setPadding(30, 30, 30, 30);
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
                p.setMargins(9, 9, 9, 9);
                requestLayout();
            }

            IvIcon = new ImageView(ctx);
            TvTitle = new TextView(ctx);
            TvSub = new TextView(ctx);
            LyText = new LinearLayout(ctx);

            IvIcon.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(ctx, 25), Do.dp2px(ctx, 25)));
            if (IvIcon.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) IvIcon.getLayoutParams();
                p.setMargins(15, 15, 15, 15);
                IvIcon.requestLayout();
            }
            IvIcon.setVisibility(View.GONE);
            IvIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            TvSub.setTextColor(Color.GRAY);

            TvTitle.setTextSize(15);
            TvSub.setTextSize(12);

            LyText.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
            LyText.setOrientation(LinearLayout.VERTICAL);
            LyText.setGravity(Gravity.CENTER | Gravity.LEFT);
            LyText.addView(TvTitle);
            LyText.addView(TvSub);

            addView(IvIcon);
            addView(LyText);
        }

        public int getIcon() {
            return STATUS;
        }

        public void setIcon(int drawable) {
            if (drawable == -1) {
                IvIcon.setVisibility(View.GONE);
            } else {
                Drawable draw = getContext().getResources().getDrawable(drawable);
                draw.setTint(ContextCompat.getColor(getContext(), R.color.opposition));
                IvIcon.setImageDrawable(draw);
                IvIcon.setVisibility(View.VISIBLE);
            }
            STATUS = drawable;
        }

        public void setTitle(String s) {
            TvTitle.setText(s);
        }

        public void setPath(File f) {
            TvSub.setText(f.getPath());
        }
    }

    public class ListCup extends LinearLayout {
        ScrollView Sv;
        RelativeLayout Rl;
        LinearLayout Ly;

        public ListCup(Context c) {
            super(c);

            Sv = new ScrollView(c);
            Sv.setFillViewport(true);
            Rl = new RelativeLayout(c);
            Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -1));
            Ly = new LinearLayout(c);
            Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            Ly.setGravity(Gravity.CENTER);
            Ly.setOrientation(LinearLayout.VERTICAL);
            Sv.addView(Rl);
            Rl.addView(Ly);

            setOrientation(LinearLayout.VERTICAL);
            setGravity(Gravity.CENTER);

            addView(Sv);
        }

        public void addLyView(View v) {
            Ly.addView(v);
        }

        public void removeLyView(View v) {
            Ly.removeView(v);
        }
    }
}
