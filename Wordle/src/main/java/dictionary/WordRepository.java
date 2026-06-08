package dictionary;

import word.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record WordRepository(
        List<Word> allowedWords,
        List<Word> goalWords,
        List<Word> pastSolutionWords,
        List<Word> archiveWords,
        String dictionaryHash, StarterCache starterCache) {

    public WordRepository(List<Word> allowedWords, List<Word> goalWords, List<Word> pastSolutionWords, List<Word> archiveWords) {
        this(allowedWords, goalWords, pastSolutionWords, archiveWords, null, null);
    }

    public WordRepository(
            List<Word> allowedWords,
            List<Word> goalWords,
            List<Word> pastSolutionWords,
            List<Word> archiveWords,
            String dictionaryHash,
            StarterCache starterCache) {

        Objects.requireNonNull(allowedWords, "allowed Words must not be null");
        Objects.requireNonNull(goalWords, "goal Words must not be null");

        // Defensive copies
        this.allowedWords = List.copyOf(allowedWords);
        this.goalWords = List.copyOf(goalWords);
        this.archiveWords = List.copyOf(archiveWords == null ? List.of() : archiveWords);
        this.pastSolutionWords = List.copyOf(pastSolutionWords == null ? List.of() : pastSolutionWords);
        this.dictionaryHash = dictionaryHash;
        if (starterCache == null) {
            starterCache = new StarterCache(null);
        }
        this.starterCache = starterCache;
        // Enforce invariants
        ensureSubset(this.goalWords, this.allowedWords,
                "goalWords must be a subset of allowedWords");
        ensureSubset(this.pastSolutionWords, this.goalWords,
                "pastSolutionWords must be a subset of goalWords");
        ensureSubset(this.archiveWords, this.goalWords,
                "archiveWords must be a subset of goalWords");
    }

    private static void ensureSubset(List<Word> subset, List<Word> superset, String message) {
        Set<Word> superSet = new HashSet<>(superset);
        for (Word w : subset) {
            if (!superSet.contains(w)) {
                throw new IllegalArgumentException(message + ": " + w);
            }
        }
    }

    public String getDictionaryHash() {
        return dictionaryHash;
    }

    public StarterCache getStarterCache() {
        return starterCache;
    }

}
