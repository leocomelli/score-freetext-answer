package org.deeplearning4j.nlp.paragraphvectors.corpus;

import static java.util.stream.Collectors.toList;
import static org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator.TaskType.TRAINING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator.TaskType;

public class CorpusHandler {

	private static final List<String> CORRECT_ANSWER_TRAINING =  Arrays.asList(new String[] { "FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj14-l1.qa209", 
			     																			  "FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj37-l1.qa183" });
	
	private static final List<String> INCORRECT_ANSWER_TRAINING =  Arrays.asList(new String[] { "FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj8-l1.qa224", 
			 																					"FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj8-l1.qa226", 
			 																					"FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj19-l1.qa241", 
			 																					"FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj27-l1.qa207", 
			 																					"FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj38-l1.qa187", 
			 																					"FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbj40-l1.qa216", 
			 																					"FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.sbjb19-l1.qa164" });

	private Question questions;
	
	public CorpusHandler(Question questions){
		this.questions = questions;
	}
	
	public List<Answer> getCorpusBy(TaskType taskType){
		return taskType.equals(TRAINING) ? getTrainingCorpus() : getTestCorpus();		
	}
	
	private List<Answer> getTrainingCorpus(){
		List<Answer> training = new ArrayList<>(questions.getReferenceAnswers());
		training.addAll(questions.getStudentAnswers()
						.stream()
						.filter(a -> CORRECT_ANSWER_TRAINING.contains(a.getId()) ||
									 INCORRECT_ANSWER_TRAINING.contains(a.getId()))
						.collect(toList()));
				
		return training;
	}

	private List<Answer> getTestCorpus(){
		List<Answer> test = new ArrayList<>();
		test.addAll(questions.getStudentAnswers()
						.stream()
						.filter(a -> !CORRECT_ANSWER_TRAINING.contains(a.getId()) &&
									 !INCORRECT_ANSWER_TRAINING.contains(a.getId()))
						.collect(toList()));
				
		return test;
	}
	
}
