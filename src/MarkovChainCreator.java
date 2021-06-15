import java.io.InputStream;
import java.util.*;

public class MarkovChainCreator {
    private Scanner scanner;
    private int maxLength = 280;
    private String firstWord, secondWord, thirdWord, combinedWords, currentString, nextString, resultString, returnString;
    private Map<String, ArrayList<String>> wordMap = new HashMap<>(); // one word -> two words, the last out of the two words -> next two words, and so on
    private List<String> sentenceStarters = new ArrayList<>();
    private ArrayList<String> possibilities;
    private StringBuffer sb;

    public MarkovChainCreator(InputStream file) {
        scanner = new Scanner(file);
    }

    public void createMarkovChainOld() {
        firstWord = scanner.next();

        if (scanner.hasNext()) {
            secondWord = scanner.next();

            if (firstWord.endsWith(".") || firstWord.endsWith("!") || firstWord.endsWith("?")) { //needed in case first sentence is only one word
                sentenceStarters.add(secondWord); //if the previous word ends with .!? the next word is a sentence starter
            }

            while (scanner.hasNext()) {
                thirdWord = scanner.next();
                if (secondWord.endsWith(".") || secondWord.endsWith("!") || secondWord.endsWith("?")) {
                    combinedWords = secondWord;
                } else {
                    combinedWords = secondWord + " " + thirdWord;
                }

                if (!wordMap.containsKey(firstWord)) {
                    wordMap.put(firstWord, new ArrayList<>());
                }
                wordMap.get(firstWord).add(combinedWords);

                if (secondWord.endsWith(".") || secondWord.endsWith("!") || secondWord.endsWith("?")) {
                    sentenceStarters.add(thirdWord); //if the previous word ends with .!? the next word is a sentence starter
                }

                firstWord = secondWord;
                secondWord = thirdWord;
            }
        }
    }

    public void createMarkovChain() {
        firstWord = scanner.next();

        if (scanner.hasNext()) { //previously while loop
            secondWord = scanner.next();

            if (firstWord.endsWith(".") || firstWord.endsWith("!") || firstWord.endsWith("?")) { //needed in case first sentence is only one word, recently added with the other changes
                sentenceStarters.add(secondWord); //if the previous word ends with .!? the next word is a sentence starter
            }

            while (scanner.hasNext()) { //previously if statement
                thirdWord = scanner.next();
                if (secondWord.endsWith(".") || secondWord.endsWith("!") || secondWord.endsWith("?")) {
                    combinedWords = secondWord;
                } else {
                    combinedWords = secondWord + " " + thirdWord;
                }

                if (!wordMap.containsKey(firstWord)) {
                    wordMap.put(firstWord, new ArrayList<>());
                }
                wordMap.get(firstWord).add(combinedWords);

                if (secondWord.endsWith(".") || secondWord.endsWith("!") || secondWord.endsWith("?")) { //previously firstWord
                    sentenceStarters.add(thirdWord); //if the previous word ends with .!? the next word is a sentence starter, prev secondWord
                }

                firstWord = secondWord; //previously firstWord = thirdWord
                secondWord = thirdWord;
            }
        }
    }

    public String generateText() {
        returnString = generateTextHelper();
        while ((resultString.split(" ").length <= 1) || (resultString.split(" ").length > 280)) {
            returnString = generateTextHelper();
            if ((resultString.split(" ").length <= 280) && (Math.random() < 0.1)) { // sometimes lets through one word outputs (10% of the times)
                break;
            }
        }
        return returnString;
    }

    public String generateTextHelper() {
        currentString = sentenceStarters.get((int)(Math.random() * sentenceStarters.size())); // get random sentence starter
        resultString = currentString;

        while (resultString.length() <= maxLength) {
            if (!wordMap.containsKey(currentString)) { //avoids nullPointerException in case of empty list
                break;
            }

            possibilities = wordMap.get(currentString);
            if (currentString.endsWith(".") || currentString.endsWith("?") || currentString.endsWith("!")) {
                nextString = sentenceStarters.get((int)(Math.random() * sentenceStarters.size())); // get random sentence starter
            } else {
                nextString = possibilities.get((int)(Math.random() * possibilities.size())); //chooses a random out of possible next words
            }

            resultString += " " + nextString;

            if (resultString.length() > maxLength) { // if over max length, break and the string will be re-done through the textGenerator method
                break;
            }

            if ((resultString.endsWith(".") || resultString.endsWith("!") || resultString.endsWith("?")) && (Math.random() < 0.80)) { // 20% chance that it continues after one sentence
                if (resultString.endsWith(".")) { // deletes punctuation in the end to fit tweet theme
                    sb = new StringBuffer(resultString);
                    sb.deleteCharAt(sb.length()-1);
                    resultString = sb.toString();
                }
                break;
            }

            currentString = resultString.substring(resultString.lastIndexOf(" ") + 1);
        }

        if (resultString.endsWith(".")) { // deletes punctuation in the end to fit tweet theme
            sb = new StringBuffer(resultString);
            sb.deleteCharAt(sb.length()-1);
            resultString = sb.toString();
        }

        return resultString;
    }
}
