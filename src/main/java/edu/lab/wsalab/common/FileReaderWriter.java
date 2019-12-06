package edu.lab.wsalab.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.lab.wsalab.FileFormat;

/**
 * @author Joshana Shakya
 *
 *         Responsible for read from and writing to files. Methods with string
 *         Data e.g. readDataFile, reads/writes values in integer or double
 *         form.
 */
public class FileReaderWriter {

	public static <T> void writeToFile(List<T> list, String path) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			for (T l : list) {
				writer.write(String.valueOf(l));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes to file in the format e.g. 1: 20, 30, 12, 15, 24.
	 * 
	 * @param map
	 * @param path
	 */
	public static void writeToFile(Map<Integer, List<Integer>> map, String path) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(entry.getKey());
				sb.append(": ");
				List<Integer> value = entry.getValue();
				int size = value.size();
				for (int i = 0; i < size; i++) {
					sb.append(value.get(i));
					if (i != size - 1) {
						sb.append(", ");
					}
				}
				writer.write(sb.toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> readFiles(String folderName) {
		String path = getPath(folderName);
		File[] files = new File(path).listFiles();
		List<String> fileContents = new ArrayList<>();
		for (File file : files) {
			fileContents.add(readFile(file));
		}
		return fileContents;
	}

	public static String readFile(String fileName) {
		String path = getPath(fileName);
		return readFile(new File(path));
	}

	public static Map<Integer, List<Double>> readDataFiles(String path) {
		File[] files = new File(path).listFiles();
		Map<Integer, List<Double>> idToData = new LinkedHashMap<>();
		for (File file : files) {
			idToData.put(Integer.valueOf(file.getName().trim()), readDataFile(file));
		}
		return idToData;
	}

	public static List<String> readFileAndList(String fileName) {
		String path = fileName.startsWith(FileFormat.FILES) ? fileName : getPath(fileName);
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

	public static List<String> readFileAndList(String fileName, boolean isQuery) {
		String path = getPath(fileName);
		List<String> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String str;
			StringBuilder sb = new StringBuilder();
			while ((str = reader.readLine()) != null) {
				if (Character.isDigit(str.charAt(0)) && sb.length() != 0) {
					list.add(sb.toString());
					sb = new StringBuilder();
				}
				sb.append(" ");
				sb.append(str);
			}
			list.add(sb.toString());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Double> readDataFile(String path) {
		return readDataFile(new File(path));
	}

	private static String readFile(File file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
				if (str.startsWith("<")) {
					continue;
				}
				sb.append(" ");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static List<Double> readDataFile(File file) {
		List<Double> list = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String str;
			while ((str = reader.readLine()) != null) {
				str = str.trim();
				list.add(Double.valueOf(str));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static String getPath(String name) {
		return Utilities.class.getClassLoader().getResource(name).getPath().replaceAll("%20", " ");
	}
}
