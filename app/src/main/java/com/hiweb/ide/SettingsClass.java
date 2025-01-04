package com.hiweb.ide;

import java.io.File;

public class SettingsClass {
	public static String SWelcomePath = null;
	public static String[] SaChars;
	public static String[] SaNames;
	/*
	 * -1:默认
	 * 0:经典
	 * 1:深邃
	 * 2:森色
	 * 3:淡雅
	 * 4:纯净
	 */
	public static int ITheme = -1;
	public static int IVirtualTheme = -1;// 虚拟主题变量用于使设置界面中的Spinner默认选项得到更改而不影响重启应用前的主题
	public static boolean BIsDarktheme = false;
	public static boolean BVirtualIsDarktheme = false;// 虚拟主题变量用于使设置界面中的Spinner默认选项得到更改而不影响重启应用前的主题
	public static int vIBackup = 60;
	public static boolean vBIsWordWarp = false;
	public static File vQsWebsite = null;
	public static boolean isAutoPreviewFull = false;
	public static boolean isAllowAbsolute = false;
	public static boolean isHideHW = false;
	public static boolean isShowWebViewAlert = true;
	public static File bgFile = null;
	public static String bgScale = null;
	public static float bgAlpha = 0.5f;
}
