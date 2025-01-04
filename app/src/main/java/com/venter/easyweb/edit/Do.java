package com.venter.easyweb.edit;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.method.*;
import android.view.*;
import android.view.inputmethod.*;
import android.webkit.*;
import com.google.gson.*;
import com.venter.easyweb.*;
import com.venter.easyweb.R;
import com.venter.easyweb.add.addViewWidget.FeedButton;
import com.venter.easyweb.server.*;
import com.venter.jssrunner.*;
import com.yanzhenjie.andserver.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import android.content.ClipboardManager;
import android.util.Base64;

import com.venter.easyweb.ProgressDialog;

import java.io.File;
import java.lang.Process;

import com.venter.easyweb.server.easyweb_server.*;
import com.yanzhenjie.andserver.website.FileBrowser;
import com.yanzhenjie.andserver.website.StorageWebsite;
import com.yanzhenjie.andserver.website.WebSite;

import android.graphics.*;
import android.view.animation.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.jsoup.nodes.Element;

public class Do {
    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static String getText(File file) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(file));
        String line = bfr.readLine();
        int lines = 0;
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            if (lines > 0)
                sb.append("\n");
            sb.append(line);
            lines++;
            line = bfr.readLine();
        }
        bfr.close();
        return sb.toString();
    }

    public static void write(String text, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(text.getBytes(), 0, text.getBytes().length);
        bos.flush();
        bos.close();
    }

    public static void write(byte[] bytes, FileOutputStream fos) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(bytes, 0, bytes.length);
        bos.flush();
        bos.close();
    }

    private static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static String testGetHtml(String urlpath, int sec) {
        try {
            URL url = new URL(urlpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(sec * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                byte[] data = readStream(inputStream);
                String html = new String(data);
                return html;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static int copyDir(String fromFile, String toFile) throws Exception {

        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copyDir(currentFiles[i].getPath() + "/", toFile + "/" + currentFiles[i].getName() + "/");

            } else//如果当前项为文件则进行文件拷贝
            {
                copySdcardFile(currentFiles[i].getPath(), toFile + "/" + currentFiles[i].getName());
            }
        }
        return 0;
    }

    public static void copySdcardFile(String fromFile, String toFile) throws Exception {

        InputStream fosfrom = new FileInputStream(fromFile);
        OutputStream fosto = new FileOutputStream(toFile);
        byte bt[] = new byte[1024];
        int c;
        while ((c = fosfrom.read(bt)) > 0) {
            fosto.write(bt, 0, c);
        }
        fosfrom.close();
        fosto.close();
    }

    public static void downloadNet(String webUrl, String toPath) throws Exception {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(webUrl);

        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        FileOutputStream fs = new FileOutputStream(toPath);

        byte[] buffer = new byte[1204];
        int length;
        while ((byteread = inStream.read(buffer)) != -1) {
            bytesum += byteread;

            fs.write(buffer, 0, byteread);
        }
    }

    public static String enbase64(String p) {
        return Base64.encodeToString(p.getBytes(), Base64.DEFAULT);
    }

    public static String debase64(String p) {
        return new String(Base64.decode(p, Base64.DEFAULT));
    }

    public static String enbase64file(File p) throws IOException {
        FileInputStream inputFile = null;

        inputFile = new FileInputStream(p);
        byte[] buffer = new byte[(int) p.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);


    }

    public static void debase64file(String p, String path) throws IOException {
        File desFile = new File(path);
        FileOutputStream fos = null;

        byte[] decodeBytes = Base64.decode(p.getBytes(), Base64.DEFAULT);
        fos = new FileOutputStream(desFile);
        fos.write(decodeBytes);
        fos.close();
    }

    public static int getMFileKey(HashMap<Integer, File> map, File value) {
        int key = -1;
        //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
        for (int getKey : map.keySet()) {
            if (((File) (map.get(getKey))).getPath().equals(value.getPath())) {
                key = getKey;
                break;
            }
        }
        return key;
        //这个key肯定是第一个满足该条件的key.
    }

    public static void copyText(Context c, String content) {
        ClipboardManager cmb = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content);
    }

    public static void copyDataToSD(Context c, String copyFile, String strOutFileName) throws IOException {
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

    public static boolean upZipFileDir(File zipFile, File folderFile) {
        ZipFile zfile = null;
        try {
            //转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList = zfile.getEntries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            //列举的压缩文件里面的各个文件，判断是否为目录
            if (ze.isDirectory()) {
                String dirstr = folderFile.getPath() + "/" + ze.getName();
                dirstr.trim();
                File f = new File(dirstr);
                f.mkdirs();
                continue;
            }
            OutputStream os = null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderFile.getPath(), ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int readLen = 0;
            //进行一些内容复制操作
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;

        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                ret = new File(ret, substr);
            }

            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            ret = new File(ret, substr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {

            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {

                return true;
            } else {

                return false;
            }
        } else {

            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {

            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {

            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {

            return true;
        } else {
            return false;
        }
    }

    public static String getErrorMsg(Exception e) {
        return ExceptionUtils.getStackTrace(e);
    }

    public static String URLEncode(String url) {
        return url
                .replace("%", "%25")
                .replace("#", "%23")
                .replace("\"", "%22")
                .replace("&", "%26")
                .replace("(", "%28")
                .replace(")", "%29")
                .replace("+", "%2B")
                .replace(",", "%2C")
                //.replace("/","%2F")
                .replace(":", "%3A")
                .replace(";", "%3B")
                .replace("<", "%3C")
                .replace("=", "%3D")
                .replace(">", "%3E")
                .replace("?", "%3F")
                .replace("@", "%40")
                .replace("\\", "%5C")
                .replace("|", "%7C")
                .replace(" ", "%20");
    }

    public static void CanSelect(TextView Tv) {
        Tv.setEnabled(true);
        Tv.setTextIsSelectable(true);
        Tv.setFocusable(true);
        Tv.setLongClickable(true);
    }

    public static String getIpAddressString() throws SocketException {
        for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
            NetworkInterface netI = enNetI.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = netI
                    .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return "0.0.0.0";
    }

    public static String getTime(String Format) {
        SimpleDateFormat df = new SimpleDateFormat(Format);
        return df.format(new Date());
    }

    public static Server.ServerListener getSsl(final int port) {
        return new Server.ServerListener() {
            @Override
            public void onStarted() {
                MainActivity.main.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            ((LocalServersManager.ServerItem) (Vers.easyWebServerWebsitesMap.get(port)[3])).setIcon(LocalServersManager.ServerItem.STATUS_RUNNING);
                        } catch (Exception e) {

                        }
                    }
                });
            }

            @Override
            public void onStopped() {
                MainActivity.main.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            ((LocalServersManager.ServerItem) (Vers.easyWebServerWebsitesMap.get(port)[3])).setIcon(LocalServersManager.ServerItem.STATUS_OFF);
                        } catch (Exception e) {

                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                MainActivity.main.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            ((LocalServersManager.ServerItem) (Vers.easyWebServerWebsitesMap.get(port)[3])).setIcon(LocalServersManager.ServerItem.STATUS_ALERT);
                            ((LocalServersManager.ServerItem) (Vers.easyWebServerWebsitesMap.get(port)[3])).SAlert = e.toString();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };
    }

    public static class Webpage {
        private String pageUrl;//定义需要操作的网页地址
        private String pageEncode = "UTF8";//定义需要操作的网页的编码

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public String getPageEncode() {
            return pageEncode;
        }

        public void setPageEncode(String pageEncode) {
            this.pageEncode = pageEncode;
        }

        //定义取源码的方法
        public String getPageSource() {
            StringBuffer sb = new StringBuffer();
            try {
                //构建一URL对象
                URL url = new URL(pageUrl);
                //使用openStream得到一输入流并由此构造一个BufferedReader对象
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), pageEncode));
                String line;
                //读取www资源
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
            } catch (Exception ex) {
                return "$Cantconnect$";
            }
            return sb.toString();
        }

        //定义一个把HTML标签删除过的源码的方法
        public String getPageSourceWithoutHtml() {
            final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
            final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
            final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
            String htmlStr = getPageSource();//获取未处理过的源码
            Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
            Matcher m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
            htmlStr = htmlStr.trim(); // 返回文本字符串
            htmlStr = htmlStr.replaceAll(" ", "");
            htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
            return htmlStr;
        }
    }

    public static Server getRegServer(Server.Builder Sb, int IPort, Server.ServerListener Ssl) {
        Server.Builder SbNew = Sb;
        if (Vers.easyWebServerRegsMap.containsKey(IPort)) {
            Map MNow = ((Map) (Vers.easyWebServerRegsMap.get(IPort)));
            for (Object SPath : MNow.keySet())
                Sb.registerHandler((String) SPath, new StartHandler(IPort, ((File) (((Object[]) MNow.get(SPath))[0])), (Integer) (((Object[]) MNow.get(SPath))[1]), (String) SPath, new JssRunner(MainActivity.main), true));
        }
        Server S = SbNew.build();
        return S;
    }

    public static void goVia(final String SUrl, final int times) throws MalformedURLException, IOException {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < times; i++) {
                    Do.testGetHtml(SUrl, 10);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
                System.gc();
            }
        }).start();
    }

    public static Intent getEasyWebServerServiceIntent() {
        Intent I = new Intent();
        I.setClassName("com.venter.easyweb", "com.venter.easyweb.server.easyweb_server.EasyWebServerService");
        return I;
    }

    public static Intent getPHPServerProcessIntent() {
        Intent I = new Intent();
        I.setClassName("com.venter.easyweb", "com.venter.easyweb.server.php_server.PHPServerProcess");
        return I;
    }

    public static void showLocalServerHelp() throws IOException {
        String SName;
        SName = "zh.txt";
        copyDataToSD(MainActivity.main, "server_help/" + SName, MainActivity.main.getFilesDir().getPath() + "/help.txt");
        String SText = getText(new File(MainActivity.main.getFilesDir(), "help.txt"));
        final String[] SaMenu = (SText.split("<split>")[0]).split("<menu>");
        final String[] SaArticle = (SText.split("<split>")[1]).split("<unit>");
        Dl Adb = new Dl(MainActivity.main);
        Adb.builder.setTitle(R.string.help_of_httpmanager);
        Adb.builder.setItems(SaMenu, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Dl AdbArticle = new Dl(MainActivity.main);
                AdbArticle.builder.setTitle(SaMenu[p2]);
                AdbArticle.builder.setMessage(SaArticle[p2]);
                AdbArticle.builder.setPositiveButton(R.string.ok, null);
                AdbArticle.show();
            }
        });
        Adb.show();
    }

    public static void saveHistory(File projectDir, String name) throws Exception {
        JsonObject JoItem = new JsonObject();
        JoItem.addProperty("name", name);
        JoItem.addProperty("path", projectDir.getPath());

        File FTemp = new File(MainActivity.main.getFilesDir(), "htx.json");
        if (!FTemp.exists()) {
            FTemp.createNewFile();
            write("[]", FTemp);
        }
        String SText = getText(FTemp);
        JsonParser Jp = new JsonParser();
        JsonArray Ja = (JsonArray) Jp.parse(SText);

        JsonArray JaFinish = new JsonArray();
        JaFinish.add(JoItem);
        int nums = (Ja.size() < 10 ? Ja.size() : 10);
        for (int i = 0; i < nums; i++) {
            if (((JsonObject) Ja.get(i)).get("path").getAsString().equals(projectDir.getPath())) {
                continue;
            }
            JaFinish.add((JsonObject) Ja.get(i));
        }
        while (JaFinish.size() > 10) {
            JaFinish.remove(JaFinish.size() - 1);
        }
        write(JaFinish.toString(), FTemp);
    }

    public static WebChromeClient WccJSSRunner = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {

            final Dl Adb = new Dl(webView.getContext());
            Adb.builder.setTitle("Alert" + "-" + Values.IPort);

            View V = LayoutInflater.from(webView.getContext()).inflate(R.layout.dialog_alert, null);

            TextView ActvHandler = V.findViewById(R.id.actv_dialog_handler);
            TextView ActvJSS = V.findViewById(R.id.actv_dialog_jss);
            TextView ActvMsg = V.findViewById(R.id.actv_dialog_alert);

            ActvMsg.setEnabled(true);
            ActvMsg.setTextIsSelectable(true);
            ActvMsg.setFocusable(true);
            ActvMsg.setLongClickable(true);

            ActvHandler.setText("\"" + Values.SPath + "\"");
            ActvJSS.setText("\"" + Values.SFilePath + "\"");
            ActvMsg.setText(message);

            Adb.builder.setView(V);
            Adb.builder.setPositiveButton(R.string.ok, null);

            Values.At.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Adb.show();
                }
            });

            result.confirm();
            return true;
        }
    };


    public static Context getThemeContext(Context a) {
        Locale l = getTheme();
        Resources res = a.getResources();
        Configuration configuration = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            configuration.setLocale(l);
            LocaleList localeList = new LocaleList(l);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            return a.createConfigurationContext(configuration);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            configuration.setLocale(l);
            return a.createConfigurationContext(configuration);

        }
        return null;
    }

    public static Locale getTheme() {
        Locale l = null;
        switch (SettingsClass.ITheme) {
            case -1:
                //默认
                l = new Locale("zh");
                break;
            case 0:
                //经典
                l = new Locale("en");
                break;
            case 1:
                //深邃
                l = new Locale("fr");
                break;
            case 2:
                //森色
                l = new Locale("de");
                break;
            case 3:
                //淡雅
                l = new Locale("it");
                break;
            case 4:
                //水墨
                l = new Locale("ru");
                break;
        }
        return l;
    }

    public static void restartApplication(Context c) {
        final Intent intent = c.getPackageManager().getLaunchIntentForPackage(c.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean isExistMainActivity(Context c, Class<?> activity) {
        Intent intent = new Intent(c, activity);
        ComponentName cmpName = intent.resolveActivity(c.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity    
            ActivityManager am = (ActivityManager) c.getSystemService(c.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);

            ActivityManager.RunningTaskInfo runningTaskInfo = taskInfoList.get(0);// 只是拿          当前运行的栈
            int numActivities = taskInfoList.get(0).numActivities;

            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
                    flag = true;
                    break;//跳出循环，优化效率
                }
            }
        }
        return flag;

    }

    public static void openFile(Context context,File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uriForFile;
        if (Build.VERSION.SDK_INT > 23){
            uriForFile = FileProvider.getUriForFile(context, "com.venter.easyweb.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            uriForFile = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uriForFile,"*/*");
        context.startActivity(intent);
    }

    public static void showImgDialog(Context c, int title, int msg, int img) {
        showImgDialogCust(c, c.getString(title), msg, img, R.string.ok, null, false);
    }

    public static void showImgDialogCust(Context c, String title, int msg, int img, int p, DialogInterface.OnClickListener click, boolean cancelable) {
        LinearLayout Ly = new LinearLayout(c);
        ScrollView Sv = new ScrollView(c);
        Sv.setFillViewport(true);
        RelativeLayout Rl = new RelativeLayout(c);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1, -2));
        Ly = new LinearLayout(c);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        Ly.setOrientation(LinearLayout.HORIZONTAL);
        Ly.setGravity(Gravity.CENTER);
        Ly.setPadding(dp2px(c, 10), dp2px(c, 10), dp2px(c, 10), dp2px(c, 10));
        Sv.addView(Rl);
        Rl.addView(Ly);

        Dl Adb = new Dl(c);
        Adb.builder.setTitle(title);
        TextView Actv = new TextView(c);
        Actv.setText(msg);
        Actv.setTextSize(15);
        Actv.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1));
        ImageView Iv = new ImageView(c);
        Iv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(c, 100), Do.dp2px(c, 180)));
        Iv.setImageDrawable(c.getResources().getDrawable(img));
        Ly.addView(Actv);
        Ly.addView(Iv);

        setMargin(Iv, dp2px(c, 10), dp2px(c, 10), dp2px(c, 10), dp2px(c, 10));

        Adb.builder.setView(Sv);
        Adb.builder.setPositiveButton(p, click);
        Adb.builder.setCancelable(cancelable);
        Adb.show();
    }

    public static void startWaiting(final boolean IsShowHide) {

        startWaiting(IsShowHide, MainActivity.main);
    }

    public static void startWaiting(final boolean IsShowHide, Context ctx) {

        ProgressDialog Pd = new ProgressDialog(ctx);
        Pd.setCancelable(false);
        Pd.Show(IsShowHide);
    }

    public static void finishWaiting() {
        try {
            ProgressDialog.dismiss();
        } catch (Exception e) {

        }
    }

    public static void showWaitAndRunInThread(final boolean isShowHide, final Runnable run) {
        showWaitAndRunInThread(isShowHide, MainActivity.main, run);
    }

    public static void showWaitAndRunInThread(final boolean isShowHide, final Activity activity, final Runnable run) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                startWaiting(isShowHide, activity);
                new Thread(run).start();
            }
        });
    }

    public static void showErrDialog(Context c, Exception e) {
        Dl Adb = new Dl(c);
        Adb.builder.setTitle(R.string.error);
        Adb.builder.setMessage(getErrorMsg(e));
        Adb.builder.setPositiveButton(R.string.close, null);
        Adb.show();
    }

    public static String[] mergeArrays(String[] a1, String[] a2) {
        Map map = new HashMap();
        int i = 0;
        for (int arrayI = 0; arrayI < a1.length; arrayI++) {
            if (!map.containsValue(a1[arrayI])) {
                map.put(i, a1[arrayI]);
                i++;
            }
        }
        for (int arrayI = 0; arrayI < a2.length; arrayI++) {
            if (!map.containsValue(a2[arrayI])) {
                map.put(i, a2[arrayI]);
                i++;
            }
        }

        String[] outputArray = new String[map.size()];
        for (Object ii : map.keySet()) {
            outputArray[(int) ii] = (String) map.get(ii);
        }

        Arrays.sort(outputArray);
        return outputArray;
    }

    public static String[] excludeArray(String[] originArray, String[] excludeArray) {
        Map originMap = new HashMap();
        Map excludeMap = new HashMap();
        for (int arrayI = 0, i = 0; arrayI < excludeArray.length; arrayI++) {
            if (!excludeMap.containsValue(excludeArray[arrayI])) {
                excludeMap.put(i, excludeArray[arrayI]);
                i++;
            }
        }

        for (int arrayI = 0, i = 0; arrayI < originArray.length; arrayI++) {
            if (!originMap.containsValue(originArray[arrayI]) && !excludeMap.containsValue(originArray[arrayI])) {
                originMap.put(i, originArray[arrayI]);
                i++;
            }
        }


        String[] outputArray = new String[originMap.size()];
        for (Object ii : originMap.keySet()) {
            outputArray[(int) ii] = (String) originMap.get(ii);
        }

        Arrays.sort(outputArray);
        return outputArray;
    }

    public static String getRelativePath(String sourcePath, String targetPath) {
        StringBuilder sb = new StringBuilder();

        String[] nowArr = sourcePath.split("/");
        String[] targetArr = targetPath.split("/");
        List<String> nowList = new ArrayList<String>();
        List<String> targetList = new ArrayList<String>();

        int minLength = nowArr.length < targetArr.length ? nowArr.length : targetArr.length;//比较两个路径哪个短
        for (int i = 0; i < minLength; i++) {
            //消除两个路径相同的地方
            if (nowArr[i].equals(targetArr[i])) {
                nowArr[i] = "";
                targetArr[i] = "";
            } else {
                break;
            }
        }

        for (int i = 0; i < nowArr.length; i++) {
            if (!nowArr[i].equals("")) {
                nowList.add(nowArr[i]);
            }
        }
        for (int i = 0; i < targetArr.length; i++) {
            if (!targetArr[i].equals("")) {
                targetList.add(targetArr[i]);
            }
        }
		
		/*
		 情况1
		 NOW /a/file
		 TAR /a/b/file2
		 消后 
		 NOW file(长=1)
		 TAR b/file2(长>1)
		*/
        if (nowList.size() == 1 && targetList.size() > 1) {
            for (int i = 0; i < targetList.size(); i++) {
                sb.append(targetList.get(i) + "/");
            }
        }
		
		/*
		 情况2
		 NOW /a/file
		 TAR /a/file2
		 消后 
		 NOW file(长=1)
		 TAR file2(长=1)
		 */
        else if (nowList.size() == 1 && targetList.size() == 1) {
            sb.append(targetList.get(0) + "#");//'#'没任何意义，只是用来占位，下同
        }
		 
		/*
		 情况3
		 NOW /a/file
		 TAR /file2
		 消后 
		 NOW a/file(长>1)
		 TAR file2(长=1)
		 */
        else if (nowList.size() > 1 && targetList.size() == 1) {
            for (int i = 0; i < nowList.size() - 1; i++) {
                sb.append("../");
            }
            sb.append(targetList.get(0) + "#");
        }
		 
		/*
		 情况4
		 NOW /a/file
		 TAR /a2/file2
		 消后 
		 NOW a/file(长>1)
		 TAR a2/file2(长>1)
		 
		 与情况3雷同
		 */
        else if (nowList.size() > 1 && targetList.size() > 1) {
            for (int i = 0; i < nowList.size() - 1; i++) {
                sb.append("../");
            }
            for (int i = 0; i < targetList.size(); i++) {
                sb.append(targetList.get(i) + "/");
            }
        }
		 
		/*
		 情况5
		 NOW /a/file
		 TAR /a/file
		 消后 
		 NOW (长=0)
		 TAR (长=0)
		 */
        else if (nowList.size() == 0 && targetList.size() == 0) {
            sb.append("#");
        }
		 
		/*
		 情况6
		 NOW /a/b/file
		 TAR /a
		 消后 
		 NOW b/file(长>0)
		 TAR (长=0)
		 */
        else if (nowList.size() > 0 && targetList.size() == 0) {
            for (int i = 0; i < nowList.size(); i++) {
                sb.append("../");
            }
            sb.append(new File(targetPath).getName() + "#");
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public static void installApkFile(Context context, String filePath) {

        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uriForFile;
        if (Build.VERSION.SDK_INT > 23){
            uriForFile = FileProvider.getUriForFile(context, "com.venter.easyweb.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            uriForFile = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uriForFile,"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * QQ包名
     */
    public static final String PACKAGENAME_QQ = "com.tencent.mobileqq";

    /**
     * 判断应用是否已安装
     */
    public static boolean checkApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 打开指定的QQ群聊天页面
     *
     * @param context 上下文
     * @param key     由QQ官网生成的Key
     */
    public static boolean joinQQGroup(Context context, String key) {
        try {
            String url = "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key;
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static void exit(Context c) {
        Vers.i.hasRunned = false;
        MainActivity.main.finish();

//		Intent i=new Intent(c,PreActivity.class);
//		i.putExtra("isExit",true);
//		c.startActivity(i);
    }

    public static void killAppProcess(Activity a) {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager) a.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static void openUrl(String url, Context c) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        c.startActivity(intent);
    }

    public static boolean isTextFile(File file) throws IOException {
        return !isBinaryFile(file);
    }

    public static boolean isBinaryFile(File file) {
        boolean isBinary = false;
        try {
            FileInputStream fin = new FileInputStream(file);
            long len = file.length();
            for (int j = 0; j < (int) len; j++) {
                int t = fin.read();
                if (t < 32 && t != 9 && t != 10 && t != 13) {
                    isBinary = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isBinary;
    }

    public static void onlyCanEdit(EditText et, String chars, int length) {
        if (chars != null)
            et.setKeyListener(DigitsKeyListener.getInstance(chars));
        if (length != -1)
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public static String runCommand(String[] cmds) throws Exception {

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmds);
        process.waitFor();

        String output = "";
        InputStream i = process.getInputStream();
        InputStreamReader r = new InputStreamReader(i);
        BufferedReader br = new BufferedReader(r);
        String line;
        while ((line = br.readLine()) != null) {
            output += (line + "\n");
        }
        if (!output.equals(""))
            return output;

        i = process.getErrorStream();
        r = new InputStreamReader(i);
        br = new BufferedReader(r);
        line = "";
        while ((line = br.readLine()) != null) {
            output += (line + "\n");
        }

        if (!output.equals(""))
            output = "Error: " + output;

        return output;
    }

    public static void hideIME() {
        if (MainActivity.main.getCurrentFocus() != null) {
            InputMethodManager editorImm = (InputMethodManager) MainActivity.main.getSystemService(Context.INPUT_METHOD_SERVICE);
            editorImm.hideSoftInputFromWindow(MainActivity.main.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showHopWebDialog() {
        Dl dl = new Dl(MainActivity.main);
        dl.builder.setIcon(R.drawable.hopweb);
        dl.builder.setTitle(R.string.get_hopweb1);
        dl.builder.setMessage(R.string.get_hopweb2);
        dl.builder.setCancelable(false);
        dl.builder.setPositiveButton(R.string.get_hopweb, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                openUrl("https://www.coolapk.com/apk/com.venter.hopweb", MainActivity.main);
            }
        });
        dl.builder.setNegativeButton(R.string.cancel, null);
        dl.show();
    }

    public static String getVersionName() {
        try {
            return MainActivity.main.getPackageManager().
                    getPackageInfo(MainActivity.main.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "App Version";
        }
    }

    public static int getVersionCode() {
        try {
            return MainActivity.main.getPackageManager().
                    getPackageInfo(MainActivity.main.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static void setMargin(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    public static boolean isServerContainWebsite(File websiteDir) {
        if (Vers.easyWebServerWebsitesMap != null) {
            for (int now : Vers.easyWebServerWebsitesMap.keySet()) {
                Object[] obj = Vers.easyWebServerWebsitesMap.get(now);
                if (((File) obj[1]).getPath().equals(websiteDir.getPath())) {
                    return true;
                }
            }
        }

        if (Vers.phpServerWebsitesMap != null) {
            for (int now : Vers.phpServerWebsitesMap.keySet()) {
                Object[] obj = Vers.phpServerWebsitesMap.get(now);
                if (((File) obj[0]).getPath().equals(websiteDir.getPath())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isServersHasPort(int port) {
        if (Vers.easyWebServerWebsitesMap != null && Vers.easyWebServerWebsitesMap.containsKey(port)) {
            return true;
        }
        if (Vers.phpServerWebsitesMap != null && Vers.phpServerWebsitesMap.containsKey(port)) {
            return true;
        }
        return false;
    }

    public static boolean isProcessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                //Log.i("Service2进程", ""+info.processName);
                isRunning = true;
            }
        }

        return isRunning;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static boolean isScreenHor(Context ctx) {
        WindowManager mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        int width = mWindowManager.getDefaultDisplay().getWidth();
        int height = mWindowManager.getDefaultDisplay().getHeight();
        return width > height;
    }

    public static boolean isLightColor(int color) {
        if (0.213 * Color.red(color) +
                0.715 * Color.green(color) +
                0.072 * Color.blue(color) >
                255 / 2) {
            //浅色
            return true;
        } else {
            //深色
            return false;
        }
    }

    public static boolean isLightColor(String color) {
        return isLightColor(Color.parseColor(color));
    }

    public static int getColor(Context ctx, int res) {
        return ContextCompat.getColor(ctx, res);
    }

    public static void setAlphaAnimation(View v, boolean direction, int time, final Runnable endRun) {
        //direction==true : 渐出
        //简单渐变动画

        AlphaAnimation alphaAnimation = new AlphaAnimation(direction ? 1 : 0, direction ? 0 : 1);//渐变度从0到1

        alphaAnimation.setDuration(time);//动画持续时间：2000毫秒

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation p1) {
                // TODO: Implement this method
            }

            @Override
            public void onAnimationEnd(Animation p1) {
                endRun.run();
            }

            @Override
            public void onAnimationRepeat(Animation p1) {
                // TODO: Implement this method
            }
        });

        v.startAnimation(alphaAnimation);
    }

    public static void showPermissions(Activity activity) {
        try {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.REQUEST_INSTALL_PACKAGES,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NOTIFICATION_POLICY
                    }, 0);
        } catch (Exception t) {

        }
    }

    public static String appendHeader(String text, String header) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(text.getBytes());
        InputStream inputStream = byteArrayInputStream;
        BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
        String line = bfr.readLine();
        int lines = 0;
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            if (lines > 0)
                sb.append("\n");
            sb.append(header + line);
            lines++;
            line = bfr.readLine();
        }
        bfr.close();

        return sb.toString();
    }

    /**
     * @param htmlElement 目标元素
     * @return 层级数，body/head 返回 1
     */
    public static int getElementLevel(Element htmlElement) {
        Element nowElement = htmlElement;
        for (int level = 1; ; level++) {
            if (nowElement.tagName().equals("body") || nowElement.tagName().equals("head"))
                return level;
            else {
                nowElement = nowElement.parent();
            }
        }
    }

    public static String getMultipleText(int multiple, String text) {
        String result = "";
        for (int i = 0; i < multiple; i++) {
            result += text;
        }
        return result;
    }

    public static Server createServer(int port, boolean form, String websiteDirectory, boolean isHttps) {
        WebSite website;
        if (form) {
            website = new StorageWebsite(websiteDirectory);
        } else {
            website = new FileBrowser(websiteDirectory);
        }

// 创建服务器。
        final Server.Builder mServer = AndServer.serverBuilder()
                .port(port)
                .website(website)
                .listener(Do.getSsl(port));
        Server Ser;
        if (Vers.easyWebServerRegsMap != null && Vers.easyWebServerRegsMap.containsKey(port)) {
            Ser = Do.getRegServer(mServer, port, Do.getSsl(port));
        } else {
            Ser = mServer.build();
        }

        return Ser;
    }

    // Java object to JSON String
    public static String getJsonFromObject(Object obj) {
        return new GsonBuilder().create().toJson(obj);
    }

    // Json String to Java object
    public static <T> T getObjectFromJson(String source, Class<T> bean) {
        return new Gson().fromJson(source, bean);
    }

    public static <T> T getObjectFromJson(String source, Type type) {
        return new Gson().fromJson(source, type);
    }

    public static boolean checkURL(String url) {
        boolean value = false;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * @param version1
     * @param version2
     * @return 第一个参数大返回正数，第二个参数大返回负数，一样返回0
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        //获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        //循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            //如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
    public static void setPaddings(View view,int valuePx)
    {
        view.setPadding(valuePx,valuePx,valuePx,valuePx);
    }
    public static void showGroup(BaseActivity activity)
    {
        LinearLayout Ly=new LinearLayout(activity);
        ScrollView Sv=new ScrollView(activity);
        Sv.setFillViewport(true);
        RelativeLayout Rl=new RelativeLayout(activity);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
        Ly=new LinearLayout(activity);
        Ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        Ly.setOrientation(LinearLayout.VERTICAL);
        Ly.setPadding(30,30,30,30);
        Sv.addView(Rl);
        Rl.addView(Ly);

        FeedButton userFeedbackBtn=new FeedButton(activity,R.string.users_feedback,R.string.users_feedback_description,activity.getDrawable(R.drawable.icon_color));
        FeedButton userFeedback2Btn=new FeedButton(activity,R.string.users_feedback_2,R.string.users_feedback_description,activity.getDrawable(R.drawable.easyweb_2));
        FeedButton webClubBtn=new FeedButton(activity,R.string.web_club,R.string.web_club_description,activity.getDrawable(R.drawable.web_club));
        TextView textView=new TextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Do.getColor(activity,R.color.opposition));
        textView.setText(R.string.long_press_copy_groupid);

        Ly.addView(userFeedbackBtn);
        Ly.addView(userFeedback2Btn);
        Ly.addView(webClubBtn);
        Ly.addView(textView);

        Dl dl=new Dl(activity);
        dl.builder
                .setTitle(R.string.join_chat)
                .setMessage(R.string.add_groups_description)
                .setView(Sv);
        AlertDialog ad = dl.show();

        userFeedbackBtn.setOnClickListener((p1) -> {
            ad.dismiss();
            Do.joinQQGroup(activity,"igAxAeGxMnD3udAlpVOA9bzeTb2m3Fwq");
        });
        userFeedbackBtn.setOnLongClickListener((p1) -> {
            Do.copyText(activity,"589618066");
            activity.toast(R.string.done);
            return true;
        });

        userFeedback2Btn.setOnClickListener((p1) -> {
            ad.dismiss();
            Do.joinQQGroup(activity,"6W_3rBTTvxbM105tn2UOU4dqDSg27cel");
        });
        userFeedback2Btn.setOnLongClickListener((p1) -> {
            Do.copyText(activity,"853826466");
            activity.toast(R.string.done);
            return true;
        });

        webClubBtn.setOnClickListener((p1) -> {
            ad.dismiss();
            Do.joinQQGroup(activity,"iV5SZxa_ix86ykBlSZ0l8z2f4drz0C7y");
        });
        webClubBtn.setOnLongClickListener((p1) -> {
            Do.copyText(activity,"959871852");
            activity.toast(R.string.done);
            return true;
        });
    }
    public static Bitmap getLoacalBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file.getPath());
            return BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
