package com.venter.easyweb.server.php_server;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.google.gson.*;
import com.venter.easyweb.*;
import com.venter.easyweb.add.addViewWidget.*;
import com.venter.easyweb.edit.*;
import com.venter.easyweb.server.*;

import java.io.*;
import java.net.*;
import java.util.*;
import android.content.*;

import androidx.core.content.ContextCompat;

public class PHPServerReceiver
{
	public static void deploy()
	{
		if(Vers.phpServerWebsitesMap !=null&&!Vers.phpServerWebsitesMap.isEmpty())
		{
			MainActivity.main.toast(R.string.php_web_server_only_one);
			return;
		}
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

                                    Dl AdbWifi=new Dl(MainActivity.main);
                                    AdbWifi.builder.setTitle(R.string.php_server_settings);
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
                                                        Toast.makeText(MainActivity.main,R.string.server_wifi_port_has,Toast.LENGTH_SHORT).show();
                                                        return;
                                          			}
                                                    
                                                    AdWifi.dismiss();
                                                    try
                                                    {                                                        
                                                        saveWifiSetting(IPort, Vers.i.ProjectDir);
                                                        Toast.makeText(MainActivity.main,R.string.server_set_done_toast,Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(MainActivity.main,R.string.server_set_err,Toast.LENGTH_SHORT).show();
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
	public static void saveWifiSetting(int IPort,File FProjectDir) throws IOException
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

        JsonObject JoServer=getServerElm(IPort);

        JoAll.add("server",JoServer);

        Do.write(JoAll.toString(),FManifest);

        putMapServer(IPort,FProjectDir);
    }
    public static void putMapServer(final int IPort,File FProjectDir)
    {
		Vers.i.nowProjectServerType =1;
		Vers.i.nowWebsitePort =IPort;
        if(Vers.phpServerWebsitesMap ==null)
            Vers.phpServerWebsitesMap =new HashMap<Integer,Object[]>();
        Vers.phpServerWebsitesMap.put(IPort,new Object[]{FProjectDir,LocalServersManager.getServerItem(1,IPort,FProjectDir)});
    }
    public static JsonObject getServerElm(int IPort)
    {
        JsonObject Jo=new JsonObject();
        Jo.addProperty("server_type",1);
        Jo.addProperty("port",IPort);
        return Jo;
    }
	
	public static void startServer(Context context,int port,boolean isAutoReconnect)
	{
	    if(Vers.i.isRunningPHPServerServiceListener)
	        return;
	    stopServer(port);
	    if(!isAutoReconnect)
        {
            try
            {
                if(PagePreviewer.isDownloadingPhp)
                {
                    MainActivity.main.toast(R.string.wait);
                    return;
                }
                File php=new File(context.getFilesDir(), "php");
                if(!PagePreviewer.isNewestPHP())
                {
                    PagePreviewer.downloadPhp(MainActivity.main);
                    return;
                }

                try
                {
                    ((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).setIcon(LocalServersManager.ServerItem.STATUS_RUNNING);
                }
                catch(Exception e)
                {

                }
            }
            catch (Exception e)
            {

            }
        }
	    try
        {
            Intent phpServerProcessIntent=Do.getPHPServerProcessIntent();
            phpServerProcessIntent.putExtra("startPHPWebsite",true);
            phpServerProcessIntent.putExtra("startPHPDir",((File) (Vers.phpServerWebsitesMap.get(port)[0])).getPath());
            phpServerProcessIntent.putExtra("startPHPWebsitePort",port);
            context.startService(phpServerProcessIntent);
        }
	    catch (Exception e)
        {

        }

        Vers.i.isRunningPHPServerServiceListener=true;
        new PHPServerReceiver().startPHPServerProcessListener(port);
	}

	public synchronized void startPHPServerProcessListener(int port)
    {
        new Thread(() -> {
            try
            {
                while(Vers.i.isRunningPHPServerServiceListener)
                {
                    try
                    {
                        Thread.sleep(3000);
                    }
                    catch(Exception e)
                    {

                    }

                    if(!Vers.i.isRunningPHPServerServiceListener)
                        return;

                    final boolean isRunning= Do.checkURL("http://0.0.0.0:"+port+"/manifest.json");

                    if(!isRunning)
                    {
                        MainActivity.main.runOnUiThread(() -> {
                            File errorFile=new File(MainActivity.main.getFilesDir(),"php_error");

                            if(errorFile.exists())
                            {
                                try {
                                    String errorMsg=Do.getText(errorFile);
                                    errorFile.delete();

                                    try
                                    {
                                        ((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).SAlert=errorMsg;
                                        ((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).setIcon(LocalServersManager.ServerItem.STATUS_ALERT);
                                    }
                                    catch(Exception e)
                                    {

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Vers.i.isRunningPHPServerServiceListener=false;
                                return;
                            }

                            try
                            {
                                ((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).SAlert=MainActivity.main.getString(R.string.reconnecting);
                                ((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).setIcon(LocalServersManager.ServerItem.STATUS_ALERT);
                            }
                            catch(Exception e)
                            {

                            }
                            Vers.i.isRunningPHPServerServiceListener=false;
                            startServer(MainActivity.main,port,true);
                        });
                        return;
                    }
                    else
                    {
                        MainActivity.main.runOnUiThread(() -> {
                            try
                            {
                                ((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).setIcon(LocalServersManager.ServerItem.STATUS_RUNNING);
                            }
                            catch(Exception e)
                            {

                            }
                        });
                    }
                }
            }
            catch(final Exception e)
            {

            }
        }).start();
    }

	public static void stopServer(final int port)
	{
        Vers.i.isRunningPHPServerServiceListener=false;
		try
		{
			((LocalServersManager.ServerItem)(Vers.phpServerWebsitesMap.get(port)[1])).setIcon(LocalServersManager.ServerItem.STATUS_OFF);
		}
		catch(Exception e)
		{

		}

        try
        {
            MainActivity.main.stopService(Do.getPHPServerProcessIntent());
        }
        catch (Exception e)
        {

        }

        File profitFile=new File(MainActivity.main.getFilesDir(),"php_profit");
        try {
            int pid=Integer.parseInt(Do.getText(profitFile).split("//////////")[2]);
            android.os.Process.killProcess(pid);
            profitFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void deleteWebsite(int port)
	{
		stopServer(port);
		
		try
		{
			final LinearLayout parent=((LinearLayout)(((ViewGroup)(Vers.phpServerWebsitesMap.get(port)[1])).getParent()));
			parent.removeView(((ViewGroup)(Vers.phpServerWebsitesMap.get(port)[1])));
			
			if (parent.getChildCount()==0) {
				TextView tv=new TextView(MainActivity.main);
				tv.setText(R.string.service_no_website);
				tv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
				tv.setGravity(Gravity.CENTER);
				parent.removeAllViews();
				parent.addView(tv);
			}
		}
		catch(Exception e)
		{
			
		}
		
		Vers.phpServerWebsitesMap.remove(port);
        if(port==Vers.i.nowWebsitePort)
        {
            Vers.i.nowWebsitePort=-1;
            Vers.i.nowProjectServerType=-1;
        }
	}
}
