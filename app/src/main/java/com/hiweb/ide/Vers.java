package com.hiweb.ide;

import android.graphics.drawable.*;
import android.os.*;
import android.view.*;

import androidx.core.content.ContextCompat;

import com.hiweb.ide.server.*;
import java.io.*;
import java.util.*;

public class Vers
{
	public static Vers i=null;

	public static final int ADD_TYPE_TEXT=0;

	public static final int ADD_TYPE_NUM=2;

	public static final int ADD_TYPE_STYLE=3;

	public static final int ADD_TYPE_CHOICE=4;

	public static final int ADD_TYPE_CHOICE_WITHOUT_DEFAULT=5;

	public static volatile  Map<Integer,Object[]> easyWebServerWebsitesMap =null;
	//↑Object[]:{BForm,FProjectDir,isHttps,ServerItem,Server}
	public static volatile  boolean isServerOn =false;//EasyWeb Server

	public static volatile  Map<Integer,Object[]> phpServerWebsitesMap =null;
	//↑Object[]:{FProjectDir,ServerItem}

	public static volatile  Map easyWebServerRegsMap =new HashMap();
	//↑KET:IPort;MNow:

	//KEY:SPath;↑Object[]:{FLink,IType,SLinkPath}
	//↑{IType}:
    /*
        0:JSS
        1:JS
        2:OTHER
    */

	public static String phpVersion="8.1.0";

	public static String buildPassword="";
	public File skipTpProjectFile=null;
	public File skipTpOpenFile=null;
	public boolean isInstalledHopWeb=false;
	public boolean hasRunned=false;
	public boolean isEW=false;
	public String userName=null;
    public final File userFile=new File(Environment.getExternalStorageDirectory().getPath()+"/"+"Venter","user_info.json");
    public File FVenter;
    public File FProjects;
	public Object[] vOaBackup=null;
	/*
		{
			String time,
			JsonObject vJaObject
		}
	*/
	public boolean isAutoStartPHPServer = false;
	
	public Menu MainMenu;
	
    public File FSetting;
    
	public File OpenFile;
    public int INowEpSID=-1;
	public boolean IsOpeningFile=false;
	public int FileType=-1;
	/*
	0:HTML/HTM
	1:JS
	2:CSS
    3:PHP
    4:XML
    5:JSS
    6:TEXT
	7:JSP/ASP/ASPX
	*/

	public boolean MainPreviewIsDone=true;
	
	public boolean RunIsViewDone=true;
	
	/*
		-1:未设置
		0:EasyWeb Server
		1:PHP Web Server
		2:Other
	*/
	public File ProjectDir;
	public String ExplorerPath="";
	public boolean IsOpenProject=false;
    
    public String ProjectName=null;
    public String ProjectAuthor=null;
    public File ProjectImg=null;
	public File ProjectJs=null;
    public File ProjectDownload=null;
    public File ProjectVideo=null;
    
    public String SProjectImg=null;
    public String SProjectJs=null;
    public String SProjectDownload=null;
    public String SProjectVideo=null;

	public String defaultChooseColor="#FFFFFF";
	public boolean IsChangeColor=false;
	
	public File nowHWPPath=null;
	
	public List<String[]> AlertList=new ArrayList();
    
    public Object[][] OaAddPub=new Object[][]
    {
        {R.string.add_ID,null,0,null,"id=\"$\""},
        {R.string.add_class,"Class",0,null,"class=\"$\""},
        {R.string.add_style,"Style",3,null,"style=\"$\""},
        {R.string.add_dir,"Dir",4,"ltr|rtl|auto","dir=\"$\""},
        {R.string.add_draggable,"Draggable",4,"true|false|auto","draggable=\"$\""},
        {R.string.add_dropzone,"Dropzone",4,"copy|move|link","dropzone=\"$\""},
        {R.string.add_accesskey,"Accesskey",0,R.string.key_code,"accesskey=\"$\""},
        {R.string.add_contenteditable,"Contenteditable",4,"true|false","contenteditable=\"$\""},
        {R.string.add_lang,"Language",0,R.string.lang_code,"lang=\"$\""},
        {R.string.add_title,"Title",0,null,"title=\"$\""}
    };
    public Object[][] OaAdd=new Object[][]
    {
        {R.string.add_em_div,"<div>",null,"<div $></div>"},
        {R.string.add_em_a,"<a>",new Object[][]
            {
                {R.string.add_down,"Download",0,null,"download=\"$\""},
                {R.string.add_href,"Href",0,null,"href=\"$\""},
                {R.string.add_target,"Target",4,"_blank|_self|_parent|_top|new","target=\"$\""},
                {R.string.add_a_type,"Type",0,R.string.add_mime,"type=\"$\""},
                {R.string.add_a_rel,"Rel",4,"alternate|stylesheet|start|next|prev|contents|index|glossary|copyright|chapter|section|subsection|appendix|help|bookmark|nofollow|licence|tag|friend","rel=\"$\""}
            },"<a $></a>"},
        
        {R.string.add_em_img,"<img>",new Object[][]
            {
                {R.string.add_img_alt,"Alt",0,null,"alt=\"$\""},
                {R.string.add_img_src,"Src",0,null,"src=\"$\""},
                {R.string.width,"Width",2,null,"width=\"$\""},
                {R.string.height,"Height",2,null,"height=\"$\""}
            },"<img $/>"},
        {R.string.add_em_style_text,"<h1>-<h6>,<del>,<em>...",new Object[][]
            {
                {R.string.add_text_style,"Format",5,"abbr|b|bdi|bdo|blockquote|cite|code|del|dfn|em|h1|h2|h3|h4|h5|h6|i|ins|kbd|mark|meter|nobr|pre|progress|q|rp|rt|ruby|s|samp|small|strike|strong|sub|sup|time|u|var|wbr","$"}
            },"<$>"},
        {R.string.add_em_hr,"<hr>",null,"<hr/>"},
        {R.string.add_em_br,"<br>",null,"<br/>"},
        {R.string.add_em_table,"<table>",null,null},
        //6↑
        {R.string.add_em_script,"<script>",new Object[][]
        {
                {R.string.add_script_async,"Async",4,"async","$"},
                {R.string.add_script_charset,"Charset",0,R.string.add_script_charset_ex,"charset=\"$\""},
                {R.string.add_script_defer,"Defer",4,"defer","$"},
                {R.string.add_script_src,"Src",0,null,"src=\"$\""},
                {R.string.add_script_type,"Type",0,R.string.add_mime,"type=\"$\""}
        },"<script $>\n    </script>"},
        {R.string.add_em_meta,"<meta>",new Object[][]
            {
                {R.string.add_meta_charset,"Charset",0,R.string.add_script_charset_ex,"charset=\"$\""},
                {R.string.add_meta_content,"Content",0,null,"content=\"$\""},
                {R.string.add_meta_http_equiv,"Http-Equiv",4,"content-type|default-style|refresh","http-equiv=\"$\""},
                {R.string.add_meta_name,"Name",4,"application-name|author|description|generator|keywords|theme-color|viewport","name=\"$\""}
            },"<meta $/>"},
        //8↑
        {R.string.add_link,"<link>",new Object[][]
            {
                {R.string.link_href,"Href",0,null,"href=\"$\""},
                {R.string.link_lang,"Hreflang",0,R.string.lang_code,"hreflang=\"$\""},
                {R.string.link_rel,"Rel",5,"alternate|author|help|icon|shortcut icon|licence|next|pingback|prefetch|prev|search|sidebar|stylesheet","rel=\"$\""},
                {R.string.link_type,"Type",0,R.string.add_mime,"type=\"$\""}
            },"<link $/>"},
        {R.string.add_css,"CSS",null,null},
        //10↑
		{R.string.add_audio,"<audio>",new Object[][]
			{
				{R.string.add_media_src,"Src",0,null,"src=\"$\""},
				{R.string.add_media_autoplay,"Autoplay",4,"autoplay","$"},
				{R.string.add_media_controls,"Controls",4,"controls","$"},
				{R.string.add_media_loop,"Loop",4,"loop","$"},
				{R.string.add_media_muted,"Muted",4,"muted","$"},
				{R.string.add_media_preload,"Preload",4,"auto|metadata|none","preload=\"$\""}
				
			},"<audio $></audio>"},
		{R.string.add_video,"<video>",new Object[][]
			{
				{R.string.add_media_src,"Src",0,null,"src=\"$\""},
				{R.string.add_media_autoplay,"Autoplay",4,"autoplay","$"},
				{R.string.add_media_controls,"Controls",4,"controls","$"},
				{R.string.add_media_loop,"Loop",4,"loop","$"},
				{R.string.add_media_muted,"Muted",4,"muted","$"},
				{R.string.add_media_preload,"Preload",4,"auto|metadata|none","preload=\"$\""},
				{R.string.add_video_poster,"Poster",0,null,"poster=\"$\""},
				{R.string.width,"Width",2,null,"width=\"$\""},
                {R.string.height,"Height",2,null,"height=\"$\""}

			
			},"<video $></video>"},
        };
    public Object[][] OaAddJS=new Object[][]
    {
        {R.string.add_js_loadurl,"window.location.href= string_url ;"},
        {R.string.add_js_back,"window.history.back();"},
        {R.string.add_js_forward,"window.history.forward();"},
        {R.string.add_js_find_em_id,"document.getElementById( string_id );"},
        {R.string.add_js_find_em_class,"document.getElementsByClassName( string_className );"},
		{R.string.add_js_create_em,"document.getElementById( string_id ).parentNode.appendChild(document.createElement( string_element ));"},
        {R.string.add_js_remove_em,"document.getElementById( string_id ).parentNode.removeChild(document.getElementById( string_id ));"},
        {R.string.add_js_alert,"alert( string_msg );"},
		{R.string.add_js_log,"console.log( string_log );"},
        {R.string.add_js_favorite,"window.external.addFavorite( string_url , string_name );"},
		{R.string.add_js_StrToInt,"parseInt( string , num_radix );"},
		{R.string.add_js_StrToFloat,"parseFloat( string , num_radix );"}
    };
    public boolean isNowProjectEnableServer=false;//配置文件中是否开启了服务器
    
    public Object[][] OaAddJSSFile=new Object[][]
    {
        {R.string.add_jss_file_new,"var file = File.newFile( string_path );"},
        {R.string.add_jss_file_create,"File.newFile( string_filePath ).createFile( boolean_canMakePathDirs );"},
        {R.string.add_jss_file_create_dir,"File.newFile( string_dirPath ).createDir( boolean_canMakePathDirs );"},
        {R.string.add_jss_file_del,"File.deleteFile( file_deleteFile );"},
        {R.string.add_jss_file_copy,"File.copy( file_sourceFile , file_targetFile );"},
        {R.string.add_jss_file_listFile,"var array = eval( file_dir .listFiles() );"},
        {R.string.add_jss_file_list,"var array = eval( file_dir .listNames() );"},
        {R.string.add_jss_file_getName,"file .getName();"},
        {R.string.add_jss_file_getPath,"file .getPath();"},
        {R.string.add_jss_file_getType,"file .getType();"},
        {R.string.add_jss_file_getParent,"file .getParent();"},
        {R.string.add_jss_file_getParentFile,"file .getParentFile();"},
        {R.string.add_jss_file_getProject,"var string_websitePath = File.getWebsitePath();"},
        {R.string.add_jss_file_getProjectFile,"var file_website = File.getWebsite();"},
        {R.string.add_jss_file_isFile,"var boolean_isFile = file .isFile();"},
        {R.string.add_jss_file_isDir,"var boolean_isDir = file .isDir();"},
        {R.string.add_jss_file_exists,"var boolean_isExists = file .exists();"},
        {R.string.add_jss_file_rename,"file_source .renameTo( file_renamed );"},
        {R.string.add_jss_file_write,"file .write( string_text );"},
        {R.string.add_jss_file_read,"var string_text = file.read();"}
    };
    public Object[][] OaAddJSSWeb=new Object[][]
    {
        {R.string.add_jss_web_urlDatas,"var dic = JSON.parse( Response.getURLData() );"},
        {R.string.add_jss_web_stringEntity,"Response.setString( string_text , string_charset );"},
        {R.string.add_jss_web_fileEntity,"Response.setFile( file_entity , string_charset );"},
        {R.string.add_jss_web_htmlCode,"Response.setHTML( string_code , string_charset );"}
    };
    public Object[][] OaAddJSSDebug=new Object[][]
    {
        {R.string.add_jss_debug_dialog,"alert( string_msg );"},
		{R.string.add_js_log,"console.log( string_log );"},
        {R.string.add_jss_debug_toast,"Debug.showToast( string_msg , boolean_isLongDuration );"}
    };
    public String[] SaDefChars="< > { } [ ] ( ) ; , . : ` & = + # \" ' ! ? % * - _ / | $ @ ~ \\".split(" ");
    public String[] SaDefNames="a;abbr;acronym;address;applet;area;article;aside;audio;b;base;basefont;bdi;bdo;big;blockquote;body;br;button;canvas;caption;center;cite;code;col;command;content;datalist;dd;del;details;dfn;dialog;dir;div;dl;dt;em;embed;fieldset;figcaption;figure;font;footer;form;frame;frameset;h1;h2;h3;h4;h5;h6;head;header;hr;html;i;iframe;img;input;ins;kbd;keygen;label;legend;li;link;map;mark;menu;meta;meter;nav;nobr;noframes;noscript;object;ol;optgroup;option;output;p;param;pre;progress;q;rp;rt;ruby;s;samp;script;section;select;small;source;span;strike;strong;style;sub;summary;sup;table;tbody;td;textarea;tfoot;th;thead;time;title;tr;track;tt;u;ul;var;video;wbr;h4".split(";");
	public String[] SaDefAttrs="action;accept-charset;autocomplete;accept;autofocus;async;axis;abbr;align;bgcolor;border;charset;color;class;contenteditable;contextmenu;code;codebase;coords;controls;char;charoff;checked;cite;cols;challenge;content;classid;codetype;cellpadding;cellspacing;colspan;data-animal-type;dir;draggable;dropzone;download;disabled;default;declare;data;defer;datetime;enctype;Frameborder;Framespacing;form;formaction;formenctype;formmethod;formnovalidate;formtarget;face;frameborder;for;frame;hidden;href;hreflang;height;hspace;http-equiv;high;headers;id;icon;ismap;keytype;kind;loop;lang;link;label;longdesc;list;low;muted;media;method;marginheight;marginwidth;max;maxlength;min;name;nohref;novalidate;noresize;noshade;nowrap;onclick;onblur;onhaschange;onload;onchange;oncontextmenu;onformchange;onforminput;oninput;oninvalid;onselect;onsubmit;onkeydown;onkeypress;onkeyup;onabort;onerror;onwaiting;ontoggle;object;open;optimum;password;preload;pattern;placeholder;pubdate;poster;rel;rev;radiogroup;rows;required;reversed;rules;rowspan;readonly;submit;size;scrolling;src;spellcheck;style;shape;span;sandbox;seamless;srcdoc;step;scheme;start;selected;standby;summary;scope;srclang;tabindex;title;translate;target;type;text;usemap;value;vspace;vlink;valign;valuetype;width;wrap".split(";");
	public String[] SaAllStyle;

	public String[] jsFuns={"alert()","confirm()","escape()","eval()","isNaN()","parseFloat()","parseInt()","prompt()"};
	public Object[][] jsPackNames ={
			{"Math",new String[]{
					"E","LN2","LN10","LOG2E","LOG10E","PI","SQRT1_2","SQRT_2",
					"abs()","acos()","asin()","atan()","atan2()","ceil()","cos()","exp()","floor()","log()","max()","min()","pow()","random()","round()","sin()","sqrt()","tan()","toSource()","valueOf()"
			}},
			{"window",new String[]{
					"frames[]","closed","defaultStatus","document","history","innerheight","innerwidth","length","location","name","Navigator","opener","outerheight","outerwidth","pageXOffset","pageYOffset","parent","Screen",
					"self","status","top","window","screenLeft","screenTop","screenX","screenY",

					"alert()","blur()","clearInterval()","clearTimeout()","close()","confirm()","createPopup()","focus()","moveBy()","moveTo()","open()","print()",
					"prompt()","resizeBy()","resizeTo()","scrollBy()","scrollTo()","setInterval()","setTimeout()"
			}},
			{"navigator",new String[]{
					"plugins[]","appCodeName","appMinorVersion","appName","appVersion","browserLanguage","cookieEnabled","cpuClass","onLine","platform",
					"systemLanguage","userAgent","userLanguage",

					"javaEnabled()","taintEnabled()"
			}},
			{"screen",new String[]{
					"availHeight","availWidth","bufferDepth","colorDepth","deviceXDPI","deviceYDPI","fontSmoothingEnabled","height","logicalXDPI","logicalYDPI","pixelDepth","updateInterval","width"
			}},
			{"history",new String[]{
					"length","back()","forward()","go()"
			}},
			{"location",new String[]{
					"hash","host","hostname","href","pathname","port","protocol","search",
					"assign()","reload()","replace()"
			}},
			{"document",new String[]{
					"all[]","anchors[]","applets[]","forms[]","images[]","links[]",
					"body","cookie","domain","lastModified","referrer","title","URL",
					"close()","getElementById()","getElementsByName()","getElementsByTagName()","open()","write()","writeln()"
			}},
			{"JSON",new String[]{
					"stringify()","parse()"
			}},
			{"console",new String[]{
					"log()","clear()","assert()","count()","error()","group()","groupCollapsed()","groupEnd()","info()","table()","time()","timeEnd()","trace()","warn()","debug()"
			}},
	};
	public Object[][] jssFuns={
			{"File",new String[]{
					"newFile()","deleteFile()","copy()","getWebsite()","getWebsitePath()"
			}},
			{"Response",new String[]{
					"getURLData()","setString()","setFile()","setHTML()"
			}},
			{"Debug",new String[]{
					"showToast()"
			}},
	};

	public Map<String,Object[][]> easyAppFunsMap =null;

	public String urlSetting="";
	
	public int USUAL_HTML_PROJ=0;
	public int PHP_PROJ=1;
	public int EASYAPP_PROJ=2;
	
	public boolean isDownloadingPacks=false;

	public boolean isUpdateProjectWhenResume=false;
	
	public boolean isFullPreview=false;
	
	public String serverHost="https://venter.coding.net/p/gloriouspast/d/gloriouspast/git/raw/master/";
	public String supportHost="https://atreep.netlify.app/";

	public boolean isFullEdit;

	public Drawable getAddIconDrawable()
	{
		Drawable d=MainActivity.main.getResources().getDrawable(R.drawable.add_outline);
		d.setTint(ContextCompat.getColor(MainActivity.main,R.color.opposition));
		return d;
	}
	
	public boolean hasShownServerDialog=false;
	
	public boolean isPreviewerZoomPC=false;

	public int nowWebsitePort =-1;
	public int nowProjectServerType =-1;

	public boolean isRunningPHPServerServiceListener =false;

	public String easyAppNewestVersion =null;

	public PackMachine nowProjectPackMachine=null;

	public AnnouncementManager.AnnouncementInf newestAnnouncementInf=null;
}
