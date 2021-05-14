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

    public MarkovChainCreator(InputStream file) {
        scanner = new Scanner(file);
    }

    public void MarkovChain() {
        firstWord = scanner.next();
        sentenceStarters.add(firstWord);

        while (scanner.hasNext()) {
            secondWord = scanner.next();

            if (firstWord.endsWith(".")) {
                sentenceStarters.add(firstWord); //om föregående ord slutar med punkt är nästa ord en meningsstartare
            }

            if (scanner.hasNext()) {
                thirdWord = scanner.next();
                combinedWords = secondWord + " " + thirdWord;

                if (!wordMap.containsKey(firstWord)) {
                    wordMap.put(firstWord, new ArrayList<>());
                }
                wordMap.get(firstWord).add(combinedWords);
                firstWord = thirdWord;
            }
        }
    }

    public String textGenerator() {
        currentString = sentenceStarters.get((int)(Math.random() * sentenceStarters.size())); // get random sentence starter
        resultString = currentString;

        while (resultString.length() <= maxLength) {
            backupResultString = resultString; // keeping a backup of resultString in case text gets too long
            possibilities = wordMap.get(currentString);
            nextString = possibilities.get((int)(Math.random() * possibilities.size())); //väljer en random
            resultString += " " + nextString;

            if (resultString.length() > maxLength) {
                if (backupResultString.endsWith(".")) {
                    sb = new StringBuffer(backupResultString);
                    sb.deleteCharAt(sb.length() - 1);
                    backupResultString = sb.toString();
                }
                return backupResultString;
            }

            currentString = resultString.substring(resultString.lastIndexOf(" ")+1);;
        }

        if (resultString.endsWith(".")) {
            sb = new StringBuffer(resultString);
            sb.deleteCharAt(sb.length()-1);
            resultString = sb.toString();
        }
        return resultString;
    }

    /*
    public void recursiveTextGenerator() {

    }

    public boolean recursiveTextGeneratorHelper(String resultString) {
        if (resultString.length() > maxLength) {
            return false;
        }

        if (recursiveTextGeneratorHelper(resultString)) {

        }
        if (row == 9) {
            return true;
        }
        if (matrix[row][col] != 0) { //kolla om ifylld siffra
            return solve(row, col + 1);
        }
        for (int number = 1; number < 10; number++) {
            if (put(number, row, col)) {
                if (solve(row, col + 1)) {
                    return true;
                }
            }
        }
        remove(row, col);
        return false;
    }*/
}
