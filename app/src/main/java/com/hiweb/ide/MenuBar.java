package com.hiweb.ide;

import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.view.Gravity;
import android.content.Context;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.hiweb.ide.edit.Do;
import android.widget.ImageView;

public class MenuBar extends LinearLayout {
	public Drawable menuBtnDrawable = null;

	public MenuBar(android.content.Context context) {
		super(context);
		init();
	}

	public MenuBar(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MenuBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MenuBar(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public LinearLayout lyBox;

	private void init() {
		setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		setOrientation(LinearLayout.VERTICAL);
		HorizontalScrollView hsv = new HorizontalScrollView(getContext());
		hsv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		hsv.setFillViewport(true);
		addView(hsv);
		lyBox = new LinearLayout(getContext());
		lyBox.setLayoutParams(new HorizontalScrollView.LayoutParams(-1, -2));
		lyBox.setOrientation(LinearLayout.HORIZONTAL);
		lyBox.setGravity(Gravity.CENTER | Gravity.LEFT);
		hsv.addView(lyBox);
	}

	public void load(Object[][] menu, int color) {
		lyBox.removeAllViews();

		int i = 0;
		for (Object[] nowMenu : menu) {
			IconButton iconBtn;
			int id = -1;
			if (nowMenu.length == 4)
				id = (int) nowMenu[3];
			else
				id = i * (0xabc) + (0xa);

			iconBtn = new IconButton(id, getContext(), (int) nowMenu[0], color, (int) nowMenu[1],
					(View.OnClickListener) nowMenu[2]);

			lyBox.addView(iconBtn);

			i++;
		}
		setVisibility(View.VISIBLE);
	}

	public void hide(int id) {
		try {
			IconButton iconButton = lyBox.findViewById(id);
			iconButton.setVisibility(View.GONE);
		} catch (Exception e) {

		}
	}

	public void visible(int id) {
		try {
			IconButton iconButton = lyBox.findViewById(id);
			iconButton.setVisibility(View.VISIBLE);
		} catch (Exception e) {

		}
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}

	public class IconButton extends ImageView {
		public IconButton(int id, final Context ctx, int drawable, int color, final int text,
				View.OnClickListener listen) {
			super(ctx);

			setLayoutParams(new LinearLayout.LayoutParams(Do.dp2px(ctx, 30), Do.dp2px(ctx, 30)));
			setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			setBackgroundDrawable(menuBtnDrawable == null ? ctx.getDrawable(R.drawable.shape_item) : menuBtnDrawable);

			Drawable draw = ctx.getDrawable(drawable);
			if (id != Menus.CONTINUE_HW)
				draw.setTint(color);
			setImageDrawable(draw);

			setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1) {
					Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
					return true;
				}
			});
			setOnClickListener(listen);

			Do.setMargin(this, Do.dp2px(ctx, 5), Do.dp2px(ctx, 5), Do.dp2px(ctx, 5), Do.dp2px(ctx, 5));

			setId(id);
		}
	}
}
