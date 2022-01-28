package main;

import java.util.List;
import java.util.Random;

public class Server {

    private final Random random;
    private final List<String> words;

    private String secret;

    public Server(List<String> words, Random random) {
        this.words = words;
        this.random = random;
    }

    public void start() {
        start(words.get(random.nextInt(words.size())));
    }

    public void start(String secret) {
        assert secret.length() == 5;
        this.secret = secret;
        System.out.println("secret: " + secret);
    }

    public String guess(String word) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char wc = word.charAt(i);
            char sc = secret.charAt(i);
            if (wc == sc) {
                result.append("X");
            } else if (secret.contains(String.valueOf(wc))) {
                result.append("Y");
            } else {
                result.append("0");
            }
        }
        assert result.length() == 5;
        return result.toString();
    }
    
}
