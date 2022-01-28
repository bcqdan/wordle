package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Wordle {

    void main() throws IOException {
        var lines = Files.readAllLines(Path.of("en-sensible.txt"));
        var words = lines.stream().map(String::trim).collect(Collectors.toList());

        var engine = new Engine(words);
        // 0: exclude letter
        // Y: include letter, wrong position
        // X: include letter, correct position
        engine.setWordStatus("audio", "00000");
        engine.setWordStatus("terms", "0XX00");
        engine.setWordStatus("perch", "XXX00");
        System.out.println(engine.nextGuess());

    }

    public static void main(String[] args) throws IOException {
        new Wordle().main();
    }
}
