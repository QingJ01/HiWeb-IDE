package com.venter.easyweb;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.venter.easyweb.edit.Do;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.view.KeyEvent;

public class PreActivity extends BaseActivity
{

//	@Override
//	protected void onNewIntent(Intent intent)
//	{
//		super.onNewIntent(intent);
//		if(intent.getBooleanExtra("isExit",false))
//		{
//			try
//			{
//				VerClass.serverNof.canShow=false;
//				VerClass.serverNof.show();
//			}
//			catch(Exception e)
//			{
//				
//			}
//			finish();
//		}
//		else if(!VerClass.hasRunned)
//		{
//			init();
//		}
//		else
//		{
//			Intent i=new Intent(PreActivity.this,MainActivity.class);
//			VerClass.isEW=true;
//			startActivity(i);
//		}
//	}

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		try
		{
			Vers.i.isInstalledHopWeb=Do.checkApkInstalled(this,"com.venter.hopweb");
		}
		catch(Exception e)
		{
			
		}
		
		Intent myIntent=getIntent();
		String openProjectPath=myIntent.getStringExtra("projectPath");
		String openFilePath=myIntent.getStringExtra("openFile");
		if(openProjectPath==null)
		{
			Vers.i.skipTpProjectFile=null;
		}
		else
		{
			Vers.i.skipTpProjectFile=new File(openProjectPath);
		}
		
		if(openFilePath==null)
		{
			Vers.i.skipTpOpenFile=null;
		}
		else
		{
			Vers.i.skipTpOpenFile=new File(openFilePath);
		}
		
		if(!Vers.i.hasRunned)
		{
			init();
		}
		else
		{
			Intent i=new Intent(PreActivity.this,MainActivity.class);
			Vers.i.isEW=true;
			startActivity(i);
			finish();
		}
    }
	private void init()
	{
		try
        {
            Vers.i.FVenter=new File(Environment.getExternalStorageDirectory().getPath()+"/"+"Venter","EasyWeb");
            if(!Vers.i.FVenter.exists())
            {
                Vers.i.FVenter.mkdirs();
            }
            Vers.i.FProjects=new File(Environment.getExternalStorageDirectory().getPath()+"/Venter/EasyWeb","Projects");
            if(!Vers.i.FProjects.exists())
            {
                Vers.i.FProjects.mkdirs();
            }
			
			try
			{
				final File bookmarkFile=new File(getFilesDir(),"bookmark.json");
				if(!bookmarkFile.exists())
				{
					bookmarkFile.createNewFile();
					Do.write("[\""+ Vers.i.FProjects.getPath()+"\"]",bookmarkFile);
				}
			}
			catch(Exception e)
			{

			}
        }
        catch(Exception e)
        {

        }
        readSetting();

		startActivity(new Intent(this,BootActivity.class));
		finish();
	}

	private boolean isContainAttribution(JsonObject JoAll,String key)
	{
		return JoAll.has(key) && !JoAll.get(key).isJsonNull();
	}

    /*
     settings.json:
     {
     "chars":[],
     "names":[]
     }

     */
    public void readSetting()
    {
        try
        {
            Arrays.sort(Vers.i.SaDefNames);

            Vers.i.FSetting=new File(getFilesDir(),"settings.json");
            String SText=Do.getText(Vers.i.FSetting);

            JsonObject JoAll=(JsonObject) JsonParser.parseString(SText);
            JsonArray JaChars= isContainAttribution(JoAll,"chars") ? JoAll.get("chars").getAsJsonArray() : new JsonArray();
            JsonArray JaNames=isContainAttribution(JoAll,"names") ? JoAll.get("names").getAsJsonArray() : new JsonArray();
            int ITheme=isContainAttribution(JoAll,"theme") ? JoAll.get("theme").getAsInt() : -1;
			boolean vBISDarktheme=isContainAttribution(JoAll,"isDarkTheme") ? JoAll.get("isDarkTheme").getAsBoolean() :false;
			int vIBackup=isContainAttribution(JoAll,"backup_frequency") ? JoAll.get("backup_frequency").getAsInt() : 60;
			File vFQsWebsite=isContainAttribution(JoAll,"custom_quickstart") ? new File(JoAll.get("custom_quickstart").getAsString()) : null;
			boolean isDialogRound=isContainAttribution(JoAll,"isDialogRound") ? JoAll.get("isDialogRound").getAsBoolean() :false;
			boolean isAutoPreviewFull=isContainAttribution(JoAll,"isAutoPreviewFull") ? JoAll.get("isAutoPreviewFull").getAsBoolean() :false;
			boolean isAllowAbsolute=isContainAttribution(JoAll,"isAllowAbsolutePath") ? JoAll.get("isAllowAbsolutePath").getAsBoolean() :false;
			boolean isHideHopWeb=isContainAttribution(JoAll,"isHideHopWeb") ? JoAll.get("isHideHopWeb").getAsBoolean() :false;
			boolean isShowWebViewAlert=isContainAttribution(JoAll,"isShowWebViewAlert") ? JoAll.get("isShowWebViewAlert").getAsBoolean() :true;
			File bgFile=isContainAttribution(JoAll,"bg") ? new File(JoAll.get("bg").getAsString()) : null;
			String bgScale=isContainAttribution(JoAll,"bg_scale") ? JoAll.get("bg_scale").getAsString() : null;
			float bgAlpha=isContainAttribution(JoAll,"bg_alpha") ? JoAll.get("bg_alpha").getAsFloat() : 0.5f;

			String[] SaChars=new String[JaChars.size()];
            String[] SaNames=new String[JaNames.size()];

            for(int i=0;i<SaChars.length;i++)
            {
                SaChars[i]=JaChars.get(i).getAsString();
            }
            for(int i=0;i<SaNames.length;i++)
            {
                SaNames[i]=JaNames.get(i).getAsString(); 
            }

            if(SaChars.length==0)
            {
                SettingsClass.SaChars= Vers.i.SaDefChars;
            }
            else
            {
                SettingsClass.SaChars=SaChars;
            }
            if(SaNames.length==0)
            {
                SettingsClass.SaNames= Vers.i.SaDefNames;
            }
            else
            {
                SettingsClass.SaNames=SaNames;
            }
            SettingsClass.ITheme=ITheme;
            SettingsClass.IVirtualTheme=ITheme;
			SettingsClass.BIsDarktheme=vBISDarktheme;
			SettingsClass.BVirtualIsDarktheme=vBISDarktheme;
			SettingsClass.vIBackup=vIBackup;
			SettingsClass.vQsWebsite=vFQsWebsite;
			SettingsClass.isAutoPreviewFull=isAutoPreviewFull;
			SettingsClass.isAllowAbsolute=isAllowAbsolute;
			SettingsClass.isHideHW=isHideHopWeb;
			SettingsClass.isShowWebViewAlert=isShowWebViewAlert;
			SettingsClass.bgFile=bgFile;
			SettingsClass.bgScale=bgScale;
			SettingsClass.bgAlpha=bgAlpha;

			if(SettingsClass.BIsDarktheme)
			{
				int hour= Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
				
				if(hour>=22||hour<5)
				{
					SettingsClass.ITheme=1;
				}
			}
        }
        catch(Exception e)
        {
            SettingsClass.SaChars= Vers.i.SaDefChars;
            SettingsClass.SaNames= Vers.i.SaDefNames;
            SettingsClass.ITheme=-1;
            SettingsClass.IVirtualTheme=-1;
			SettingsClass.BIsDarktheme=false;
			SettingsClass.BVirtualIsDarktheme=false;
			SettingsClass.vIBackup=60;
			SettingsClass.vQsWebsite=null;
			SettingsClass.isAutoPreviewFull=false;
			SettingsClass.isAllowAbsolute=false;
			SettingsClass.isHideHW=false;
			SettingsClass.isShowWebViewAlert=true;
        }
        finally
        {
            Arrays.sort(SettingsClass.SaNames);
        }
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return false;
	}
	
}
