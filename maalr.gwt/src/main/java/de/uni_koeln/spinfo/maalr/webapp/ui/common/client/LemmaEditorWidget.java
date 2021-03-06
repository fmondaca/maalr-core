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
import java.util.logging.Logger;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueSpecification;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueValidator;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.services.user.shared.LexService;
import de.uni_koeln.spinfo.maalr.services.user.shared.LexServiceAsync;
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

	private static LexServiceAsync lexService = GWT.create(LexService.class);
	private static LemmaEditorBinder uiBinder = GWT.create(LemmaEditorBinder.class);

	Logger logger = Logger.getLogger("LemmaEditorWidget");

	// Base
	@UiField
	HorizontalPanel finalBase = new HorizontalPanel();

	// Images
	@UiField
	ScrollPanel imageContainer = new ScrollPanel();
	@UiField
	FlowPanel imagePanel = new FlowPanel();

	// Text
	@UiField
	VerticalPanel textEditorPanel = new VerticalPanel();
	@UiField
	VerticalPanel headerPanel = new VerticalPanel();
	@UiField
	VerticalPanel bodyPanel = new VerticalPanel();
	@UiField
	VerticalPanel correctionPanel = new VerticalPanel();

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
	 * @param lemma
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
	public LemmaEditorWidget(final LemmaVersion lemma, final LemmaDescription description, final UseCase useCase,
			final int columns, final boolean showLanguageHeader, Button toggleAntlr) {

		this.description = description;

		setWidget(uiBinder.createAndBindUi(this));

		LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(TranslationMap translations) {

				// this.translations = translations;

				DOM.setElementAttribute(finalBase.getElement(), "id", "finalBase");
				DOM.setElementAttribute(textEditorPanel.getElement(), "id", "textEditor");
				DOM.setElementAttribute(imageContainer.getElement(), "id", "imageContainer");
				DOM.setElementAttribute(imagePanel.getElement(), "id", "imagePanel");

				headerPanel = headerContainer(translations);
				bodyPanel = bodyContainer(translations);

				textEditorPanel.add(headerPanel);
				textEditorPanel.add(bodyPanel);
				correctionPanel = correctionContainer(translations);
				textEditorPanel.add(correctionPanel);

				imagePanel.setWidth("500px");
				imagePanel.setHeight("380px");

				String baseURL = "/images/";

				List<Image> images = new ArrayList<>();
				String[] pfl = lemma.getEntryValue("pages").split(",");

				// Add page-mapping
				for (String i : pfl) {
					Image page = new Image();
					StringBuffer buffer = new StringBuffer();
					buffer.append(baseURL);
					buffer.append("page-");
					buffer.append(i.trim());
					buffer.append(".jpg");
					page.setUrl(buffer.toString());
					images.add(page);
				}

				for (Image i : images) {
					imagePanel.add(i);

				}
				imageContainer.setSize("500px", "380px");
				imageContainer.setWidget(imagePanel);

				finalBase.add(imageContainer);
				finalBase.add(textEditorPanel);

			}
		});

	}

	private VerticalPanel headerContainer(TranslationMap translations) {
		Label lbl = new Label(translations.get("header"));
		RichTextArea rta_a = new RichTextArea();
		DOM.setElementAttribute(rta_a.getElement(), "id", "rtaHeaderPanel");
		DOM.setElementAttribute(headerPanel.getElement(), "id", "headerPanel");

		rta_a.setHeight("2em");
		headerPanel.add(lbl);
		headerPanel.add(rta_a);
		fields.put("header", rta_a);

		return headerPanel;
	}

	private VerticalPanel bodyContainer(TranslationMap translations) {
		Label lbl = new Label(translations.get("body"));
		RichTextArea rta_b = new RichTextArea();
		RichTextToolbar toolbar = new RichTextToolbar(rta_b);
		DOM.setElementAttribute(bodyPanel.getElement(), "id", "bodyPanel");
		DOM.setElementAttribute(rta_b.getElement(), "id", "rtaBodyPanel");
		bodyPanel.add(lbl);
		bodyPanel.add(toolbar);
		bodyPanel.add(rta_b);

		fields.put("body", rta_b);

		return bodyPanel;
	}

	private VerticalPanel correctionContainer(TranslationMap translations) {
		Label lbl = new Label(translations.get("correction"));

		RichTextArea per = new RichTextArea();
		per.setHeight("2em");
		DOM.setElementAttribute(per.getElement(), "id", "correctionPanel");

		correctionPanel.add(lbl);
		correctionPanel.add(per);
		fields.put("correction", per);

		return correctionPanel;
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
		initial.setEntryValues(new HashMap<String, String>(lemma.getEntryValues()));
		initial.setMaalrValues(new HashMap<String, String>(lemma.getMaalrValues()));
	}

	/**
	 * Copies the values from the editor into the given {@link LemmaVersion}.
	 * 
	 * @param lemma
	 *            must not be <code>null</code>.
	 */
	public void updateFromEditor(final LemmaVersion lemma, final Button ok, final Modal popup, final Button cancel,
			final Button reset, final TranslationMap translation) {

		String header = fields.get("header").getHTML();

		String body = fields.get("body").getHTML();

		String correction = fields.get("correction").getHTML();

		Map<String, String> toUpdate = new HashMap<>();

		toUpdate.put("header", header);

		toUpdate.put("body", body);

		// cleanup string
		correction = correction.replaceAll("[^\\d]", "");

		toUpdate.put("correction", correction);

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				cancel.setEnabled(true);
				reset.setEnabled(true);
				ok.setText(translation.get("button.ok"));
				ok.setType(ButtonType.DANGER);
				ok.setEnabled(false);
				Dialog.showError(translation.get("dialog.failure"), translation.get(caught.getMessage())); // exception
																											// from
																											// SpringBackend

				popup.hide();
				return;

			}

			@Override
			public void onSuccess(String result) {
				ok.setText(translation.get("dialog.success"));
				ok.setType(ButtonType.SUCCESS);
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

					@Override
					public boolean execute() {
						popup.hide();
						return false;
					}
				}, 1000);

			}

		};

		lexService.suggestModification(lemma, toUpdate, callback);

	}

	public void updateFromAdvancedEditor(LemmaVersion lemma) {
		List<String> toSet = new ArrayList<String>();

		// header
		toSet.addAll(description.getEditorFields(true));
		// body
		toSet.addAll(description.getEditorFields(false));
		//correction
		toSet.add("correction");

		for (String key : toSet) {
			HasHTML field = fields.get(key);
			if (field != null) {

				String text = field.getHTML();

				if (text != null && text.trim().length() > 0) {

					lemma.putEntryValue(key, text.trim());

				}

				else {
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

	public void markFields(Map<String, Difference> compared, Map<String, String> oldValues) {
		for (String field : groups.keySet()) {
			feedback.get(field).setVisible(false);
			groups.get(field).setType(ControlGroupType.NONE);
		}
		for (String field : compared.keySet()) {
			if (compared.get(field) != Difference.NEW) {
				feedback.get(field).setText(compared.get(field).getDisplayName() + ", was: " + oldValues.get(field));
			} else {
				feedback.get(field).setText(compared.get(field).getDisplayName());
			}
			feedback.get(field).setVisible(true);
			groups.get(field).setType(ControlGroupType.WARNING);
		}
	}

}
