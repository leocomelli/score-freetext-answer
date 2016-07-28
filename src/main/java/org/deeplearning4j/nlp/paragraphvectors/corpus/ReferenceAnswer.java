package org.deeplearning4j.nlp.paragraphvectors.corpus;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;
import static org.deeplearning4j.nlp.paragraphvectors.corpus.Accuracy.CORRECT;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(FIELD)
public class ReferenceAnswer implements Answer{

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String category;

	@XmlValue
	private String referenceAnswer;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getReferenceAnswer() {
		return referenceAnswer;
	}

	public void setReferenceAnswer(String referenceAnswer) {
		this.referenceAnswer = referenceAnswer;
	}

	@Override
	public String getAnswer() {
		return getReferenceAnswer();
	}

	@Override
	public Accuracy getAccuracy() {
		return CORRECT;
	}
	
}
