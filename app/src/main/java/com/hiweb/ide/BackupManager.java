package com.hiweb.ide;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import com.hiweb.ide.edit.Do;
import com.google.gson.JsonParser;

public class BackupManager
{
	private static Thread thread=null;
	private static boolean isStoping=false;
	private static File recoverFile=new File(MainActivity.main.getFilesDir(),"recover.json");
	public synchronized static void start()
	{
		isStoping=false;
		thread = new Thread(new Runnable(){

				@Override
				public void run()
				{
					while(!isStoping)
					{
						try
						{
							Thread.sleep(SettingsClass.vIBackup*1000);
							
							JsonObject vJaObject=new JsonObject();
							/*
								"time":"2020.3.13 20:03"
								"isopenedwebsite":true,
								"websitepath":"/sdcard/xxx",//仅"isopenedwebsite"为true时使用。
								"files":{
									(vJaOpenedFiles)
								}
							*/
							
							JsonArray vJaOpenedFiles=new JsonArray();
							/*
								JsonObject item:
								{
									"isstar":true, //EPI加不加*，加*时为true
									"path":"/sdcard/xxx.html",
									"edittext":"..."//编辑器文本，仅"isstar"为true时使用。
								}
							*/
							
							Map<Integer,Object[]> MSID=MainActivity.main.binding.Ep.MSID;
							if(MSID.size()==0)
								continue;
							boolean hasStar=false;
							for(int sid:MSID.keySet())
							{
								Object[] nowArr=MSID.get(sid);
								EditorPage.EPItem EPI=(EditorPage.EPItem) nowArr[0];
								Editor Eo=(Editor) nowArr[1];
								boolean isStar=!EPI.BIsSave;
								String path=EPI.getFile().getPath();
								JsonObject item=new JsonObject();
								item.addProperty("isstar",isStar);
								item.addProperty("path",path);
								if(isStar)
								{
									hasStar=true;
									item.addProperty("edittext",Eo.getString());
								}
								vJaOpenedFiles.add(item);
							}
							if(!hasStar)
								continue;
							
							SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
							vJaObject.addProperty("time",df.format(new Date()));
							
							boolean isOpenWebsite=(Vers.i.ProjectDir!=null);
							vJaObject.addProperty("isopenedwebsite",isOpenWebsite);
							if(isOpenWebsite)
							{
								vJaObject.addProperty("websitepath", Vers.i.ProjectDir.getPath());
							}
							vJaObject.add("files",vJaOpenedFiles);
							
							recoverFile.createNewFile();
							Do.write(vJaObject.toString(),recoverFile);
						}
						catch (Exception e)
						{
							
						}
					}
				}
			});
		thread.start();
	}
	public static void stop()
	{
		isStoping=true;
	}
	
	public static void recoverMode()
	{
		if(recoverFile.exists())
		{
			try
			{
				String json=Do.getText(recoverFile);
				JsonObject vJaObject=(JsonObject) JsonParser.parseString(json);
				String time=vJaObject.get("time").getAsString();
				Vers.i.vOaBackup=new Object[]{time,vJaObject};
				MainActivity.main.binding.wwvWelcome.loadWhenShowWelcome();
			}
			catch(Exception e)
			{
				
			}
		}
	}
}
