package com.venter.jsappactions.actions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebStorage;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.venter.jsappactions.Utils;
import com.venter.jsappactions.Vers;

public class System {

    @JavascriptInterface
    public boolean isHaveStoragePermission()
    {
        return ContextCompat.checkSelfPermission(Vers.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }

    @JavascriptInterface
    public void showRequestStoragePermissionWindow()
    {
        try {
            ActivityCompat.requestPermissions(Vers.activity,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, 0);
        } catch (Exception t) {

        }
    }
    @JavascriptInterface
    public String getStoragePath()
    {
        return Environment.getExternalStorageDirectory().getPath();
    }

    @JavascriptInterface
    public boolean startApp(String packageName) {
        try {
            Vers.activity.startActivity(Vers.context.getPackageManager().getLaunchIntentForPackage(packageName));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @JavascriptInterface
    public void exitApp() {
        Vers.activity.finish();
    }

    @JavascriptInterface
    public void clearData()
    {
        //清除数据库缓存
        Vers.context.deleteDatabase("webview.db");
        Vers.context.deleteDatabase("webviewCache.db");
        java.io.File file = Vers.activity.getApplicationContext().getCacheDir().getAbsoluteFile();
        Utils.delete.delete(file.getPath());

        //清空Cookie
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        //清空LocalStorage
        WebStorage.getInstance().deleteAllData();
    }
}
