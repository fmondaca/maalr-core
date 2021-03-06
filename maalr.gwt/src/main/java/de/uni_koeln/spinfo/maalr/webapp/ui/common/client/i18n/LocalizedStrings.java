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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonService;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.LocalDB;

public class LocalizedStrings {

	private static CommonServiceAsync service = GWT.create(CommonService.class);
	private static TranslationMap translation = null;

	public static void afterLoad(final AsyncCallback<TranslationMap> callback) {
		final String locale = Document.get().getElementsByTagName("html").getItem(0).getAttribute("lang");
		//SimpleWebLogger.log("LocalizedStrings afterLoad(final AsyncCallback<TranslationMap> callback) [locale=" + locale+"]");
		if (translation == null) {
			translation = LocalDB.getEditorTranslation(locale);
			if (translation == null) {
				service.getEditorTranslation(locale,
						new AsyncCallback<TranslationMap>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onSuccess(TranslationMap result) {
								translation = result;
								LocalDB.setEditorTranslation(locale, translation);
								callback.onSuccess(translation);
							}
						});

			} else {
				callback.onSuccess(translation);	
			}
		} else {
			callback.onSuccess(translation);
		}
	}

}
