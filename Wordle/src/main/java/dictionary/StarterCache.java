package dictionary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import solver.GuessScore;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StarterCache {

    private final File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public StarterCache(File file) {
        this.file = file;
    }

    private static class CacheEntry {
        String dictionaryHash;
        List<GuessScore> starters;
    }

    private static class CacheRoot {
        Map<String, CacheEntry> modes = new HashMap<>();
    }

    public Optional<List<GuessScore>> load(String mode, String dictHash) {
        if (!file.exists()) return Optional.empty();

        try (Reader r = new FileReader(file)) {
            CacheRoot root = gson.fromJson(r, CacheRoot.class);
            CacheEntry entry = root.modes.get(mode);

            if (entry != null && dictHash.equals(entry.dictionaryHash)) {
                return Optional.of(entry.starters);
            }
        } catch (IOException e) {
            // ignore, treat as cache miss
        }

        return Optional.empty();
    }

    public void save(String mode, String dictHash, List<GuessScore> starters) {
        CacheRoot root = new CacheRoot();

        if (file.exists()) {
            try (Reader r = new FileReader(file)) {
                root = gson.fromJson(r, CacheRoot.class);
                if (root == null) root = new CacheRoot();
            } catch (IOException ignored) {}
        }

        CacheEntry entry = new CacheEntry();
        entry.dictionaryHash = dictHash;
        entry.starters = starters;

        root.modes.put(mode, entry);

        try (Writer w = new FileWriter(file)) {
            gson.toJson(root, w);
        } catch (IOException ignored) {}
    }
}
