package com.venter.jssrunner;
import com.google.gson.JsonObject;
import java.util.Map;
import android.webkit.JavascriptInterface;

import java.io.IOException;
import android.os.Looper;
import android.os.Handler;

import android.util.Log;

import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.FileEntity;
import org.apache.httpcore.entity.StringEntity;

public class Response extends Object
{
    @JavascriptInterface
    public String getURLData()
    {
        try
        {
            JsonObject Jo=new JsonObject();

            Map MDatas=Values.MDatas;

            for(Object SKey:MDatas.keySet())
            {
                Jo.addProperty((String) SKey,MDatas.get(SKey)+"");
            }

            return Jo.toString();
        }
        catch(Exception e)
        {
            return null;
        }
    }
	
	@JavascriptInterface
    public String getURLDatas()
	{
		return getURLData();
	}
	
    private boolean isFirst()
    {
        if(Values.Se==null&&Values.Fe==null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @JavascriptInterface
    public void setString(final String SText,final String SCharset)
    {
        try 
        {  
            if(isFirst())
            {
                StringEntity Se=new StringEntity(SText,SCharset);
                Values.Se=Se;
            }

        }
        catch(Exception e)
        {
            
        }
    }
    
    @JavascriptInterface
    public void setFile(final File FEntity,final String SCharset)
    {    
        try 
        {            
            if(isFirst())
            {
                FileEntity Fe=new FileEntity(new java.io.File(FEntity.getPath()), ContentType.create("*/*",SCharset));
                Values.Fe=Fe;
            }
        }
        catch(Exception e)
        {
            
        }
    }
    
    private java.io.File FTempFile;
    @JavascriptInterface
    public void setHTML(String SCode,final String SCharset) throws IOException
    {
        FTempFile=new java.io.File(Values.Ctx.getCacheDir(),"temp.html");
        
        newTempFile(FTempFile);
        
        Utils.write(SCode,FTempFile);                    
        
        try
        {
            if(isFirst())
            {
                FileEntity Fe=new FileEntity(new java.io.File(FTempFile.getPath()),ContentType.create("*/*",SCharset));
                Values.Fe=Fe;
            }
        }
        catch(Exception e)
        {
            
        }
    }
    private void newTempFile(java.io.File FCreate) throws IOException
    {
        if(FCreate.exists())
        {
            
            if(FCreate.getName().lastIndexOf("_")==-1)
            {
                newTempFile(new java.io.File(Values.Ctx.getCacheDir(),"temp_1.html"));
            }
            else
            {
                int ITimes=0;
                try
                {
                    ITimes=Integer.parseInt(FCreate.getName().replace(".html","").split("_")[1])+1;
                }
                catch(Exception e)
                {
                    ITimes=1;
                }
                
                newTempFile(new java.io.File(Values.Ctx.getCacheDir(),"temp_"+ITimes+".html"));
            }
        }
        else
        {
            FCreate.createNewFile();
            FTempFile=FCreate;
        }
    }
}
