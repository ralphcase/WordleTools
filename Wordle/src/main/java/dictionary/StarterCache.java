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
import java.util.Map;
import java.util.Optional;

public class StarterCache {

    private static final Type MAP_TYPE =
            new TypeToken<Map<String, GuessScore>>() {
            }.getType();
    private final File file;
    private final Gson gson;
    // Map<cacheKey, GuessScore>
    private Map<String, GuessScore> cacheMap;

    public StarterCache(File file) {
        this.file = file;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadFromDisk();
    }

    public Optional<GuessScore> load(String solverMode, String dictHash) {
        return Optional.ofNullable(cacheMap.get(makeKey(solverMode, dictHash)));
    }

    public void save(String solverMode, String dictHash, GuessScore bestStarter) {
        cacheMap.put(makeKey(solverMode, dictHash), bestStarter);
        writeToDisk();
    }

    private String makeKey(String solverMode, String dictHash) {
        return solverMode + ":" + dictHash;
    }

    private void loadFromDisk() {
        try (FileReader reader = new FileReader(file)) {
            Map<String, GuessScore> loaded = gson.fromJson(reader, MAP_TYPE);
            cacheMap = (loaded != null) ? loaded : new HashMap<>();
        } catch (Exception e) {
            cacheMap = new HashMap<>();
        }
    }

    private void writeToDisk() {
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(cacheMap, writer);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write starter cache", e);
            }
        }
    }
}
