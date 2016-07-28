package org.deeplearning4j.nlp.paragraphvectors.tools;

import static java.util.Arrays.asList;
import static org.deeplearning4j.nlp.paragraphvectors.corpus.Accuracy.CORRECT;
import static org.deeplearning4j.nlp.paragraphvectors.corpus.Accuracy.INCORRECT;
import static org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator.TaskType.TRAINING;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.deeplearning4j.nlp.paragraphvectors.corpus.Answer;
import org.deeplearning4j.nlp.paragraphvectors.corpus.Corpus;
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
		document.setLabel(answer.getAccuracy().name());

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
		protected Corpus corpus;

		protected TaskType type;

		public Builder() {
			super();
		}

		public Builder using(Corpus corpus) {
			this.corpus = corpus;
			return this;
		}

		public Builder setTask(TaskType type) {
			this.type = type;
			return this;
		}

		public QuestionLabelAwareIterator build() {
			List<Answer> answers = type.equals(TRAINING) ? corpus.getTraining() : corpus.getTest();

			List<String> labels = asList(new String[]{ CORRECT.name(), INCORRECT.name() });

			LabelsSource source = new LabelsSource(labels);
			QuestionLabelAwareIterator iterator = new QuestionLabelAwareIterator(answers, source);

			return iterator;
		}
	}
}