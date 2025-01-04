package com.hiweb.ide;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.hiweb.ide.edit.Do;
import java.util.HashMap;
import java.util.Map;
import android.widget.AdapterView;
import android.view.View;
import android.widget.TextView;
import android.widget.ScrollView;

import androidx.core.content.ContextCompat;

import java.io.File;

public class Console extends LinearLayout {
	public Console(android.content.Context context) {
		super(context);
		init();
	}

	public Console(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Console(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public Console(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	ListView lv;
	Map<Integer, Object[]> dataMap;
	/*
	 * Object[]:
	 * [0]:Spannable text
	 * [1]:String sourceId
	 * [2]:String time
	 * [3]:ConsoleMessage consoleMessage
	 */
	int nums = 0;

	private void init() {
		reset();
	}

	public void reset() {
		removeAllViews();
		lv = new ListView(getContext());
		lv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		addView(lv);
		dataMap = new HashMap<Integer, Object[]>();
		nums = 0;
		updateList();
	}

	public WebChromeClient wcc = new WebChromeClient() {
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			Spannable text = null;

			if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.DEBUG) {
				String title = "Debug(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());
				text.setSpan(new ForegroundColorSpan(Color.GRAY), 0, title.length(),
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			} else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
				String title = "Error(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());
				text.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			} else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.LOG) {
				String title = "Log(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());
				text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MainActivity.main, R.color.opposition)), 0,
						title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			} else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.TIP) {
				String title = "Tip(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());
				text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, title.length(),
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			} else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.WARNING) {
				String title = "Warning(Line:" + consoleMessage.lineNumber() + "):";
				text = new SpannableString(title + consoleMessage.message());
				text.setSpan(new ForegroundColorSpan(Color.parseColor("#FF8000")), 0, title.length(),
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			String source = "";
			if (consoleMessage.sourceId()
					.equals("file://" + new File(getContext().getFilesDir(), "temp_javascript").getPath())) {
				source = "[File] " + (Vers.i.OpenFile.getPath());
			} else {
				source = consoleMessage.sourceId();
			}

			dataMap.put(nums, new Object[] { text, source, Do.getTime("HH:mm:ss"), consoleMessage });
			nums++;

			updateList();

			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			String title = "Alert:";
			Spannable text = new SpannableString(title + message);
			text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MainActivity.main, R.color.opposition)), 0,
					title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			text.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			String SUrl = view.getUrl();
			String source = "";
			if (SUrl.equals("about:blank") || url.equals("about:blank")) {
				source = "[File] " + (Vers.i.OpenFile.getPath());
			} else {
				source = url;
			}

			dataMap.put(nums, new Object[] { text, source, Do.getTime("HH:mm:ss") });
			nums++;

			updateList();

			result.confirm();
			return true;
		}
	};

	public void updateList() {
		if (dataMap == null || dataMap.size() == 0) {
			lv.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
					new String[] { getContext().getString(R.string.no_items) }));
			lv.setOnItemClickListener(null);
			return;
		}

		Spannable[] SaMessage = new Spannable[dataMap.size()];

		int i = 0;
		for (int key : dataMap.keySet()) {
			SaMessage[i] = ((Spannable) (dataMap.get(key))[0]);
			i++;
		}
		ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, SaMessage);

		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
				Dl adb = new Dl(getContext());
				adb.builder.setTitle(R.string.detail);

				ScrollView sv = new ScrollView(getContext());
				sv.setFillViewport(true);

				TextView tv = new TextView(getContext());
				tv.setLayoutParams(new ScrollView.LayoutParams(-1, -1));
				tv.setPadding(Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10),
						Do.dp2px(MainActivity.main, 10), Do.dp2px(MainActivity.main, 10));

				sv.addView(tv);
				Do.CanSelect(tv);

				StringBuilder sb = new StringBuilder();
				Object[] now = dataMap.get(p3);

				sb.append(getContext().getString(R.string.website_note) + now[2] + "\n");
				if (now.length == 3) {
					// Alert

					sb.append(getContext().getString(R.string.message_c) + now[0].toString().substring(6) + "\n");
				} else if (now.length == 4) {
					// Console
					sb.append(getContext().getString(R.string.message_c) + ((ConsoleMessage) now[3]).message() + "\n");
					sb.append(getContext().getString(R.string.line_c) + ((ConsoleMessage) now[3]).lineNumber() + "\n");
					sb.append(getContext().getString(R.string.importance_c));
					if (((ConsoleMessage) now[3]).messageLevel() == ConsoleMessage.MessageLevel.DEBUG)
						sb.append("DEBUG");
					else if (((ConsoleMessage) now[3]).messageLevel() == ConsoleMessage.MessageLevel.ERROR)
						sb.append("ERROR");
					else if (((ConsoleMessage) now[3]).messageLevel() == ConsoleMessage.MessageLevel.LOG)
						sb.append("LOG");
					else if (((ConsoleMessage) now[3]).messageLevel() == ConsoleMessage.MessageLevel.TIP)
						sb.append("TIP");
					else if (((ConsoleMessage) now[3]).messageLevel() == ConsoleMessage.MessageLevel.WARNING)
						sb.append("WARNING");

					sb.append("\n");
				}
				sb.append(getContext().getString(R.string.source_c) + now[1]);

				tv.setText(sb.toString());
				adb.builder.setView(sv);
				adb.show();
			}
		});
	}
}
