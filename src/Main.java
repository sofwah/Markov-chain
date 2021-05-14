//javac Main.java MarkovChainGenerator.java
//java Main < 1.in

public class Main {

    public static void main(String[] args) {
        MarkovChainCreator markovChainCreator = new MarkovChainCreator(System.in);
        System.out.println(markovChainCreator.textGenerator());
    }
}