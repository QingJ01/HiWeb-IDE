package com.venter.jssrunner;

import android.app.Activity;
import android.content.Context;

import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.FileEntity;
import org.apache.httpcore.entity.StringEntity;

public class Values 
{
    public static Context Ctx;
    public static Activity At;
    public static java.io.File FWebsite;
    public static Map MDatas;
    public static HttpResponse Hps;
    public static int IPort=-1;
    public static String SPath;
    public static String SFilePath;
	
    public static StringEntity Se=null;
    public static FileEntity Fe=null;
    public static boolean BIsFinish=false;
    
    public static boolean BIsLooping=false;
}
