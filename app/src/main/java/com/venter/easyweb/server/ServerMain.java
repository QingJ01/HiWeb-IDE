package com.venter.easyweb.server;

import android.content.*;
import com.google.gson.*;
import com.venter.easyweb.*;
import com.venter.easyweb.edit.*;
import java.io.*;
import java.util.*;
import com.venter.easyweb.server.easyweb_server.*;
import com.venter.easyweb.server.php_server.*;
import com.venter.easyweb.server.other.*;

public class ServerMain
{
    public static boolean isPortExists=false;
    public static boolean hasDeploy=false;
    public static int hasDeployPort=-1;
	public static boolean isHasPHP;
    public void main() throws Exception
    { 
        isPortExists=false;
        hasDeploy=false;
        hasDeployPort=-1;
		isHasPHP=false;
        if(!readManifest())
        {
			if(isHasPHP)
			{
				MainActivity.main.toast(R.string.php_web_server_only_one);
				return;
			}
            if(isPortExists)
            {
                MainActivity.main.toast(R.string.have_same_port);
                return;
            }
            if(hasDeploy)
            {
				LocalServersManager.showWebsiteDialog(MainActivity.main, Vers.i.nowProjectServerType,hasDeployPort);
                return;
            }
            MainActivity.main.toast(R.string.read_manifest_cant);
        }
        main2();
    }

	private void main2()
	{
		if (!Vers.i.isNowProjectEnableServer)
        {
            createServer();
        }
        else
        {
			switch(Vers.i.nowProjectServerType)
			{
				case 0:
				case 1:
				case 2:
					LocalServersManager.showWebsiteDialog(MainActivity.main, Vers.i.nowProjectServerType, Vers.i.nowWebsitePort);
					MainActivity.main.toast(R.string.server_set_done_toast);
					break;
				default:
					MainActivity.main.toast(R.string.unknow_server);
					break;
			}
        }
	}
	
    private void createServer()
    {
        String[] SaTypes=new String[]
        {
            MainActivity.main.getString(R.string.server_type_wifi),
			MainActivity.main.getString(R.string.server_type_php),
			MainActivity.main.getString(R.string.server_type_other)
        };
        Dl AdbType=new Dl(MainActivity.main);
        AdbType.builder.setTitle(R.string.server_type);
        AdbType.builder.setItems(SaTypes, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    switch(p2)
                    {
                        case 0:
                            EasyWebServerReceiver.deploy();
                            break;
						case 1:
							PHPServerReceiver.deploy();
							break;
						case 2:
							OtherReceiver.deploy(-1);
							break;
                    }
                }
            });
        AdbType.show();
    }
	
    public static boolean readManifest() throws Exception
    {
		Vers.i.isNowProjectEnableServer=false;
        if(Vers.i.nowHWPPath==null||!Vers.i.nowHWPPath.exists())
        {
            return false;
        }
        String SJson=Do.getText(Vers.i.nowHWPPath);
        JsonParser Jp=new JsonParser();
        JsonObject JoAll;
        JsonObject JoServer=null;
        try
        {
            JoAll=(JsonObject) Jp.parse(SJson);
        }
        catch(Exception e)
        {
            return false;
        }
        if(JoAll.has("server"))
        {
            try
            {
                JoServer=(JsonObject) JoAll.get("server");
            }
            catch(Exception e)
            {
                return false;
            }
            
            if(!JoServer.has("server_type"))
            {
                return false;
            }
            else
            {
                try
                {
                    int IType=Integer.parseInt(JoServer.get("server_type").getAsString());
					if(IType==1&& Vers.phpServerWebsitesMap !=null&& Vers.phpServerWebsitesMap.size()==1)
					{
						if(!Do.isServerContainWebsite(Vers.i.ProjectDir))
						{
							isHasPHP=true;
							return false;
						}
					}
					
                    switch(IType)
                    {
                        case 0:
                            if(!JoServer.has("port")||
                            !JoServer.has("form"))
                            {                                
                                return false;
                            }
                            else
                            {
                                try
                                {
                                    if(Vers.easyWebServerWebsitesMap ==null)
                                        Vers.easyWebServerWebsitesMap =new HashMap<Integer,Object[]>();
                                    
                                    int IPort=Integer.parseInt(JoServer.get("port").getAsString());
									if(IPort<0)
										throw new Exception();
									
                                    if(Do.isServersHasPort(IPort))
                                    {
                                        if(!Do.isServerContainWebsite(Vers.i.ProjectDir))
                                        {
                                            isPortExists=true;
                                        }
                                        else
                                        {
											Vers.i.nowProjectServerType =IType;
											Vers.i.nowWebsitePort =IPort;
                                            hasDeploy=true;
                                            hasDeployPort=IPort;
                                        }
                                        return false;
                                    }
									Vers.i.nowProjectServerType =-1;
									
									Vers.i.nowWebsitePort =-1;
                                    boolean BIsWebsite=JoServer.get("form").getAsBoolean();
                                    boolean isHttps=false;
                                    
                                    EasyWebServerReceiver.putMapServer(IPort,BIsWebsite,isHttps, Vers.i.ProjectDir);
                                    
                                    if(JoServer.has("handlers"))
                                    {
                                        //获取路径处理器
                                        Map MNow=new HashMap();
                                        JsonArray JaHandlers=JoServer.get("handlers").getAsJsonArray();
                                        for(int i=0;i<JaHandlers.size();i++)
                                        {
                                            MNow.put(JaHandlers.get(i).getAsJsonObject().get("path").getAsString(),new Object[]{new File(Vers.i.ProjectDir,JaHandlers.get(i).getAsJsonObject().get("file_path").getAsString()),JaHandlers.get(i).getAsJsonObject().get("file_type").getAsInt()});
                                        }
                                        Vers.easyWebServerRegsMap.put(IPort,MNow);
                                    }
									Vers.i.nowProjectServerType =IType;
                                    Vers.i.nowWebsitePort =IPort;
                                    Vers.i.isNowProjectEnableServer=true;
                                    return true;
                                }
                                catch(Exception e)
                                {
                                    return false;
                                }
                            }
						case 1:
							if(!JoServer.has("port"))
                            {                                
                                return false;
                            }
                            else
                            {
                                try
                                {
                                    if(Vers.phpServerWebsitesMap ==null)
                                        Vers.phpServerWebsitesMap =new HashMap<Integer,Object[]>();

                                    int IPort=Integer.parseInt(JoServer.get("port").getAsString());
									if(IPort<0)
										throw new Exception();
                                    if(Do.isServersHasPort(IPort))
                                    {
                                        if(!Do.isServerContainWebsite(Vers.i.ProjectDir))
                                        {
                                            isPortExists=true;
                                        }
                                        else
                                        {
											Vers.i.nowProjectServerType =IType;
											Vers.i.nowWebsitePort =IPort;
                                            hasDeploy=true;
                                            hasDeployPort=IPort;
                                        }
                                        return false;
                                    }
									Vers.i.nowProjectServerType =-1;

									Vers.i.nowWebsitePort =-1;

                                    PHPServerReceiver.putMapServer(IPort, Vers.i.ProjectDir);

									Vers.i.nowProjectServerType =IType;
                                    Vers.i.nowWebsitePort =IPort;
                                    Vers.i.isNowProjectEnableServer=true;
                                    return true;
                                }
                                catch(Exception e)
                                {
                                    return false;
                                }
                            }
						case 2:
							if(!JoServer.has("port"))
                            {                                
                                return false;
                            }
                            else
                            {
                                try
                                {
                                    int IPort=Integer.parseInt(JoServer.get("port").getAsString());
									if(IPort<0)
										throw new Exception();
									if(Vers.i.nowProjectServerType ==2&& Vers.i.nowWebsitePort ==IPort)
									{
										hasDeploy=true;
										hasDeployPort= Vers.i.nowWebsitePort;
										return false;
									}
                                    if(Do.isServersHasPort(IPort))
                                    {
                                        isPortExists=true;
                                        return false;
                                    }
									
									Vers.i.nowProjectServerType =IType;
                                    Vers.i.nowWebsitePort =IPort;
                                    Vers.i.isNowProjectEnableServer=true;
                                    return true;
                                }
                                catch(Exception e)
                                {
                                    return false;
                                }
                            }
                        default:
							Vers.i.nowProjectServerType =IType;
							Vers.i.isNowProjectEnableServer=true;
							Vers.i.nowWebsitePort =-1;
                            return false;
                    }
					
                }
                catch(Exception e)
                {
					Vers.i.nowProjectServerType =-1;
					Vers.i.nowWebsitePort =-1;
					Vers.i.isNowProjectEnableServer=false;
                    return false;
                }
            }
        }
        else
        {
			Vers.i.nowProjectServerType =-1;
			Vers.i.isNowProjectEnableServer=false;
			Vers.i.nowWebsitePort =-1;
            return true;
        }
    }
}
