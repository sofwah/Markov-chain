import java.io.File;
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
    private String backupResultString;
    private ArrayList<String> possibilities;
    private StringBuffer sb;
    private boolean doAgain;

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
                }

                firstWord = thirdWord;
            }
        }
    }

    public String generateText() { // gives only one word pretty often
        doAgain = true;

        while (doAgain) { //re-do the text until okay

            currentString = sentenceStarters.get((int)(Math.random() * sentenceStarters.size())); // get random sentence starter
            resultString = currentString;

            while (resultString.length() <= maxLength) {
                backupResultString = resultString; // keeping a backup of resultString in case text gets too long

                if (!wordMap.containsKey(currentString)) { //fixed nullPointerException
                    break;
                }

                possibilities = wordMap.get(currentString);
                nextString = possibilities.get((int)(Math.random() * possibilities.size())); //väljer en random
                resultString += " " + nextString;

                if (resultString.length() > maxLength) {
                    if (backupResultString.endsWith(".")) {
                        sb = new StringBuffer(backupResultString);
                        sb.deleteCharAt(sb.length() - 1);
                        backupResultString = sb.toString();
                    }
                    if ((backupResultString.split(" ").length <= 1) && (Math.random() < 0.20)) { // 20% chans att man accepterar ett ord
                        return backupResultString;
                    }
                    break;
                }

                if ((resultString.endsWith(".") || resultString.endsWith("!") || resultString.endsWith("?")) && (Math.random() < 0.80)) { // 20% chance that it continues after one sentence
                    sb = new StringBuffer(resultString);
                    sb.deleteCharAt(sb.length() - 1);
                    resultString = sb.toString();

                    if ((resultString.split(" ").length <= 1) && (Math.random() < 0.20)) { // 20% chans att man accepterar ett ord
                        return resultString;
                    }
                    break;
                }

                currentString = resultString.substring(resultString.lastIndexOf(" ") + 1);
            }

            if (resultString.endsWith(".")) {
                sb = new StringBuffer(resultString);
                sb.deleteCharAt(sb.length()-1);
                resultString = sb.toString();
            }
            if ((resultString.split(" ").length <= 1) && (Math.random() < 0.20)) { // 20% chans att man accepterar ett ord
                return resultString;
            }
            break;
        }

        return resultString;
    }
}
