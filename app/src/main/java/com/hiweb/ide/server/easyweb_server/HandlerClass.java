package com.hiweb.ide.server.easyweb_server;

import com.hiweb.ide.*;
import com.hiweb.ide.edit.*;
import com.venter.jssrunner.*;
import com.yanzhenjie.andserver.util.*;
import java.io.*;
import java.util.*;
import org.apache.httpcore.*;
import org.apache.httpcore.entity.*;
import org.apache.httpcore.protocol.*;

import java.io.File;

public class HandlerClass {

    public int IPort;
    public File FLink;
    public int IType;
    public String SPath;
    public JssRunner Jr;

    public HandlerClass(int IPort, File FLink, int IType, String SPath, JssRunner Jr) {
        this.IPort = IPort;
        this.FLink = FLink;
        this.IType = IType;
        this.SPath = SPath;
        this.Jr = Jr;
    }

    private java.io.File FTempFile;

    public void setHTML(String SCode, HttpResponse Hps) throws IOException {
        FTempFile = new java.io.File(MainActivity.main.getCacheDir(), "temphtml.html");

        newTempFile(FTempFile);

        Do.write(SCode, FTempFile);

        FileEntity Fe = new FileEntity(new java.io.File(FTempFile.getPath()), ContentType.create("*/*", "uft-8"));
        Hps.setEntity(Fe);
    }

    private void newTempFile(java.io.File FCreate) throws IOException {
        if (FCreate.exists()) {

            if (FCreate.getName().lastIndexOf("_") == -1) {
                newTempFile(new java.io.File(MainActivity.main.getCacheDir(), "temphtml_1.html"));
            } else {
                int ITimes = 0;
                try {
                    ITimes = Integer.parseInt(FCreate.getName().replace(".html", "").split("_")[1]) + 1;
                } catch (Exception e) {
                    ITimes = 1;
                }

                newTempFile(new java.io.File(MainActivity.main.getCacheDir(), "temphtml_" + ITimes + ".html"));
            }
        } else {
            FCreate.createNewFile();
            FTempFile = FCreate;
        }
    }

    public synchronized void handle(HttpRequest req, final HttpResponse res, HttpContext con) throws IOException {
        final Map<String, String> params = HttpRequestParser.parseParams(req);

        try {

            switch (IType) {
                case 0: {
                    Values.BIsFinish = false;

                    final String SCode = Do.getText(FLink);

                    MainActivity.main.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Jr.run(Do.WccJSSRunner, MainActivity.main, SCode,
                                    ((File) ((Vers.easyWebServerWebsitesMap.get(IPort))[1])), params, res, IPort, SPath,
                                    FLink.getPath().replace(
                                            ((File) ((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath() + "/",
                                            ""));
                        }
                    });

                    while (true) {
                        Thread.sleep(10);
                        if (Values.BIsFinish) {
                            if (Values.Se != null || Values.Fe != null) {
                                HttpEntity He = (Values.Se == null) ? Values.Fe : Values.Se;
                                try {
                                    res.setEntity(He);
                                } catch (Exception e) {
                                    res.setEntity(new StringEntity(e.toString(), "UTF-8"));
                                }
                            }

                            break;
                        }
                    }
                }
                    break;
                case 1: {
                    String SCode = Do.getText(FLink);
                    setHTML("<script>(function(){\n" + SCode + "\n})();</script>", res);
                }
                    break;
                case 2:
                    FileEntity Fe = new FileEntity(FLink, ContentType.create("*/*", "utf-8"));
                    res.setEntity(Fe);
                    break;
            }
        } catch (Exception e) {
            StringEntity SeErr = new StringEntity(e.getMessage());
            res.setEntity(SeErr);
        }
    }
}
