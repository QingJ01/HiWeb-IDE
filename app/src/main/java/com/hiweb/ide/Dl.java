package com.hiweb.ide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.hiweb.ide.edit.Do;

import java.util.Map;
import java.util.HashMap;

public class Dl {
    private Context c;
    public AlertDialog.Builder builder;

    public Dl(Context ctx) {
        c = ctx;
        builder = new AlertDialog.Builder(ctx);
    }

    public Dl(Context ctx, CharSequence title, CharSequence msg, CharSequence ok) {
        c = ctx;
        builder = new AlertDialog.Builder(ctx);

        builder
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(ok, null);

        show();
    }

    public Dl(Context ctx, int title, int msg, int ok) {
        c = ctx;
        builder = new AlertDialog.Builder(ctx);

        builder
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(ok, null);

        show();
    }

    public AlertDialog show() {
        AlertDialog alertDialog = builder.show();
        int resId = R.drawable.dialog_round;
        alertDialog.getWindow().setBackgroundDrawableResource(resId);
        return alertDialog;
    }

    public AlertDialog showNormal() {
        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }

    public AlertDialog createNormal() {
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public AlertDialog create() {
        AlertDialog alertDialog = builder.create();
        int resId = R.drawable.dialog_round;
        alertDialog.getWindow().setBackgroundDrawableResource(resId);
        return alertDialog;
    }

    public void setIcon(int resId) {
        Drawable drawable = c.getDrawable(resId);
        drawable.setTint(Do.getColor(c, R.color.display_color));
        builder.setIcon(drawable);
    }
}
