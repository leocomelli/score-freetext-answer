package org.deeplearning4j.nlp.paragraphvectors.corpus;

import java.util.List;

public class Corpus {

	private final List<Answer> training;
	
	private final  List<Answer> test;

	public Corpus(List<Answer> training, List<Answer> test) {
		this.training = training;
		this.test = test;
	}

	public List<Answer> getTraining() {
		return training;
	}

	public List<Answer> getTest() {
		return test;
	}
	
}
