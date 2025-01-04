/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */

package com.myopicmobile.textwarrior.common;


public class ColorSchemeDark extends ColorScheme {

	public ColorSchemeDark(){
		setColor(Colorable.FOREGROUND, 0xFFFFFFFF);
		setColor(Colorable.BACKGROUND, 0xFF212121);
		setColor(Colorable.SELECTION_FOREGROUND, 0xFFE0E0E0);
		setColor(Colorable.SELECTION_BACKGROUND, 0xFF757575);
		setColor(Colorable.LINE_HIGHLIGHT, 0x15FFFFFF);
		setColor(Colorable.NON_PRINTING_GLYPH, 0xFF89B0E1);
		setColor(Colorable.COMMENT, 0xFFBDBDBD); //注释
		setColor(Colorable.KEYWORD, 0xFFF57C00); //关键字
		setColor(Colorable.NUMBER, 0xFF8BC34A); // 数字
		setColor(Colorable.STRING, 0xFFE91E63); //字符串
		setColor(Colorable.SECONDARY, 0xFFFFA03F);//宏定义
	}

	private static final int BEIGE = 0xFFD7BA7D;
	private static final int DARK_GREY = 0xFF606060;
	private static final int FLUORESCENT_YELLOW = 0xFFEFF193;
	private static final int JUNGLE_GREEN = 0xFF608B4E;
	private static final int LIGHT_GREY = 0xFFD3D3D3;
	private static final int MARINE = 0xFF569CD6;
	private static final int OCEAN_BLUE = 0xFF256395;
	private static final int OFF_BLACK = 0xFF040404;
	private static final int OFF_WHITE = 0xFFD0D2D3;
	private static final int PEACH = 0xFFD69D85;

	@Override
	public boolean isDark() {
		return true;
	}
}
