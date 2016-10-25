package de.spinfo.uni_koeln.html_parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

import de.spinfo.uni_koeln.html_parser.data.RawEntry;
import de.spinfo.uni_koeln.html_parser.parser.ParseHtml;
import de.spinfo.uni_koeln.html_parser.utils.FileUtils;

public class ParseHtmlTest {

	ParseHtml parser = new ParseHtml();

	// WORKFLOW

	// 1. mark uncorrected entries
	// 2. modifyTags
	// 3. addEntryTags
	// 4. formatEntries
	@Ignore
	@Test
	public void doTheFirstThreeSteps() throws IOException {

		List<String> firstIdentation = parser.markUncorrectedEntries(ParseHtml.input_dir + ParseHtml.doc);

		List<String> modified = parser.modifyTags(firstIdentation);

		List<String> markedentries = parser.addEntryTags(modified);

		FileUtils.printList(markedentries, ParseHtml.input_dir, "markedEntries", "html");

	}

	// Do the final one
	@Ignore
	@Test
	public void testFormatEntries() throws IOException {

		File f = new File(ParseHtml.input_dir + ParseHtml.markedLemmata);

		List<RawEntry> rawEntries = parser.formatEntries(f);

		System.out.println(rawEntries.size());

		// int max_length_content = parser.getMaxLengthContent(rawEntries);

		// Get the longest string in content
		// System.out.println(max_length_content);
		//
		FileUtils.printList(rawEntries, ParseHtml.output_dir, "rawEntries", "txt");

	}

	// Get a List of the lemmata
	@Ignore
	@Test
	public void testGetLemmata() throws IOException {

		File f = new File(ParseHtml.input_dir + ParseHtml.markedLemmata);

		List<RawEntry> rawEntries = parser.formatEntries(f);

		// System.out.println(rawEntries.size());

		List<String> lemmata = parser.getLemmata(rawEntries);

		FileUtils.printList(lemmata, ParseHtml.output_dir, "lemmata_cleaned", "txt");

	}

	@Ignore
	@Test
	public void getPageMappingTest() throws IOException {

		Map<String, List<Integer>> map = parser.getPageMapping(ParseHtml.input_dir + "20150898_mappedentries.txt");

		FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

	@Ignore
	@Test
	public void getAllFontTypes() throws IOException {

		Set<String> myset = parser.getAllFontTypes(ParseHtml.input_dir + ParseHtml.doc);

		for (String s : myset) {

			System.out.println(s);

		}

		// FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

	@Test
	@Ignore
	public void backwards() throws IOException {

		List<String> list = Files.readAllLines(new File(ParseHtml.input_dir + "lemmata.txt").toPath(),
				Charset.defaultCharset());

		Collections.reverse(list);

		FileUtils.printList(list, ParseHtml.output_dir, "lemmata_rev", "txt");

	}

	@Ignore
	@Test
	public void testReplace() {

		String s = "<font style=\"font-size: medium;font-family: Constantia;font-weight: bold;\">";
		s = s.replaceAll("<font style=\"font-size: medium;", "<font size=\"3\" style=\"");

		System.out.println(s);

	}

	@Test
	public void testJSOUP() {

		String s = "<font face=\"Constantia\" >  casa de chile<font face=\"Constantia\" > camarones salteados </font> </font>";

		Document doc = Jsoup.parse(s);

		Elements fonts = doc.getElementsByTag("font");

		for (org.jsoup.nodes.Element e : fonts) {

			String currentElement = e.outerHtml();
			System.out.println("0: " + currentElement);

			// Default: if size not set, check if the parent is set to 2. If
			// not, set all to 3

			if (e.attr("size").equals("") && !e.parent().attr("size").equals("2") || !e.attr("size").equals("2")
					|| !e.attr("size").equals("2")) {

				e.attr("size", "3");
				System.out.println("1: " + e);

			}

			// if parent set to 2, but children are 3
			if (e.attr("size").equals("3") && e.parent().attr("size").equals("2")) {

				e.attr("size", "2");
				System.out.println("2: " + e);
			}

		}

		System.out.println(doc.body().html());
	}
}
