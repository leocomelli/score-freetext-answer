package org.deeplearning4j.nlp.paragraphvectors.corpus;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "accuracy")
public enum Accuracy {
	@XmlEnumValue("correct") CORRECT, @XmlEnumValue("incorrect") INCORRECT;

	public String value() {
		return name();
	}

	public static Accuracy fromValue(String value) {
		return valueOf(value);
	}
	
	public String title(){
		return name().toLowerCase();
	}
}
