package edu.lab.wsalab;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import edu.lab.wsalab.common.FileReaderWriter;
import edu.lab.wsalab.common.Preprocessor;

/**
 * @author Joshana Shakya
 *
 */
public class ApplicationTest {
	
	private static final Logger LOGGER = Logger.getLogger(ApplicationTest.class.getName());

	public static void main(String[] args) {
		ApplicationTest app = new ApplicationTest();
		Preprocessor preprocessor = new Preprocessor();

		String text = FileReaderWriter.readFile("cran-test");
		System.out.format("The original content is: \n%s\n\n", text);
		// Eliminate SGML tags
		text = preprocessor.removeSGML(text);
		System.out.format("The content after removing SGML tags: \n%s\n\n", text);

		// Tokenize the text
		String[] tokens = preprocessor.tokenize(text);
		System.out.format("The tokens obtained are: \n%s\n\n", Arrays.toString(tokens));

		LOGGER.info("Before using Porter Stemmer and removing stop words\n");
		app.print(tokens);

		LOGGER.info("After using Porter Stemmer and removing stop words\n");
		String[] stemmed = preprocessor.applyStemmer(tokens);
		String[] cleaned = preprocessor.removeStopWords(stemmed);
		app.print(cleaned);

	}
	
	void print(String[] tokens) {
		// Vocabulary size
		Counter counter = new Counter();
		int vocabSize = counter.countUniqueWords(tokens);
		System.out.format("Vocabulary size: %d\n\n", vocabSize);

		// Top 10 words in ranking
		Map<String, Integer> wordToFreq = counter.calWordToFreq(tokens, vocabSize);
		printTopWords(wordToFreq, 10);

		// minimum number of unique words accounting for half of the total number of
		// words in the collection
		int count = counter.minHalfWordCount(wordToFreq);
		System.out.format("Minimum number of unique words accounting for half of the total number of words: %d\n\n",
				count);
	}
	
	private void printTopWords(Map<String, Integer> wordToFreq, int size) {
		int i = 1;
		System.out.format("Top %d words:\n\nWord:Frequency\n--------------\n", size);
		for (Map.Entry<String, Integer> entry : wordToFreq.entrySet()) {
			if (i > size) {
				break;
			}
			System.out.format("%s: %d\n", entry.getKey(), entry.getValue());
			i++;
		}
		System.out.println();
	}
}
