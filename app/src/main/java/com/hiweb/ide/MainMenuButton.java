package com.hiweb.ide;

import android.content.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import android.widget.TableRow.*;
import android.view.View.*;
import android.graphics.*;
import android.graphics.drawable.*;

import androidx.core.content.ContextCompat;

import com.hiweb.ide.edit.*;

public class MainMenuButton extends LinearLayout {
	ImageView leftIconIv;
	TextView rightTv;
	Context c;

	public MainMenuButton(Context context) {
		super(context);
		c = context;
		init();
	}

	public MainMenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
		init();
	}

	public MainMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		c = context;
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER);
		setPadding(Do.dp2px(getContext(), 20), Do.dp2px(getContext(), 10), Do.dp2px(getContext(), 20),
				Do.dp2px(getContext(), 10));
		leftIconIv = new ImageView(c);
		rightTv = new TextView(c);
		leftIconIv.setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(c, 20), Do.dp2px(c, 20)));
		rightTv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		rightTv.setTextColor(ContextCompat.getColor(getContext(), R.color.opposition));
		rightTv.setTextSize(10);

		setMargin(leftIconIv, 0, 0, Do.dp2px(getContext(), 20), 0);

		addView(leftIconIv);
		addView(rightTv);
	}

	public void setText(String t) {
		rightTv.setText(t);
	}

	public void setIcon(Drawable d) {
		leftIconIv.setBackgroundDrawable(d);
	}

	private void setMargin(View v, int left, int top, int right, int bottom) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins(left, top, right, bottom);
			requestLayout();
		}
	}
}
