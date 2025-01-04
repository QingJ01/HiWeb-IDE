package com.venter.easyweb.server;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import com.venter.easyweb.*;
import java.util.*;

public class ServerNotification {
    public Context ctx;
    public Notification notification;
    public boolean canShow = true;
    public NotificationManager mNotifyMgr;
    public PendingIntent contentIntent;
    public Thread thread = null;
    public boolean isStop = true;

    public ServerNotification(Context ctx,int type) {
        this.ctx = ctx;
        this.canShow = canShow;
        Intent i = new Intent(ctx, MainActivity.class);
        i.putExtra("showServerDialog", true);
        contentIntent = PendingIntent.getActivity(ctx, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyMgr =
                (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            mNotifyMgr.createNotificationChannels(Arrays.asList(
                    new NotificationChannel(
                            "easyweb_server",
                            ctx.getString(R.string.easyweb_server_on),
                            NotificationManager.IMPORTANCE_LOW),
                    new NotificationChannel(
                            "php_web_server",
                            ctx.getString(R.string.php_server_on),
                            NotificationManager.IMPORTANCE_LOW)
            ));
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            builder = new Notification.Builder(ctx, type==0 ? "easyweb_server" : "php_web_server");
        } else {
            builder = new Notification.Builder(ctx);
        }
        builder
                .setSmallIcon(R.drawable.server_network)
                .setLargeIcon(new BitmapFactory().decodeResource(ctx.getResources(), R.drawable.icon_color))
                .setContentTitle(type==0 ? ctx.getString(R.string.easyweb_server_on) : ctx.getString(R.string.php_server_on))
                .setContentText(ctx.getString(R.string.click_to_show_more))
                .setContentIntent(contentIntent)
                .setSound(null)
                .setWhen(System.currentTimeMillis());
        notification = builder.build();

        notification.flags = notification.FLAG_NO_CLEAR;
    }
    public void cancle(int type)
    {
        mNotifyMgr.cancel(type);
    }
}
