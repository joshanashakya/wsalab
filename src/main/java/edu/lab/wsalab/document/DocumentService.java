package edu.lab.wsalab.document;

import java.util.List;
import java.util.Map;

/**
 * @author Joshana Shakya
 *
 */
public interface DocumentService {

	/**
	 * Create document from query or raw text
	 * 
	 * @param text
	 * @return document with its property id, text, and preprocessed text set
	 */
	Document create(int id, String text);

	/**
	 * Extract document from provided folder
	 * 
	 * @param folderName
	 * @return list of documents created from files
	 */
	List<Document> extract(String folderName);

	/**
	 * Parse text with tags and populate Document
	 * 
	 * @param text
	 * @return document with its properties set
	 */
	Document parse(String text);

	/**
	 * Combine document title, author, biblio and preprocessed text and add them to
	 * content field of document
	 * 
	 * @param document
	 * @return document with assigned proprocessed text
	 */
	Document process(Document doc);

	/**
	 * Calculate idfs and save in file
	 * Calculate tf-idf of each document
	 * 
	 * @param document
	 * @return list of documents with assigned weights
	 */
	List<Document> calWeight(List<Document> docs, List<String> vocab);

	/**
	 * Calculate tf-idf of single document
	 * 
	 * @param doc
	 * @param vocab
	 * @return document with weights assigned
	 */
	Document calWeight(Document doc, List<String> vocab, List<Double> idfs);
	
	/**
	 * 
	 * @param query
	 * @param idToWeights
	 * @return list of ids of documents ranked in descending order of their similarity with the query
	 */
	List<Integer> rank(Document query, Map<Integer, List<Double>> idToWeights);
}
