/**
     * @author Nathan Buyrchiyev
     * @JDK 22
*/
package ie.atu.sw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Runner {

	private static final int vectorSize = 50;
	private static Map<String, double[]> wordVectors = new HashMap<>();
	private static Scanner scanner = new Scanner(System.in);
	private static String outputFile = "./out.txt";
	private static boolean highToLow = true;

	public static void main(String[] args) throws Exception {
		displayMenu();
	}

	private static void displayMenu() throws Exception {
		while (true) {
			System.out.println(ConsoleColour.WHITE);
			System.out.println("************************************************************");
			System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
			System.out.println("*                                                          *");
			System.out.println("*          Similarity Search with Word Embeddings          *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");
			System.out.println("1. Specify Embedding File");
			System.out.println("2. Specify Output File (default is './out.txt')");
			System.out.println("3. Find Similar Words");
			System.out.println("4. Options");
			System.out.println("5. Quit");
			System.out.print("Enter your choice: ");

			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
			case 1:
				loadWordVectors();
				break;
			case 2:
				specifyOutputFile();
				break;
			case 3:
				findSimilarWords();
				break;
			case 4:
				optionsMenu();
				break;
			case 5:
				System.exit(0);
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/**
	 * Breakdown of the optionsMenu method: Big O notation is constant time
	 * complexity. All operations inside the method operate within constant time.
	 */

	// Time complexity: O(1)
	private static void optionsMenu() throws IOException {
		System.out.println(
				"Do you wish to populate the output file from highest similarity or from lowest similarity? \nInput 0 for highest, 1 for lowest: ");
		int optionChoice = Integer.parseInt(scanner.nextLine());
		switch (optionChoice) {
		case 0:
			highToLow = true;
			break;
		case 1:
			highToLow = false;
			break;
		default:
			System.out.println("Invalid choice. Please try again.");
			break;
		}
	}

	/**
	 * Breakdown of the loadWordVectors method: The user inputs the directory of the
	 * embedding file which is stored into a variable. A BufferedReader object is
	 * created to read the characters from the file specified. I initially used
	 * FileReader but that was less efficient seeing as you can only read one
	 * character one by one as opposed to BufferedReader. A String named line is
	 * created to store each line read from the file. The while loop continues to
	 * read from the file until the end, separating the each line into parts
	 * whenever it finds a comma, (being the vector values), and storing them into
	 * an array. The stored vectors then get parsed into a double so they are no
	 * longer strings. The word and its corresponding vector is stored into the
	 * wordVectors map.
	 * 
	 * Big O notation is linear time complexity with respect to the number of
	 * embeddings in the file.
	 */

	// Time complexity: O(n), where n is the number of words in the word vectors
	// file
	private static void loadWordVectors() {
		System.out.println("Enter the directory of the embedding file:");
		String filePath = scanner.nextLine().trim();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(", ");
				String word = parts[0];
				double[] vector = new double[vectorSize];
				for (int i = 1; i < parts.length; i++) {
					vector[i - 1] = Double.parseDouble(parts[i]);
				}
				wordVectors.put(word, vector);
			}
			reader.close();

			System.out.println("Word vectors loaded successfully.");
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Please enter a valid directory.");
		} catch (IOException e) {
			System.out.println("An error occurred while reading the file.");
		}
	}

	/**
	 * Breakdown of specifyOutputFile method: Simply asks the user to set a
	 * directory for the output file. The user given output is saved into a
	 * variable. That variable is then used later in a FileWriter object to create a
	 * file with the variable's name. If this method is never called, outputFile is
	 * set as "./out.txt", set at the beginning of the code.
	 *
	 * Big O notation is constant time. All operations run in constant time.
	 */

	// Time complexity: O(1)
	private static void specifyOutputFile() {
		System.out.println("Please enter the directory for your output file:");
		outputFile = scanner.nextLine().trim();
		System.out.println("Output file path set.");
	}

	/**
	 * Breakdown of findSimilarWords method: The user is asked to input a word and
	 * that word is saved to a variable. If the word is not in the wordVector map,
	 * it asks the user to input another word. The user is then asked to enter how
	 * many similar words should be printed on the output file. A HashMap is then
	 * created to store key-value pairs, where each key is a word and each value is
	 * its similarity score. A variable called inputVector collects the vector
	 * values from the input word. A for loop is created to loop over everything
	 * stored in the wordVectors map. It stores the word and vectors of each word
	 * into separate variables and uses them to calculate the similarity value to
	 * the input word. The similarity score is then stored into its own map. The
	 * input word is removed from the similarity score map.
	 *
	 * A new List map is created to sort the similar words in order of most similar.
	 * Using reverseOrder, it lists from highest to lowest, as opposed to
	 * naturalOrder which does the opposite.
	 *
	 * Using FileWriter, the output file is created and uses the sortedSimilarWords
	 * map to populate the text file with the most similar words.
	 *
	 * Big O notation is linear as there are 'n' number in the word vectors map
	 */

	// Time complexity: O(n), where n is the number of words in the word vectors map
	private static void findSimilarWords() throws IOException {
		System.out.print("Enter a word: ");
		String inputWord = scanner.nextLine().trim().toLowerCase();

		if (!wordVectors.containsKey(inputWord)) {
			System.out.println("Word not found, please enter another.");
			return;
		}

		System.out.print("Enter the number of similar words to find: ");
		int numSimilarWords = Integer.parseInt(scanner.nextLine());

		Map<String, Double> similarWords = new HashMap<>();
		double[] inputVector = wordVectors.get(inputWord);
		for (Map.Entry<String, double[]> entry : wordVectors.entrySet()) {
			String word = entry.getKey();
			double[] vector = entry.getValue();
			double similarity = calculateCosineSimilarity(inputVector, vector);
			similarWords.put(word, similarity);
		}

		similarWords.remove(inputWord);

		List<Map.Entry<String, Double>> sortedSimilarWords = new ArrayList<>(similarWords.entrySet());
		if (highToLow == true)
			sortedSimilarWords.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		else if (highToLow == false)
			sortedSimilarWords.sort(Map.Entry.comparingByValue(Comparator.naturalOrder()));

		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write("Similar words for '" + inputWord + "':\n");
			for (int i = 0; i < numSimilarWords && i < sortedSimilarWords.size(); i++) {
				Map.Entry<String, Double> entry = sortedSimilarWords.get(i);
				writer.write(entry.getKey() + " : " + entry.getValue() + "\n");
			}
		}

		System.out.println("Similar words written to the output file.");
	}

	/**
	 * This method calculates the cosine similarity between two vectors. It starts
	 * by multiplying each vector of the input word and the word in the
	 * word-embeddings.txt and storing it into the dotProduct. This keeps going
	 * until the vectors in the input word reach the end. Using the dotProduct, we
	 * divide it by the square root of the lengths of List1 times the length of
	 * List. This returns us with a value between -1 and 1. The closer the returning
	 * value is to 1 means the more similar the scanned word in the list and the
	 * inputed word are.
	 * 
	 * Big O notation is linear.
	 * 
	 * This method was not originally made by me, I found it on a StackOverflow
	 * thread.
	 * https://stackoverflow.com/questions/520241/how-do-i-calculate-the-cosine-similarity-of-two-vectors
	 */

	// Time complexity: O(n), where n is size of the vectors
	private static double calculateCosineSimilarity(double[] vec1, double[] vec2) {
		double dotProduct = 0.0;
		double normVec1 = 0.0;
		double normVec2 = 0.0;
		for (int i = 0; i < vec1.length; i++) {
			dotProduct += vec1[i] * vec2[i];
			normVec1 += Math.pow(vec1[i], 2);
			normVec2 += Math.pow(vec2[i], 2);
		}
		if (normVec1 == 0 || normVec2 == 0) {
			return 0.0;
		}
		return dotProduct / (Math.sqrt(normVec1) * Math.sqrt(normVec2));
	}
}
