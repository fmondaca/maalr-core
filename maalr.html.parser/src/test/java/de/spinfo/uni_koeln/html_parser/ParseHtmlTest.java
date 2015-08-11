package de.spinfo.uni_koeln.html_parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.spinfo.uni_koeln.html_parser.data.RawEntry;
import de.spinfo.uni_koeln.html_parser.run.ParseHtml;
import de.spinfo.uni_koeln.html_parser.utils.FileUtils;

public class ParseHtmlTest {

	ParseHtml parser = new ParseHtml();

	// WORKFLOW

	// 1. modifyTags
	// 2. addEntryTags
	// 3. identateEntries

	@Test
	public void doOneAndTwo() throws IOException {

		List<String> modified = parser.modifyTags(ParseHtml.input_dir
				+ ParseHtml.doc);
		List<String> markedentries = parser.addEntryTagsfromList(modified);

		FileUtils.printList(markedentries, ParseHtml.input_dir,
				"markedEntries", "html");

	}

	// THREE

	// @Ignore
	@Test
	public void testIdentateEntries() throws IOException {

		File f = new File(ParseHtml.input_dir + ParseHtml.markedLemmata);

		List<RawEntry> rawEntries = parser.identateEntries(f);

		for (RawEntry e : rawEntries) {

			if (e.getPages() == null) {

				System.out.println(e.getLemma());
			}
		}

		FileUtils.printList(rawEntries, ParseHtml.output_dir, "rawEntries_",
				"txt");

		// List<Entry> entries = parser.getEntries(rawEntries);

	}

	// @Ignore
	@Test
	public void testAddEntryTags() throws IOException {

		parser.addEntryTags(ParseHtml.input_dir + ParseHtml.doc);

	}

	@Test
	public void getPageMappingTest() throws IOException {

		Map<String, List<Integer>> map = parser
				.getPageMapping(ParseHtml.input_dir
						+ "20150898_mappedentries.txt");

		FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

	@Test
	public void modifyTagsTest() throws IOException {

		parser.modifyTags(ParseHtml.input_dir + ParseHtml.doc);

		// FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

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
