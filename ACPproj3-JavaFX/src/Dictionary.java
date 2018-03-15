import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class to handle the dictionary. Opens the file and hashes the contents into
 * a set.
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 3 File Name: Dictionary.java
 */
public class Dictionary {
	/**
	 * Constant for the name of the dictionary text file to read
	 */
	private static final String DICTIONARY = "Words.txt";
	/**
	 * The Scanner object to read the file
	 */
	private Scanner file;
	/**
	 * The Hash Set of dictionary words
	 */
	private HashSet<String> dictionary;

	/**
	 * Constructs a Dictionary object and instantiates the hash set. Sets the
	 * file to its default value - null.
	 */
	public Dictionary() {
		this.file = null;
		this.dictionary = new HashSet<String>();
	}

	/**
	 * Opens the dictionary file for input
	 */
	public void open() {
		try {
			if (file != null) {
				file.close();
			}

			file = new Scanner(new FileInputStream(DICTIONARY));
			file.useDelimiter("\n|\r");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the dictionary file
	 */
	public void close() {
		if (file != null) {
			file.close();
		}

		file = null;
	}

	/**
	 * Generates the hash table with the words in the dictionary file
	 */
	public void generateHashTable() {
		while (file.hasNext()) {
			insert(file.next());
		}
	}

	/**
	 * Inserts a word into the hash table
	 *
	 * @param word the dictionary word
	 */
	private void insert(String word) {
		dictionary.add(word.toLowerCase());
	}

	/**
	 * Returns whether the dictionary contains a word
	 *
	 * @param word the word to hash
	 * @return the boolean value of the words existence in the dictionary
	 */
	public boolean containsWord(String word) {
		return dictionary.contains(word);
	}

}
