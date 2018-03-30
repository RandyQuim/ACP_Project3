import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * A class to handle Action Events related to the text area. Opens a file and
 * inputs its text onto the text field, saves the contents of the text area to
 * file, and spell checks the contents of the text area through use of menu
 * items "Open", "Save", and "Spell Check", respectively. The spell check logic
 * is in a separate, inner class - GenerateSuggestions - within this class.
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 3 File Name: TextFieldHandler.java
 */
public class TextFieldHandler implements EventHandler<ActionEvent> {
	/**
	 * The Dictionary object to check a dictionary of words
	 */
	private Dictionary dictionary;
	/**
	 * The TextArea object needed to get and append text to the text area
	 */
	private TextArea textArea;
	/**
	 * The Scanner object for reading a text file
	 */
	private Scanner fileIn;
	/**
	 * The PrintWriter object for writing to a text file
	 */
	private PrintWriter fileOut;
	/**
	 * The primary stage to produce a dialogue on for opening and saving a text file
	 */
	private Stage stage;

	/**
	 * Constructs a TextFieldHandler object that takes in the text area and dictionary objects
	 *
	 * @param textArea the TextArea object needed to get and append text
	 * @param dictionary the dictionary object to check a dictionary of words
	 */
	public TextFieldHandler(TextArea textArea, Dictionary dictionary, Stage stage) {
		this.textArea = textArea;
		this.dictionary = dictionary;
		this.fileIn = null;
		this.fileOut = null;
		this.stage = stage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.event.EventHandler#handle(javafx.event.Event) Tests whether
	 * menu items "Open", "Spell Check", or "Save" was chosen and calls methods
	 * related to each.  Added a FileChooser per instruction.
	 */
	@Override
	public void handle(ActionEvent event) {
		MenuItem mItem = (MenuItem) event.getSource();
		String item = mItem.getText();
		FileChooser fileChooser = new FileChooser();
		if (item.equalsIgnoreCase("Open")) {
			fileChooser.setTitle("Open Text File");
		    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All text files", "*.txt"));
			File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                openReader(file);
                applyFileText();
            }
		} else if (item.equalsIgnoreCase("Spell Check")) {
			GenerateSuggestions start = new GenerateSuggestions();
			start.spellCheck();
		} else if (item.equalsIgnoreCase("Save")) {
            fileChooser.setTitle("Save Text File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All text files", "*.txt"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null){
            	openWriter(file);
            	saveToFile();
            }
		}

	}

	/**
	 * Inner class to handle spell check logic
	 *
	 * @author Randy Quimby
	 * @version 1.0
	 *
	 *          COP4027 Project#: 3 File Name: GenerateSuggestions.java
	 */
	private class GenerateSuggestions {
		/**
		 * Constant for the beginning of a range of characters to delete
		 */
		private static final int FIRST_CHAR = 2;
		/**
		 * Constant for the end of a range of characters to delete
		 */
		private static final int SECOND_CHAR = 4;
		/**
		 * The StringBuilder object to insert and delete characters for spell check
		 */
		private StringBuilder strBuilder;
		/**
		 * The new word to be tested for a match to the dictionary
		 */
		private String newWord;
		/**
		 * The ArrayList of suggested words in the dictionary
		 */
		private ArrayList<String> suggestions;

		/**
		 * Constructs a GenerateSuggestions object that instantiates a new ArrayList
		 */
		public GenerateSuggestions() {
			suggestions = new ArrayList<String>();
		}

		/**
		 * Acquires the text in the text area and establishes whether each word
		 * is contained in the dictionary
		 */
		public void spellCheck() {
			String[] words = textArea.getText().split(" ");
			for (String word : words) {
				suggestions.clear();
				// Accounts for white-space
				if (word.equals(""))
					continue;
				if (String.valueOf(word.substring(word.length() - 1)).matches("[.]|[!]|[?]|[,]")) {
					word = word.substring(0, word.length() - 1);
				}
				if (!dictionary.containsWord(word.toLowerCase())) {
					displayNoMatch(word);
				}
			}
		}

		/**
		 * Displays to console if there was a word not found in the dictionary
		 *
		 * @param word the misspelled word
		 */
		private void displayNoMatch(String word) {
			System.out.println("Spelling not in dictionary. " + "\n\"" + word + "\"\nGenerating suggestions...");
			oneLetterMissing(word);
		}

		/**
		 * Tests the word for a missing letter
		 *
		 * @param word the misspelled word
		 */
		private void oneLetterMissing(String word) {
			strBuilder = new StringBuilder(word);
			String letters = "abcdefghijklmnopqrstuvwxyz";
			char[] alphabet = letters.toCharArray();
			for (int i = 0; i <= word.length(); i++) {
				// Inner loop to apply the alphabet to each position in the word
				for (int j = 0; j < alphabet.length; j++) {
					newWord = strBuilder.insert(i, alphabet[j]).toString();
					if (dictionary.containsWord(newWord.toLowerCase())
							&& !suggestions.contains(newWord.toLowerCase())) {
						addSuggestion(newWord.toLowerCase());
					}

					newWord = strBuilder.deleteCharAt(i).toString();
				}
			}

			oneLetterAdded(word);
		}

		/**
		 * Tests the word for a letter added
		 *
		 * @param word the misspelled word
		 */
		private void oneLetterAdded(String word) {
			char temp = '\u0000';
			for (int i = 0; i < word.length(); i++) {
				temp = word.charAt(i);
				newWord = strBuilder.deleteCharAt(i).toString();
				if (dictionary.containsWord(newWord.toLowerCase()) && !suggestions.contains(newWord.toLowerCase())) {
					addSuggestion(newWord.toLowerCase());
				}

				newWord = strBuilder.insert(i, temp).toString();
			}

			lettersReversed(word);
		}

		/**
		 * Reverses letters within the word and tests for matches in the dictionary
		 *
		 * @param word the misspelled word
		 */
		private void lettersReversed(String word) {
			String char1 = "";
			String char2 = "";
			for (int i = 0; i < word.length() - 1; i++) {
				strBuilder = new StringBuilder(word);
				char1 = String.valueOf(word.charAt(i + 1));
				char2 = String.valueOf(word.charAt(i));
				// Reversing the letters and inserting them with new format
				strBuilder.insert(i, char1).toString();
				strBuilder.insert(i + 1, char2).toString();
				// Deleting the letter's old positions
				strBuilder.delete(i + FIRST_CHAR, i + SECOND_CHAR).toString();
				newWord = strBuilder.toString();
				if (dictionary.containsWord(newWord.toLowerCase()) && !suggestions.contains(newWord.toLowerCase())) {
					addSuggestion(newWord.toLowerCase());
				}
			}

			displaySuggestions();
		}

		/**
		 * If the list contains words, the method displays the array list of suggestions
		 */
		private void displaySuggestions() {
			if (suggestions.size() == 0) {
				displayNoSuggestions();
			} else {
				System.out.println(suggestions + "\n");
			}
		}

		/**
		 * Displays there were no suggestions found
		 */
		private void displayNoSuggestions() {
			System.out.println("No suggestions were found\n");
		}

		/**
		 * Adds a suggestion to the array list
		 *
		 * @param word the misspelled word
		 */
		private void addSuggestion(String word) {
			suggestions.add(word);
		}
	}

	/**
	 * Applies the text within an input file to the text area
	 */
	private void applyFileText() {
		while (fileIn.hasNext()) {
			textArea.appendText(fileIn.next() + " ");
		}

	}

	/**
	 * Saves the text area to file
	 */
	private void saveToFile() {
		fileOut.write(textArea.getText());
		fileOut.flush();
	}

	/**
	 * Opens the input file
	 */
	private void openReader(File file) {
		try {
			if (fileOut != null) {
				fileOut.close();
			}
			fileIn = new Scanner(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the output file
	 */
	private void openWriter(File file) {
		try {
			if (fileIn != null) {
				fileIn.close();
			}
			fileOut = new PrintWriter(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
