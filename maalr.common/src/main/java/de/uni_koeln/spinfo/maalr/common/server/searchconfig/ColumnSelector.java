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
package de.uni_koeln.spinfo.maalr.common.server.searchconfig;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnSelector {
	
	@XmlAttribute(required=true)
	private String id;
	
	@XmlAttribute
	private String depends;
	
	public String getDepends() {
		return depends;
	}

	@XmlElementWrapper(name="options")
	@XmlElement(name="option")
	private List<ColumnSelectorOption> options = new ArrayList<ColumnSelectorOption>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ColumnSelectorOption> getOptions() {
		return options;
	}

	public void setOptions(List<ColumnSelectorOption> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "ColumnSelector [id=" + id + ", depends=" + depends
				+ ", options=" + options + "]";
	}
	
	

}
