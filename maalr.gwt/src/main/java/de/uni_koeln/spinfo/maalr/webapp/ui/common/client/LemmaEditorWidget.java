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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueSpecification;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueValidator;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.toolbar.RichTextToolbar;

/**
 * A dynamic form to edit or create entries. It is used within the editor area
 * as well as within the dialogs provided to 'default'-users.
 * 
 * @author sschwieb
 *
 */
public class LemmaEditorWidget extends SimplePanel {

	@UiTemplate("LemmaEditorWidget.ui.xml")
	interface LemmaEditorBinder extends UiBinder<Widget, LemmaEditorWidget> {
	}

	private static LemmaEditorBinder uiBinder = GWT
			.create(LemmaEditorBinder.class);

	Logger logger = Logger.getLogger("LemmaEditorWidget");

	@UiField
	FlowPanel finalBase = new FlowPanel();
	@UiField
	VerticalPanel languages = new VerticalPanel();
	@UiField
	FlowPanel imagePanel = new FlowPanel();
	@UiField
	VerticalPanel langA = new VerticalPanel();
	@UiField
	VerticalPanel langB = new VerticalPanel();
	@UiField
	FlowPanel percentage = new FlowPanel();

	private LemmaDescription description;

	/**
	 * Contains a mapping of field-ids to ui-elements (such as {@link TextBox},
	 * {@link ListBox} etc.) Required to set and update the values of an entry.
	 */
	private Map<String, HasHTML> fields = new HashMap<String, HasHTML>();

	/**
	 * Contains a mapping of field-ids to {@link ControlGroup}-elements.
	 * Required to change the ui-style of the elements when feedback is
	 * presented in case of an invalid entry.
	 */
	private Map<String, ControlGroup> groups = new HashMap<String, ControlGroup>();

	/**
	 * Contains a mapping of field-ids to {@link HelpInline}-elements. Required
	 * to provide feedback in case of an invalid entry.
	 */
	private Map<String, HelpInline> feedback = new HashMap<String, HelpInline>();

	/**
	 * Contains a mapping of field-ids to {@link ValueSpecification}-elements.
	 * Required to validate the input provided by a user. See
	 * {@link ValueSpecification#getValidator()} and
	 * {@link ValueValidator#validate(String)} for details.
	 */
	private HashMap<String, ValueSpecification> valueSpecifications;

	private LemmaVersion initial;

	/**
	 * Create a new {@link LemmaEditorWidget} for the given
	 * {@link LemmaDescription}.
	 * 
	 * @param description
	 *            the {@link LemmaDescription}
	 * @param useCase
	 *            the {@link UseCase} which defines if the widget is used within
	 *            the 'default' user environment (
	 *            {@link UseCase#FIELDS_FOR_SIMPLE_EDITOR}) or within the editor
	 *            environment ({@link UseCase#FIELDS_FOR_ADVANCED_EDITOR}).
	 *            Other usecases are not supported.
	 * @param vertical
	 *            Whether or not the fields should be oriented vertically.
	 * @param columns
	 *            The number of columns
	 * @param showLanguageHeader
	 *            Whether or not a language header should be shown
	 * @param toggleAntlr
	 *            The button responsible for displaying the antlr input field.
	 *            Can be <code>null</code>. If a button is provided, a handler
	 *            must be set to show/hide the input field.
	 * 
	 */
	@UiConstructor
	public LemmaEditorWidget(final LemmaDescription description,
			final UseCase useCase, final int columns,
			final boolean showLanguageHeader, Button toggleAntlr) {

		this.description = description;

		setWidget(uiBinder.createAndBindUi(this));

		LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(TranslationMap translations) {

				DOM.setElementAttribute(languages.getElement(), "id",
						"languages");
				DOM.setElementAttribute(finalBase.getElement(), "id",
						"finalBase");

				DOM.setElementAttribute(imagePanel.getElement(), "id",
						"imagePanel");

				langA = textEdit();
				langB = richEdit();

				languages.add(langA);
				languages.add(langB);

				Image dictPage_1 = new Image();
				dictPage_1.setUrl(GWT.getModuleBaseURL() + "/images/page-1.jpg");
				Image dictPage_2 = new Image();
				dictPage_2.setUrl(GWT.getModuleBaseURL() + "/images/page-2.jpg");

				imagePanel.add(dictPage_1);
				imagePanel.add(dictPage_2);

				percentage = modifyPercentage();

				finalBase.add(imagePanel);
				finalBase.add(languages);
				finalBase.add(percentage);

			}
		});

	}

	private VerticalPanel textEdit() {

		RichTextArea rta_a = new RichTextArea();
		DOM.setElementAttribute(rta_a.getElement(), "id", "rta_langA");
		DOM.setElementAttribute(langA.getElement(), "id", "langA");

		rta_a.setHeight("2em");
		langA.add(rta_a);
		fields.put("DStichwort", rta_a);

		return langA;
	}

	private VerticalPanel richEdit() {

		RichTextArea rta_b = new RichTextArea();
		RichTextToolbar toolbar = new RichTextToolbar(rta_b);
		DOM.setElementAttribute(langB.getElement(), "id", "langB");
		DOM.setElementAttribute(rta_b.getElement(), "id", "rta_langB");

		langB.add(toolbar);
		langB.add(rta_b);

		fields.put("RStichwort", rta_b);

		return langB;
	}

	private FlowPanel modifyPercentage() {
		RichTextArea per = new RichTextArea();
		per.setHeight("2em");
		DOM.setElementAttribute(per.getElement(), "id", "per");
		percentage.add(per);
		fields.put("correction", per);

		return percentage;
	}

	/**
	 * Copies the data from the given {@link LemmaVersion} into the editor and
	 * updates the ui.
	 * 
	 * @param
	 */
	public void setData(LemmaVersion lemma) {
		List<String> toSet = new ArrayList<String>();
		// first language
		toSet.addAll(description.getEditorFields(true));
		// second language
		toSet.addAll(description.getEditorFields(false));

		// Add correction
		toSet.add("correction");

		if (lemma == null) {
			for (String key : toSet) {
				HasText field = fields.get(key);
				if (field != null) {
					field.setText(null);
				}
			}
		} else {
			for (String key : toSet) {
				HasHTML field = fields.get(key);

				if (field != null) {

					HTML html = new HTML(lemma.getEntryValue(key));
					field.setHTML(html.getHTML());

				}

			}
		}
		initial = new LemmaVersion();
		initial.setEntryValues(new HashMap<String, String>(lemma
				.getEntryValues()));
		initial.setMaalrValues(new HashMap<String, String>(lemma
				.getMaalrValues()));
	}

	/**
	 * Copies the values from the editor into the given {@link LemmaVersion}.
	 * 
	 * @param lemma
	 *            must not be <code>null</code>.
	 */
	public void updateFromEditor(LemmaVersion lemma) {
		List<String> toSet = new ArrayList<String>();

		// first language
		toSet.addAll(description.getEditorFields(true));
		// second language
		toSet.addAll(description.getEditorFields(false));

		// Add correction
		toSet.add("correction");

		for (String key : toSet) {
			HasHTML field = fields.get(key);
			if (field != null) {

				String text = field.getHTML();

				if (text != null && text.trim().length() > 0) {

					//logger.log(Level.INFO, "UPDATE: " + key + " " + text);

					//

					if (key.equals("correction")) {
						//lemma.putEntryValue(key, text.trim());
						int correction = Integer.parseInt(text);
						logger.log(Level.INFO, "CORRECTION " + correction);

						if (correction > 100 || correction < 15) {

							Window.alert("VDFVDF");
							

						} else if (correction > 15 && correction <= 100) {
							lemma.putEntryValue(key, text.trim());
						}

					} else {
						lemma.putEntryValue(key, text.trim());
					}

				} else {
					lemma.removeEntryValue(key);
				}

			}
		}
	}

	/**
	 * Resets the data shown by the editor, by clearing all fields and
	 * displaying the values which were defined in the lemma set by
	 * {@link LemmaEditorWidget#setData(LemmaVersion)).
	 */
	public void clearData() {
		if (initial == null) {
			setData(new LemmaVersion());
		} else {
			setData(initial);
		}
	}

	public boolean isValid(boolean all, boolean showError) {
		boolean valid = true;
		Set<String> keys = fields.keySet();
		boolean oneValid = false;
		for (String key : keys) {
			if (!validate(key, showError)) {
				valid = false;
			} else {
				oneValid = true;
			}
		}
		return all ? valid : oneValid;
	}

	private boolean validate(String field, boolean showError) {

		return true;
	}

	public void markFields(Map<String, Difference> compared,
			Map<String, String> oldValues) {
		for (String field : groups.keySet()) {
			feedback.get(field).setVisible(false);
			groups.get(field).setType(ControlGroupType.NONE);
		}
		for (String field : compared.keySet()) {
			if (compared.get(field) != Difference.NEW) {
				feedback.get(field).setText(
						compared.get(field).getDisplayName() + ", was: "
								+ oldValues.get(field));
			} else {
				feedback.get(field).setText(
						compared.get(field).getDisplayName());
			}
			feedback.get(field).setVisible(true);
			groups.get(field).setType(ControlGroupType.WARNING);
		}
	}

}
