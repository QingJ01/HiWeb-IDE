package com.venter.jsappactions;

import android.content.Context;
import android.graphics.Color;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils
{
    public static class delete
    {
        /**
         * 删除文件，可以是文件或文件夹
         *
         * @param fileName
         *            要删除的文件名
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
         * @param fileName
         *            要删除的文件的文件名
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
         * @param dir
         *            要删除的目录的文件路径
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
    }

    public static int copyDir(String fromFile, String toFile)
    {

        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists())
        {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists())
        {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i= 0;i < currentFiles.length;i++)
        {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copyDir(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            }
            else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }
    public static int CopySdcardFile(String fromFile, String toFile)
    {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        }
        catch (Exception ex)
        {
            return -1;
        }
    }

    public static void copy(String fromFile, String toFile) throws Exception
    {
        if(new java.io.File(fromFile).isFile())
        {
            if(CopySdcardFile(fromFile,toFile)==-1)
                throw new IOException("EasyWeb can't copy the file.");
        }
        else
        {
            if(copyDir(fromFile,toFile)==-1)
                throw new IOException("EasyWeb can't copy the dir.");
        }
    }

    public static String read(File file) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(file));
        String line = bfr.readLine();
        int lines=0;
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
        FileOutputStream fos=new FileOutputStream(file);
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        bos.write(text.getBytes(), 0, text.getBytes().length);
        bos.flush();
        bos.close();
    }
    public static void webViewEvalJS(String js)
    {
        Vers.webView.post(() -> {
            Vers.webView.evaluateJavascript(js,null);
        });
    }
    public static void runOnActivity(Runnable runnable)
    {
        Vers.activity.runOnUiThread(runnable);
    }
    public static int dp2px(Context ctx, float dpValue) {
        float scale=ctx.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static String convertToRGB(int color,boolean isWell) {
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        if(isWell)
            return "#" + red.toUpperCase() + green.toUpperCase() + blue.toUpperCase();
        else
            return red.toUpperCase() + green.toUpperCase() + blue.toUpperCase();
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

}