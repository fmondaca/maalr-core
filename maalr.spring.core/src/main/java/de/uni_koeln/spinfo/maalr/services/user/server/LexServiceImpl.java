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

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
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

	private Set<String> whiteList = Configuration.getInstance().getWhiteList();

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

		// Get text from source LemmaVersion for comparing with modified
		String lemma_src = entry.getEntryValue("Lemma");
		String content_src = entry.getEntryValue("Content");
		String correction_src = entry.getEntryValue("Correction");

		String lemma_new = toUpdate.get("Lemma");
		String content_new = toUpdate.get("Content");
		String correction_new = toUpdate.get("Correction");

		// Clean input
		lemma_new = cleanInput(lemma_new);
		content_new = cleanInput(content_new);
		correction_new = cleanInput(correction_new);

		// If all 3 variables are clean
		if (lemma_new != null && content_new != null && correction_new != null) {

			// If Lemma and Content have not been modified, go back
			if (lemma_src.equals(lemma_new) && content_new.equals(content_src)) {
				throw new MaalrException("dialog.nochanges");
			} else {

				// is the correction value still the same?
				if (correction_new.equals(correction_src)) {

					throw new MaalrException("dialog.nocorrection");

				}
				// Put the new values into the LemmaVersion

				else {

					logger.info("new correction value, update values");

					// Lemma
					// html
					entry.putEntryValue("Lemma", lemma_new.trim());
					// txt
					String lemma_txt = lemma_new.replaceAll("<[^>]*>", "");
					entry.putEntryValue("Lemma_txt", lemma_txt.trim());

					// Content
					// html
					entry.putEntryValue("Content", content_new.trim());
					// txt
					String content_txt = content_new.replaceAll("<[^>]*>", "");
					entry.putEntryValue("Lemma_txt", content_txt.trim());

					// Correction
					entry.putEntryValue("Correction", correction_new.trim());

				}

			}

		} else {
			throw new MaalrException("dialog.bad");

		}

		// UPDATE
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

	private String cleanInput(String toCheck) {

		Set<String> tags = new LinkedHashSet<>();

		String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
		Pattern pattern = Pattern.compile(HTML_TAG_PATTERN);

		// Normalize input
		toCheck = Normalizer.normalize(toCheck, Normalizer.Form.NFC);
		// Unescape entities
		toCheck = StringEscapeUtils.unescapeHtml4(toCheck);

		Matcher matcher = pattern.matcher(toCheck);

		// Get all tags
		while (matcher.find()) {
			tags.add(matcher.group());
		}

		int counter = 0;

		for (String s : tags) {

			if (!whiteList.contains(s)) {
				logger.info("NOT FOUND: " + s);
				counter++;

			}

		}

		if (counter > 0) {

			return null;

		} else {
			return toCheck;
		}
	}

}
