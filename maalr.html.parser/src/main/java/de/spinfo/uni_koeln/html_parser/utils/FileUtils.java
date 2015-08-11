package de.spinfo.uni_koeln.html_parser.utils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtils {

	private FileUtils() {
		throw new AssertionError();
	}

	public static String outputPath = "../data/output";
	public static String inputPath = "../data/input";

	public static <T> void writeList(List<T> matchedWords, String fileName)
			throws IOException {

		ObjectOutputStream outputStream = new ObjectOutputStream(
				new FileOutputStream(outputPath + fileName
						+ getISO8601StringForCurrentDate()));

		outputStream.writeObject(matchedWords);

		outputStream.close();

	}

	public static <K, V> File printMap(Map<K, V> map, String destPath,
			String fileName) throws IOException {

		File file = new File(destPath + fileName
				+ getISO8601StringForCurrentDate() + ".txt");
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Map.Entry<K, V> entry : map.entrySet()) {
			out.append(entry.getKey() + " : " + entry.getValue());
			out.append("\n");
		}

		out.flush();
		out.close();

		return file;
	}

	public static <T> File printSet(Set<T> set, String destPath, String filename)
			throws IOException {

		File file = new File(destPath + filename
				+ getISO8601StringForCurrentDate() + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Object o : set) {
			writer.append(o + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public static <T> File printList(List<T> list, String destPath,
			String filename, String fileType) throws IOException {

		File file = new File(destPath + filename + "_"
				+ getISO8601StringForCurrentDate() + "." + fileType);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Object o : list) {
			writer.append(o + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static String getISO8601StringForCurrentDate() {
		Date now = new Date();
		return getISO8601StringForDate(now);
	}

	private static String getISO8601StringForDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMANY);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		return dateFormat.format(date);
	}

}
