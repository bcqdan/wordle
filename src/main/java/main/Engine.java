package main;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

public class Engine {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

    private List<String> words;
    private final Random random;

    private final Set<Character> excluded;
    private final Map<Character, Set<Integer>> included;

    public Engine(List<String> words) {
        this.words = words.stream().filter(word -> word.length() == 5).collect(Collectors.toList());
        Collections.shuffle(this.words);
        random = new Random();
        excluded = new HashSet<>();
        included = new HashMap<>();
    }

    public String nextGuess() {
        return words.get(random.nextInt(words.size()));
    }

    public void setWordStatus(String word, String status) {
        Preconditions.checkArgument(word.length() == 5);
        Preconditions.checkArgument(status.length() == 5);
        for (int i = 0; i < 5; i++) {
            var c = word.charAt(i);
            var s = status.charAt(i);
            switch (s) {
                case '0' -> excluded.add(c);
                case 'X' -> {
                    var set = new HashSet<Integer>();
                    set.add(i);
                    included.put(c, set);
                }
                case 'Y' -> {
                    if (!included.containsKey(c)) {
                        var set = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4));
                        included.put(c, set);
                    }
                    included.get(c).remove(i);
                }
                default -> throw new IllegalArgumentException();
            }
        }
        words.removeIf(s -> !filter(s));
        System.out.println("Remaining: " + words);
    }

    private boolean filter(String word) {
        if (!checkExcluded(word)) {
            return false;
        }
        if (!checkIncluded(word)) {
            return false;
        }
        return true;
    }

    private boolean checkIncluded(String word) {
        for (int i = 0; i < 5; i++) {
            var c = word.charAt(i);
            if (included.containsKey(c)) {
                var set = included.get(c);
                if (!set.contains(i)) {
                    return false;
                }
            }
        }
        for (var c : included.keySet()) {
            if (!word.contains("" + c)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkExcluded(String word) {
        for (var c : word.toCharArray()) {
            if (excluded.contains(c)) {
                return false;
            }
        }
        return true;
    }

}
