package edu.lab.wsalab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import opennlp.tools.stemmer.PorterStemmer;

public class Preprocessor {

	private static final Logger LOGGER = Logger.getLogger(Preprocessor.class.getName());

	public static void main(String[] args) {
		Preprocessor preprocessor = new Preprocessor();
		String text = Utilities.readFile("cran.all");
		System.out.format("The original content is: \n%s\n\n", text);
		// Eliminate SGML tags
		text = preprocessor.removeSGML(text);
		System.out.format("The content after removing SGML tags: \n%s\n\n", text);

		// Tokenize the text
		String[] tokens = preprocessor.tokenize(text);
		System.out.format("The tokens obtained are: \n%s\n\n", Arrays.toString(tokens));

		LOGGER.info("Before using Porter Stemmer and removing stop words\n");
		preprocessor.print(tokens);

		LOGGER.info("After using Porter Stemmer and removing stop words\n");
		String[] stemmed = preprocessor.applyStemmer(tokens);
		String[] cleaned = preprocessor.removeStopWords(stemmed);
		preprocessor.print(cleaned);

	}

	private String[] removeStopWords(String[] tokens) {
		List<String> stopWords = Utilities.readFileAndList("stopwords.txt");
		List<String> words = new ArrayList<>(Arrays.asList(tokens));
		words.removeAll(stopWords);
		String[] cleaned = new String[words.size()];
		words.toArray(cleaned);
		return cleaned;
	}

	private String[] applyStemmer(String[] tokens) {
		PorterStemmer stemmer = new PorterStemmer();
		int len = tokens.length;
		String[] stemmed = new String[len];
		for (int i = 0; i < len; i++) {
			stemmed[i] = stemmer.stem(tokens[i]);
		}
		return stemmed;
	}

	public void print(String[] tokens) {
		// Vocabulary size
		int vocabSize = countUniqueWords(tokens);
		System.out.format("Vocabulary size: %d\n\n", vocabSize);

		// Top 10 words in ranking
		Map<String, Integer> wordToFreq = calWordToFreq(tokens, vocabSize);
		printTopWords(wordToFreq, 10);

		// minimum number of unique words accounting for half of the total number of
		// words in the collection
		int count = minHalfWordCount(wordToFreq);
		System.out.format("Minimum number of unique words accounting for half of the total number of words: %d\n\n",
				count);
	}

	private int minHalfWordCount(Map<String, Integer> wordToFreq) {
		// calculate total word count
		int total = Utilities.sumValues(wordToFreq);

		// calculate minimum half word count
		int half = total / 2;
		int tempHalf = 0;
		int count = 0;
		int prev = 0;
		for (Map.Entry<String, Integer> entry : wordToFreq.entrySet()) {
			if (tempHalf == half) {
				break;
			}
			int value = entry.getValue();
			if (tempHalf < half) {
				tempHalf += value;
				count = count + 1;
			} else {
				tempHalf -= prev;
				tempHalf += value;
			}
			prev = entry.getValue();
		}
		return count;
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

	private Map<String, Integer> calWordToFreq(String[] words, int vocabSize) {
		int len = words.length;
		Map<String, Integer> wordToFreq = new HashMap<>();
		for (int i = 0; i < len; i++) {
			String key = words[i];
			if (wordToFreq.containsKey(key)) {
				wordToFreq.put(key, wordToFreq.get(key) + 1);
			} else {
				wordToFreq.put(key, 1);
			}
		}
		return Utilities.sortByValue(wordToFreq);
	}

	private int countUniqueWords(String[] words) {
		HashSet<String> set = new HashSet<>(Arrays.asList(words));
		return set.size();
	}

	private String removeSGML(String str) {
		return str.replaceAll("</?\\w*/?>", " ");
	}

	private String[] tokenize(String str) {
		str = str.replaceAll("[^\\w ] ", " ").replaceAll("[.,/-]", " ").replaceAll(" +", " ").toLowerCase();
		return str.trim().split("\\s");
	}
}
