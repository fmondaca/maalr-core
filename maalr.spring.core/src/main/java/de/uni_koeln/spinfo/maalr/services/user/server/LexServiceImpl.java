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
package de.uni_koeln.spinfo.maalr.services.user.server;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.MaalrException;
import de.uni_koeln.spinfo.maalr.mongo.SpringBackend;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.services.user.shared.LexService;

@Service("lexService" /* Don't forget to add new services to gwt-servlet.xml! */)
public class LexServiceImpl implements LexService {

	private Whitelist whiteList = Configuration.getInstance().getWhiteList();

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SpringBackend db;

	@Override
	public String suggestNewEntry(LemmaVersion entry) throws MaalrException {
		try {
			LexEntry le = new LexEntry(entry);
			db.suggestNewEntry(le);
			return null;
		} catch (Exception e) {
			logger.error("Failed to suggest new entry", e);
			// throw new MaalrException("Failed to suggest new entry: " + e);
			throw new MaalrException(e.getMessage());
		}
	}

	@Override
	public String suggestModification(LemmaVersion entry, Map<String, String> toUpdate) throws MaalrException {

		String newHeader = toUpdate.get("header").trim();
		String newBody = toUpdate.get("body").trim();
		String newCorrection = toUpdate.get("correction").trim();
		String actualCorrection = entry.getEntryValue("correction").trim();

		// Check size attr
		newHeader = checkSize(newHeader);
		newBody = checkSize(newBody);

		// Clean input
		newHeader = Jsoup.clean(newHeader, whiteList);
		newBody = Jsoup.clean(newBody, whiteList);
		newCorrection = Jsoup.clean(newCorrection, whiteList);
		int correction = Integer.parseInt(newCorrection);

		if ((newCorrection.equals(actualCorrection))) {
			throw new MaalrException("dialog.nocorrection");
		} else if (correction > 100 || correction <= 15) {
			throw new MaalrException("correction.wrongvalue");
		} else {
			entry = updateEntryValues(entry, newHeader, newBody, newCorrection);
		}

		// WRITE CHANGES INTO THE DB
		try {
			BasicDBObject old = Database.getInstance().getById(entry.getLexEntryId());
			LexEntry oldEntry = Converter.convertToLexEntry(old);
			db.suggestUpdate(oldEntry, entry);
			return null;
		} catch (Exception e) {
			logger.error("Failed to suggest modification", e);
			// throw new MaalrException("Failed to suggest modification: " + e);
			throw new MaalrException(e.getMessage());
		}
	}

	private LemmaVersion updateEntryValues(LemmaVersion entry, String newHeader, String newBody,
			String newCorrection) {

		// Header
		entry.putEntryValue("header", newHeader);
		
		// Body
		// html
		entry.putEntryValue("body", newBody);
		// txt
		entry.putEntryValue("bodyt_txt", new HtmlToPlainText().getPlainText(Jsoup.parse(newBody)));

		// Correction
		entry.putEntryValue("correction", newCorrection);

		return entry;
	}

	private String checkSize(String s) {

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

		return doc.body().html().trim();
	}

}
