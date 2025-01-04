package com.venter.jsappactions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.venter.jsappactions.actions.UI;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionWebView extends WebView {

    public ActionWebView(Context context, Activity activity, Boolean isActions, TextView titleView, LinearLayout titleLayout,int statueBarHeight,LinearLayout waitLayout,TextView waitTextView) {
        super(context);
        init(context,activity,isActions,titleView,titleLayout,statueBarHeight,waitLayout,waitTextView);
    }

    private void init(Context context,Activity activity,Boolean isActions, TextView titleView, LinearLayout titleLayout,int statueBarHeight,LinearLayout waitLayout,TextView waitTextView)
    {
        Vers.context=context;
        Vers.activity=activity;
        Vers.webView=this;
        Vers.titleView=titleView;
        Vers.titleLayout=titleLayout;
        Vers.statueBarHeight=statueBarHeight;
        Vers.waitLayout=waitLayout;
        Vers.waitTextView=waitTextView;

        WebSettings webSettings = getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        if(!isActions)
            return;
        try {
            addJavascriptInterface(new com.venter.jsappactions.actions.File(null), "File");
            addJavascriptInterface(new com.venter.jsappactions.actions.UI(), "UI");
            addJavascriptInterface(new com.venter.jsappactions.actions.System(),"System");
        } catch (Exception e) {

        }
    }
}
