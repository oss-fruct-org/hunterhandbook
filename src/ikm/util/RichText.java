package ikm.util;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

interface Renderer {
	void drawSubstring(String str, int from, int length, int x, int y);
}

public class RichText {
	private int width;
	private Vector lines = new Vector();
	private Vector fonts = new Vector();
	
	public RichText(int width) {
		this.width = width;
	}
	
	// Freeze if word longer than line
	public void addText(String text, Font font) {
		int p = 0;
		int dotWidth = font.charWidth('.');

		if (text.length() == 0) {
			lines.addElement("");
			fonts.addElement(font);
			return;
		}
		
		while (p < text.length()) {
			int remainChars = width / dotWidth;
			if (remainChars + p >= text.length())
				remainChars = text.length() - p;

			int approxWidth = font.substringWidth(text, p, remainChars);

			for (;remainChars > 0; remainChars--) {
				char c = text.charAt(p + remainChars - 1);
				if ((c == ' ' || p + remainChars == text.length()) && approxWidth < width) {
					//r.drawSubstring(text, p, remainChars, x, y);
					lines.addElement(text.substring(p, p + remainChars));
					fonts.addElement(font);
					p += remainChars;
					break;
				}
				approxWidth -= font.charWidth(c);
			}
		}
	}
	
	public void addFormattedText(String str, Font normalFont, Font boldFont) {
		boolean tagOpen = false;
		int tagStart = 0;
		int blockStart = 0;

		String block = null;
		Font font = normalFont;
		
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (tagOpen) {
				if (c == '>') {
					String tag = str.substring(tagStart, i + 1);
					tagOpen = false;
					
					if (blockStart < tagStart || "<br/>".equals(tag)) {
						block = str.substring(blockStart, tagStart);
					}
					blockStart = i + 1;
					
					if ("<b>".equals(tag)) {
					} else if ("</b>".equals(tag)) {
						font = boldFont;
					}
				}
			} else {
				if (c == '\n') {
					if (blockStart < i) {
						block = str.substring(blockStart, i);
						font = normalFont;
					}
					blockStart = i + 1;
				} else if (c == '<') {
					tagOpen = true;
					tagStart = i;
				}
			}
			
			if (block != null) {
				addText(block, font);
				block = null;
				font = normalFont;
			}
		}
		
		if (blockStart < str.length()) {
			String text = str.substring(blockStart);
			addText(text, font);
		}
	}
	
	public void draw(Graphics g) {
		int y = 0;

		int i = 0;
		for (Enumeration en = lines.elements(); en.hasMoreElements();) {
			Font font = (Font) fonts.elementAt(i++);
			g.setFont(font);
			g.drawString((String) en.nextElement(), 0, y, Graphics.TOP | Graphics.LEFT);
			y += font.getHeight();
		}
	}
	
	public int getLineCount() {
		return lines.size();
	}
}
