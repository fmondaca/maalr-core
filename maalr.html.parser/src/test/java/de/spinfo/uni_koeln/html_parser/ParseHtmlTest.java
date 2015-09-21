package de.spinfo.uni_koeln.html_parser;

import java.io.File;
import java.io.IOException;
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

		List<String> firstIdentation = parser
				.markUncorrectedEntries(ParseHtml.input_dir + ParseHtml.doc);

		List<String> modified = parser.modifyTags(firstIdentation);

		List<String> markedentries = parser.addEntryTags(modified);

		FileUtils.printList(markedentries, ParseHtml.input_dir,
				"markedEntries", "html");

	}

	// Do the final one
	// @Ignore
	@Test
	public void testFormatEntries() throws IOException {

		File f = new File(ParseHtml.input_dir + ParseHtml.markedLemmata);

		List<RawEntry> rawEntries = parser.formatEntries(f);
		
		System.out.println(rawEntries.size());

		// int max_length_content = parser.getMaxLengthContent(rawEntries);

		// Get the longest string in content
		// System.out.println(max_length_content);
//
//		FileUtils.printList(rawEntries, ParseHtml.output_dir,
//				"rawEntries", "txt");

	}

	@Ignore
	@Test
	public void getPageMappingTest() throws IOException {

		Map<String, List<Integer>> map = parser
				.getPageMapping(ParseHtml.input_dir
						+ "20150898_mappedentries.txt");

		FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

	@Ignore
	@Test
	public void getAllFontTypes() throws IOException {

		Set<String> myset = parser.getAllFontTypes(ParseHtml.input_dir
				+ ParseHtml.doc);

		for (String s : myset) {

			System.out.println(s);

		}

		// FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

	
}
