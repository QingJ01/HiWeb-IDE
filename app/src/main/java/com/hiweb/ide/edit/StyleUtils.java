package com.venter.easyweb.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class StyleUtils
{
	public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) 
	{
		if (isMIUI())
		{
			MIUISetStatusBarLightMode(activity, dark);
		}
		else if (isFlyme())
		{
			setFlymeLightStatusBar(activity, dark);
		}
		else
		{
			View decor = activity.getWindow().getDecorView();
			if (dark)
			{
				decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			}
			else
			{
				decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			}
		}
	}
	public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark)
	{
		boolean result = false;
		Window window = activity.getWindow();
		if (window != null)
		{
			Class clazz = window.getClass();
			try
			{
				int darkModeFlag = 0;
				Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
				Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);
				Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
				if (dark)
				{
					extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
				}
				else
				{
					extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
				}
				result = true;

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Integer.parseInt((getSystemProperty("ro.miui.ui.version.name").substring(1))) >= 7)
				{
					//开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
					if (dark)
					{
						activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
					}
					else
					{
						activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
					}
				}
			}
			catch (Exception e)
			{

			}
		}
		return result;
	}
	public static String getSystemProperty(String propName)
	{
		String line;
		BufferedReader input = null;
		try
		{
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propName);
			input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
			line = input.readLine();
			input.close();
		}
		catch (IOException ex)
		{
        	return null;
		}
		finally
		{
            if (input != null)
            {
				try
				{
					input.close();
				}
				catch (IOException e)
				{
				}
            }
		}
		return line;
	}
	private static boolean setFlymeLightStatusBar(Activity activity, boolean dark)
	{
		boolean result = false;
		if (activity != null)
		{
			try
			{
				WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark)
				{
					value |= bit;
				}
				else
				{
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				activity.getWindow().setAttributes(lp);
				result = true;
			}
			catch (Exception e)
			{
			}
		}
		return result;
	}
	public static void intent(Activity a, Class<?> c)
	{
		a.startActivity(new Intent(a, c));
	}

	private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isFlyme()
	{
        try
		{
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        }
		catch (final Exception e)
		{
            return false;
        }
    }

    public static boolean isEMUI()
	{
        return isPropertiesExist(KEY_EMUI_VERSION_CODE);
    }

    public static boolean isMIUI()
	{
        return isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME, KEY_MIUI_INTERNAL_STORAGE);
    }

    private static boolean isPropertiesExist(String... keys)
	{
        if (keys == null || keys.length == 0)
		{
            return false;
        }
        try
		{
            BuildProperties properties = BuildProperties.newInstance();
            for (String key : keys)
			{
                String value = properties.getProperty(key);
                if (value != null)
                    return true;
            }
            return false;
        }
		catch (IOException e)
		{
            return false;
        }
    }
	private static final class BuildProperties
	{

        private final Properties properties;

        private BuildProperties() throws IOException
		{
            properties = new Properties();
            // 读取系统配置信息build.prop类
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key)
		{
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value)
		{
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet()
		{
            return properties.entrySet();
        }

        public String getProperty(final String name)
		{
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue)
		{
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty()
		{
            return properties.isEmpty();
        }

        public Enumeration<Object> keys()
		{
            return properties.keys();
        }

        public Set<Object> keySet()
		{
            return properties.keySet();
        }
        public int size()
		{
            return properties.size();
        }
        public Collection<Object> values()
		{
            return properties.values();
        }
        public static BuildProperties newInstance() throws IOException
		{
            return new BuildProperties();
        }
    }
}

