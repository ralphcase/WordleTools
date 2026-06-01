package dictionary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import solver.GuessScore;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StarterCache {

    private final File file;
    private final Gson gson;

    // Map<cacheKey, List<GuessScore>>
    private Map<String, List<GuessScore>> cacheMap;

    private static final Type MAP_TYPE =
            new TypeToken<Map<String, List<GuessScore>>>() {}.getType();

    public StarterCache(File file) {
        this.file = file;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.cacheMap = new HashMap<>();
        loadFromDisk();
    }

    // ------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------

    public Optional<List<GuessScore>> load(String solverMode,
                                           String starterWord,
                                           String dictHash) {
        String key = makeKey(solverMode, starterWord, dictHash);
        return Optional.ofNullable(cacheMap.get(key));
    }

    public void save(String solverMode,
                     String starterWord,
                     String dictHash,
                     List<GuessScore> starters) {
        String key = makeKey(solverMode, starterWord, dictHash);
        cacheMap.put(key, starters);
        writeToDisk();
    }

    // ------------------------------------------------------------
    // Key format
    // ------------------------------------------------------------

    private String makeKey(String solverMode, String starterWord, String dictHash) {
        return solverMode + ":" + starterWord + ":" + dictHash;
    }

    // ------------------------------------------------------------
    // Persistence
    // ------------------------------------------------------------

    private void loadFromDisk() {
        if (!file.exists()) {
            cacheMap = new HashMap<>();
            return;
        }
        try (FileReader reader = new FileReader(file)) {
            Map<String, List<GuessScore>> loaded = gson.fromJson(reader, MAP_TYPE);
            cacheMap = (loaded != null) ? loaded : new HashMap<>();
        } catch (Exception e) {
            // If the file is corrupted, start fresh
            cacheMap = new HashMap<>();
        }
    }

    private void writeToDisk() {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(cacheMap, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write starter cache", e);
        }
    }
}