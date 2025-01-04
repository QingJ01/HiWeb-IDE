package com.venter.jssrunner;
import android.app.Activity;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.httpcore.HttpResponse;

public class JssRunner extends WebView {
    public static String VER="0.0.4";
    public JssRunner(Context ctx) {
        super(ctx);

        WebSettings webSettings = getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
		webSettings.setAllowContentAccess(true);

        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);

        webSettings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存

        setWebViewClient(Wvc);
        try {
            addJavascriptInterface(new File(null), "File"); 
            addJavascriptInterface(new Response(), "Response");
            addJavascriptInterface(new Debug(), "Debug");
        } catch (Exception e) {

		}
    }

    public static String getVersionName()
    {
        return VER;
    }
    
    WebViewClient Wvc=new WebViewClient()
    {
        @Override
        public void onPageFinished(android.webkit.WebView view, java.lang.String url)
        {
            Values.BIsFinish=true;
            
        }
        
    };
    
    public void run(WebChromeClient Wcc, Activity A, String SCode, java.io.File FWebsite, Map MDatas, HttpResponse Hps, int IPort, String SPath, String SFilePath) {

        setWebChromeClient(Wcc);
        
        Values.Ctx=getContext();
        
        Values.FWebsite = FWebsite;

        Values.MDatas = MDatas;
        
        Values.Hps = Hps;
        
        Values.At=A;
        
        Values.IPort=IPort;
        
        Values.SPath=SPath;
        
        Values.SFilePath=SFilePath;
		
        Values.Se=null;
        
        Values.Fe=null;
        
		java.io.File tempJSFile=new java.io.File(getContext().getFilesDir(),"temp_javascript");
		try
		{
			tempJSFile.createNewFile();
			write(SCode, tempJSFile);
			loadDataWithBaseURL("file://" + tempJSFile.getParent() + "/", "<script src='temp_javascript' type='text/javascript'></script>"+"\n<script>\n"+"Response.setString(\"\",\"utf-8\");"+"\n</script>", "text/html", "utf-8", null);
		}
		catch (Exception e)
		{
			
		}
    }
	private void write(String text, java.io.File file) throws IOException {
		FileOutputStream fos=new FileOutputStream(file);
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        bos.write(text.getBytes(), 0, text.getBytes().length);
        bos.flush();
        bos.close();
	}
}
