package dictionary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import word.Word;

import java.util.ArrayList;
import java.util.List;

public class WordRepositoryTest {

    @Test
    void nullPastSolutionsBecomesEmptyList() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, null, null, null   );

        Assertions.assertNotNull(repo.pastSolutionWords());
        Assertions.assertTrue(repo.pastSolutionWords().isEmpty());
    }

    @Test
    void goalsMustBeSubsetOfAllowed() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("SLATE")); // not in allowed

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new WordRepository(allowed, goals, null, null, null));
    }

    @Test
    void pastSolutionsMustBeSubsetOfGoals() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"));
        List<Word> goals = List.of(new Word("CRANE"));
        List<Word> past = List.of(new Word("SLATE")); // not in goals

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new WordRepository(allowed, goals, past,  null, null));
    }

    @Test
    void listsAreDefensivelyCopied() {
        List<Word> allowed = new ArrayList<>();
        allowed.add(new Word("CRANE"));

        List<Word> goals = new ArrayList<>();
        goals.add(new Word("CRANE"));

        List<Word> past = new ArrayList<>();
        past.add(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, past,  null, null );

        allowed.clear();
        goals.clear();
        past.clear();

        Assertions.assertEquals(1, repo.allowedWords().size());
        Assertions.assertEquals(1, repo.goalWords().size());
        Assertions.assertEquals(1, repo.pastSolutionWords().size());
    }

    @Test
    void returnedListsAreImmutable() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, null, null, null );

        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                List.copyOf(repo.allowedWords()).add(new Word("SLATE")));
    }
}
