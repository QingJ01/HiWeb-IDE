/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */
package com.myopicmobile.textwarrior.common;

import java.util.HashMap;
import java.util.*;

/**
 * Base class for programming language syntax.
 * By default, C-like symbols and operators are included, but not keywords.
 */
public abstract class Language
{
	public final static char EOF = '\uFFFF';
	public final static char NULL_CHAR = '\u0000';
	public final static char NEWLINE = '\n';
	public final static char BACKSPACE = '\b';
	public final static char TAB = '\t';
	public final static String GLYPH_NEWLINE = "\u21b5";
	public final static String GLYPH_SPACE = "\u00b7";
	public final static String GLYPH_TAB = "\u00bb";


	private final static char[] BASIC_C_OPERATORS = {
			'(', ')', '{', '}', '.', ',', ';', '=','+','-',
			'/', '*', '&', '!', '|', ':', '[', ']', '<', '>',
			'?', '~', '%', '^'
	};

	private String[] _keyword;
	private String[] _name;
	protected HashMap<String, Integer> _attrs = new HashMap<String, Integer>(0);
	protected HashMap<String, Integer> _css = new HashMap<String, Integer>(0);
	protected HashMap<String, Integer> _packNames = new HashMap<String, Integer>(0);
	protected HashMap<String, Integer> _names = new HashMap<String, Integer>(0);
	protected HashMap<String, String[]> _bases = new HashMap<String, String[]>(0);
	protected HashMap<String, String[]> _basesLower = new HashMap<String, String[]>(0);
	protected HashMap<String, Integer> _jsKeywordsMap = new HashMap<String, Integer>(0);
	protected HashMap<String, Integer> _htmlElementsMap = new HashMap<String, Integer>(0);
	protected HashMap<String, Integer> _phpElementsMap = new HashMap<String, Integer>(0);
	protected HashMap<String, Integer> _keywordsMap = new HashMap<String, Integer>(0);

	protected HashMap<Character, Integer> _operatorsMap = generateOperators(BASIC_C_OPERATORS);

	private ArrayList<String> _userCache = new ArrayList<String>();

	public String[] getBasePackage(String name) {
		return _basesLower.get(name);
	}

	public void addBasePackage(String name, String[] names) {
		_bases.put(name, names);

		_basesLower.put(name,names);
	}

	public void removeBasePackage(String name) {
		_bases.remove(name);
	}
	public void removeBasePackage() {
		_bases=new HashMap<String, String[]>(0);
	}
	public final boolean isBasePackage(String s) {
		return _basesLower.containsKey(s);
	}

	public final boolean isBaseWord(String p, String s) {
		String[] pkg = _bases.get(p);
		for (String n : pkg) {
			if (n.equals(s))
				return true;
		}
		return false;
	}

	protected void setOperators(char[] operators)
	{
		_operatorsMap = generateOperators(operators);
	}

	private HashMap<Character, Integer> generateOperators(char[] operators)
	{
		HashMap<Character, Integer> operatorsMap = new HashMap<Character, Integer>(operators.length);
		for (int i = 0; i < operators.length; ++i)
		{
			operatorsMap.put(operators[i], Lexer.OPERATOR);
		}
		return operatorsMap;
	}

	public final boolean isOperator(char c)
	{
		return _operatorsMap.containsKey(Character.valueOf(c));
	}

	public String[] getKeywords()
	{
		return _keyword;
	}
	public final boolean isKeyword(String s)
	{
		return _keywordsMap.containsKey(s);
	}
	public final boolean isJsKeyword(String s)
	{
		return _jsKeywordsMap.containsKey(s);
	}
	public final boolean isHtmlElement(String s)
	{
		return _htmlElementsMap.containsKey(s);
	}
	public final boolean isPHPKeywords(String s)
	{
		return _phpElementsMap.containsKey(s);
	}
	public final boolean isName(String s)
	{
		return _names.containsKey(s);
	}
	public final boolean isCss(String s)
	{
		return _css.containsKey(s);
	}
	public final boolean isAttr(String s)
	{
		return _attrs.containsKey(s);
	}
	public final boolean isPackName(String s)
	{
		return _packNames.containsKey(s);
	}
	public void setKeywords(String[] keywords)
	{
		_keyword = new String[keywords.length];
		for(int i=0;i<keywords.length;i++){
			_keyword[i]=keywords[i];
		}

		_keywordsMap = new HashMap<String, Integer>(keywords.length);
		for (int i = 0; i < keywords.length; ++i)
		{
			_keywordsMap.put(keywords[i], Lexer.KEYWORD);
		}
	}
	public void setJsKeywords(Map map) {
		_jsKeywordsMap=(HashMap<String, Integer>) map;
	}
	public void setHtmlElements(Map map) {
		_htmlElementsMap=(HashMap<String, Integer>) map;
	}
	public void setPHPKeywords(Map map) {
		_phpElementsMap=(HashMap<String, Integer>) map;
	}
	public String[] getNames() {
		return _name;
	}
	public HashMap<String,Integer> getNamesMap()
	{
		return _names;
	}
	public HashMap<String,Integer> getCssMap()
	{
		return _css;
	}
	public void setNames(String[] names) {
		_name = names;
		ArrayList<String> buf = new ArrayList<String>();
		_names = new HashMap<String, Integer>(names.length);
		for (int i = 0; i < names.length; ++i) {
			if (!buf.contains(names[i]))
				buf.add(names[i]);
			_names.put(names[i], Lexer.NAME);
		}
		_name = new String[buf.size()];
		buf.toArray(_name);
	}
	public void setCss(Map map) {
		_css=(HashMap<String, Integer>) map;
	}
	public void setAttrs(Map map) {
		_attrs=(HashMap<String, Integer>) map;
	}
	public void setPackNames(Map map) {
		_packNames=(HashMap<String, Integer>) map;
	}
	private boolean contains(String[] a, String s)
	{
		for (String n:a)
		{
			if (n.equals(s))
				return true;
		}
		return false;
	}

	private boolean contains(ArrayList<String> a, String s)
	{
		for (String n:a)
		{
			if (n.equals(s))
				return true;
		}
		return false;
	}

	/**
	 * 空白符
	 * @param c
	 * @return
	 */
	public boolean isWhitespace(char c)
	{
		return (c == ' ' || c == '\n' || c == '\t' ||
				c == '\r' || c == '\f' || c == EOF);
	}

	/**
	 * 点运算符
	 * @param c
	 * @return
	 */
	public boolean isSentenceTerminator(char c)
	{
		return (c == '.');
	}

	/**
	 * 斜杠
	 * @param c
	 * @return
	 */
	public boolean isEscapeChar(char c)
	{
		return (c == '\\');
	}

	/**
	 * Derived classes that do not do represent C-like programming languages
	 * should return false; otherwise return true
	 */
	public boolean isProgLang()
	{
		return true;
	}

	/**
	 * Whether the word after c is a token
	 */
	public boolean isWordStart(char c)
	{
		return false;
	}

	/**
	 * Whether cSc is a token, where S is a sequence of characters that are on the same line
	 * 字符串引号
	 */
	public boolean isDelimiterA(char c)
	{
		return (c == '"');
	}

	/**
	 * Same concept as isDelimiterA(char), but Language and its subclasses can
	 * specify a second type of symbol to use here
	 * 单个字符引号
	 */
	public boolean isDelimiterB(char c)
	{
		return (c == '\'');
	}

	/**
	 * Whether cL is a token, where L is a sequence of characters until the end of the line
	 * 宏定义
	 */
	public boolean isLineAStart(char c)
	{
		return (c == '#');
	}

	/**
	 * Same concept as isLineAStart(char), but Language and its subclasses can
	 * specify a second type of symbol to use here
	 */
	public boolean isLineBStart(char c)
	{
		return false;
	}

	/**
	 * Whether c0c1L is a token, where L is a sequence of characters until the end of the line
	 * 单行注释
	 */
	public boolean isLineStart(char c0, char c1)
	{
		return (c0 == '/' && c1 == '/');
	}

	/**
	 * Whether c0c1 signifies the start of a multi-line token
	 * 多行注释开始
	 */
	public boolean isMultilineStartDelimiter(char c0, char c1)
	{
		return (c0 == '/' && c1 == '*');
	}

	/**
	 * Whether c0c1 signifies the end of a multi-line token
	 * 多行注释结束
	 */
	public boolean isMultilineEndDelimiter(char c0, char c1)
	{
		return (c0 == '*' && c1 == '/');
	}
}
