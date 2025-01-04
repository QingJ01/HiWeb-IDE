package com.venter.jsappactions.actions;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.venter.jsappactions.Utils;
import com.venter.jsappactions.Vers;

public class UI {
    @JavascriptInterface
    public void showToast(final String msg,boolean isLengthLong)
    {
        int length;
        if(isLengthLong)
            length= Toast.LENGTH_LONG;
        else
            length=Toast.LENGTH_SHORT;
        Utils.runOnActivity(() -> Toast.makeText(Vers.context,msg,length).show());
    }
    @JavascriptInterface
    public void setTitle(String title)
    {
        Utils.runOnActivity(() -> Vers.titleView.setText(title));
    }
    @JavascriptInterface
    public void setTitleColor(String color)
    {
        int col=Color.parseColor(color);
        Utils.runOnActivity(() ->{
            Vers.titleLayout.setBackgroundColor(col);
            if(0.213 * Color.red(col) +
                    0.715 * Color.green(col) +
                    0.072 * Color.blue(col) >
                    255 / 2)
            {
                //浅色
                Vers.titleView.setTextColor(Color.BLACK);
            }
            else
            {
                //深色
                Vers.titleView.setTextColor(Color.WHITE);
            }
        });
    }
    @JavascriptInterface
    public String getTitle()
    {
        return Vers.titleView.getText().toString();
    }
    @JavascriptInterface
    public String getTitleColor()
    {
        return Utils.convertToRGB(((ColorDrawable) Vers.titleLayout.getBackground()).getColor(),true);
    }
    @JavascriptInterface
    public void setTitleVisible(boolean isVisible)
    {
        Utils.runOnActivity(() -> Vers.titleLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE));
    }
    @JavascriptInterface
    public boolean getTitleVisible()
    {
        return Vers.titleLayout.getVisibility()==View.VISIBLE;
    }
    @JavascriptInterface
    public int getStatueBarHeight()
    {
        return Utils.px2dp(Vers.context,Vers.statueBarHeight);
    }
    @JavascriptInterface
    public void showProgress(String information)
    {
        if(information==null)
        {
            Utils.runOnActivity(() -> {
                Vers.waitTextView.setVisibility(View.GONE);
                Vers.webView.setVisibility(View.GONE);
                Vers.waitLayout.setVisibility(View.VISIBLE);
            });
        }
        else
        {
            Utils.runOnActivity(() -> {
                Vers.waitTextView.setVisibility(View.VISIBLE);
                Vers.waitTextView.setText(information);
                Vers.webView.setVisibility(View.GONE);
                Vers.waitLayout.setVisibility(View.VISIBLE);
            });
        }
    }
    @JavascriptInterface
    public void hideProgress()
    {
        Utils.runOnActivity(() -> {
            Vers.webView.setVisibility(View.VISIBLE);
            Vers.waitLayout.setVisibility(View.GONE);
        });
    }

    @JavascriptInterface
    public void clearHistory()
    {
        Utils.runOnActivity(() -> {
            Vers.webView.clearHistory();
        });
    }

    @JavascriptInterface
    public void goBack()
    {
        Utils.runOnActivity(() -> {
            Vers.webView.goBack();
        });
    }

    @JavascriptInterface
    public boolean canGoBack()
    {
        return Vers.canGoBack;
    }

    @JavascriptInterface
    public void allowSkip(boolean isAllowSkip)
    {
        Vers.isAllowSkip=isAllowSkip;
    }

    @JavascriptInterface
    public String getURL()
    {
        return Vers.nowUrl;
    }

    @JavascriptInterface
    public void setScreenOrientation(String orientation)
    {
        if(orientation.equals("landscape"))
            Vers.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else if(orientation.equals("portrait"))
            Vers.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            Vers.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }
}