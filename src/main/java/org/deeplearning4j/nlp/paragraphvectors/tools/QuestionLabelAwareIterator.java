package org.deeplearning4j.nlp.paragraphvectors.tools;

import static org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator.TaskType.TRAINING;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.deeplearning4j.nlp.paragraphvectors.corpus.Answer;
import org.deeplearning4j.nlp.paragraphvectors.corpus.Question;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;

public class QuestionLabelAwareIterator implements LabelAwareIterator {
	protected List<Answer> answers;
	protected AtomicInteger position = new AtomicInteger(0);
	protected LabelsSource labelsSource;

	protected QuestionLabelAwareIterator(List<Answer> answers, LabelsSource source) {
		this.answers = answers;
		this.labelsSource = source;
	}

	@Override
	public boolean hasNextDocument() {
		return position.get() < answers.size();
	}

	@Override
	public LabelledDocument nextDocument() {
		Answer answer = answers.get(position.getAndIncrement());
		LabelledDocument document = new LabelledDocument();
		document.setContent(answer.getAnswer());
		document.setLabel(answer.getId());

		return document;
	}

	@Override
	public void reset() {
		position.set(0);
	}

	@Override
	public LabelsSource getLabelsSource() {
		return labelsSource;
	}

	public enum TaskType {
		TRAINING, TEST;
	}

	public static class Builder {
		protected File fileToRead;

		protected TaskType type;

		public Builder() {
			super();
		}

		public Builder registerFileToRead(File file) {
			fileToRead = file;
			return this;
		}

		public Builder setTask(TaskType type) {
			this.type = type;
			return this;
		}

		public QuestionLabelAwareIterator build() {
			Question questions = null;
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Question.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				questions = (Question) jaxbUnmarshaller.unmarshal(fileToRead);
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			List<Answer> answers = type.equals(TRAINING) ? questions.getReferenceAnswers()
					: questions.getStudentAnswers();

			List<String> labels = new ArrayList<>();
			for (Answer question : answers) {
				labels.add(question.getId());
			}

			LabelsSource source = new LabelsSource(labels);
			QuestionLabelAwareIterator iterator = new QuestionLabelAwareIterator(answers, source);

			return iterator;
		}
	}
}