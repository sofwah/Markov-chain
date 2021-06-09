import java.io.InputStream;
import java.util.*;

public class MarkovChainCreator {
    private Scanner scanner;
    private int maxLength = 280;
    private String firstWord;
    private String secondWord;
    private String thirdWord;
    private String combinedWords;
    private Map<String, ArrayList<String>> wordMap = new HashMap<>(); // ett ord -> två ord, det sista av de två orden -> nästa två ord, osv
    private List<String> sentenceStarters = new ArrayList<>();
    private String currentString;
    private String nextString;
    private String resultString;
    private ArrayList<String> possibilities;
    private StringBuffer sb;
    private String returnString;

    public MarkovChainCreator(InputStream file) {
        scanner = new Scanner(file);
    }

    public void createMarkovChain() {
        firstWord = scanner.next();

        while (scanner.hasNext()) {
            secondWord = scanner.next();

            if (scanner.hasNext()) {
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

                if (firstWord.endsWith(".") || firstWord.endsWith("!") || firstWord.endsWith("?")) {
                    sentenceStarters.add(secondWord); //om föregående ord slutar med punkt är nästa ord en meningsstartare
                } //this should maybe be moved to earlier in the loop

                firstWord = thirdWord;
            }
        }
        //the above loop is currently after each loop giving firstWord the value of thirdWord, could be changed to secondWord to get a better chain and more keys in the map
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
