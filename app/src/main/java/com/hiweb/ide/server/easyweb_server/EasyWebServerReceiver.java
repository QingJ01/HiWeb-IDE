package com.hiweb.ide.server.easyweb_server;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.*;
import com.hiweb.ide.*;
import com.hiweb.ide.add.addViewWidget.*;
import com.hiweb.ide.edit.*;
import com.hiweb.ide.server.*;
import com.yanzhenjie.andserver.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class EasyWebServerReceiver
{
	public static void openServer(final Context c) {
        Intent I=Do.getEasyWebServerServiceIntent();
        I.putExtra("startServer",true);
        c.startService(I);
    }

    public static void shutdownServer(Context c)
    {
        c.stopService(Do.getEasyWebServerServiceIntent());
        for(final int i:Vers.easyWebServerWebsitesMap.keySet())
        {
            if(Vers.easyWebServerWebsitesMap.get(i).length<5)
                continue;
            Server S=(Server) (Vers.easyWebServerWebsitesMap.get(i)[4]);

            MainActivity.main.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    try
                    {
                        ((LocalServersManager.ServerItem)(Vers.easyWebServerWebsitesMap.get(i)[3])).setIcon(LocalServersManager.ServerItem.STATUS_OFF);
                    }
                    catch(Exception e)
                    {

                    }
                }
            });

            S.shutdown();
        }
        Vers.isServerOn = false;
    }

    private static String[] SaPaths;
    private static TextLayout TlPath;
    private static TextLayout TlFile;
    private static Dl AdbBuilder;
    private static ScrollView Sv;
    private static Map MNow;
    public static void showRegManager(final int IPort) {
        //↓编辑或注册处理器对话框
        LinearLayout.LayoutParams Lylp=new LinearLayout.LayoutParams(-1, -2);
        ScrollView.LayoutParams Svlp=new ScrollView.LayoutParams(-1, -1);
        RelativeLayout.LayoutParams Rllp=new RelativeLayout.LayoutParams(-1, -1);
        {
            Sv = new ScrollView(MainActivity.main);
            Sv.setFillViewport(true);
            RelativeLayout Rl=new RelativeLayout(MainActivity.main);
            Rl.setLayoutParams(Svlp);
            Sv.addView(Rl);
            LinearLayout Ly=new LinearLayout(MainActivity.main);
            Ly.setLayoutParams(Rllp);
            Ly.setGravity(Gravity.CENTER);
            Ly.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
            Ly.setOrientation(LinearLayout.VERTICAL);

            Rl.addView(Ly);

            TlPath = new TextLayout(MainActivity.main);
            TlPath.build(0, null, R.string.handler_path, R.string.handler_path_example);

            TlFile = new TextLayout(MainActivity.main);
            TlFile.build(1, null, R.string.handler_file, R.string.handler_file_example);

            Ly.addView(TlPath);
            Ly.addView(TlFile);
        }

        TextView Actv=new TextView(MainActivity.main);
        Actv.setText(R.string.no_handlers);
        Actv.setGravity(Gravity.CENTER);
        Actv.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));

        Dl Adb=new Dl(MainActivity.main);
        Adb.builder.setTitle(R.string.register_manager);

        MNow=((HashMap)(Vers.easyWebServerRegsMap.get(IPort)));

        if(MNow==null)
            MNow=new HashMap();

        if (MNow.isEmpty()) {
            SaPaths = new String[]{};
            Adb.builder.setView(Actv);
        } else {
            SaPaths = new String[MNow.size()];
            int i=0;
            for (Object S:MNow.keySet()) {
                SaPaths[i] = (String) S;
                i++;
            }

            Adb.builder.setItems(SaPaths, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface p1, final int I) {
                        //显示编辑处理器对话框


                        final String SPath=SaPaths[I];
                        File FLink=(File) (((Object[]) MNow.get(SPath))[0]);

                        TlPath.Acet.setText(SPath);
                        TlFile.Acet.setText(FLink.getPath().replace(((File)((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath()+"/",""));
                        AdbBuilder=new Dl(MainActivity.main);
                        AdbBuilder.builder.setView(Sv);
                        AdbBuilder.builder.setTitle(R.string.handler_edit);
                        AdbBuilder.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    //编辑完成
                                    try {
                                        String SNewPath=TlPath.getText("$");
                                        String SNewFile=TlFile.getText("$");
                                        File FNewFile=new File(((File)((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath() + "/" + SNewFile);
                                        if (!SNewPath.equals(SPath)&&(MNow.containsKey(SNewPath)) || !FNewFile.exists()||FNewFile.isDirectory()) {
                                            MainActivity.main.toast(R.string.handler_save_err);
                                            return;
                                        }
                                        //保存配置操作
                                        saveHandlerManifest(SNewPath,FNewFile,IPort);

                                    } catch (Exception e) {
                                        Do.showErrDialog(MainActivity.main,e);
                                        return;
                                    }
                                }
                            });
                        AdbBuilder.builder.setNeutralButton(R.string.main_explorer_delete, new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    try
                                    {
                                        saveDelHandlerManifest(IPort,I);
                                    }
                                    catch(Exception e)
                                    {
                                        Do.showErrDialog(MainActivity.main,e);
                                    }
                                }
                            });
                        AdbBuilder.show();
                    }
                });
        }  

        Adb.builder.setPositiveButton(R.string.reg_handler, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    //显示注册处理器对话框
                    TlPath.Acet.setText("");
                    TlFile.Acet.setText("");

                    AdbBuilder=new Dl(MainActivity.main);
                    AdbBuilder.builder.setView(Sv);
                    AdbBuilder.builder.setTitle(R.string.reg_handler);
                    AdbBuilder.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface p1, int p2) {

                                try {
                                    String SNewPath=TlPath.getText("$");
                                    String SNewFile=TlFile.getText("$");
                                    File FNewFile=new File(((File)((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath() + "/" + SNewFile);
                                    if (MNow.containsKey(SNewPath) || !FNewFile.exists()||FNewFile.isDirectory()) {
                                        MainActivity.main.toast(R.string.handler_save_err);
                                        return;
                                    }
                                    //保存配置操作
                                    saveHandlerManifest(SNewPath,FNewFile,IPort);
                                } catch (Exception e) {
                                    Do.showErrDialog(MainActivity.main,e);
                                    return;
                                }
                            }
                        });
                    AdbBuilder.show();
                }
            });
        Adb.builder.setNeutralButton(R.string.help, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    //显示帮助对话框
                    try {
                        Do.showLocalServerHelp();
                    } catch (IOException e) {}
                }
            });
        Adb.show();
    }

    public static void saveHandlerManifest(String SNewPath,File FNewFile,int IPort) throws Exception
    {
        if (SNewPath == null)
            throw new Exception();
        Map M;
        if(Vers.easyWebServerRegsMap.containsKey(IPort))
        {
			M=(Map) Vers.easyWebServerRegsMap.get(IPort);
        }
        else
        {
            M=new HashMap();
        }
        if(FNewFile.getName().toLowerCase().endsWith(".jss"))
        {
            M.put(SNewPath,new Object[]{FNewFile,0});
            Vers.easyWebServerRegsMap.put(IPort,M);
        }
        else if(FNewFile.getName().toLowerCase().endsWith(".js"))
        {
            M.put(SNewPath,new Object[]{FNewFile,1});
            Vers.easyWebServerRegsMap.put(IPort,M);
        }
        else
        {
            M.put(SNewPath,new Object[]{FNewFile,2});
            Vers.easyWebServerRegsMap.put(IPort,M);
        }

        {
            //保存到manifest.json
            if(Vers.i.nowHWPPath==null)
            {
                Vers.i.nowHWPPath=new File(((File)((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath(),"manifest.json");
                Vers.i.nowHWPPath.createNewFile();
            }
            String SJson=Do.getText(Vers.i.nowHWPPath);

            JsonParser Jp=new JsonParser();
            JsonObject JoAll;
            try
            {
                JoAll=(JsonObject) Jp.parse(SJson);
            }
            catch(Exception e)
            {
                SJson="{}";
                JoAll=(JsonObject) Jp.parse(SJson);
            }

            JsonObject JoServer=JoAll.get("server").getAsJsonObject();

            JsonArray JaHandler=getHandlerArray(IPort);
            JoServer.add("handlers",JaHandler);

            JoAll.remove("server");
            JoAll.add("server",JoServer);

            Do.write(JoAll.toString(), Vers.i.nowHWPPath);

        }
        controlWebsite(MainActivity.main,false,IPort);
        MainActivity.main.toast(R.string.handler_done);
    }
    public static void saveDelHandlerManifest(int IPort,int IIndex) throws IOException
    {
        Map M=(Map) Vers.easyWebServerRegsMap.get(IPort);
        M.remove(SaPaths[IIndex]);
        Vers.easyWebServerRegsMap.put(IPort,M);
        {
            //保存到manifest.json
            if(Vers.i.nowHWPPath==null)
            {
                Vers.i.nowHWPPath=new File(((File)((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath(),"manifest.json");
                Vers.i.nowHWPPath.createNewFile();
            }
            String SJson=Do.getText(Vers.i.nowHWPPath);

            JsonParser Jp=new JsonParser();
            JsonObject JoAll;
            try
            {
                JoAll=(JsonObject) Jp.parse(SJson);
            }
            catch(Exception e)
            {
                SJson="{}";
                JoAll=(JsonObject) Jp.parse(SJson);
            }

            JsonObject JoServer=JoAll.get("server").getAsJsonObject();

            JsonArray JaHandler=getHandlerArray(IPort);
            JoServer.add("handlers",JaHandler);

            JoAll.remove("server");
            JoAll.add("server",JoServer);

            Do.write(JoAll.toString(), Vers.i.nowHWPPath);

        }
        controlWebsite(MainActivity.main,false,IPort);
        MainActivity.main.toast(R.string.handler_done);
    }
    private static JsonArray getHandlerArray(int IPort)
    {
        JsonArray Ja=new JsonArray();
        Map MNow=((Map)(Vers.easyWebServerRegsMap.get(IPort)));
        for(Object SPath:MNow.keySet())
        {
            JsonObject JoPath=new JsonObject();
            JoPath.addProperty("path", (String) SPath);
            String SFilePath=((File) ((Object[])(MNow.get(SPath)))[0]).getPath().replace(((File)((Vers.easyWebServerWebsitesMap.get(IPort))[1])).getPath()+"/","");
            int IType=((int) ((Object[])(MNow.get(SPath)))[1]);
            JoPath.addProperty("file_path",SFilePath);
            JoPath.addProperty("file_type",IType);

            Ja.add(JoPath);
        }
        return Ja;
    }

    public static void deleteWebsite(boolean isShutdownServer, int port) {
		if(port==Vers.i.nowWebsitePort)
        {
            Vers.i.nowWebsitePort=-1;
            Vers.i.nowProjectServerType=-1;
        }

		controlWebsite(MainActivity.main,false,port);

        try
        {
            final LinearLayout parent=((LinearLayout)(((ViewGroup)(Vers.easyWebServerWebsitesMap.get(port)[3])).getParent()));
            parent.removeView(((ViewGroup)(Vers.easyWebServerWebsitesMap.get(port)[3])));
        } catch (Exception e) {}

        Vers.easyWebServerWebsitesMap.remove(port);
        if(Vers.easyWebServerRegsMap.containsKey(port))
            Vers.easyWebServerRegsMap.remove(port);
    }
    public static void controlWebsite(Context c, boolean isOpen, int port) {
        try
        {
            if (Vers.isServerOn) {
                ((LocalServersManager.ServerItem)(Vers.easyWebServerWebsitesMap.get(port)[3])).setIcon(isOpen ? LocalServersManager.ServerItem.STATUS_RUNNING : LocalServersManager.ServerItem.STATUS_OFF);
                if (isOpen) {
                    //启用
                    Intent intent = Do.getEasyWebServerServiceIntent();
                    intent.putExtra("startWebsite", true);
                    intent.putExtra("port",port);
                    c.startService(intent);
                } else {
                    //停止
/*                    Intent intent = Do.getEasyWebServerServiceIntent();
                    intent.putExtra("port",port);
                    intent.putExtra("shutdownWebsite", true);
                    c.startService(intent);*/
                    Server server = (Server) Vers.easyWebServerWebsitesMap.get(port)[4];
                    server.shutdown();
                }
            }
        }
        catch(Exception e)
        {
            Log.e("Server",e.toString());
        }        
    }
	/*
     @manifest.json:server:handler:
     {
     "key":"login",
     ""
     }
     */
    public void readHandler() throws Exception {
        File FManifest= Vers.i.nowHWPPath;
        JsonParser Jp=new JsonParser();
        JsonObject JoAll=(JsonObject) Jp.parse(Do.getText(FManifest));
        if (!JoAll.has("server"))
            throw new Exception();
        JsonObject JoServer=(JsonObject) JoAll.get("server");
        if (!JoServer.has("handler"))
            throw new Exception();
    }
	
	public static void deploy()
    {
        Do.showWaitAndRunInThread(false,new Runnable(){
			
				String SIP;
                @Override
                public void run() {
                    try
                    {
                        SIP = Do.getIpAddressString();
                        MainActivity.main.runOnUiThread(new Runnable(){

                                LinearLayout Ly;
                                @Override
                                public void run() {
                                    Do.finishWaiting();

                                    ScrollView Sv=new ScrollView(MainActivity.main);
                                    Sv.setFillViewport(true);
                                    RelativeLayout Rl=new RelativeLayout(MainActivity.main);
                                    Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
                                    Ly=new LinearLayout(MainActivity.main);
                                    Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
                                    Ly.setOrientation(LinearLayout.VERTICAL);
                                    Ly.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
                                    Sv.addView(Rl);
                                    Rl.addView(Ly);

                                    TextView ActvMsg=new TextView(MainActivity.main);
                                    ActvMsg.setText(R.string.server_wifi_msg);
                                    ActvMsg.setTextColor(Color.GRAY);
                                    Ly.addView(ActvMsg);

                                    TextView ActvIP=new TextView(MainActivity.main);
                                    ActvIP.setText(SIP);
                                    ActvIP.setTextColor(ContextCompat.getColor(MainActivity.main,R.color.opposition));
                                    Do.CanSelect(ActvIP);

                                    final ObjectLayout OlIP=new ObjectLayout(MainActivity.main);
                                    OlIP.build (0,null,R.string.server_wifi_ip,ActvIP);
                                    Ly.addView(OlIP);

                                    final NumLayout NlPort=new NumLayout(MainActivity.main);
                                    NlPort.build(false,1,null,R.string.server_wifi_port,R.string.server_wifi_port_8080);
                                    Ly.addView(NlPort);

                                    final ChoiceLayout ClForm=new ChoiceLayout(MainActivity.main);
                                    ClForm.build(false,2,null,R.string.server_wifi_website_form,MainActivity.main.getString(R.string.server_wifi_website_form_page)+"|"+MainActivity.main.getString(R.string.server_wifi_website_form_explorer));
                                    Ly.addView(ClForm);
                                    
                                    Dl AdbWifi=new Dl(MainActivity.main);
                                    AdbWifi.builder.setTitle(R.string.easyweb_server_settings);
                                    AdbWifi.builder.setView(Sv);
                                    AdbWifi.builder.setPositiveButton(R.string.server_set_done, null);
                                    final AlertDialog AdWifi=AdbWifi.create();
                                    AdWifi.show();
                                    AdWifi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

                                            @Override
                                            public void onClick(View p1) {
                                                try
                                                {
                                                    int IPort;
                                                    if(NlPort.getText("$")==null)
                                                    {
                                                        IPort=8080;
                                                    }
                                                    else
                                                    {
                                                        IPort=Integer.parseInt(NlPort.getText("$"));
														if(IPort<0)
														{
															throw new Exception();
														}
                                                    }

                                                    if(Do.isServersHasPort(IPort))
                                                    {
                                                        MainActivity.main.toast(R.string.server_wifi_port_has);
                                                        return;
                                                    }

                                                    boolean BIsWebsite;
                                                    String SType=ClForm.getText("$");
                                                    if(SType.equals(MainActivity.main.getString(R.string.server_wifi_website_form_page)))
                                                    {
                                                        BIsWebsite=true;
                                                    }
                                                    else if(SType.equals(MainActivity.main.getString(R.string.server_wifi_website_form_explorer)))
                                                    {
                                                        BIsWebsite=false;
                                                    }
                                                    else
                                                    {
                                                        throw new Exception();
                                                    }
                                                    
                                                    AdWifi.dismiss();
                                                    try
                                                    {                                                        
                                                        saveSetting(IPort,BIsWebsite,false, Vers.i.ProjectDir);
                                                        MainActivity.main.toast(R.string.server_set_done_toast);
                                                    }
                                                    catch(Exception e)
                                                    {
                                                        Do.showErrDialog(MainActivity.main,e);
                                                        return;
                                                    }

                                                }
                                                catch(Exception e)
                                                {
                                                    AdWifi.dismiss();
                                                    MainActivity.main.toast(R.string.server_set_err);
                                                }

                                            }
                                        });

                                }
                            });

                    } 
                    catch (final SocketException e) 
                    {
                        MainActivity.main.runOnUiThread(new Runnable(){

                                @Override
                                public void run() {
                                    Do.showErrDialog(MainActivity.main,e);
                                }
                            });
                    }
                }
            });
    }
    public static void saveSetting(int IPort, boolean BIsWebsite,boolean isHttps, File FProjectDir) throws IOException
    {
        File FManifest=new File(FProjectDir,"manifest.json");
        if(!FManifest.exists())
        {
            if(!FManifest.createNewFile())
                throw new IOException();
        }
        String SJson=Do.getText(FManifest);

        JsonParser Jp=new JsonParser();
        JsonObject JoAll;
        try
        {
            JoAll=(JsonObject) Jp.parse(SJson);
        }
        catch(Exception e)
        {
            SJson="{}";
            JoAll=(JsonObject) Jp.parse(SJson);
        }
        if(JoAll.has("server"))
        {
            JoAll.remove("server");
        }

        JsonObject JoServer=getServerElm(IPort,BIsWebsite,isHttps);

        JoAll.add("server",JoServer);

        Do.write(JoAll.toString(),FManifest);

        putMapServer(IPort,BIsWebsite,isHttps,FProjectDir);
    }
    public static void putMapServer(final int IPort,boolean BIsWebsite,boolean isHttps,File FProjectDir)
    {
		Vers.i.nowProjectServerType =0;
		Vers.i.nowWebsitePort =IPort;
        if(Vers.easyWebServerWebsitesMap ==null)
            Vers.easyWebServerWebsitesMap =new HashMap<Integer,Object[]>();
        Vers.easyWebServerWebsitesMap.put(IPort,new Object[]{BIsWebsite,FProjectDir,isHttps,LocalServersManager.getServerItem(0,IPort,FProjectDir)});
    }
    public static JsonObject getServerElm(int IPort,boolean BIsWebsite,boolean isHttps)
    {
        JsonObject Jo=new JsonObject();
        Jo.addProperty("server_type",0);
        Jo.addProperty("port",IPort);
        Jo.addProperty("form",BIsWebsite);
        Jo.addProperty("https",isHttps);
        return Jo;
    }
}
