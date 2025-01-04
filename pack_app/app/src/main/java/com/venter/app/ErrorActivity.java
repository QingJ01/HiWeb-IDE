package com.venter.app;
import android.app.Activity;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Bundle;

public class ErrorActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String msg=getIntent().getStringExtra("ErrorMsg");
        ScrollView sv=new ScrollView(this);
        TextView tv=new TextView(this);
        tv.setText(msg);
        sv.setFillViewport(true);
        sv.addView(tv);
        setContentView(sv);
    }
}
