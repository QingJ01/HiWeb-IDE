package com.venter.jssrunner;
import android.webkit.JavascriptInterface;
import com.google.gson.JsonArray;
import java.io.IOException;

public class File extends Object
{
    private String SPath;
    
    public File(String SPath)
    {
        this.SPath=SPath;
    }
    
    @JavascriptInterface
    public File newFile(String SPath)
    {
        return new File(SPath);
    }
    
    @JavascriptInterface
    public void createFile(boolean BCanMakePathDir)
    {
        if(BCanMakePathDir)
            new java.io.File(SPath).getParentFile().mkdirs();
    }
    
    @JavascriptInterface
    public void createDir(boolean BCanMakePathDir)
    {
        if(BCanMakePathDir)
            new java.io.File(SPath).getParentFile().mkdirs();
    }
    
    @JavascriptInterface
    public void delete(File FDeleteFile)
    {
        Utils.delete.delete(FDeleteFile.getPath());
    }
    @JavascriptInterface
    public void deleteFile(File FDeleteFile) throws IOException
    {
		delete(FDeleteFile);
    }
    @JavascriptInterface
    public String getName()
    {
        return new java.io.File(SPath).getName();
    }
    
    @JavascriptInterface
    public String getPath()
    {
        return SPath;
    }
    
    @JavascriptInterface
    public String getParent()
    {
        return new java.io.File(SPath).getParent();
    }
    
    @JavascriptInterface
    public File getParentFile()
    {
        return newFile(new java.io.File(SPath).getParent());
    }
    
    @JavascriptInterface
    public String getType()
    {
        String originName = new java.io.File(SPath).getName();
        if(originName.lastIndexOf(".")!=-1)
        {
            String[] names = originName.split("\\.");
            return names[names.length-1];
        }
        else
        {
            return null;
        }
    }
    
    @JavascriptInterface
    public void copy( File FSource , File FTarget ) throws Exception
    {
        Utils.copy(FSource.SPath,FTarget.SPath);
    }
    
    @JavascriptInterface
    public File getWebsite()
    {
        if(Values.FWebsite==null)
            return null;
        return newFile(Values.FWebsite.getPath());
    }
    
    @JavascriptInterface
    public String getWebsitePath()
    {
        if(Values.FWebsite==null)
            return null;
        return Values.FWebsite.getPath();
    }
    
    @JavascriptInterface
    public boolean isFile()
    {
        return new java.io.File(SPath).isFile();
    }
    
    @JavascriptInterface
    public boolean isDir()
    {
        return new java.io.File(SPath).isDirectory();
    }
    
    @JavascriptInterface
    public boolean exists()
    {
        return new java.io.File(SPath).exists();
    }
    
    @JavascriptInterface
    public void renameTo(File FRenamed)
    {
        new java.io.File(SPath).renameTo(new java.io.File(FRenamed.SPath));
    }
    
    @JavascriptInterface
    public String read() throws IOException
    {
        return Utils.read(new java.io.File(SPath));
    }
    
    @JavascriptInterface
    public void write(String SText) throws IOException
    {
        Utils.write(SText,new java.io.File(SPath));
    }
    
    @JavascriptInterface
    public String listNames()
    {
        String[] Sa = new java.io.File(SPath).list();
        JsonArray Ja=new JsonArray();
        for(String S:Sa)
        {
            Ja.add(S);
        }
        return Ja.toString();
    }
    
    @JavascriptInterface
    public String listFiles()
    {
        java.io.File[] Fa=new java.io.File(SPath).listFiles();
        JsonArray JaTo=new JsonArray();
        
        
        for(int i=0;i<Fa.length;i++)
        {
            JaTo.add(Fa[i].getPath());
        }
        
        return JaTo.toString();
    }
}

