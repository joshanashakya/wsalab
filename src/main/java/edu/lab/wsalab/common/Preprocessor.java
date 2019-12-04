package edu.lab.wsalab.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.lab.wsalab.FileFormat;
import opennlp.tools.stemmer.PorterStemmer;

public class Preprocessor {

	public String preprocess(String text) {
		String[] tokens = tokenize(text);
		tokens = applyStemmer(tokens);
		String[] cleaned = removeStopWords(tokens);
		int len = cleaned.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(cleaned[i]);
			sb.append(" ");
		}
		return sb.toString();
	}

	public String removeSGML(String str) {
		return str.replaceAll("</?\\w*/?>", " ");
	}

	public String[] tokenize(String str) {
		str = str.replaceAll("[^A-Za-z ]", " ").replaceAll(" +", " ").toLowerCase();
		return str.trim().split("\\s");
	}

	public String[] applyStemmer(String[] tokens) {
		PorterStemmer stemmer = new PorterStemmer();
		int len = tokens.length;
		String[] stemmed = new String[len];
		for (int i = 0; i < len; i++) {
			stemmed[i] = stemmer.stem(tokens[i]);
		}
		return stemmed;
	}

	public String[] removeStopWords(String[] tokens) {
		List<String> stopWords = FileReaderWriter.readFileAndList(FileFormat.STOPWORDS);
		List<String> words = new ArrayList<>(Arrays.asList(tokens));
		words.removeAll(stopWords);
		String[] cleaned = new String[words.size()];
		words.toArray(cleaned);
		return cleaned;
	}
	
	public List<Integer> clean(String str) {
		String[] arr = str.split(":")[1].split(",");
		List<Integer> vals = new ArrayList<>();
		for (String s : arr) {
			vals.add(Integer.valueOf(s.trim()));
		}
		return vals;
	}
}
