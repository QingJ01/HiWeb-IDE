package com.venter.easyweb.server.other;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.google.gson.*;
import com.venter.easyweb.*;
import com.venter.easyweb.add.addViewWidget.*;
import com.venter.easyweb.edit.*;
import java.io.*;
import java.net.*;
import com.venter.easyweb.server.*;

public class OtherReceiver
{
	public static void deploy(final int defultPort)
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
                                    ActvMsg.setText(R.string.server_other_msg);
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
									if(defultPort!=-1)
									{
										NlPort.Acet.setText(defultPort+"");
									}
                                    Ly.addView(NlPort);

                                    Dl AdbWifi=new Dl(MainActivity.main);
                                    AdbWifi.builder.setTitle(R.string.other_server_settings);
                                    AdbWifi.builder.setView(Sv);
                                    AdbWifi.builder.setPositiveButton(R.string.server_set_done, null);
									if(defultPort!=-1)
										AdbWifi.builder.setNeutralButton(R.string.stop_link_to_other_server,null);
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

                                                    AdWifi.dismiss();
                                                    try
                                                    {                                                        
                                                        saveWifiSetting(IPort, Vers.i.ProjectDir);
														if(defultPort==-1)
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
									if(defultPort!=-1)
									{
										AdWifi.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){

												@Override
												public void onClick(View p1)
												{
													AdWifi.dismiss();
													Vers.i.nowProjectServerType =-1;
													Vers.i.nowWebsitePort =-1;
													MainActivity.main.toast(R.string.done);
												}
											});
									}
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
		
		try
		{
			ServerMain.readManifest();
		}
		catch (Exception e)
		{}
    }
	public static JsonObject getServerElm(int IPort)
    {
        JsonObject Jo=new JsonObject();
        Jo.addProperty("server_type",2);
        Jo.addProperty("port",IPort);
        return Jo;
    }
}
