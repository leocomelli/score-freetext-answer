package org.deeplearning4j.nlp.paragraphvectors;

import static org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator.TaskType.TEST;
import static org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator.TaskType.TRAINING;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.nlp.paragraphvectors.tools.LabelSeeker;
import org.deeplearning4j.nlp.paragraphvectors.tools.MeansBuilder;
import org.deeplearning4j.nlp.paragraphvectors.tools.QuestionLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParagraphVectorsClassifier {
	private static final Logger log = LoggerFactory.getLogger(ParagraphVectorsClassifier.class);
	
	private static final String FILE_NAME = "corpus/semeval2013-task7/training/2way/beetle/FaultFinding-BULB_C_VOLTAGE_EXPLAIN_WHY1.xml";

	public static void main(String[] args) throws Exception {

		ClassPathResource filePath = new ClassPathResource(FILE_NAME);
		LabelAwareIterator labeledIterator = new QuestionLabelAwareIterator.Builder()
				.registerFileToRead(filePath.getFile()).setTask(TRAINING).build();

		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());

		ParagraphVectors paragraphVectors = new ParagraphVectors.Builder().learningRate(0.025).minLearningRate(0.001)
				.batchSize(1000).epochs(1).iterate(labeledIterator).trainWordVectors(true).tokenizerFactory(t).build();

		paragraphVectors.fit();

		LabelAwareIterator unlabeledIterator = new QuestionLabelAwareIterator.Builder()
				.registerFileToRead(filePath.getFile()).setTask(TEST).build();

		MeansBuilder meansBuilder = new MeansBuilder((InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
				t);
		LabelSeeker seeker = new LabelSeeker(labeledIterator.getLabelsSource().getLabels(),
				(InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

		while (unlabeledIterator.hasNextDocument()) {
			LabelledDocument document = unlabeledIterator.nextDocument();
			try {

				INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
				List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);

				Collections.sort(scores, new Comparator<Pair<String, Double>>() {
					@Override
					public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
						return o2.getSecond().compareTo(o1.getSecond());
					}
				});

				log.info("Answer: " + document.getLabel());
				log.info("        " + scores.get(0).getFirst() + ": " + scores.get(0).getSecond());
				log.info("        " + scores.get(1).getFirst() + ": " + scores.get(1).getSecond());
				log.info("        " + scores.get(2).getFirst() + ": " + scores.get(2).getSecond());
			} catch (IllegalArgumentException e) {
				log.error("The word is too short.");
			}
		}
	}
}
