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
import org.jsoup.safety.Whitelist;
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
	public String suggestModification(LemmaVersion entry,
			Map<String, String> toUpdate) throws MaalrException {

		String lemma_new = toUpdate.get("Lemma").trim();
		String content_new = toUpdate.get("Content").trim();
		String correction_new = toUpdate.get("Correction").trim();
		String correction_src = entry.getEntryValue("Correction").trim();

		// Clean input
		lemma_new = Jsoup.clean(lemma_new, whiteList);
		content_new = Jsoup.clean(content_new, whiteList);
		correction_new = Jsoup.clean(correction_new, whiteList);
		int correction = Integer.parseInt(correction_new);

		if ((correction_new.equals(correction_src))) {
			throw new MaalrException("dialog.nocorrection");
		} else if (correction > 100 || correction <= 15) {
			throw new MaalrException("correction.wrongvalue");
		} else {
			entry = updateEntryValues(entry, lemma_new, content_new,
					correction_new);
		}

		// WRITE CHANGES INTO THE DB
		try {
			BasicDBObject old = Database.getInstance().getById(
					entry.getLexEntryId());
			LexEntry oldEntry = Converter.convertToLexEntry(old);
			db.suggestUpdate(oldEntry, entry);
			return null;
		} catch (Exception e) {
			logger.error("Failed to suggest modification", e);
			// throw new MaalrException("Failed to suggest modification: " + e);
			throw new MaalrException(e.getMessage());
		}
	}

	private LemmaVersion updateEntryValues(LemmaVersion entry,
			String lemma_new, String content_new, String correction_new) {

		// Lemma
		// html
		entry.putEntryValue("Lemma", lemma_new);
		// txt
		entry.putEntryValue("Lemma_txt",
				new HtmlToPlainText().getPlainText(Jsoup.parse(lemma_new)));

		// Content
		// html
		entry.putEntryValue("Content", content_new);
		// txt
		entry.putEntryValue("Content_txt",
				new HtmlToPlainText().getPlainText(Jsoup.parse(content_new)));

		// Correction
		entry.putEntryValue("Correction", correction_new);

		return entry;
	}

}
