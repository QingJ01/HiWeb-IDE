package com.venter.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.venter.jsappactions.ActionWebView;
import com.venter.jsappactions.Vers;
import com.venter.jsappactions.actions.Event;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MainActivity extends Activity
{
    int titleColor;
    boolean isTitleLight;
    boolean isShowTitle;
    boolean canLongClick;
    boolean isShowWait;
    boolean isAllowZoom;
    boolean isZoomAsPC;
    boolean isAllowEasyApp;

    int statusBarHeight;

    LinearLayout titleBar;
    TextView titleView;
    ActionWebView webView;
    LinearLayout waitLayout;
    ProgressBar progressBar;
    LinearLayout mainLyout;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CrashHandler crashHandler=CrashHandler.getInstance();
        crashHandler.init(this);

        try
        {
            copyDataToSD("manifest", getFilesDir().getPath() + "/manifest");
            String[] manifests=read(new File(getFilesDir().getPath() + "/manifest")).split("\\|");
            titleColor=Color.parseColor(manifests[0]);
            isShowTitle=manifests[1].equals("true") ? true : false;
            canLongClick=manifests[2].equals("true") ? true : false;
            isShowWait=manifests[3].equals("true") ? true : false;
            isAllowZoom=manifests[4].equals("true") ? true : false;
            isZoomAsPC=manifests[5].equals("true") ? true : false;
            isAllowEasyApp =manifests[6].equals("true") ? true : false;
            if(0.213 * Color.red(titleColor) +
                    0.715 * Color.green(titleColor) +
                    0.072 * Color.blue(titleColor) >
                    255 / 2)
            {
                //浅色
                isTitleLight=true;
            }
            else
            {
                //深色
                isTitleLight=false;
            }
        }
        catch (IOException e)
        {
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // 部分机型的 StatusBar 会有半透明的黑色背景
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);// SDK21
            // 获得状态栏高度
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        StyleUtils.setAndroidNativeLightStatusBar(this,isTitleLight);

        mainLyout=new LinearLayout(this);
        mainLyout.setOrientation(LinearLayout.VERTICAL);

        titleBar=new LinearLayout(this);
        titleBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        titleBar.setGravity(Gravity.START|Gravity.CENTER);
        titleBar.setOrientation(LinearLayout.VERTICAL);
        mainLyout.addView(titleBar);

        titleView=new TextView(this);
        titleView.setTextSize(20);
        titleBar.addView(titleView);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLyout.addView(relativeLayout);

        waitLayout=new LinearLayout(this);
        waitLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        waitLayout.setVisibility(View.GONE);
        waitLayout.setGravity(Gravity.CENTER);
        waitLayout.setOrientation(LinearLayout.VERTICAL);
        relativeLayout.addView(waitLayout);

        progressBar=new ProgressBar(this,null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(titleColor == Color.WHITE ? Color.BLACK : titleColor));
        waitLayout.addView(progressBar);

        TextView textView=new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        setPadding(textView,dp2px(20));
        textView.setVisibility(View.GONE);
        waitLayout.addView(textView);

        webView=new ActionWebView(this,this, isAllowEasyApp,titleView,titleBar,statusBarHeight,waitLayout,textView);
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setWebChromeClient(webChromeClient);
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri=Uri.parse(url);
            Intent intent=new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        relativeLayout.addView(webView);

        setContentView(mainLyout);

        if(isShowTitle)
        {
            titleBar.setBackgroundColor(titleColor);
            titleBar.setPadding(dp2px(20),dp2px(20)+statusBarHeight,dp2px(20),dp2px(20));
            titleView.setText(getAppName(this));
            titleView.setTextColor(isTitleLight ? Color.BLACK : Color.WHITE);
        }
        else
        {
            titleBar.setVisibility(View.GONE);
        }
        
        webView.setWebViewClient(new WebViewClient(){
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                if(isShowWait)
                {
                    webView.setVisibility(View.GONE);
                    waitLayout.setVisibility(view.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }

            public void onPageFinished(WebView view, String url)
            {
                if(isShowWait)
                {
                    webView.setVisibility(View.VISIBLE);
                    waitLayout.setVisibility(view.GONE);
                }
                if(isAllowEasyApp)
                {
                    webView.evaluateJavascript(Event.onFinished,null);
                }

                Vers.canGoBack = view.canGoBack();
                Vers.nowUrl = view.getUrl();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newurl) {

                if(!Vers.isAllowSkip)
                {
                    if(isAllowEasyApp)
                    {
                        webView.evaluateJavascript(Event.onSkip(newurl),null);
                    }
                    return true;
                }

                try {
                    //处理intent协议
                    if (newurl.startsWith("intent://")) {
                        Intent intent;
                        try {
                            intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setComponent(null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                intent.setSelector(null);
                            }
                            List<ResolveInfo> resolves = MainActivity.this.getPackageManager().queryIntentActivities(intent,0);
                            if(resolves.size()>0){
                                MainActivity.this.startActivityIfNeeded(intent, -1);
                            }
                            return true;
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    // 处理自定义scheme协议
                    if (!newurl.trim().equals("")&&!newurl.toLowerCase().startsWith("http://")&&!newurl.toLowerCase().startsWith("https://")&&!newurl.toLowerCase().startsWith("file://")&&!newurl.toLowerCase().startsWith("ftp://")&&!newurl.toLowerCase().startsWith("ftps://")&&!newurl.toLowerCase().startsWith("sftp://")&&!newurl.toLowerCase().startsWith("javascript:")&&!newurl.toLowerCase().equals("about:blank")) {
                        try {
                            // 以下固定写法
                            final Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(newurl));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            MainActivity.this.startActivity(intent);
                        } catch (Exception e) {
                        }
                        return true;
                    }
                }
                catch (Exception e) {}


                return insertJSCode(newurl);
            }
        });

        webView.setDownloadListener((String url, String userAgent,String contentDisposition, String mimetype, long contentLength) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        WebSettings webSettings=webView.getSettings();

        if(isAllowZoom)
        {
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
        }
        webSettings.setUseWideViewPort(isZoomAsPC);

        webSettings.setDisplayZoomControls(false);

        webSettings.setLoadsImagesAutomatically(true);

        if(!canLongClick)
        {
            webView.setLongClickable(true);
            webView.setOnLongClickListener(v -> true);
        }

        insertJSCode("file:///android_asset/index.html");
    }

    private boolean insertJSCode(String newurl)
    {
        try
        {
            if(newurl.toLowerCase().startsWith("file:///android_asset/") && newurl.toLowerCase().endsWith(".html"))
            {
                String path=newurl.substring(22);
                copyDataToSD(path,getFilesDir().getPath()+"/_init.html");
                String html = "<script src='file:///android_asset/easyapp-events.js'></script>\n" + read(new File(getFilesDir(),"_init.html"));
                webView.loadDataWithBaseURL("file:///android_asset/",html,"text/html","UTF-8",null);

                return true;
            }
        }
        catch (Exception e)
        {}
        return false;
    }

    private final WebChromeClient webChromeClient=new WebChromeClient(){
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,null)
                    .show();
            result.confirm();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                    .setPositiveButton(android.R.string.cancel, (dialog, which) -> result.cancel())
                    .show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            EditText editText=new EditText(view.getContext());
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setView(editText)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm(editText.getText().toString()))
                    .setPositiveButton(android.R.string.cancel, (dialog, which) -> result.cancel())
                    .show();
            return true;
        }

        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");//文件上传
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            // Uri result = (((data == null) || (resultCode != RESULT_OK)) ? null : data.getData());
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else {
            //这里uploadMessage跟uploadMessageAboveL在不同系统版本下分别持有了
            //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
            //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            } else if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    public void setPadding(View v,int padding)
    {
        v.setPadding(padding,padding,padding,padding);
    }

    public void setMargin(View v,int left, int top, int right, int bottom)
    {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }
    public int dp2px(float dpValue) {
        float scale=MainActivity.this.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
    public void copyDataToSD(String copyFile, String strOutFileName) throws IOException {
        Context c=this;
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = c.getAssets().open(copyFile);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
    public String read(File file) throws IOException
    {
        BufferedReader bfr = new BufferedReader(new FileReader(file));
        String line = bfr.readLine();
        int lines=0;
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            if(lines>0)
                sb.append("\n");
            sb.append(line);
            lines++;
            line = bfr.readLine();
        }
        bfr.close();
        return sb.toString();
    }
    public void write(String text, File file) throws IOException {
        FileOutputStream fos=new FileOutputStream(file);
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        bos.write(text.getBytes(), 0, text.getBytes().length);
        bos.flush();
        bos.close();
    }
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if(isAllowEasyApp&&!(webView.getUrl().startsWith("http://")||webView.getUrl().startsWith("https://")))
        {
            webView.evaluateJavascript(Event.onBack,null);
            return;
        }
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }
}
class StyleUtils
{
    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark)
    {
        if (isMIUI())
        {
            MIUISetStatusBarLightMode(activity, dark);
        }
        else if (isFlyme())
        {
            setFlymeLightStatusBar(activity, dark);
        }
        else
        {
            View decor = activity.getWindow().getDecorView();
            if (dark)
            {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else
            {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark)
    {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null)
        {
            Class clazz = window.getClass();
            try
            {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark)
                {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                }
                else
                {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Integer.parseInt((getSystemProperty("ro.miui.ui.version.name").substring(1))) >= 7)
                {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark)
                    {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    else
                    {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            }
            catch (Exception e)
            {

            }
        }
        return result;
    }
    public static String getSystemProperty(String propName)
    {
        String line;
        BufferedReader input = null;
        try
        {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        }
        catch (IOException ex)
        {
            return null;
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return line;
    }
    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark)
    {
        boolean result = false;
        if (activity != null)
        {
            try
            {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark)
                {
                    value |= bit;
                }
                else
                {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            }
            catch (Exception e)
            {
            }
        }
        return result;
    }
    public static void intent(Activity a, Class<?> c)
    {
        a.startActivity(new Intent(a, c));
    }

    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isFlyme()
    {
        try
        {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    public static boolean isEMUI()
    {
        return isPropertiesExist(KEY_EMUI_VERSION_CODE);
    }

    public static boolean isMIUI()
    {
        return isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME, KEY_MIUI_INTERNAL_STORAGE);
    }

    private static boolean isPropertiesExist(String... keys)
    {
        if (keys == null || keys.length == 0)
        {
            return false;
        }
        try
        {
            BuildProperties properties = BuildProperties.newInstance();
            for (String key : keys)
            {
                String value = properties.getProperty(key);
                if (value != null)
                    return true;
            }
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
    }
    private static final class BuildProperties
    {

        private final Properties properties;

        private BuildProperties() throws IOException
        {
            properties = new Properties();
            // 读取系统配置信息build.prop类
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key)
        {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value)
        {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet()
        {
            return properties.entrySet();
        }

        public String getProperty(final String name)
        {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue)
        {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty()
        {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys()
        {
            return properties.keys();
        }

        public Set<Object> keySet()
        {
            return properties.keySet();
        }
        public int size()
        {
            return properties.size();
        }
        public Collection<Object> values()
        {
            return properties.values();
        }
        public static BuildProperties newInstance() throws IOException
        {
            return new BuildProperties();
        }
    }
}
class CrashHandler implements Thread.UncaughtExceptionHandler
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
