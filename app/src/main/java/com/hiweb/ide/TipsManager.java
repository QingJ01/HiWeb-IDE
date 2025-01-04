package com.venter.easyweb;

import android.content.Context;
import java.io.File;
import java.io.IOException;

public class TipsManager 
{
    private Context ctx;
    private File signs;
    
    public TipsManager(Context c)
    {
        ctx=c;
        signs=new File(ctx.getFilesDir(),"signs");
        signs.mkdir();
    }
    
    public void create(String name)
    {
        try 
        {
            new File(signs, name).createNewFile();
        } 
        catch (IOException e)
        {}
    }

    public void delete(String name)
    {
        new File(signs, name).delete();
    }
    
    public boolean isExists(String name)
    {
        return new File(signs,name).exists();
    }
}
