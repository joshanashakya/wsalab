package edu.lab.wsalab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.lab.wsalab.common.Utilities;

/**
 * @author Joshana Shakya
 *
 */
public class Counter {

	int minHalfWordCount(Map<String, Integer> wordToFreq) {
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

	Map<String, Integer> calWordToFreq(String[] words, int vocabSize) {
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
		return Utilities.sortByIntegerValue(wordToFreq);
	}

	int countUniqueWords(String[] words) {
		HashSet<String> set = new HashSet<>(Arrays.asList(words));
		return set.size();
	}
}
