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

//        player.setWordStatus("tares", "00XY0");
//        player.setWordStatus("ferny", "0XX0X");
//        System.out.println(player.nextGuess());

        // 0: exclude letter
        // Y: include letter, wrong position
        // X: include letter, correct position
        server.start();
        var numGuesses = 0;
        while (true) {
            var guess = player.nextGuess();
            numGuesses++;
            var result = server.guess(guess);
            System.out.printf("%s %s\n", guess, result);
            player.setWordStatus(guess, result);
            if (result.equals("XXXXX")) {
                break;
            }
        }
        System.out.println("guesses: " + numGuesses);
    }

    public static void main(String[] args) throws IOException {
        new Wordle().main();
    }
}
