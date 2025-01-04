package com.venter.easyweb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.venter.easyweb.edit.Do;
import id.zelory.compressor.Compressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ImagePlayer 
{
    public void play(final File F) throws  UnsupportedEncodingException
    {
        ScrollView Sv=new ScrollView(MainActivity.main);
        Sv.setFillViewport(true);
        RelativeLayout Rl=new RelativeLayout(MainActivity.main);
        Rl.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
        LinearLayout ly=new LinearLayout(MainActivity.main);
        ly.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
        ly.setOrientation(LinearLayout.VERTICAL);
        ly.setPadding(Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10),Do.dp2px(MainActivity.main,10));
        ly.setGravity(Gravity.CENTER);
        TextView sizeTv=new TextView(MainActivity.main);
        sizeTv.setPadding(0,Do.dp2px(MainActivity.main,10),0,0);
        sizeTv.setText(F.length()+" Byte");
        sizeTv.setGravity(Gravity.CENTER);
        Dl picBuilder=new Dl(MainActivity.main);
        picBuilder.builder.setTitle(F.getName());
        Bitmap bitmap=BitmapFactory.decodeFile(F.getPath());
        
        if(F.getName().toLowerCase().endsWith(".svg"))
        {
            ImageWebView Iwv=new ImageWebView(MainActivity.main);
            Iwv.setImage(F);
            ly.addView(Iwv);
        }
        else if(F.getName().toLowerCase().endsWith(".png")||F.getName().toLowerCase().endsWith(".jpg")||F.getName().toLowerCase().endsWith(".jpeg")||F.getName().toLowerCase().endsWith(".webp"))
        {
            ImageView picIv=new ImageView(MainActivity.main);
            picIv.setImageBitmap(bitmap);
            ly.addView(picIv);
            picBuilder.builder.setNeutralButton(R.string.zip_pic, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface p1, int p2)
                    {
                        Dl zipDl = new Dl(MainActivity.main);

                        LinearLayout ly = new LinearLayout(MainActivity.main);
                        ly.setOrientation(LinearLayout.VERTICAL);
                        ly.setGravity(Gravity.CENTER);

                        SeekBar seekBar=new SeekBar(MainActivity.main);
                        seekBar.setMax(100);
                        seekBar.setProgress(75);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            seekBar.setMin(1);
                        }
                        ly.addView(seekBar);

                        EditText editText = new EditText(MainActivity.main);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        editText.setText("75");
                        editText.setGravity(Gravity.CENTER);
                        editText.setEms(3);
                        Do.onlyCanEdit(editText,"1234567890",3);
                        ly.addView(editText);

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                editText.setText(String.valueOf(i));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                int progress = Integer.parseInt(editable.toString());
                                if(progress>0)
                                    seekBar.setProgress(progress);
                            }
                        });

                        zipDl.builder
                                .setTitle(R.string.zip_rate)
                                .setView(ly)
                                .setPositiveButton(R.string.zip, (dialogInterface, i) -> {
                                    try
                                    {
                                        Bitmap.CompressFormat format = null;
                                        if(F.getName().toLowerCase().endsWith(".png"))
                                            format=Bitmap.CompressFormat.PNG;
                                        else if(F.getName().toLowerCase().endsWith(".jpg")||F.getName().toLowerCase().endsWith(".jpeg"))
                                            format=Bitmap.CompressFormat.JPEG;
                                        else if(F.getName().toLowerCase().endsWith(".webp"))
                                            format=Bitmap.CompressFormat.WEBP;
                                        File compressedImageFile =  new Compressor(MainActivity.main)
                                                .setMaxWidth(5000)
                                                .setMaxHeight(5000)
                                                .setQuality(Integer.parseInt(editText.getText().toString()))
                                                .setCompressFormat(format)
                                                .compressToFile(F);

                                        if(CopySdcardFile(compressedImageFile.getPath(),F.getPath())!=0)
                                        {
                                            throw new Exception();
                                        }
                                        try
                                        {
                                            compressedImageFile.delete();
                                        }
                                        catch(Exception e){}
                                        MainActivity.main.toast(R.string.done);
                                    }
                                    catch(Exception e)
                                    {
                                        Do.showErrDialog(MainActivity.main,e);
                                    }
                                });
                        zipDl.show();

                    }
                });
        }
		else
        {
            ImageView picIv=new ImageView(MainActivity.main);
            picIv.setImageBitmap(bitmap);
            ly.addView(picIv);
        }
        ly.addView(sizeTv);
        Rl.addView(ly);
        Sv.addView(Rl);
        picBuilder.builder.setView(Sv);
        picBuilder.show();
    }
    public int copyDir(String fromFile, String toFile)
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
    public int CopySdcardFile(String fromFile, String toFile)
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
    
    public class ImageWebView extends WebView
    {
        public ImageWebView(Context c)
        {
            super(c);
            
            WebSettings webSettings=getSettings();
            webSettings.setJavaScriptEnabled(true);//允许使用js

            /**
             * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
             * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
             * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
             * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
             */
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

            //支持屏幕缩放
            webSettings.setSupportZoom(false);
            webSettings.setBuiltInZoomControls(false);

            webSettings.setUseWideViewPort(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setLoadWithOverviewMode(true);
            //不显示webview缩放按钮
            webSettings.setDisplayZoomControls(false);
            
            setScrollContainer(false);
            setVerticalScrollBarEnabled(false);
            setHorizontalScrollBarEnabled(false);
        }
        public void setImage(File F) throws UnsupportedEncodingException
        {
            String SCode="<div style=\"text-align:center;\" ><img src=\""+F.getName()+"\"></div>";
            String SUrl="file://"+Do.URLEncode(F.getParent()+"/");
            loadDataWithBaseURL(SUrl,SCode,"text/html","utf-8",null);
        }

        @Override
        public void scrollTo(int x, int y) {
            // TODO: Implement this method
            super.scrollTo(0,0);
        }
        
    }
}
