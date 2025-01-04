package com.hiweb.ide.add;

import android.content.*;
import com.hiweb.ide.*;
import com.flask.colorpicker.builder.*;
import com.flask.colorpicker.*;

import android.graphics.*;
import android.widget.EditText;

public class ChooseColor {
	private String colorString;
	private String ToColor = "";
	String HEXText;
	boolean IsWell = true;

	public ChooseColor noWell() {
		IsWell = false;
		return this;
	}

	public void choose(final Context c, String toColor, final EditText dialogEdit) {
		colorString = toColor;
		HEXText = toColor;
		try {
			Color.parseColor(toColor);
			ToColor = toColor;
		} catch (Exception e) {
			ToColor = Vers.i.defaultChooseColor;
		}
		ColorPickerDialogBuilder
				.with(c)
				.setTitle(com.hiweb.ide.R.string.main_menu_add_choose_color_title)
				.initialColor(Color.parseColor(ToColor))
				.wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
				.setCancelable(false)
				.density(12)
				.lightnessSliderOnly()
				.setOnColorSelectedListener(new OnColorSelectedListener() {
					@Override
					public void onColorSelected(int selectedColor) {
						HEXText = convertToRGB(selectedColor, IsWell);
					}
				})
				.setPositiveButton(com.hiweb.ide.R.string.ok, new ColorPickerClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
						HEXText = convertToRGB(selectedColor, IsWell);
						colorString = HEXText;
						dialogEdit.setText(colorString);
					}
				})
				.setNegativeButton(com.hiweb.ide.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.build()
				.show();
	}

	/**
	 * int转#ARGB
	 * 
	 * @param color
	 * @return String #ARGB
	 */
	public static String convertToRGB(int color, boolean isWell) {
		String red = Integer.toHexString(Color.red(color));
		String green = Integer.toHexString(Color.green(color));
		String blue = Integer.toHexString(Color.blue(color));
		if (red.length() == 1) {
			red = "0" + red;
		}

		if (green.length() == 1) {
			green = "0" + green;
		}

		if (blue.length() == 1) {
			blue = "0" + blue;
		}

		if (isWell)
			return "#" + red.toUpperCase() + green.toUpperCase() + blue.toUpperCase();
		else
			return red.toUpperCase() + green.toUpperCase() + blue.toUpperCase();
	}

}
