package com.hiweb.ide;

import java.util.HashMap;
import java.util.Map;

public class FileTypeClass {
    public static int TYPE_IMG = 0;
    public static int TYPE_JS = 1;
    public static int TYPE_DOWN = 2;
    public static int TYPE_VIDEO = 3;
    public static int TYPE_WEB = 4;

    public static Map FileTypeMap() {
        Map MFileType = new HashMap();

        MFileType.put("png", TYPE_IMG);
        MFileType.put("jpg", TYPE_IMG);
        MFileType.put("gif", TYPE_IMG);
        MFileType.put("bmp", TYPE_IMG);
        MFileType.put("svg", TYPE_IMG);

        MFileType.put("js", TYPE_JS);
        MFileType.put("vbs", TYPE_JS);
        MFileType.put("jss", TYPE_JS);

        MFileType.put("", TYPE_DOWN);

        MFileType.put("mp4", TYPE_VIDEO);
        MFileType.put("ogg", TYPE_VIDEO);
        MFileType.put("webm", TYPE_VIDEO);
        MFileType.put("mp3", TYPE_VIDEO);
        MFileType.put("wav", TYPE_VIDEO);

        MFileType.put("html", TYPE_WEB);
        MFileType.put("css", TYPE_WEB);
        MFileType.put("php", TYPE_WEB);
        MFileType.put("xml", TYPE_WEB);
        return MFileType;
    }
}
