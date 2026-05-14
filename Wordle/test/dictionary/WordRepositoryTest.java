package dictionary;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import word.Word;

import java.util.List;
import java.util.ArrayList;

public class WordRepositoryTest {

    @Test
    void nullPastSolutionsBecomesEmptyList() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, null, null);

        Assertions.assertNotNull(repo.getPastSolutionWords());
        Assertions.assertTrue(repo.getPastSolutionWords().isEmpty());
    }

    @Test
    void goalsMustBeSubsetOfAllowed() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("SLATE")); // not in allowed

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new WordRepository(allowed, goals, null, null));
    }

    @Test
    void pastSolutionsMustBeSubsetOfGoals() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"));
        List<Word> goals = List.of(new Word("CRANE"));
        List<Word> past = List.of(new Word("SLATE")); // not in goals

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new WordRepository(allowed, goals, past, null));
    }

    @Test
    void listsAreDefensivelyCopied() {
        List<Word> allowed = new ArrayList<>();
        allowed.add(new Word("CRANE"));

        List<Word> goals = new ArrayList<>();
        goals.add(new Word("CRANE"));

        List<Word> past = new ArrayList<>();
        past.add(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, past, null);

        allowed.clear();
        goals.clear();
        past.clear();

        Assertions.assertEquals(1, repo.getAllowedWords().size());
        Assertions.assertEquals(1, repo.getGoalWords().size());
        Assertions.assertEquals(1, repo.getPastSolutionWords().size());
    }

    @Test
    void returnedListsAreImmutable() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, null, null);

        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                repo.getAllowedWords().add(new Word("SLATE")));
    }
}
