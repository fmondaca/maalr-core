package de.spinfo.uni_koeln.html_parser.data;

import java.io.Serializable;
import java.util.List;

public class RawEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lemma;

	private List<Integer> pages;

	private String content;

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer();

		buffer.append(lemma);
		buffer.append("\n");
		buffer.append("<c>");
		buffer.append(content);
		buffer.append("</c>");
		buffer.append("\n");
		buffer.append("<p>");
		buffer.append(pages);
		buffer.append("</p>");

		return buffer.toString();
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}
}
