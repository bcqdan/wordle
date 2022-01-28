package main;

import java.util.*;

public class Player {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

    private final List<String> words;
    private final Random random;

    private final Set<Character> excluded;
    private final Set<Character> included;
    private final char[] known;
    private final Map<Character, Set<Integer>> excludedPositions;

    public Player(List<String> words, Random random) {
        this.words = new ArrayList<>(words);
        this.random = random;
        excluded = new HashSet<>();
        included = new HashSet<>();
        known = new char[5];
        excludedPositions = new HashMap<>();
    }

    public String nextGuess() {
        var best = new ArrayList<String>();
        var bestNumVowels = -1;
        var bestNumLetters = -1;
        for (var word : words) {
            var numVowels = getNumDistinctVowels(word);
            var numLetters = getNumDistinctLetters(word);
            var replace = false;
            var add = false;
            if (numLetters > bestNumLetters) {
                replace = true;
            } else if (numLetters == bestNumLetters) {
                if (numVowels > bestNumVowels) {
                    replace = true;
                } else if (numVowels == bestNumVowels) {
                    add = true;
                }
            }
            if (replace) {
                bestNumLetters = numLetters;
                bestNumVowels = numVowels;
                best.clear();
                best.add(word);
            } else if (add) {
                best.add(word);
            }
        }
//        System.out.printf("%4d candidates: %s\n", best.size(),  best);
        return best.get(random.nextInt(best.size()));
    }

    private int getNumDistinctLetters(String word) {
        return (int) word.chars().distinct().count();
    }

    private int getNumDistinctVowels(String word) {
        return (int) word.chars().filter(x -> VOWELS.contains((char) x)).distinct().count();
    }

    public void setWordStatus(String word, String status) {
        assert word.length() == 5;
        assert status.length() == 5;
        for (int i = 0; i < 5; i++) {
            var c = word.charAt(i);
            var s = status.charAt(i);
            switch (s) {
                case '0' -> {
                    assert !included.contains(c);
                    assert !excluded.contains(c);
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
