package edu.lab.wsalab.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Utilities {

	public static List<Double> multiply(List<Integer> list1, List<Double> list2) {
		List<Double> products = new ArrayList<>();
		for (int i = 0; i < list1.size(); i++) {
			products.add(list1.get(i) * list2.get(i));
		}
		return products;
	}

	public static int sumValues(Map<String, Integer> map) {
		int sum = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sum += entry.getValue();
		}
		return sum;
	}

	public static Map<String, Integer> sortByIntegerValue(Map<String, Integer> unsortedMap) {
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		Map<String, Integer> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static Map<Integer, Double> sortByDoubleValue(Map<Integer, Double> unsortedMap) {
		List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(unsortedMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		Map<Integer, Double> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<Integer, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
