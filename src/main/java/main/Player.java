package main;

import java.util.*;
import java.util.stream.Collectors;

public class Player {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

    private final List<String> words;
    private final Random random;

    private final Set<Character> excluded;
    private final Set<Character> included;
    private final char[] known;
    private final Map<Character, Set<Integer>> excludedPositions;
    private boolean firstGuess;

    public Player(List<String> words, Random random) {
        this.words = new ArrayList<>(words);
        this.random = random;
        excluded = new HashSet<>();
        included = new HashSet<>();
        known = new char[5];
        excludedPositions = new HashMap<>();
        firstGuess = true;
    }

    public String nextGuess() {
        if (firstGuess) {
            firstGuess = false;
            return "audio";
        }
        var bestScore = -1;
        var bestGuess = new ArrayList<String>();
        for (var guess : words) {
            var score = getScore(guess);
            if (score > bestScore) {
                bestScore = score;
                bestGuess.clear();
                bestGuess.add(guess);
            } else if (score == bestScore) {
                bestGuess.add(guess);
            }
        }
        return bestGuess.get(random.nextInt(bestGuess.size()));
    }

    private int getScore(String guess) {
        var score = 0;
        for (var secret : words) {
            score += getScore(guess, secret);
        }
        return score;
    }

    private int getScore(String guess, String secret) {
        var score = 0;
//        for (int i = 0; i < 5; i++) {
//            if (guess.charAt(i) == secret.charAt(i)) {
//                score += 1;
//            }
//        }
        var guessSet = guess.chars().boxed().collect(Collectors.toSet());
        var secretSet = secret.chars().boxed().collect(Collectors.toSet());
        guessSet.retainAll(secretSet);
        return score + guessSet.size();
    }

    public void setWordStatus(String word, String status) {
        firstGuess = false;
        assert word.length() == 5;
        assert status.length() == 5;
        for (int i = 0; i < 5; i++) {
            var c = word.charAt(i);
            var s = status.charAt(i);
            switch (s) {
                case '0' -> {
                    assert !included.contains(c);
                    excluded.add(c);
                }
                case 'X' -> {
                    assert !excluded.contains(c);
                    known[i] = c;
                    included.add(c);
                    excludedPositions.remove(c);
                }
                case 'Y' -> {
                    assert !excluded.contains(c);
                    included.add(c);
                    excludedPositions.computeIfAbsent(c, k -> new HashSet<>()).add(i);
                }
                default -> throw new IllegalArgumentException();
            }
        }
        words.removeIf(s -> !filter(s));
        // System.out.printf("%4d remaining: %s\n", words.size(), words);
    }

    private boolean filter(String word) {
        for (int i = 0; i < 5; i++) {
            var c = word.charAt(i);
            if (excluded.contains(c)) {
                return false;
            }
            if (known[i] != 0 && known[i] != c) {
                return false;
            }
            if (excludedPositions.containsKey(c) && excludedPositions.get(c).contains(i)) {
                return false;
            }
        }
        for (var c : included) {
            if (!word.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

}
