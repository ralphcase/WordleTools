package dictionary;

import word.Word;

import java.util.List;

public final class DictionaryInitializer {

    private final DictionaryConfig config;
    private final WordLoader loader = new WordLoader();

    public DictionaryInitializer(DictionaryConfig config) {
        this.config = config;
    }

    public DictionaryInitializer() {
        this.config = DictionaryConfig.defaultConfig();
    }

    public WordRepository loadDictionaries() {

        List<Word> allowed = loader.loadWords(config.allowedWordsPath());

        List<Word> goals = loader.loadWords(config.goalWordsPath());
        List<Word> archiveWords = loader.loadWords(config.archiveWordsPath());

        List<Word> pastSolutions = null;
        if (config.pastSolutionsPath().toFile().exists()) {
            pastSolutions = loader.loadWords(config.pastSolutionsPath());
        }
        String dictHash = DictionaryHash.compute(
                config.allowedWordsPath(),
                config.goalWordsPath()
        );
        StarterCache cache = new StarterCache(config.starterCachePath().toFile());

        return new WordRepository(allowed, goals, pastSolutions, archiveWords, dictHash, cache);
    }
}
