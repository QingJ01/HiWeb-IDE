package com.hiweb.ide.add.addViewWidget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hiweb.ide.edit.Do;
import com.hiweb.ide.R;

public class ButtonLayout extends LinearLayout {
	public ImageView leftImg;
	public TextView titleTv;
	public TextView descriptionTv;
	LinearLayout textLy;

	public ButtonLayout(Context ctx) {
		super(ctx);
		leftImg = new ImageView(ctx);
		titleTv = new TextView(ctx);
		descriptionTv = new TextView(ctx);
		textLy = new LinearLayout(ctx);

		setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		setClickable(true);
		setFocusable(true);
		setBackground(ctx.getResources().getDrawable(R.drawable.shape_item));
		setPadding(Do.dp2px(ctx, 10), Do.dp2px(ctx, 10), Do.dp2px(ctx, 10), Do.dp2px(ctx, 10));
		setOrientation(LinearLayout.HORIZONTAL);
		Do.setMargin(this, Do.dp2px(ctx, 3), Do.dp2px(ctx, 3), Do.dp2px(ctx, 3), Do.dp2px(ctx, 3));

		leftImg.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(ctx, 25), Do.dp2px(ctx, 25)));
		Do.setMargin(leftImg, Do.dp2px(ctx, 5), Do.dp2px(ctx, 5), Do.dp2px(ctx, 5), Do.dp2px(ctx, 5));

		leftImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		titleTv.setTextColor(ContextCompat.getColor(getContext(), R.color.opposition));
		descriptionTv.setTextColor(Color.GRAY);

		titleTv.setTextSize(15);
		descriptionTv.setTextSize(12);

		textLy.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
		textLy.setOrientation(LinearLayout.VERTICAL);
		textLy.setGravity(Gravity.CENTER | Gravity.LEFT);
		textLy.addView(titleTv);
		textLy.addView(descriptionTv);

		addView(leftImg);
		addView(textLy);
	}
}
