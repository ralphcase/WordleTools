package dictionary;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DictionaryHash {

    public static String compute(Path allowedWords, Path solutions) {
        try {
            String allowed = Files.readString(allowedWords, StandardCharsets.UTF_8);
            String sols = Files.readString(solutions, StandardCharsets.UTF_8);

            String combined = allowed.replace("\r\n", "\n")
                    + "\n---\n"
                    + sols.replace("\r\n", "\n");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));

            return toHex(hash);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute dictionary hash", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
