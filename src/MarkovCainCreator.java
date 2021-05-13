import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MarkovCainCreator {
    private String firstWord;
    private String secondWord;
    private String combinedWords;
    private Map<String, ArrayList<String>> wordMap = new HashMap<>();
    private Scanner scanner;
    private String currentString;
    private String resultString;

    public MarkovCainCreator (InputStream file) {
        scanner = new Scanner(file);
    }

    public void MarkovChain() {
        firstWord = scanner.next();

        while (scanner.hasNext()) {
            secondWord = scanner.next();
            if (secondWord.endsWith(".")) { // ???
                break;
            }

            combinedWords = firstWord + " " + secondWord;

            if (scanner.hasNext()) {
                firstWord = scanner.next();

                if (!wordMap.containsKey(combinedWords)) {
                    wordMap.put(combinedWords, new ArrayList<>());
                }
                wordMap.get(combinedWords).add(firstWord);
            }
        }
    }

    public void print() {
        currentString = "ett ord";
        resultString = currentString;

        // for sålänge mindre än 280 tecken
        // Array possiblities = de två sista ordens värde
        // next = välj en random
        // resultString += next
    }
}
