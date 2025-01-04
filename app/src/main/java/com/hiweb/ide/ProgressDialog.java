package com.hiweb.ide;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.AlertDialog;

public class ProgressDialog extends AlertDialog.Builder
{
    public static AlertDialog Ad=null;
    public boolean IsShowHide=false;
    private static boolean isDismissed=true;
    public ProgressDialog(Context context)
    {
        super(context);
        init();
    }

    public ProgressDialog(Context context, int themeResId)
    {
        super(context,themeResId);
        init();
    }

    public void showHide()
    {
        Ad.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
        this.IsShowHide=true;
    }
    public void hideHide()
    {
        Ad.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        this.IsShowHide=false;
    }
    private void init() 
    {
        LinearLayout Ly=new LinearLayout(getContext());
        Ly.setGravity(Gravity.CENTER);
        ProgressBar Pb=new ProgressBar(getContext(),null,android.R.attr.progressBarStyle);
        TextView Tv=new TextView(getContext());
        Tv.setText(R.string.wait);
        
        Tv.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
        Tv.setGravity(Gravity.CENTER);
        Ly.addView(Pb);
        Ly.addView(Tv);
        Ly.setPadding(60,60,60,60);
        setTitle(null);
        setPositiveButton(R.string.hide, null);
        setView(Ly);
    }
    public void Show(boolean IsShowHide)
    {
        if(!isDismissed)
            return;
		Ad=show();
        int resId=R.drawable.dialog_round;
        Ad.getWindow().setBackgroundDrawableResource(resId);
//        if(Ad==null)
//            Ad=show();
//        else
//            Ad.show();
        if(IsShowHide)
        {
            showHide();
        }
        else
        {
            hideHide();
        }
        isDismissed=false;
    }
    public static void dismiss()
    {
        Ad.dismiss();
        isDismissed=true;
    }
}

