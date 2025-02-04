package com.hiweb.ide;

import com.hiweb.ide.edit.Do;
import java.io.File;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class MDUIManager extends LibraryManager {
	public MDUIManager() {
		super("mdui");
	}

	// @Override
	// public void download(String ver)
	// {
	// url="https://www.mdui.org/source/mdui-v"+ver+".zip";
	// super.download(ver);
	// }

	@Override
	public String regTheHtml(File now, Document doc, File libFolder) {
		String cssPath = Do.getRelativePath(now.getPath(), libFolder.getPath() + "/css/mdui.min.css");
		String jsPath = Do.getRelativePath(now.getPath(), libFolder.getPath() + "/js/mdui.min.js");

		Attributes attrsCss = new Attributes();
		attrsCss.put(new Attribute("rel", "stylesheet"));
		attrsCss.put(new Attribute("href", cssPath));
		Element css = new Element(Tag.valueOf("link"), "file://" + now.getParent(), attrsCss);

		Attributes attrsJs = new Attributes();
		attrsJs.put(new Attribute("src", jsPath));
		Element js = new Element(Tag.valueOf("script"), "file://" + now.getParent(), attrsJs);

		if (!doc.head().children().contains(css)) {
			doc.head().appendChild(css);
		}

		if (!doc.head().children().contains(js)) {
			doc.head().appendChild(js);
		}

		return doc.html();
	}
}
