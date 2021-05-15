//javac Main.java MarkovChainCreator.java
//java Main < unicorn.txt

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        MarkovChainCreator markovChainCreator = new MarkovChainCreator(System.in);
        markovChainCreator.createMarkovChain();
        String markovString = markovChainCreator.generateText();
        /* test:

        System.out.println(markovString);

        try {
            Files.write(Paths.get("file.txt"), markovString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        for (int i = 1; i <= 100; i++) {
            markovString = markovChainCreator.generateText();

            try {
                Files.write(Paths.get("output/"+ i + ".txt"), markovString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}