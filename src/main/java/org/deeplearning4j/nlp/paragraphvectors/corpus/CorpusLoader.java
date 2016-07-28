package org.deeplearning4j.nlp.paragraphvectors.corpus;

import static java.util.stream.Collectors.toList;
import static org.deeplearning4j.nlp.paragraphvectors.corpus.Accuracy.CORRECT;
import static org.deeplearning4j.nlp.paragraphvectors.corpus.Accuracy.INCORRECT;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class CorpusLoader {
	
	private static final double TRAINING_DELTA = 0.9;

	private Question questions;
	
	public CorpusLoader(File fileToRead){
		this.questions = read(fileToRead);
	}
	
	
	public Corpus load(){
				
		List<Answer> corrects = getShuffleList(CORRECT);
		List<Answer> incorrects = getShuffleList(INCORRECT);
		
		// Training
		List<Answer> training= new ArrayList<>();
		training.addAll(corrects.subList(0, (int) (corrects.size() * TRAINING_DELTA)));
		training.addAll(incorrects.subList(0, (int) (incorrects.size() * TRAINING_DELTA)));
		training.addAll(questions.getReferenceAnswers());
		
		// Test
		List<Answer> all = new ArrayList<>(corrects);
		all.addAll(incorrects);
		
		List<Answer> test= new ArrayList<>();
		test.addAll(all.stream()
					   .filter(a -> !training.contains(a))
					   .collect(toList()));
		
		return new Corpus(training, test);
	}
	
	private List<Answer> getShuffleList(Accuracy accuracy){
		List<Answer> answers = questions.getStudentAnswers().stream()
				 											.filter(a -> a.getAccuracy().equals(accuracy))
				 											.collect(toList());
		
		Collections.shuffle(answers);
		
		return answers;
	}
	
	private Question read(File fileToRead){
		Question questions = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Question.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			questions = (Question) jaxbUnmarshaller.unmarshal(fileToRead);
		} catch (JAXBException e) {
			throw new RuntimeException(e.getMessage());
		}

		return questions;
	}
}
