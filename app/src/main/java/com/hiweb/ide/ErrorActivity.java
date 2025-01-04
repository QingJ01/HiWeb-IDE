package com.hiweb.ide;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hiweb.ide.databinding.ErrorBinding;

import java.util.Locale;

public class ErrorActivity extends Activity {
    private ErrorBinding binding;
    int statusBarHeight;
    String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Vers.i == null) {
            Vers.i = new Vers();
        }

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            // 获得状态栏高度
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = ErrorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lyErrorFront.setPadding(dp2px(this, 10), statusBarHeight, dp2px(this, 10), dp2px(this, 10));

        final Intent intent = getIntent();

        errorMsg = intent.getStringExtra("ErrorMsg");
        try {
            String mtyb = android.os.Build.BRAND;
            String mtype = android.os.Build.MODEL; // 手机型号
            WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            String width = mWindowManager.getDefaultDisplay().getWidth() + "";
            String height = mWindowManager.getDefaultDisplay().getHeight() + "";
            String OSVersion = android.os.Build.VERSION.RELEASE;
            String OSVersionSDK = android.os.Build.VERSION.SDK;
            errorMsg = ("Brand:" + mtyb + "\nModel:" + mtype + "\nOS Version:" + OSVersion + "|" + OSVersionSDK
                    + "\nApplication Version:" + (getPackageManager().getPackageInfo(getPackageName(), 0).versionName)
                    + "|" + (getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) + "\nWidth:" + width
                    + "\nHeight:" + height + "\n\n") + errorMsg;
        } catch (Exception e) {

        }

        binding.tvErrMsg.setText(errorMsg);
        CanSelect(binding.tvErrMsg);
        binding.btnErrClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                ClipboardManager cmb = (ClipboardManager) ErrorActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(errorMsg);

                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(getThemeContext(newBase));
    }

    private void CanSelect(TextView Tv) {
        Tv.setEnabled(true);
        Tv.setTextIsSelectable(true);
        Tv.setFocusable(true);
        Tv.setLongClickable(true);
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private Context getThemeContext(Context a) {
        String locale = "en";
        if (!Vers.buildPassword.equals("")) {
            locale = "de";
        }

        Locale l = new Locale(locale);

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
}
