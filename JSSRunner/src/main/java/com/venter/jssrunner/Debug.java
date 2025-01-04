package com.venter.jssrunner;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class Debug 
{
    @JavascriptInterface
    public void showToast(final String SMsg,boolean BIsLongTime)
    {
        final int ITime;
        if(BIsLongTime)
            ITime= Toast.LENGTH_LONG;
        else
            ITime=Toast.LENGTH_SHORT;
        Values.At.runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(Values.Ctx,SMsg,ITime).show();
                }
            });
    }
}
