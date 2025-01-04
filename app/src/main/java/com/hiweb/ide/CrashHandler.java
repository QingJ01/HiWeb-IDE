package com.hiweb.ide;

import android.content.Context;
import android.content.Intent;
import com.hiweb.ide.ErrorActivity;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;

public class CrashHandler implements Thread.UncaughtExceptionHandler
{
	private static CrashHandler instance;
	private Context ctx;
	public static CrashHandler getInstance()
	{
		if(instance==null)
		{
			instance=new CrashHandler();
		}
		return instance;
	}
	public void init(Context ctx)
	{
		this.ctx=ctx;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	@Override
	public void uncaughtException(Thread p1, Throwable p2)
	{
		err(p2);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	private synchronized void err(Throwable t)
    {

        String msg= ExceptionUtils.getStackTrace(t);
        Intent i=new Intent();
        i.setClass(ctx,ErrorActivity.class);
        i.putExtra("ErrorMsg",msg);

        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  

        ctx.startActivity(i);


    }
}
