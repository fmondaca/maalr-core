package de.uni_koeln.spinfo.maalr.lucene.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestEscapeHTML {

	@Test
	public void testEscapeHTML() throws IOException, SAXException,
			TikaException {

		String toEscape = "<p><font style=\"font-size:medium;font-family:Constantia;font-weight:bold;\">achuchúi, </font>";

		String escaped = StringEscapeUtils.escapeHtml4(toEscape);
		System.out.println(escaped);

		// InputStream input = new FileInputStream("myfile.html");

		InputStream is = new ByteArrayInputStream(toEscape.getBytes());
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		new HtmlParser().parse(is, handler, metadata, new ParseContext());
		String plainText = handler.toString();
		System.out.println(plainText);

	}

}
