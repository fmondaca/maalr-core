package de.spinfo.uni_koeln.html_parser.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.spinfo.uni_koeln.html_parser.data.Entry;
import de.spinfo.uni_koeln.html_parser.data.RawEntry;
import de.spinfo.uni_koeln.html_parser.utils.FileUtils;

public class ParseHtml {

	public static String doc = "lenz-20150625.htm";
	public static String addenda = "lenz-20150625_addenda.html";
	public static String markedLemmata = "markedEntries_2015-08-21T17:44:34Z.html";
	public static String markedLemmata_add = "markedEntries_2015-08-07T16:32:04Z.html";

	private static String pathToReplace = "../maalr.lenz/toReplace.tab";

	public static String output_dir = "data/output/";
	public static String input_dir = "data/input/";

	private String bold = "<font style=\"font-size:medium;font-family:Constantia;font-weight:bold;\">";
	private String bold_serif = "<font style=\"font-size:medium;font-family:Constantia, serif;font-weight:bold;\">";
	private String bold_italic = "<font style=\"font-size:medium;font-family:Constantia, serif;font-weight:bold;font-style:italic;\">";
	private String bold_italic_b = "<font style=\"font-size:medium;font-family:Constantia, serif;font-style:italic;font-weight:bold;\">";
	private String normal = "<font style=\"font-size:medium;font-family:Constantia;\">";
	private String normal_serif = "<font style=\"font-size:medium;font-family:Constantia, serif;\">";
	private String normal_serif_italic = "<font style=\"font-size:medium;font-family:Constantia, serif;font-style:italic;\">";
	private String normal_italic = "<font style=\"font-size:medium;font-family:Constantia;font-style:italic;\">";
	private String doulos_normal = "<font style=\"font-size:medium;font-family:Doulos SIL, fantasy;\">";
	private String doulos_italic = "<font style=\"font-size:medium;font-family:Doulos SIL, fantasy;font-style:italic;\">";
	private String small_italic_serif = "<font style=\"font-size:85%;font-family:Constantia, serif;font-style:italic;\">";
	private String small_italic = "<font style=\"font-size:85%;font-family:Constantia;font-style:italic;\">";
	private String small_normal_serif = "<font style=\"font-size:85%;font-family:Constantia, serif;\">";
	private String small_bold_serif = "<font style=\"font-size:85%;font-family:Constantia, serif;font-weight:bold;\">";
	private String small_bold_serif_italic = "<font style=\"font-size:85%;font-family:Constantia, serif;font-weight:bold;font-style:italic;\">";
	private String small_normal = "<font style=\"font-size:85%;font-family:Constantia;\">";
	private String font_end = "</font>";
	private String head_start = "<h3>";
	private String head_end = "</h3>";
	private String p_start = "<p>";
	private String p_end = "</p>";

	private boolean found = false;

	public Set<String> getTags() {

		Set<String> tags = new HashSet<>();
		tags.add("<no>");
		tags.add("<ni>");
		tags.add("<bo>");
		tags.add("<bi>");
		tags.add("<sn>");
		tags.add("<si>");
		tags.add("<sb>");
		tags.add("<se>");
		tags.add("<dn>");
		tags.add("<di>");
		tags.add("<en>");

		return tags;
	}

	public Set<String> getMarkedEntriesTags() {

		Set<String> tags = new HashSet<>();
		tags.add("<p>");
		tags.add("<h3>");
		tags.add("<en>");

		return tags;
	}

	public List<String> markUncorrectedEntries(String fileToParse)
			throws IOException {
		List<String> list = new ArrayList<>();

		File toRead = new File(fileToParse);
		FileInputStream inputStream = new FileInputStream(toRead);
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "UTF8");

		LineNumberReader reader = new LineNumberReader(inputStreamReader);
		String line;

		final Pattern pattern = Pattern
				.compile("<h2>(.+?)</h2>|<h3>(.+?)</h3>|<h4>(.+?)</h4>|<h5>(.+?)</h5>");

		while ((line = reader.readLine()) != null) {

			// Normalize input
			line = Normalizer.normalize(line, Normalizer.Form.NFC);

			final Matcher matcher = pattern.matcher(line);

			if (matcher.find()) {
				// if already at the beginning of the line, do nothing
				if (line.startsWith(matcher.group())) {

					// add to the list
					list.add(line);
					// go to next line
					continue;

				}

				// delete founded string from line
				line = line.replace(matcher.group(), "");
				// add to the list
				list.add(line);
				// add founded string
				list.add(matcher.group());

			} else {

				list.add(line);
			}

		}

		reader.close();

		return list;

	}

	public List<String> modifyTags(List<String> lines) throws IOException {

		List<String> modified = new ArrayList<>();

		Set<String> fonts = getAllFontTypes(input_dir + doc);
		fonts.remove("Constantia");
		fonts.remove("Doulos SIL");

		for (String line : lines) {

			// add a space between a colon ':'
			line = line.replace(":", ": ");
			// change 85% to small
			line = line.replace("85%", "small");
			line = line.replace("large", "medium");

			// delete unwanted tags
			line = line.replaceAll("<a href=(.+?)\">", "").replaceAll("</a>",
					"");
			line = line.replaceAll("<a name=(.+?)\">", "");
			line = line.replaceAll("<h1>", "").replaceAll("</h1>", "");
			line = line.replaceAll("<h2>", "").replaceAll("</h2>", "");
			line = line.replaceAll("<h3>", "").replaceAll("</h3>", "");
			line = line.replaceAll("<h4>", "").replaceAll("</h4>", "");
			line = line.replaceAll("<h5>", "").replaceAll("</h5>", "");

			// font-variant: small-caps;
			// font-variant: large-caps;

			line = line.replace("font-variant: large-caps;", "");
			line = line.replace("font-variant: small-caps;", "");

			// making it like breaks from the editor
			line = line.replace("<br clear=\"all\"/>", "<br clear=\"all\">");

			for (String s : fonts) {

				line = line.replaceAll(
						"<font style=\"font-size: medium;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"3\">");
				line = line.replaceAll(
						"<font style=\"font-size: large;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"3\">");
				line = line.replaceAll(
						"<font style=\"font-size: x-large;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"3\">");
				line = line.replaceAll(
						"<font style=\"font-size: xx-large;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"3\">");

				line = line.replaceAll(
						"<font style=\"font-size: xx-small;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"2\">");
				line = line.replaceAll(
						"<font style=\"font-size: x-small;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"2\">");
				line = line.replaceAll(
						"<font style=\"font-size: small;font-family: " + s
								+ "(.+?)\">",
						"<font face=\"Constantia\" size=\"2\">");

			}
			// System.out.println(line);
			modified.add(line);

		}

		return modified;

	}

	public List<String> addEntryTags(List<String> toProcess) throws IOException {
		List<String> lemmas = new ArrayList<>();
		List<String> list = new ArrayList<String>();
		int counter = 0;
		for (String s : toProcess) {

			final Pattern pattern = Pattern
					.compile("<font style=\"font-size: medium;font-family: Constantia, serif;font-weight: bold;\">(.+?)</font>");
			final Matcher matcher = pattern.matcher(s);

			if (matcher.find()) {
				String found = matcher.group(1);
				found = found
						.replaceAll(
								"<font style=\"font-size: medium;font-family: Constantia, serif;font-weight: bold;\">",
								"").replaceAll("&nbsp;", "");

				// if it's the start of an entry
				if (Character.isDigit(found.charAt(0))) {
					counter++;
					StringBuffer buffer = new StringBuffer();
					buffer.append("\n");
					buffer.append("<en>");
					buffer.append(found);
					buffer.append("</en>");
					// buffer.append("\n");
					list.add(buffer.toString());
					lemmas.add(found);

				} else {
					StringBuffer buffer = new StringBuffer();
					buffer.append(s);
					buffer.append("\t");
					// buffer.append("\n");
					list.add(buffer.toString());

				}

			} else {
				StringBuffer buffer = new StringBuffer();
				buffer.append(s);
				buffer.append("\t");
				// buffer.append("\n");
				list.add(buffer.toString());
			}

		}

		System.out.println(counter);

		FileUtils.printList(lemmas, ParseHtml.output_dir, "lemmata", "txt");

		return list;
	}

	public Map<String, List<Integer>> getPageMapping(String file)
			throws IOException {

		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

		File toRead = new File(file);
		FileInputStream inputStream = new FileInputStream(toRead);
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "UTF8");
		LineNumberReader reader = new LineNumberReader(inputStreamReader);
		String line;

		while ((line = reader.readLine()) != null) {

			String[] array = line.split("\t");
			System.out.println(array[0] + "\t" + array[1]);

			String[] pages = array[1].split("\\.\\s?");

			System.out.println("pages size" + pages.length);
			List<Integer> pagesList = new ArrayList<>();

			if (pages.length > 1) {
				for (String i : pages) {

					pagesList.add(Integer.parseInt(i));
					System.out.println("pages " + i);
				}
			} else {

				pagesList.add(Integer.parseInt(array[1]));

			}

			map.put(array[0], pagesList);

		}

		reader.close();

		return map;
	}

	public Set<String> getAllFontTypes(String fileToParse) throws IOException {
		Set<String> set = new HashSet<>();
		File toRead = new File(fileToParse);
		FileInputStream inputStream = new FileInputStream(toRead);
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "UTF8");
		LineNumberReader reader = new LineNumberReader(inputStreamReader);
		String line;

		while ((line = reader.readLine()) != null) {

			final Pattern pattern = Pattern.compile("font-family:(.+?)(,|;)");
			final Matcher matcher = pattern.matcher(line);

			while (matcher.find()) {
				set.add(matcher.group(1));
			}

		}
		reader.close();
		return set;

	}

	public List<RawEntry> identateEntries(File toRead) throws IOException {
		List<RawEntry> entries = new ArrayList<>();
		FileInputStream inputStream = new FileInputStream(toRead);
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, "UTF8");
		LineNumberReader reader = new LineNumberReader(inputStreamReader);
		String line;

		Map<String, List<Integer>> map = getPageMapping(ParseHtml.input_dir
				+ "20150898_mappedentries.txt");

		while ((line = reader.readLine()) != null) {

			if (line.startsWith("<en>")) {

				RawEntry e = new RawEntry();
				// line = line.replaceAll("<en>", "").replaceAll("</en>", "");
				e.setLemma(line);

				StringBuffer content = new StringBuffer();
				while ((line = reader.readLine()) != null) {

					if (!line.equals("")) {

						for (String s : getMarkedEntriesTags()) {

							if (line.startsWith(s)) {

								found = true;

							}

						}

						if (found == true) {
							found = false;

							// line = replaceTags(line);
							content.append(line);
							content.append("\n");

						} else {
							if (content.length() > 1)
								// Delete linebreak
								content.setLength(content.length() - 1);
							// Append current line to the previous one
							// line = replaceTags(line);
							content.append(line);
							content.append("\n");

						}

					} else {
						e.setContent(content.toString());

						// Add page mapping
						String s = e.getLemma();
						s = s.replace("<en>", "").replace("</en>", "");
						String[] t = s.split("\\s+");
						StringBuffer buffer = new StringBuffer();

						// We skip the entry's number
						// buffer.append(t[0]);
						// buffer.append("\t");

						for (int i = 1; i < t.length; i++) {

							buffer.append(t[i]);
							buffer.append(" ");
						}

						String lemma = buffer.toString().trim();

						for (String l : map.keySet()) {

							if (lemma.equals(l.trim())) {

								e.setPages(map.get(l));

							}

						}

						entries.add(e);
						break;
					}

				}

			}

		}

		reader.close();

		return entries;
	}



	public List<String> getLemmata(List<RawEntry> rawEntries) {

		List<String> lemmata = new ArrayList<>();

		for (RawEntry e : rawEntries) {
			String s = e.getLemma();
			s = s.replace("<en>", "").replace("</en>", "");
			String[] t = s.split("\\s+");
			StringBuffer buffer = new StringBuffer();

			buffer.append(t[0]);
			buffer.append("\t");

			for (int i = 1; i < t.length; i++) {

				buffer.append(t[i]);
				buffer.append(" ");
			}

			lemmata.add(buffer.toString());
		}
		return lemmata;

	}

}