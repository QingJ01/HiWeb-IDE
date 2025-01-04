package com.myopicmobile.textwarrior;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import com.myopicmobile.textwarrior.android.FreeScrollingTextField;
import com.myopicmobile.textwarrior.android.YoyoNavigationMethod;
import com.myopicmobile.textwarrior.common.ColorScheme;
import com.myopicmobile.textwarrior.common.ColorSchemeDark;
import com.myopicmobile.textwarrior.common.ColorSchemeLight;
import com.myopicmobile.textwarrior.common.Document;
import com.myopicmobile.textwarrior.common.DocumentProvider;
import com.myopicmobile.textwarrior.common.Language;
import com.myopicmobile.textwarrior.common.Lexer;
import com.myopicmobile.textwarrior.onTextChangeEvent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import com.myopicmobile.textwarrior.android.CopyBored;
import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.Map;
import android.widget.TextView;

public class TextEditor extends FreeScrollingTextField {
	private boolean _isWordWrap;
	private Context mContext;
	private int _index;
	public onTextChangeEvent Otce;

	public TextEditor(Context context) {
		super(context);
		mContext = context;
	}

	public TextEditor(Context context,AttributeSet attributeSet){
		super(context,attributeSet);
		mContext = context;
	}

	private  void init()
	{
		setTypeface(Typeface.MONOSPACE);
		DisplayMetrics dm=mContext.getResources().getDisplayMetrics();
		//设置字体大小
		float size= TypedValue.applyDimension(2, BASE_TEXT_SIZE_PIXELS, dm);
		setTextSize((int)size);
		setShowLineNumbers(true);
		setHighlightCurrentRow(true);
		setAutoIndentWidth(4);
		setWordWrap(false);
		setNavigationMethod(new YoyoNavigationMethod(this));
	}
	public void setKeywords(String[] words) {
		Language lang=Lexer.getLanguage();
		lang.setKeywords(words);
		Lexer.setLanguage(lang);
		respan();
		invalidate();
	}
	public void addKeywords(String[] words) {
		Language lang=Lexer.getLanguage();
		String[] old=lang.getKeywords();
		String[] news=new String[old.length + words.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(words, 0, news, old.length, words.length);
		lang.setKeywords(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();
	}
	public void setJsKeywords(String[] words) {
		Language lang=Lexer.getLanguage();

		ArrayList<String> buf = new ArrayList<String>();
		Map jsKeywordsMap=new HashMap<String, Integer>(words.length);
		for (int i = 0; i < words.length; ++i) {
			if (!buf.contains(words[i]))
				buf.add(words[i]);
			jsKeywordsMap.put(words[i], Lexer.NAME);
		}
		lang.setJsKeywords(jsKeywordsMap);

		String[] old=lang.getKeywords();
		String[] news=new String[old.length + words.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(words, 0, news, old.length, words.length);
		lang.setKeywords(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	public void setHtmlElements(String[] words) {
		Language lang=Lexer.getLanguage();

		ArrayList<String> buf = new ArrayList<String>();
		Map elementsMap=new HashMap<String, Integer>(words.length);
		for (int i = 0; i < words.length; ++i) {
			if (!buf.contains(words[i]))
				buf.add(words[i]);
			elementsMap.put(words[i], Lexer.NAME);
		}
		lang.setHtmlElements(elementsMap);

		String[] old=lang.getKeywords();
		String[] news=new String[old.length + words.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(words, 0, news, old.length, words.length);
		lang.setKeywords(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	public void setPHPKeywords(String[] words) {
		Language lang=Lexer.getLanguage();

		ArrayList<String> buf = new ArrayList<String>();
		Map phpKeywordsMap=new HashMap<String, Integer>(words.length);
		for (int i = 0; i < words.length; ++i) {
			if (!buf.contains(words[i]))
				buf.add(words[i]);
			phpKeywordsMap.put(words[i], Lexer.NAME);
		}
		lang.setPHPKeywords(phpKeywordsMap);

		String[] old=lang.getKeywords();
		String[] news=new String[old.length + words.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(words, 0, news, old.length, words.length);
		lang.setKeywords(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}

	public void addBasePackage(String name,String[] words) {
		Language lang=Lexer.getLanguage();
		lang.addBasePackage(name,words);
		Lexer.setLanguage(lang);
		respan();
		invalidate();
	}

	public void removeBasePackage(String name) {
		Language lang=Lexer.getLanguage();
		lang.removeBasePackage(name);
		Lexer.setLanguage(lang);
		respan();
		invalidate();
	}

	public void removeBasePackage() {
		Language lang=Lexer.getLanguage();
		lang.removeBasePackage();
		Lexer.setLanguage(lang);
		respan();
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO: Implement this method
		super.onLayout(changed, left, top, right, bottom);
		if (_index != 0 && right > 0) {
			moveCaret(_index);
			_index = 0;
		}
	}

	public void setLanguage(Language L)
	{
		Lexer.setLanguage(L);
		_autoCompletePanel.setLanguage(L);
		respan();
		invalidate();
	}
	public void setDark()
	{
		setColorScheme(new ColorSchemeDark());
		isDark=true;
	}
	public void setLight()
	{
		setColorScheme(new ColorSchemeLight());
		setBackgroundColor(Color.parseColor("#FFF6F6F6"));
	}
	public void setEvent(onTextChangeEvent Otce)
	{
		this.Otce=Otce;
	}
	public void setPanelBackgroundColor(int color) {
		// TODO: Implement this method
		_autoCompletePanel.setBackgroundColor(color);
	}

	public void setPanelTextColor(int color) {
		// TODO: Implement this method
		_autoCompletePanel.setTextColor(color);
	}

	public void setKeywordColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.KEYWORD, color);
	}

	public void setBaseWordColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.NAME, color);
	}

	public void setStringColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.STRING, color);
	}

	public void setCommentColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.COMMENT, color);
	}

	public void setBackgroundColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.BACKGROUND, color);
	}

	public void setTextColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.FOREGROUND, color);
	}

	public void setTextHighlightColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.SELECTION_BACKGROUND, color);
	}

	public String getSelectedText() {
		// TODO: Implement this method
		return  _hDoc.subSequence(getSelectionStart(), getSelectionEnd() - getSelectionStart()).toString();
	}

	public void gotoLine(int line) {
		if (line > _hDoc.getRowCount()) {
			line = _hDoc.getRowCount();

		}
		int i=getText().getLineOffset(line - 1);
		setSelection(i);
	}
	public void setPasteBar(LinearLayout PasteBar,CopyBored cb,TextView title,Toolbar T)
	{
		this.PasteBar=PasteBar;
		this.vCopyBored=cb;
		this.TitleTextView=title;
		this.T=T;
		initView();
		init();
	}
	public void setGreen()
	{
		setColorScheme(new ColorSchemeLight());
		setBackgroundColor(Color.parseColor("#FFD8FFD3"));
	}
	public void setPink()
	{
		setColorScheme(new ColorSchemeLight());
		setBackgroundColor(Color.parseColor("#FFFFF3F3"));
	}
	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		final int filteredMetaState = event.getMetaState() & ~KeyEvent.META_CTRL_MASK;
		if (KeyEvent.metaStateHasNoModifiers(filteredMetaState)) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_A:
					selectAll();
					return true;
				case KeyEvent.KEYCODE_X:
					cut();
					return true;
				case KeyEvent.KEYCODE_C:
					copy();
					return true;
				case KeyEvent.KEYCODE_V:
					paste();
					return true;
			}
		}
		return super.onKeyShortcut(keyCode, event);
	}

	public String getString() {
		return getText().toString();
	}
	public void setNames(String[] names) {
		Language lang=Lexer.getLanguage();
		lang.setNames(names);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	public void addNames(String[] names) {
		Language lang=Lexer.getLanguage();
		String[] old=lang.getNames();
		String[] news=new String[old.length + names.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(names, 0, news, old.length, names.length);
		lang.setNames(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	public void setAttrs(String[] names) {
		Language lang=Lexer.getLanguage();

		ArrayList<String> buf = new ArrayList<String>();
		Map attrsMap=new HashMap<String, Integer>(names.length);
		for (int i = 0; i < names.length; ++i) {
			if (!buf.contains(names[i]))
				buf.add(names[i]);
			attrsMap.put(names[i], Lexer.NAME);
		}
		lang.setAttrs(attrsMap);

		String[] old=lang.getNames();
		String[] news=new String[old.length + names.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(names, 0, news, old.length, names.length);
		lang.setNames(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	public void setCss(String[] names) {
		Language lang=Lexer.getLanguage();

		ArrayList<String> buf = new ArrayList<String>();
		Map cssMap=new HashMap<String, Integer>(names.length);
		for (int i = 0; i < names.length; ++i) {
			if (!buf.contains(names[i]))
				buf.add(names[i]);
			cssMap.put(names[i], Lexer.NAME);
		}
		lang.setCss(cssMap);

		String[] old=lang.getNames();
		String[] news=new String[old.length + names.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(names, 0, news, old.length, names.length);
		lang.setNames(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	public void setPackNames(String[] names) {
		Language lang=Lexer.getLanguage();

		ArrayList<String> buf = new ArrayList<String>();
		Map packNamesMap=new HashMap<String, Integer>(names.length);
		for (int i = 0; i < names.length; ++i) {
			if (!buf.contains(names[i]))
				buf.add(names[i]);
			packNamesMap.put(names[i], Lexer.NAME);
		}
		lang.setPackNames(packNamesMap);

		String[] old=lang.getNames();
		String[] news=new String[old.length + names.length];
		System.arraycopy(old, 0, news, 0, old.length);
		System.arraycopy(names, 0, news, old.length, names.length);
		lang.setNames(news);
		Lexer.setLanguage(lang);
		respan();
		invalidate();

	}
	@Override
	public void setWordWrap(boolean enable) {
		// TODO: Implement this method
		_isWordWrap = enable;
		super.setWordWrap(enable);
	}

	public DocumentProvider getText() {
		return createDocumentProvider();
	}

	public void insert(int idx, String text) {
		selectText(false);
		moveCaret(idx);
		paste(text);
	}

	private String appendHeader(String text, String header) throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(text.getBytes());
		InputStream inputStream = byteArrayInputStream;
		BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
		String line = bfr.readLine();
		int lines = 0;
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			if (lines > 0)
				sb.append("\n");
			sb.append(header + line);
			lines++;
			line = bfr.readLine();
		}
		bfr.close();

		return sb.toString();
	}

	public void replaceAll(CharSequence c) {
		replaceText(0, getLength() - 1, c.toString());
	}

	public void setText(CharSequence c) {
		Document doc=new Document(this);
		doc.setWordWrap(_isWordWrap);
		doc.setText(c);
		setDocumentProvider(new DocumentProvider(doc));
	}

	public void setSelection(int index) {
		selectText(false);
		if (!hasLayout())
			moveCaret(index);
		else
			_index = index;
	}


	public void undo() {

		DocumentProvider doc=createDocumentProvider();
		int newPosition = doc.undo();

		if (newPosition >= 0) {
			//TODO editor.setEdited(false);
			// if reached original condition of file
			setEdited(true);
			respan();
			selectText(false);
			moveCaret(newPosition);
			invalidate();
			changeText();
		}

	}

	public void redo() {

		DocumentProvider doc = createDocumentProvider();
		int newPosition = doc.redo();

		if (newPosition >= 0) {
			setEdited(true);

			respan();
			selectText(false);
			moveCaret(newPosition);
			invalidate();
			changeText();
		}

	}
}
