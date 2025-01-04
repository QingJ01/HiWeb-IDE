package com.hiweb.ide.server.easyweb_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.hiweb.ide.Vers;
import com.hiweb.ide.edit.Do;
import com.hiweb.ide.server.ServerNotification;
import com.yanzhenjie.andserver.Server;
import java.io.File;

public class EasyWebServerService extends Service {

    private ServerNotification serverNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private synchronized void startServer() {
        if (Vers.isServerOn)
            return;

        for (int port : Vers.easyWebServerWebsitesMap.keySet()) {
            createWebsiteAndPut(port);
        }

        Vers.isServerOn = true;

        serverNotification = new ServerNotification(this, 0);
        serverNotification.mNotifyMgr.notify(0, serverNotification.notification);
    }

    private void createWebsiteAndPut(int port) {
        File file = (File) Vers.easyWebServerWebsitesMap.get(port)[1];
        String websiteDirectory = file.getAbsolutePath();
        boolean form = (boolean) Vers.easyWebServerWebsitesMap.get(port)[0];
        boolean isHttps = (boolean) Vers.easyWebServerWebsitesMap.get(port)[2];

        Server server = Do.createServer(port, form, websiteDirectory, isHttps);
        Object[] objects = new Object[] { form, file, isHttps, Vers.easyWebServerWebsitesMap.get(port)[3], server };
        Vers.easyWebServerWebsitesMap.put(port, objects);
        new Thread(() -> {
            server.startup();
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("startServer", false)) {
            startServer();
        } else if (intent.getBooleanExtra("shutdownWebsite", false)) {
            Server server = (Server) Vers.easyWebServerWebsitesMap.get(intent.getIntExtra("port", -1))[4];
            server.shutdown();
        } else if (intent.getBooleanExtra("startWebsite", false)) {
            final int port = intent.getIntExtra("port", -1);

            createWebsiteAndPut(port);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        serverNotification.cancle(0);
        for (final int i : Vers.easyWebServerWebsitesMap.keySet()) {
            if (Vers.easyWebServerWebsitesMap.get(i).length < 5)
                continue;
            Server S = (Server) (Vers.easyWebServerWebsitesMap.get(i)[4]);

            S.shutdown();
        }
        Vers.isServerOn = false;
        super.onDestroy();
    }
}