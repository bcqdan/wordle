package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Collectors;

public class Wordle {

    void main() throws IOException {
        var lines = Files.readAllLines(Path.of("en-sensible.txt"));
        var words = lines.stream().map(String::trim).collect(Collectors.toList());

        var random = new Random();
        var server = new Server(words, random);
        var player = new Player(words, random);
        // 0: exclude letter
        // Y: include letter, wrong position
        // X: include letter, correct position

        server.start();
        while (true) {
            var guess = player.nextGuess();
            var result = server.guess(guess);
            System.out.printf("%s %s\n", guess, result);
            if (result.equals("XXXXX")) {
                break;
            }
            player.setWordStatus(guess, result);
        }


    }

    public static void main(String[] args) throws IOException {
        new Wordle().main();
    }
}
