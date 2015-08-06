/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.maalr.services.admin.shared;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.login.LoginManager;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;

@Service
public class DataLoader {

	@Autowired
	private Index index;

	@Autowired
	private LoginManager loginManager;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void createFromSQLDump(File file, int maxEntries)
			throws IOException, NoDatabaseAvailableException,
			InvalidEntryException, IndexException {

		if (!file.exists()) {
			logger.info("No data to import - file " + file + " does not exist.");
			return;
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(fis, "UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LineNumberReader reader = new LineNumberReader(isr);

		String line;
		Database db = Database.getInstance();
		List<DBObject> entries = new ArrayList<DBObject>();
		int counter = 0;
		String userId = loginManager.getCurrentUserId();

		while ((line = reader.readLine()) != null) {

			// Normalize text
			line = Normalizer.normalize(line, Normalizer.Form.NFC);

			LemmaVersion version = new LemmaVersion();

			if (line.startsWith("<en>")) {

				version = new LemmaVersion();

				line = line.replace("<en>", "").replace("</en>", "");

				version.setValue("Lemma", line);
				version.setValue("Lemma_txt", getTextFromHTML(line));

				StringBuffer content = new StringBuffer();

				while ((line = reader.readLine()) != null) {

					// Normalize text
					line = Normalizer.normalize(line, Normalizer.Form.NFC);

					if (!line.startsWith("</c>")) {

						line = line.replace("<c>", "");
						content.append(line);
						content.append("\n");
					} else {
						version.setValue("Content", content.toString());
						version.setValue("Content_txt",
								getTextFromHTML(content.toString()));

						break;
					}

				}

				if (counter <= 1000) {
					version.setValue("Correction", "95");
				} else {
					version.setValue("Correction", "15");
				}

				LexEntry entry = new LexEntry(version);
				System.out.println(entry.toString());
				entry.setCurrent(version);
				entry.getCurrent().setStatus(Status.NEW_ENTRY);
				entry.getCurrent().setVerification(Verification.ACCEPTED);
				long timestamp = System.currentTimeMillis();
				String embeddedTimeStamp = version
						.getEntryValue(LemmaVersion.TIMESTAMP);
				if (embeddedTimeStamp != null) {
					timestamp = Long.parseLong(embeddedTimeStamp);
					version.removeEntryValue(LemmaVersion.TIMESTAMP);
				}
				entry.getCurrent().setUserId(userId);
				entry.getCurrent().setTimestamp(timestamp);
				entry.getCurrent().setCreatorRole(Role.ADMIN_5);
				entries.add(Converter.convertLexEntry(entry));

				System.out.println(version.toString());
				if (entries.size() == 100) {
					db.insertBatch(entries);
					entries.clear();
				}
				counter++;
				if (counter == maxEntries) {
					logger.warn("Skipping db creation, as max entries is "
							+ maxEntries);
					break;
				}
			}

		}
		db.insertBatch(entries);

		System.out.println(entries.size());
		logger.info("ENTRIES SIZE: ", entries.size());
		entries.clear();

		// loginManager.login("admin", "admin");
		Iterator<LexEntry> iterator = db.getEntries();
		index.dropIndex();
		index.addToIndex(iterator);
		logger.info("Index has been created, swapping to RAM...");
		index.reloadIndex();
		logger.info("RAM-Index updated.");
		reader.close();
		// if (zipFile != null) {
		// zipFile.close();
		// }
		// loginManager.logout();
		logger.info("Dataloader initialized.");
	}

	private String getTextFromHTML(String stringWithHTML) throws IOException {

		InputStream is = new ByteArrayInputStream(stringWithHTML.getBytes());
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		try {
			new HtmlParser().parse(is, handler, metadata, new ParseContext());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String plainText = handler.toString();

		return plainText;

	}

}
