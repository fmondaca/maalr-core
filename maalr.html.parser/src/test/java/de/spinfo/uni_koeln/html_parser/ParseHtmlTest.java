package de.spinfo.uni_koeln.html_parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.spinfo.uni_koeln.html_parser.data.RawEntry;
import de.spinfo.uni_koeln.html_parser.run.ParseHtml;
import de.spinfo.uni_koeln.html_parser.utils.FileUtils;

public class ParseHtmlTest {

	ParseHtml parser = new ParseHtml();

	// @Ignore
	@Test
	public void testAddEntryTags() throws IOException {

		parser.addEntryTags(ParseHtml.input_dir + ParseHtml.doc);

	}

	// @Ignore
	@Test
	public void testIdentateEntries() throws IOException {

		List<RawEntry> rawEntries = parser.identateEntries(ParseHtml.input_dir
				+ ParseHtml.markedLemmata);
		
		
		for (RawEntry e : rawEntries){
			
			if (e.getPages()== null){
				
				System.out.println(e.getLemma());
			}
		}

		FileUtils.printList(rawEntries, ParseHtml.output_dir, "rawEntries_");

		// List<Entry> entries = parser.getEntries(rawEntries);

	}

	@Test
	public void getPageMappingTest() throws IOException {

		Map<String, List<Integer>> map = parser
				.getPageMapping(ParseHtml.input_dir
						+ "20150898_mappedentries.txt");

		FileUtils.printMap(map, ParseHtml.output_dir, "mapping");

	}

}
