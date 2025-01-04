package com.hiweb.ide;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.myopicmobile.textwarrior.TextEditor;
import com.myopicmobile.textwarrior.common.Language;
import com.myopicmobile.textwarrior.common.LanguageCpp;
import com.myopicmobile.textwarrior.common.LanguageNonProg;
import com.hiweb.ide.add.addViewWidget.TextLayout;
import com.hiweb.ide.edit.Do;
import com.hiweb.ide.edit.JavaForm;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import androidx.core.content.ContextCompat;

import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Editor extends TextEditor {

	public static String[] JSKeywords = {
			"abstract", "boolean", "break", "byte", "case", "catch", "char",
			"class", "const", "continue", "debugger", "default", "delete", "do",
			"double", "else", "enum", "export", "extends", "false", "final",
			"finally", "float", "for", "function", "goto", "if", "implements",
			"import", "in", "instanceof", "int", "interface", "long", "native",
			"new", "null", "package", "private", "protected", "public", "return",
			"short", "static", "super", "switch", "synchronized", "this", "throw",
			"throws", "transient", "true", "try", "typeof", "var", "void",
			"volatile", "while", "with"
	};
	public static String[] PHPKeywords = { "abstract", "and", "array", "as", "break", "callable", "case", "catch",
			"class", "clone", "const", "continue",
			"declare", "default", "die", "do", "echo", "else", "elseif", "empty", "enddeclare", "endfor", "endforeach",
			"endif", "endswitch", "endwhile", "eval", "exit", "extends",
			"final", "for", "foreach", "function", "global", "goto", "if", "implements", "include", "include_once",
			"instanceof", "insteadof", "interface", "isset", "list", "namespace", "new", "or", "php",
			"print", "private", "protected", "public", "require", "require_once", "return", "static", "switch", "throw",
			"trait", "try", "unset", "use", "var", "while", "xor", "EOF" };
	public static String[] PHPCount = { "__CLASS__", "__DIR__", "__FILE__", "__FUNCTION__", "__LINE__", "__METHOD__",
			"__NAMESPACE__", "__TRAIT__", "PHP_VERSION", "PHP_OS", "TRUE", "FALSE", "NULL", "E_ERROR", "E_WARNING",
			"E_PARSE",
			"E_NOTICE" };
	public static String[] PHPFuns = {
			"__halt_compiler()", "usleep()", "unpack()", "uniqid()", "time_sleep_until()", "time_nanosleep()",
			"sleep()", "show_source()", "strip_whitespace()", "pack()", "ignore_user_abort()", "highlight_string()",
			"highlight_file()", "get_browser()", "exit()", "eval()", "die()", "defined()", "define()",
			"constant()", "connection_status()", "connection_aborted()", "zip_read()", "zip_open()", "zip_entry_read()",
			"zip_entry_open()", "zip_entry_name()", "zip_entry_filesize()", "zip_entry_compressionmethod()",
			"zip_entry_compressedsize()", "zip_entry_close()", "zip_close()", "xml_set_unparsed_entity_decl_handler()",
			"xml_set_processing_instruction_handler()", "xml_set_object()", "xml_set_notation_decl_handler()",
			"xml_set_external_entity_ref_handler()",
			"xml_set_element_handler()", "xml_set_default_handler()", "xml_set_character_data_handler()",
			"xml_parser_set_option()", "xml_parser_get_option()", "xml_parser_free()", "xml_parser_create()",
			"xml_parser_create_ns()", "xml_parse_into_struct()", "xml_parse()", "xml_get_error_code()",
			"xml_get_current_line_number()", "xml_get_current_column_number()", "xml_get_current_byte_index()",
			"xml_error_string()", "utf8_encode()", "utf8_decode()", "wordwrap()",
			"vsprintf()", "vprintf()", "vfprintf()", "ucwords()", "ucfirst()", "trim()", "substr_replace()",
			"substr_count()", "substr_compare()", "substr()", "strtr()", "strtoupper()", "strtolower()", "strtok()",
			"strstr()", "strspn()", "strrpos()", "strripos()",
			"strrev()", "strrchr()", "strpos()", "strpbrk()", "strncmp()", "strncasecmp()", "strnatcmp()",
			"strnatcasecmp()", "strlen()", "stristr()", "stripos()", "stripslashes()", "stripcslashes()",
			"strip_tags()", "strcspn()", "strcoll()", "strcmp()", "strchr()",
			"strcasecmp()", "str_word_count()", "str_split()", "str_shuffle()", "str_rot13()", "str_replace()",
			"str_repeat()", "str_pad()", "str_ireplace()", "sscanf()", "sprintf()", "soundex()", "similar_text()",
			"sha1_file()", "sha1()", "setlocale()", "rtrim()", "rtrim()",
			"PHP", "quotemeta()", "quoted_printable_decode()", "printf()", "print()", "parse_str()", "ord()",
			"number_format()", "nl2br()", "nl_langinfo()", "money_format()", "metaphone()", "md5_file()", "md5()",
			"ltrim()", "localeconv()", "levenshtein()", "",
			"implode()", "htmlspecialchars()", "html_entity_decode()", "chars_decode()", "PHP", "htmlentities()",
			"html_entity_decode()", "hebrevc()", "hebrev()", "get_html_translation_table()", "fprintf()", "explode()",
			"echo()", "crypt()", "crc32()", "count_chars()", "convert_uuencode()", "convert_uudecode()",
			"convert_cyr_string()", "chunk_split()", "chr()", "chop()", "bin2hex()", "addslashes()", "addcslashes()",
			"xpath()", "simplexml_load_string()", "simplexml_load_file()", "simplexml_import_dom()",
			"registerXPathNamespace()", "getNamespace()", "getName()", "getDocNamespaces()", "children()",
			"attributes()", "asXML()",
			"addChild()", "addAttribute()", "__construct()", "mysql_unbuffered_query()", "mysql_thread_id()",
			"mysql_stat()", "mysql_select_db()", "mysql_result()", "mysql_real_escape_string()", "mysql_query()",
			"mysql_ping()", "mysql_pconnect()", "mysql_num_rows()", "mysql_num_fields()", "mysql_list_processes()",
			"mysql_list_dbs()", "mysql_insert_id()", "mysql_info()",
			"mysql_get_server_info()", "mysql_get_proto_info()", "mysql_get_host_info()", "mysql_get_client_info()",
			"mysql_free_result()", "mysql_field_type()", "mysql_field_table()", "mysql_field_seek()",
			"mysql_field_name()", "mysql_field_len()", "mysql_field_flags()", "mysql_fetch_row()",
			"mysql_fetch_object()", "mysql_fetch_lengths()", "mysql_fetch_field()", "mysql_fetch_assoc()",
			"mysql_fetch_array()", "mysql_errno()",
			"mysql_db_name()", "mysql_data_seek()", "mysql_connect()", "mysql_close()", "mysql_client_encoding()",
			"mysql_affected_rows()", "tanh()", "tan()", "srand()", "sqrt()", "sinh()", "sin()", "round()", "rand()",
			"rad2deg()", "pow()", "pi()", "octdec()",
			"mt_srand()", "mt_rand()", "mt_getrandmax()", "min()", "log1p()", "log10()", "lcg_value()", "is_nan()",
			"is_finite()", "hypot()", "hexdec()", "fmod()", "fmod()", "floor()", "expm1()", "exp()", "deg2rad()",
			"decoct()",
			"dechex()", "decbin()", "cosh()", "cos()", "ceil()", "bindec()", "base_convert()", "atanh()", "atan()",
			"PHP", "asinh()", "asin()", "acosh()", "acos()", "abs()", "mail()", "libxml_use_internal_errors()",
			"libxml_get_last_error()",
			"libxml_get_errors()", "libxml_clear_errors()", "setrawcookie()", "setcookie()", "headers_sent()",
			"headers_list()", "header()", "ftp_systype()", "ftp_ssl_connect()", "ftp_size()", "ftp_site()",
			"ftp_set_option()", "ftp_rmdir()", "ftp_rename()", "ftp_rawlist()", "ftp_raw()", "ftp_quit()", "ftp_pwd()",
			"ftp_put()", "ftp_pasv()", "ftp_nlist()", "ftp_nb_put()", "ftp_nb_get()", "ftp_nb_fput()", "ftp_nb_fget()",
			"ftp_nb_continue()", "ftp_mkdir()", "ftp_mdtm()", "ftp_login()", "ftp_get()", "ftp_get_option()",
			"ftp_fput()", "ftp_fget()", "ftp_exec()", "ftp_connect()", "ftp_close()",
			"ftp_chmod()", "ftp_chdir()", "ftp_cdup()", "ftp_alloc()", "filter_var()", "filter_var_array()",
			"filter_list()", "filter_input_array()", "filter_input()", "filter_id()", "filter_has_var()", "unlink()",
			"umask()", "touch()", "tmpfile()", "tempnam()", "symlink()", "stat()",
			"set_file_buffer()", "rmdir()", "rewind()", "rename()", "realpath()", "readlink()", "readfile()", "popen()",
			"pclose()", "pathinfo()", "parse_ini_file()", "move_uploaded_file()", "mkdir()", "lstat()", "linkinfo()",
			"link()", "is_writeable()", "is_writable()",
			"is_uploaded_file()", "is_readable()", "is_link()", "is_file()", "is_executable()", "is_dir()", "glob()",
			"fwrite()", "ftruncate()", "ftell()", "fstat()", "fseek()", "fscanf()", "fread()", "fputs()", "fputcsv()",
			"fpassthru()", "fopen()",
			"fnmatch()", "flock()", "filetype()", "filesize()", "fileperms()", "fileowner()", "filemtime()",
			"fileinode()", "filegroup()", "filectime()", "fileatime()", "file_put_contents()", "file_get_contents()",
			"file_exists()", "file()", "fgetss()", "fgets()", "fgetcsv()",
			"fgetc()", "fflush()", "feof()", "fclose()", "diskfreespace()", "disk_total_space()", "disk_free_space()",
			"dirname()", "clearstatcache()", "clearstatcache()", "chown()", "chmod()", "chgrp()", "basename()",
			"set_exception_handler()", "", "PHP", "set_exception_handler()",
			"set_error_handler()", "restore_exception_handler()", "set_exception_handler()", "restore_error_handler()",
			"set_error_handler()", "error_reporting()", "error_log()", "error_get_last()", "debug_print_backtrace()",
			"debug_backtrace()", "PHP", "scandir()", "rewinddir()", "opendir()", "readdir()", "opendir()", "getcwd()",
			"closedir()",
			"dir()", "chroot()", "chdir()", "time()", "strtotime()", "strptime()", "strftime()", "mktime()",
			"microtime()", "localtime()", "idate()", "gmstrftime()", "gmmktime()", "gmdate()", "gettimeofday()",
			"getdate()", "date()", "date_sunset()",
			"date_sunrise()", "date_default_timezone_set()", "date_default_timezone_get()", "checkdate()", "UnixToJD()",
			"JulianToJD()", "JewishToJD()", "JDToUnix()", "PHP", "JDToGregorian()", "JDToFrench()", "JDMonthName()",
			"JDDayOfWeek()", "GregorianToJD()", "FrenchToJD()", "easter_days()", "easter_date()", "cal_to_jd()",
			"cal_info()", "cal_from_jd()", "cal_days_in_month()", "uksort()", "uasort()", "sort()", "sizeof()",
			"shuffle()", "rsort()", "reset()", "range()", "prev()", "PHP", "pos()", "next()", "natsort()",
			"natcasesort()", "list()",
			"ksort()", "krsort()", "key()", "in_array()", "extract()", "PHP", "end()", "each()", "current()", "count()",
			"compact()", "asort()", "arsort()", "array_walk_recursive()", "cursive()", "array_walk()", "array_values()",
			"array_unshift()",
			"array_unique()", "array_uintersect_assoc()", "array_uintersect()", "array_udiff_uassoc()",
			"array_udiff_assoc()", "array_udiff()", "array_sum()", "array_splice()", "array_slice()", "array_shift()",
			"array_search()", "array_reverse()", "array_reduce()", "array_rand()", "array_push()", "array_product()",
			"array_pop()", "array_pad()",
			"array_multisort()", "array_merge_recursive()", "array_merge()", "array_map()", "array_keys()",
			"array_key_exists()", "array_intersect_ukey()", "array_intersect_uassoc()", "array_intersect_key()",
			"array_intersect_assoc()", "array_intersect()", "array_flip()", "array_filter()", "array_fill()",
			"array_diff_ukey()", "array_diff_uassoc()", "array_diff_key()", "array_diff_assoc()",
			"array_diff()", "array_count_values()", "array_combine()", "array_chunk()", "array_change_key_case()",
			"array()", "phpinfo()"
	};
	public int mode = -1;
	public int sid = -1;
	public boolean isSetEven = true;

	public void build(int mode, String text) {
		this.mode = mode;
		Language L;
		setLang(mode);
		switch (mode) {
			case 0:
			case 2:
			case 3:
				// HTML CSS php

			case 1:
			case 5:
				// JS JSS

				L = LanguageCpp.getInstance();
				setLanguage(L);
				if (text != null)
					setText(text);
				break;
			default:
				// XML 文本类

				L = LanguageNonProg.getInstance();
				setLanguage(L);
				if (text != null)
					setText(text);
				break;

		}
		setEvent(() -> {
			if (sid != -1 && isSetEven) {
				MainActivity.main.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MainActivity.main.getEpi(sid).star();
					}
				});
			}
		});
	}

	public void inNames() {
		setKeywords(new String[] {});
		setNames(new String[] {});
		removeBasePackage();
		switch (mode) {
			case 0:
			case 2:
			case 3:
				// HTML CSS php
				if (mode == 0 || mode == 3) {
					setHtmlElements(SettingsClass.SaNames);
					setAttrs(Vers.i.i.SaDefAttrs);
				}
				setCss(Vers.i.i.SaAllStyle);
				if (mode == 3) {
					setPHPKeywords(Do.mergeArrays(PHPKeywords, PHPCount));
					setPackNames(PHPFuns);
				}

				if (mode == 2 || mode == 3)
					break;
			case 1:
			case 5:
				// JS JSS
				setJsKeywords(JSKeywords);
				String[] packNameArray = new String[Vers.i.jsPackNames.length + Vers.i.jsFuns.length];
				for (int i = 0; i < Vers.i.jsPackNames.length; i++) {
					packNameArray[i] = Vers.i.jsPackNames[i][0].toString();
					addBasePackage(Vers.i.jsPackNames[i][0].toString(), (String[]) Vers.i.jsPackNames[i][1]);
				}
				for (int a = 0, i = Vers.i.jsPackNames.length; a < Vers.i.jsFuns.length; a++, i++) {
					packNameArray[i] = Vers.i.jsFuns[a];
				}

				if (mode == 5) {
					String[] jssPackNameArray = new String[Vers.i.jssFuns.length];
					for (int i = 0; i < Vers.i.jssFuns.length; i++) {
						jssPackNameArray[i] = Vers.i.jssFuns[i][0].toString();
						addBasePackage(Vers.i.jssFuns[i][0].toString(), (String[]) Vers.i.jssFuns[i][1]);
					}
					String[] news = new String[packNameArray.length + jssPackNameArray.length];
					System.arraycopy(packNameArray, 0, news, 0, packNameArray.length);
					System.arraycopy(jssPackNameArray, 0, news, packNameArray.length, jssPackNameArray.length);
					packNameArray = news;
				}

				setPackNames(packNameArray);
				break;
		}
	}

	public void insert(String text) {
		paste(text);
	}

	private String getEscapeText(String origin, boolean isRecover) {
		String[][] table = {
				{ "&lt;", "!!!!!!lt!!!!!!;" },
				{ "&gt;", "!!!!!!gt!!!!!!;" },
				{ "&amp;", "!!!!!!amp!!!!!!;" }
		};
		for (String[] now : table) {
			origin = origin.replace(now[isRecover ? 1 : 0], now[isRecover ? 0 : 1]);
		}
		return origin;
	}

	public void format() {
		switch (mode) {
			case 1:
			case 5:
				// JS JSS
				replaceAll(JavaForm.formJava(getString()));
				break;
			case 0:
				// HTML
				try {
					String origin = getString();
					origin = getEscapeText(origin, false);
					Document doc = Jsoup.parse(origin);
					doc.outputSettings().indentAmount(4);
					String html = doc.html();
					doc = Jsoup.parse(html);
					doc.outputSettings().prettyPrint(false);

					Elements scriptElements = doc.select("script");
					for (Element nowElement : scriptElements) {
						if (nowElement.data().trim().equals(""))
							continue;

						int level = Do.getElementLevel(nowElement);
						String formatCode = "\n"
								+ Do.appendHeader(JavaForm.formJava(nowElement.data()),
										Do.getMultipleText(level + 1, "    "))
								+ "\n" + Do.getMultipleText(level, "    ");
						nowElement.html(formatCode);
					}

					html = doc.html();
					html = html.substring(0, html.length() - 19) + "    </body>\n</html>";
					html = Parser.unescapeEntities(html, false);
					html = getEscapeText(html, true);
					html = html.replace("    ", "\t");
					replaceAll(html);
				} catch (Exception e) {
					Do.showErrDialog(getContext(), e);
				}
				break;
		}
		{
			// 响应文本改变监听器
			Otce.run();
		}
	}

	public void replaceAll(CharSequence c) {
		setSelection(0, 0);
		replaceText(0, getString().length(), c.toString());
		{
			// 响应文本改变监听器
			Otce.run();
		}
	}

	@Override
	public void changeText() {
		Otce.run();
	}

	@Override
	public void showAutoComplateTip(boolean isOnlySaveDataButDontShow) {
		if (!new TipsManager(getContext()).isExists("editor_autocomplate")) {
			if (!isOnlySaveDataButDontShow)
				Do.showImgDialog(getContext(), R.string.try_new_fun, R.string.editor_autocomplate_tip,
						R.drawable.editor_autocomplate);
			new TipsManager(getContext()).create("editor_autocomplate");
		}
	}

	@Override
	public void showFilePathTip() {
		if (!new TipsManager(getContext()).isExists("editor_add_file")) {
			Do.showImgDialog(getContext(), R.string.editor_add_file_title, R.string.editor_add_file_msg,
					R.drawable.editor_add_file);
			new TipsManager(getContext()).create("editor_add_file");
		}
	}

	public void findText() {
		LinearLayout vLy = (LinearLayout) MainActivity.main.findViewById(R.id.ly_editor_findandreplace);
		vLy.setVisibility(vLy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
		MainActivity.main.GoneMore();
		MainActivity.main.binding.lyMainMenu.setVisibility(vLy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
		MainActivity.main.binding.btnMainMore
				.setVisibility(vLy.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

		final EditText vEtFind = MainActivity.main.findViewById(R.id.et_editor_find);
		Button vBtnFind = (Button) MainActivity.main.findViewById(R.id.btn_editor_find);

		final EditText vEtReplace = (EditText) MainActivity.main.findViewById(R.id.et_editor_replace);
		Button vBtnReplace = (Button) MainActivity.main.findViewById(R.id.btn_editor_replace);

		CheckBox vCbRex = (CheckBox) MainActivity.main.findViewById(R.id.cb_editor_replace_opt_rex);
		CheckBox vCbNoCase = (CheckBox) MainActivity.main.findViewById(R.id.cb_editor_replace_opt_no_case);

		vLy.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.alpha_theme_color));

		vBtnFind.setOnClickListener(new View.OnClickListener() {

			class FindDone extends Exception {
			};

			@Override
			public void onClick(View p1) {
				try {
					String findText = vEtFind.getText().toString();
					if (findText.equals("")) {
						throw new FindDone();
					}
					String text = getString();
					int index = getSelectionEnd();
					int targetStart = text.indexOf(findText, index);
					if (targetStart == -1) {
						int newTarget = text.indexOf(findText);
						if (newTarget == -1)
							throw new FindDone();
						else {
							setSelection(newTarget, findText.length());
							throw new FindDone();
						}
					}
					setSelection(targetStart, findText.length());
				} catch (FindDone ok) {
					MainActivity.main.toast(R.string.find_done);
				}

			}
		});
		vBtnReplace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View p1) {
				try {

					String findText = vEtFind.getText().toString();
					String replaceText = vEtReplace.getText().toString();
					if (findText.equals("")) {
						throw new Exception();
					}
					String text = getString();
					text = text.replace(findText, replaceText);
					replaceAll(text);
					MainActivity.main.toast(R.string.done);
				} catch (Exception e) {
					MainActivity.main.toast(R.string.replace_err);
				}

			}
		});
	}

	public void gotoline() {
		final TextLayout Tl = new TextLayout(getContext());
		Tl.build(-1, null, R.string.line_num, null);
		Tl.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
				Do.dp2px(MainActivity.main, 10));
		Tl.Acet.setKeyListener(DigitsKeyListener.getInstance("1234567890"));

		Dl Adb = new Dl(getContext());
		Adb.builder.setTitle(R.string.go_to_line);
		Adb.builder.setView(Tl);
		Adb.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface p1, int p2) {
				if (Tl.getText("$") != null) {
					try {
						gotoLine(Integer.parseInt(Tl.getText("$")));
					} catch (Exception e) {

					}
				}
			}
		});
		Adb.show();
	}

	public Editor(Context c, int sid, boolean isSetEven) {
		super(c);
		this.sid = sid;
		this.isSetEven = isSetEven;
		this.themeCode = SettingsClass.ITheme;
		this.EWActionBar = MainActivity.main.binding.actionbar;

		Arrays.sort(JSKeywords);
		Arrays.sort(PHPKeywords);
		Arrays.sort(PHPCount);
		Arrays.sort(PHPFuns);

		if (sid != -1) {
			if (SettingsClass.ITheme == 1) {
				setDark();
			} else if (SettingsClass.ITheme == 2) {
				setGreen();
			} else if (SettingsClass.ITheme == 3) {
				setPink();
			} else {
				setLight();
			}
		}
		if (sid != -1) {
			setPasteBar(MainActivity.main.binding.pastebar, MainActivity.main.binding.pastebarCb,
					MainActivity.main.binding.pastebarTitle, MainActivity.main.binding.toolbar);
		}
	}
}
