package edu.lab.wsalab;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import edu.lab.wsalab.common.FileReaderWriter;
import edu.lab.wsalab.common.Preprocessor;
import edu.lab.wsalab.document.Document;
import edu.lab.wsalab.document.DocumentService;
import edu.lab.wsalab.document.DocumentServiceImpl;

/**
 * @author Joshana Shakya
 *
 */
public class Application {

	private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

	public static void main(String[] args) {
		Application app = new Application();
		System.out.println("Options:\n1. Index\n2. Query\n3. Measure Performance\nEnter[1 or 2 or 3]:");
		Scanner sc = new Scanner(System.in);
		int option = sc.nextInt();
		switch (option) {
		case 1:
			app.index();
			break;
		case 2:
			app.query();
			break;
		case 3:
			app.measurePerformance();
			break;
		default:
			System.exit(0);
		}
		sc.close();
	}

	public void measurePerformance() {
		List<String> predicted = FileReaderWriter.readFileAndList(String.format(FileFormat.PREDICTION, 10));
		List<String> actual = FileReaderWriter.readFileAndList(FileFormat.RESULT);
		int size = actual.size();
		Preprocessor preProcessor = new Preprocessor();
		for (int i = 0; i < size; i++) {
			List<Integer> p = preProcessor.clean(predicted.get(i));
			List<Integer> a = preProcessor.clean(actual.get(i));
			int retrievedSize = p.size();
			int relevantSize = a.size();
			p.retainAll(a);
			double precision = p.size() / (double) retrievedSize;
			double recall = p.size() / (double) relevantSize;
			System.out.format("Query Id: %d\n Precison: %.2f, Recall: %.2f\n", (i + 1), precision, recall);
		}
	}

	public void query() {
		LOGGER.info("Started querying.");
		List<String> queries = FileReaderWriter.readFileAndList(FileFormat.QUERIES_PATH, true);
		Map<Integer, List<Integer>> idToDocId = new LinkedHashMap<>();
		int size = 500;
		for (String query : queries) {
			String[] arr = query.split("\\.");
			int id = Integer.valueOf(arr[0].trim());
			String value = arr[1];
			List<Integer> docIds = query(id, value).subList(0, size);
			idToDocId.put(id, docIds);
		}
		FileReaderWriter.writeToFile(idToDocId, String.format(FileFormat.RESULT, size));
		LOGGER.info("Completed querying.");
	}

	public List<Integer> query(int queryNo, String query) {
		DocumentService docService = new DocumentServiceImpl();
		// create document
		Document doc = docService.create(queryNo, query);
		// read vocab.txt
		List<String> vocab = FileReaderWriter.readFileAndList(FileFormat.VOCABULARY);
		// read inverse document frequency
		List<Double> idfs = FileReaderWriter.readDataFile(FileFormat.DOCS_IDF);

		doc = docService.calWeight(doc, vocab, idfs);

		// read resource document weights
		Map<Integer, List<Double>> idToWeights = FileReaderWriter.readDataFiles(FileFormat.DOC_WEIGHTS_READ);
		// rank documents
		return docService.rank(doc, idToWeights);
	}

	public void index() {
		LOGGER.info("Started indexing documents.");
		DocumentService docService = new DocumentServiceImpl();
		List<Document> docs = docService.extract(FileFormat.DOCS_PATH);

		// create vocabulary file
		VocabBuilder builder = new VocabBuilder();
		List<String> vocab = builder.build(docs);
		FileReaderWriter.writeToFile(vocab, FileFormat.VOCABULARY);

		// calculate weight of each document
		List<Document> docsWithWeight = docService.calWeight(docs, vocab);
		LOGGER.info("Completed indexing documents.");
	}
}
