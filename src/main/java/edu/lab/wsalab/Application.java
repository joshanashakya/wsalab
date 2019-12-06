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
		Scanner sc = new Scanner(System.in);
		System.out.println("Options:\n1. Index\n2. Query\n3. Measure Performance\nEnter[1 or 2 or 3]:");
		int option = sc.nextInt();
		switch (option) {
		case 1:
			app.index();
			break;
		case 2:
			System.out.println("Enter the number of relevant documents you wish to retrieve: ");
			app.query(sc.nextInt());
			break;
		case 3:
			System.out.println("Enter the number of documents to be retrieved (10, 50, 100, or 500): ");
			app.measurePerformance(sc.nextInt());
			break;
		default:
			System.exit(0);
		}
		sc.close();
	}

	/**
	 * Calculates precision and recall using result.txt file.
	 * 
	 * @param retrievedSize
	 */
	public void measurePerformance(int retrievedSize) {
		List<String> predicted = FileReaderWriter.readFileAndList(String.format(FileFormat.PREDICTION, retrievedSize));
		List<String> actual = FileReaderWriter.readFileAndList(FileFormat.RESULT);
		int size = actual.size();
		Preprocessor preProcessor = new Preprocessor();
		System.out.println("Query Id | Precision | Recall");
		System.out.println("-----------------------------");
		for (int i = 0; i < size; i++) {
			List<Integer> p = preProcessor.clean(predicted.get(i));
			List<Integer> a = preProcessor.clean(actual.get(i));
//			int retrievedSize = p.size();
			int relevantSize = a.size();
			p.retainAll(a);
			double precision = p.size() / (double) retrievedSize;
			double recall = p.size() / (double) relevantSize;
			System.out.format("%8d | %9.2f | %6.2f\n", (i + 1), precision, recall);
		}
	}

	/**
	 * Prints in console and writes in file the document ids relevant to the query.
	 * 
	 * @param size
	 */
	public void query(int size) {
		LOGGER.info("Started querying.");
		List<String> queries = FileReaderWriter.readFileAndList(FileFormat.QUERIES_PATH, true);
		Map<Integer, List<Integer>> idToDocId = new LinkedHashMap<>();
		for (String query : queries) {
			String[] arr = query.split("\\.");
			int id = Integer.valueOf(arr[0].trim());
			String value = arr[1];
			List<Integer> docIds = query(id, value).subList(0, size);
			idToDocId.put(id, docIds);
		}
		print(idToDocId, size);
		FileReaderWriter.writeToFile(idToDocId, String.format(FileFormat.RESULT, size));
		LOGGER.info("Completed querying.");
	}

	/**
	 * Creates document from given queryNo and query. Reads vocabulary file and IDF
	 * file. Calculates weight of words in vocabulary for that query document. Lists
	 * all documents in descending order of their relevance with query.
	 * 
	 * @param queryNo
	 * @param query
	 * @return
	 */
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

	/**
	 * Reads files from given folder. Extracts content and creates Document type
	 * object of each file. Pre-processes the properties of Document type object and
	 * store in single property. Constructs vocabulary collecting distinct words
	 * from all documents. Constructs IDF (Inverse Document Frequency) file for
	 * vocabulary. Calculates weight of each document and stores in file.
	 */
	public void index() {
		LOGGER.info("Started indexing documents.");
		DocumentService docService = new DocumentServiceImpl();
		List<Document> docs = docService.extract(FileFormat.DOCS_PATH);

		// create vocabulary file
		VocabBuilder builder = new VocabBuilder();
		List<String> vocab = builder.build(docs);
		FileReaderWriter.writeToFile(vocab, FileFormat.VOCABULARY);

		// calculate weight of each document
		docService.calWeight(docs, vocab);
		LOGGER.info("Completed indexing documents.");
	}

	private void print(Map<Integer, List<Integer>> idToDocId, int size) {
		System.out.format("Relevant documents size: %d\n", size);
		for (Map.Entry<Integer, List<Integer>> entry : idToDocId.entrySet()) {
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey());
			sb.append(": ");
			List<Integer> docIds = entry.getValue();
			for (Integer docId : docIds) {
				sb.append(docId);
				sb.append(" ");
			}
			System.out.println(sb.toString());
		}
	}
}
