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
    private boolean doAgain;
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
                }

                firstWord = thirdWord;
            }
        }
    }

    public String generateText() {
        returnString = generateTextHelper();
        while ((resultString.split(" ").length <= 1) || (resultString.split(" ").length > 280)) {
            returnString = generateTextHelper();
            if ((resultString.split(" ").length <= 280) && (Math.random() < 0.05)) { // sometimes lets through one word outputs (5% of the times)
                break;
            }
        }
        return resultString;
    }

    public String generateTextHelper() { // gives only one word pretty often
        currentString = sentenceStarters.get((int)(Math.random() * sentenceStarters.size())); // get random sentence starter
        resultString = currentString;

        while (resultString.length() <= maxLength) {

            if (!wordMap.containsKey(currentString)) { //fixed nullPointerException
                break;
            }

            possibilities = wordMap.get(currentString);
            if (currentString.endsWith(".") || currentString.endsWith("?") || currentString.endsWith("!")) {
                nextString = sentenceStarters.get((int)(Math.random() * sentenceStarters.size())); // get random sentence starter
            } else {
                nextString = possibilities.get((int)(Math.random() * possibilities.size())); //väljer en random startare
            }

            resultString += " " + nextString;

            if (resultString.length() > maxLength) {
                break;
            }

            if ((resultString.endsWith(".") || resultString.endsWith("!") || resultString.endsWith("?")) && (Math.random() < 0.80)) { // 20% chance that it continues after one sentence
                sb = new StringBuffer(resultString);
                sb.deleteCharAt(sb.length() - 1);
                resultString = sb.toString();
                /*
                if ((resultString.split(" ").length <= 1) && (Math.random() < 0.05)) { // 5% chans att man accepterar ett ord
                    return resultString;
                } gets checked in textGenerator()*/
                break;
            }

            currentString = resultString.substring(resultString.lastIndexOf(" ") + 1);
        }

        if (resultString.endsWith(".")) {
            sb = new StringBuffer(resultString);
            sb.deleteCharAt(sb.length()-1);
            resultString = sb.toString();
        }
        /*
        if ((resultString.split(" ").length <= 1) && (Math.random() < 0.05)) { // 5% chans att man accepterar ett ord
            return resultString;
        } gets checked in textGenerator()*/
        return resultString;
    }
}
