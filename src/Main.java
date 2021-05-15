//javac Main.java MarkovChainCreator.java
//java Main < 1.in

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        MarkovChainCreator markovChainCreator = new MarkovChainCreator(System.in);
        markovChainCreator.createMarkovChain();
        String markovString = markovChainCreator.generateText();
        System.out.println(markovString);

        try {
            Files.write(Paths.get("file.txt"), markovString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < 10; i++) {
            markovString = markovChainCreator.generateText();

            try {
                Files.write(Paths.get(i + ".txt"), markovString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}