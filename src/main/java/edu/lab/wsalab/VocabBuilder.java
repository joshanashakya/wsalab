package edu.lab.wsalab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.lab.wsalab.document.Document;

public class VocabBuilder {

	List<String> build(List<Document> docs) {
		Set<String> vocabs = new HashSet<>();
		for (Document doc : docs) {
			vocabs.addAll(Arrays.asList(doc.getProcessedText().split(" ")));
		}
		List<String> sortedVocab = new ArrayList<>(vocabs);
		Collections.sort(sortedVocab);
		return sortedVocab;
	}
}
