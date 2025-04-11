# Similarity Search

Author: Nathan Buyrchiyev  
Version: Java 17

---

## Description

This is a console-based Java application that allows users to perform similarity searches using word embeddings.

The application reads a user-specified `.txt` file containing words and their corresponding vector values (e.g., GloVe-style word embeddings). Users can then select a word, and the system returns the most similar words based on cosine similarity.

---

## How to Run

1. Compile or navigate to the JAR file directory.
2. Run the program using the following command:

```bash
java -cp ./dsa.jar ie.atu.sw.Runner
```

3. Follow the menu prompts to:
   - Load an embedding file.
   - Set an output file directory (optional).
   - Choose a word to search.
   - Select the number of similar words to display.
   - Choose the sorting order (most to least similar or vice versa).

> 🔸 If no output file is specified, the default is `./out.txt`.  
> 🔸 If no embedding file is selected, the search feature will not be available.

---

## Features

- Load a custom text file containing word embeddings.
- Search for a specific word in the embedding space.
- Choose the number of most similar words to output.
- Save results to a specified output file.
- Option to sort results from most to least similar (or vice versa).
- Uses cosine similarity to compare word vectors.

---

## Sample Output

```
Input Word: king  
Top 5 Similar Words:
1. queen (0.87)
2. prince (0.85)
3. emperor (0.82)
4. monarch (0.81)
5. ruler (0.80)
```

---

##  Technologies Used

- Java 17
- Cosine Similarity
- File I/O and Menu-based UI

---

## Notes

This project was developed as part of a coursework assignment focused on applying data structures and algorithms to real-world problems, emphasizing performance and memory management for large files, and as a gateway into AI.

---



---
