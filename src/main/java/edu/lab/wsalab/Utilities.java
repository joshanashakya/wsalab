package edu.lab.wsalab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Utilities {

	public static int sumValues(Map<String, Integer> map) {
		int sum = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sum += entry.getValue();
		}
		return sum;
	}

	public static String readFile(String fileName) {
		String path = Utilities.class.getClassLoader().getResource(fileName).getPath().replaceAll("%20", " ");
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
				sb.append(" ");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static List<String> readFileAndList(String fileName) {
		String path = Utilities.class.getClassLoader().getResource(fileName).getPath().replaceAll("%20", " ");
		List<String> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String str;
			while ((str = reader.readLine()) != null) {
				list.add(str);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Map<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {
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
}
