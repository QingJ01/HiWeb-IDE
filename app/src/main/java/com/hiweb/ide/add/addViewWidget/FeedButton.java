package com.hiweb.ide.add.addViewWidget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Xml;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiweb.ide.R;
import com.hiweb.ide.Vers;
import com.hiweb.ide.edit.Do;

import org.xmlpull.v1.XmlPullParser;

public class FeedButton extends LinearLayout {

    public Drawable icon = null;
    public int name = -1;
    public int description = -1;
    public ImageView iv;
    public TextView tvTitle;
    public TextView tvDescription;

    public FeedButton(Context c, int name, int description, Drawable icon) {
        super(c);

        this.icon = icon;
        this.name = name;
        this.description = description;

        setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setPadding(Do.dp2px(getContext(), 5), Do.dp2px(getContext(), 5), Do.dp2px(getContext(), 5),
                Do.dp2px(getContext(), 5));
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER | Gravity.LEFT);
        setBackground(getRippleDrawable(-1));
        setClickable(true);
        Do.setMargin(this, Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10),
                Do.dp2px(getContext(), 10));

        iv = new ImageView(c);
        iv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(getContext(), 30), Do.dp2px(getContext(), 30)));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageDrawable(icon);
        Do.setMargin(iv, Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 10),
                Do.dp2px(getContext(), 10));
        addView(iv);

        LinearLayout ly = new LinearLayout(c);
        ly.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1));
        ly.setGravity(Gravity.LEFT | Gravity.CENTER);
        ly.setOrientation(LinearLayout.VERTICAL);
        Do.setPaddings(ly, Do.dp2px(c, 10));
        addView(ly);

        tvTitle = new TextView(c);
        if (name != -1)
            tvTitle.setText(name);
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(Do.getColor(c, R.color.opposition));
        ly.addView(tvTitle);

        tvDescription = new TextView(c);
        if (description != -1) {
            tvDescription.setText(description);
            tvDescription.setTextSize(13);
            tvDescription.setTextColor(Color.GRAY);
            ly.addView(tvDescription);
        }
    }

    public void diminish() {
        setPadding(Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 3), Do.dp2px(getContext(), 10),
                Do.dp2px(getContext(), 3));
        iv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(getContext(), 20), Do.dp2px(getContext(), 20)));
        tvTitle.setTextSize(12);
        tvDescription.setTextSize(9);
    }

    private RippleDrawable getRippleDrawable(int bgColor) {

        if (bgColor == -1)
            return (RippleDrawable) getContext().getDrawable(R.drawable.ripple_round_rectangular);
        float r = Do.dp2px(getContext(), 20);
        float[] outRadius = new float[] { r, r, r, r, r, r, r, r };
        RoundRectShape roundRectShape = new RoundRectShape(outRadius, null, null);

        ShapeDrawable contentDrawable = new ShapeDrawable();

        contentDrawable.setShape(roundRectShape);
        contentDrawable.getPaint().setColor(bgColor);
        contentDrawable.getPaint().setAlpha(120);
        contentDrawable.getPaint().setStyle(Paint.Style.FILL);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(r);
        gradientDrawable.setStroke(Do.dp2px(getContext(), 2), bgColor);

        RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(bgColor), gradientDrawable,
                contentDrawable);

        return rippleDrawable;
    }

    public void setColor(int color) {
        setBackground(getRippleDrawable(color));
        icon.setTint(color);
        tvTitle.setTextColor(color);
        if (description != -1)
            tvDescription.setTextColor(color);
    }
}
