package org.deeplearning4j.nlp.paragraphvectors.corpus;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "question")
@XmlAccessorType(FIELD)
public class Question {

	@XmlAttribute
	private String id;

	@XmlElement
	private String questionText;
	
	@XmlElement(name = "referenceAnswer", type = ReferenceAnswer.class)
	@XmlElementWrapper(name = "referenceAnswers")
	private List<Answer> referenceAnswers;

	@XmlElement(name = "studentAnswer", type = StudentAnswer.class)
	@XmlElementWrapper(name = "studentAnswers")
	private List<Answer> studentAnswers;

	Question(){
		super();
	}
	
	public Question(String id, String questionText, List<Answer> referenceAnswers, List<Answer> studentAnswers) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.referenceAnswers = referenceAnswers;
		this.studentAnswers = studentAnswers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<Answer> getReferenceAnswers() {
		return referenceAnswers;
	}

	public void setReferenceAnswers(List<Answer> referenceAnswers) {
		this.referenceAnswers = referenceAnswers;
	}

	public List<Answer> getStudentAnswers() {
		return studentAnswers;
	}

	public void setStudentAnswers(List<Answer> studentAnswers) {
		this.studentAnswers = studentAnswers;
	}

}
