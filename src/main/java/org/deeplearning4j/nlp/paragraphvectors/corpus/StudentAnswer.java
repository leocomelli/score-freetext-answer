package org.deeplearning4j.nlp.paragraphvectors.corpus;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(FIELD)
public class StudentAnswer implements Answer {

	@XmlAttribute
	private String id;

	@XmlAttribute(name = "accuracy")
	private Accuracy accuracy;

	@XmlValue
	private String studentAnswer;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAccuracy(Accuracy accuracy) {
		this.accuracy = accuracy;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	@Override
	public String getAnswer() {
		return getStudentAnswer();
	}
	
	@Override
	public Accuracy getAccuracy() {
		return accuracy;
	}

}
