package edu.lab.wsalab.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.lab.wsalab.FileFormat;
import edu.lab.wsalab.common.FileReaderWriter;
import edu.lab.wsalab.common.Preprocessor;
import edu.lab.wsalab.common.Utilities;

/**
 * @author Joshana Shakya
 *
 */
public class DocumentServiceImpl implements DocumentService {

	@Override
	public Document create(int id, String text) {
		Document doc = new Document();
		doc.setId(id);
		doc.setText(text);
		doc = process(doc);
		return doc;
	}

	@Override
	public List<Document> extract(String folderName) {
		List<String> fileContents = FileReaderWriter.readFiles(folderName);
		List<Document> docs = new ArrayList<>();
		for (String content : fileContents) {
			Document doc = parse(content);
			doc = process(doc);
			docs.add(doc);
		}
		return docs;
	}

	@Override
	public Document parse(String text) {
		String pattern = "(<DOC>(<DOCNO>.*</DOCNO>)(<TITLE>.*</TITLE>)(<AUTHOR>.*</AUTHOR>)"
				+ "(<BIBLIO>.*</BIBLIO>)(<TEXT>.*</TEXT>)</DOC>)";
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(text);
		Document doc = new Document();
		while (matcher.find()) {
			doc.setId(Integer.valueOf(getContent(matcher.group(2))));
			doc.setTitle(getContent(matcher.group(3)));
			doc.setAuthor(getContent(matcher.group(4)));
			doc.setBiblio(getContent(matcher.group(5)));
			doc.setText(getContent(matcher.group(6)));
		}
		return doc;
	}

	@Override
	public Document process(Document doc) {
		Preprocessor preprocessor = new Preprocessor();
		String text = preprocessor.preprocess(doc.getText());
		StringBuilder sb = new StringBuilder();
//		sb.append(doc.getTitle());
//		sb.append(" ");
//		sb.append(doc.getAuthor());
//		sb.append(" ");
//		sb.append(doc.getBiblio());
//		sb.append(" ");
		sb.append(text);
		doc.setProcessedText(sb.toString());
		return doc;
	}

	@Override
	public List<Document> calWeight(List<Document> docs, List<String> vocab) {
		// calculate document frequency
		List<Double> idfs = calIdfs(docs, vocab);
		FileReaderWriter.writeToFile(idfs, FileFormat.DOCS_IDF);

		// calculate TF-IDF
		for (Document doc : docs) {
			List<Integer> tfs = calTfs(doc, vocab);
			List<Double> weights = Utilities.multiply(tfs, idfs);
			doc.setWeights(weights);
			FileReaderWriter.writeToFile(weights, String.format(FileFormat.DOC_WEIGHTS_WRITE, doc.getId()));
		}
		return docs;
	}

	@Override
	public Document calWeight(Document doc, List<String> vocab, List<Double> idfs) {
		List<Integer> tfs = calTfs(doc, vocab);
		List<Double> weights = Utilities.multiply(tfs, idfs);
		doc.setWeights(weights);
		return doc;
	}

	@Override
	public List<Integer> rank(Document query, Map<Integer, List<Double>> idToWeights) {
		// create map of doc-id and similarity cal with query
		Map<Integer, Double> idToCosSim = new HashMap<>();
		for (Map.Entry<Integer, List<Double>> entry : idToWeights.entrySet()) {
			int id = entry.getKey();
			double value = similarity(query.getWeights(), entry.getValue());
			idToCosSim.put(id, value);
		}
		Map<Integer, Double> sortedIdToCosSim = Utilities.sortByDoubleValue(idToCosSim);
		List<Integer> ids = new ArrayList<>();
		for (Map.Entry<Integer, Double> entry : sortedIdToCosSim.entrySet()) {
			ids.add(entry.getKey());
		}
		return ids;
	}

	private double similarity(List<Double> queryWeights, List<Double> docWeights) {
		int size = queryWeights.size();
		// compute numerator
		double dotProd = 0;
		double sqQuery = 0;
		double sqDoc = 0;
		for (int i = 0; i < size; i++) {
			double queryWeight = queryWeights.get(i);
			double docWeight = docWeights.get(i);
			dotProd = dotProd + (queryWeight * docWeight);
			sqQuery = sqQuery + (queryWeight * queryWeight);
			sqDoc = sqDoc + (docWeight * docWeight);
		}
		// compute denominator
		double normQuery = Math.sqrt(sqQuery);
		double normDoc = Math.sqrt(sqDoc);
		return dotProd / (normQuery * normDoc);
	}

	private List<Double> calIdfs(List<Document> docs, List<String> vocab) {
		int n = docs.size();
		List<Double> idfs = new ArrayList<>();
		for (String word : vocab) {
			int count = getCount(docs, word);
			double idf = Math.log10(n / (double) count);
			idfs.add(idf);
		}
		return idfs;
	}

	private int getCount(List<Document> docs, String word) {
		int count = 0;
		for (Document doc : docs) {
			String text = doc.getProcessedText();
			if (text.contains(word)) {
				count += 1;
			}
		}
		return count;
	}

	private List<Integer> calTfs(Document doc, List<String> vocab) {
		List<Integer> tfs = new ArrayList<>();
		for (String word : vocab) {
			String text = doc.getProcessedText();
			int splitCount = text.split(String.format("\\b%s\\b", word)).length;
			int count = text.endsWith(word) ? splitCount : splitCount - 1;
			tfs.add(count);
		}
		return tfs;
	}

	private String getContent(String str) {
		Pattern p = Pattern.compile("(<.*>(.*)</.*>)");
		Matcher matcher = p.matcher(str);
		String content = null;
		while (matcher.find()) {
			content = matcher.group(2).trim();
		}
		return content;
	}
}
