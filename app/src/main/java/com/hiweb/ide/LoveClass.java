package com.hiweb.ide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import com.hiweb.ide.edit.Do;

public class LoveClass 
{
    Context C;

    //  HTTPS://QR.ALIPAY.COM/FKX03133TLJFCY8UNXHC56
    private String payCode = "fkx00296gu5d2iqf6ta5h87";  //支付宝支付扫描码，只截取后面的
    public void GiveLove(final Context context)
    {
        C=context;
        
        Dl Adb=new Dl(context);
        Adb.builder.setTitle(R.string.main_love_title);
        Adb.builder.setMessage(R.string.main_love_message);
        Adb.builder.setPositiveButton(R.string.micromsg_pay, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    try 
                    {
                        donateWeixin();       
                        Toast.makeText(context,R.string.weixin_toast,Toast.LENGTH_SHORT).show();
                    } 
                    catch (Exception e) 
                    {
                        Do.showErrDialog(context,e);
                    } 
                }
            });
        Adb.builder.setNegativeButton(R.string.alipay, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    donateAlipay(payCode);
                }
            });
        Adb.builder.setNeutralButton(R.string.star_love, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    ping();
                }
            });
        Adb.show();
    }
    private void ping()
    {
        Uri  uri = Uri.parse("https://www.coolapk.com/apk/com.hiweb.ide");
        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
        C.startActivity(intent);
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     *          wxp://f2f0j1REHFC8YJor7UUsS6N-1PZiFE2mhOht
     */
    private void donateWeixin() throws IOException {
        InputStream weixinQrIs = C.getAssets().open("weixin_pay.png");
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/weixin_pay.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(MainActivity.main, qrPath);
    }




    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 HTTPS://QR.ALIPAY.COM/FKX03133TLJFCY8UNXHC56 ，则
     *                payCode = FKX03133TLJFCY8UNXHC56
     *                注：不区分大小写
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(C);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(MainActivity.main, payCode);
        }
    }
    
}
