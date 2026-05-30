package solver;

import constraints.Constraint;
import dictionary.WordRepository;
import feedback.Feedback;
import feedback.Mark;
import org.junit.jupiter.api.Test;
import word.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static feedback.Mark.ABSENT;
import static feedback.Mark.CORRECT;
import static org.junit.jupiter.api.Assertions.*;

public class SolverTest {

    @Test
    void remainingCandidatesRespectsConstraints() {
        WordRepository repo = new WordRepository(
                List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK")),
                List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK")),
                List.of());

        Solver solver = new Solver(repo);

        Word guess = new Word("CRANE");

        Feedback fb = Feedback.of(
                Mark.ABSENT, // C
                Mark.PRESENT, // R
                Mark.ABSENT, // A
                Mark.ABSENT, // N
                Mark.ABSENT // E
        );

        solver.applyFeedback(guess, fb);

        List<Word> candidates = solver.remainingCandidates();

        assertTrue(candidates.isEmpty());
    }

    @Test
    void riserScenario() {
        WordRepository repo = new WordRepository(
                List.of(new Word("MISER"), new Word("WISER"), new Word("RISER")),
                List.of(new Word("MISER"), new Word("WISER"), new Word("RISER")),
                List.of());

        Solver solver = new Solver(repo);

        solver.applyFeedback(new Word("MISER"), Feedback.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT));

        List<Word> candidates = solver.remainingCandidates();

        assertTrue(candidates.contains(new Word("RISER")));
        assertFalse(candidates.contains(new Word("MISER")));
    }

    // ============================================================
    // NEW TESTS: Multi-round game scenarios
    // ============================================================
    @Test
    void twoRoundGameNarrowsDown() {
        WordRepository repo = new WordRepository(
                List.of(new Word("PLANT"), new Word("PLANK"), new Word("SLATE")),
                List.of(new Word("PLANT"), new Word("PLANK"), new Word("SLATE")),
                List.of());

        Solver solver = new Solver(repo);

        // Round 1: guess PLACE, feedback as if target is PLANT
        // P=CORRECT, L=CORRECT, A=CORRECT, C=ABSENT, E=ABSENT
        solver.applyFeedback(new Word("PLACE"),
                Feedback.of(CORRECT, CORRECT, CORRECT, ABSENT, ABSENT));

        List<Word> after1 = solver.remainingCandidates();
        assertEquals(2, after1.size());
        assertTrue(after1.contains(new Word("PLANT")));
        assertTrue(after1.contains(new Word("PLANK")));
        assertFalse(after1.contains(new Word("SLATE"))); // has no P, L, A

        // Round 2: guess PLANK, feedback as if target is PLANT
        // P=CORRECT, L=CORRECT, A=CORRECT, N=CORRECT, K=ABSENT
        solver.applyFeedback(new Word("PLANK"),
                Feedback.of(CORRECT, CORRECT, CORRECT, CORRECT, ABSENT));

        List<Word> after2 = solver.remainingCandidates();
        assertEquals(1, after2.size());
        assertTrue(after2.contains(new Word("PLANT")));
    }

    @Test
    void threeRoundGameReachesAnswer() {
        WordRepository repo = new WordRepository(
                List.of(new Word("SLATE"), new Word("PLATE"), new Word("FLARE")),
                List.of(new Word("SLATE"), new Word("PLATE"), new Word("FLARE")),
                List.of());

        Solver solver = new Solver(repo);

        // Round 1: guess SLATE, feedback as if target is PLATE
        solver.applyFeedback(new Word("SLATE"),
                Feedback.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT));

        List<Word> after1 = solver.remainingCandidates();
        assertTrue(after1.contains(new Word("PLATE")));
        assertFalse(after1.contains(new Word("SLATE")));

        // Round 2: guess FLARE, feedback as if target is PLATE
        // F=ABSENT, L=CORRECT (at pos 1), A=CORRECT, R=ABSENT, E=CORRECT
        solver.applyFeedback(new Word("FLARE"),
                Feedback.of(ABSENT, CORRECT, CORRECT, ABSENT, CORRECT));

        List<Word> after2 = solver.remainingCandidates();
        assertTrue(after2.contains(new Word("PLATE")));

        // Round 3: guess PLATE (confirm answer)
        solver.applyFeedback(new Word("PLATE"),
                Feedback.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT));

        List<Word> after3 = solver.remainingCandidates();
        assertEquals(1, after3.size());
        assertEquals(new Word("PLATE"), after3.getFirst());
    }

    // ============================================================
    // NEW TESTS: Solver modes (ARCHIVE, NEW, ALL)
    // ============================================================

    @Test
    void modeArchiveInitializesWithPastSolutions() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> past = List.of(new Word("CRANE"), new Word("SLATE"));

        WordRepository repo = new WordRepository(allowed, goals, past);

        // Just verify that ARCHIVE mode constructor doesn't throw
        Solver solver = new Solver(repo, false, Solver.Mode.ARCHIVE);
        assertNotNull(solver);
    }

    @Test
    void modeNewInitializesWithNewSolutions() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> past = List.of(new Word("CRANE"), new Word("SLATE"));

        WordRepository repo = new WordRepository(allowed, goals, past);

        // Just verify that NEW mode constructor doesn't throw
        Solver solver = new Solver(repo, false, Solver.Mode.NEW);
        assertNotNull(solver);
    }

    @Test
    void modeAllInitializesWithAllGoals() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> past = List.of(new Word("CRANE"), new Word("SLATE"));

        WordRepository repo = new WordRepository(allowed, goals, past);

        // Just verify that ALL mode constructor doesn't throw
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);
        assertNotNull(solver);
    }

    @Test
    void modeArchiveReturnsOnlyPastSolutions() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> past = List.of(new Word("CRANE"), new Word("SLATE"));

        WordRepository repo = new WordRepository(allowed, goals, past);
        Solver solver = new Solver(repo, false, Solver.Mode.ARCHIVE);

        List<Word> candidates = solver.remainingCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Word("CRANE")));
        assertTrue(candidates.contains(new Word("SLATE")));
        assertFalse(candidates.contains(new Word("BRICK")));
    }

    @Test
    void modeNewReturnsOnlyNewSolutions() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> past = List.of(new Word("CRANE"), new Word("SLATE"));

        WordRepository repo = new WordRepository(allowed, goals, past);
        Solver solver = new Solver(repo, false, Solver.Mode.NEW);

        List<Word> candidates = solver.remainingCandidates();
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new Word("BRICK")));
    }

    @Test
    void modeAllReturnsAllGoals() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));
        List<Word> past = List.of(new Word("CRANE"), new Word("SLATE"));

        WordRepository repo = new WordRepository(allowed, goals, past);
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        List<Word> candidates = solver.remainingCandidates();
        assertEquals(3, candidates.size());
    }

    @Test
    void modeSmartThrowsUnsupported() {
        List<Word> allowed = List.of(new Word("CRANE"));
        List<Word> goals = List.of(new Word("CRANE"));

        WordRepository repo = new WordRepository(allowed, goals, null);

        assertThrows(UnsupportedOperationException.class, () ->
                new Solver(repo, false, Solver.Mode.SMART));
    }

    // ============================================================
    // NEW TESTS: nextGuessSimple()
    // ============================================================

    @Test
    void nextGuessSimpleReturnsFirstCandidate() {
        WordRepository repo = new WordRepository(
                List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK")),
                List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK")),
                List.of());

        Solver solver = new Solver(repo);
        Word guess = solver.nextGuessSimple();

        assertEquals(new Word("CRANE"), guess); // first in the list
    }

    @Test
    void nextGuessSimpleReturnsNullWhenNoCandidates() {
        WordRepository repo = new WordRepository(
                List.of(new Word("CRANE")),
                List.of(new Word("CRANE")),
                List.of());

        Solver solver = new Solver(repo);

        // Eliminate all candidates
        solver.applyFeedback(new Word("CRANE"),
                Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT));

        Word guess = solver.nextGuessSimple();
        assertNull(guess);
    }

    // ============================================================
    // NEW TESTS: Hardmode behavior
    // ============================================================

    @Test
    void hardmodeFiltersAllowedWords() {
        List<Word> allowed = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"), new Word("GAVEL"));
        List<Word> goals = List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK"));

        WordRepository repo = new WordRepository(allowed, goals, null);
        Solver solver = new Solver(repo, true, Solver.Mode.ALL); // hardmode enabled, use ALL mode

        // In hardmode, applyFeedback should also filter allowed words
        solver.applyFeedback(new Word("CRANE"),
                Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT)); // C correct at pos 0

        List<Word> remaining = solver.remainingCandidates();
        // Goal words: CRANE (starts with C, ok), SLATE (S != C, rejected), BRICK (B != C, rejected)
        assertTrue(remaining.isEmpty());
    }

    @Test
    void constructorRejectsNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new Solver(null));
    }

    @Test
    void remainingCandidatesWithConstraint() {
        WordRepository repo = new WordRepository(
                List.of(new Word("CLOTH"), new Word("SLATE"), new Word("BRICK")),
                List.of(new Word("CLOTH"), new Word("SLATE"), new Word("BRICK")),
                List.of());

        Solver solver = new Solver(repo);

        // Constraint: guessed CRISP, got C=CORRECT at pos 0, R/I/S/P=ABSENT
        // This means candidates must start with C and not contain R, I, S, P
        Constraint constraint = new Constraint(
                new Word("CRISP"),
                Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT));

        List<Word> filtered = solver.remainingCandidates(constraint);

        // CLOTH: starts with C ✓, no R/I/S/P ✓
        // SLATE: starts with S ✗
        // BRICK: starts with B ✗
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains(new Word("CLOTH")));
    }

    @Test
    void nextGuess_noConstraints_returnsBestStarter() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("SLATE"),
                new Word("BRINE")
        );

        WordRepository repo = new WordRepository(
                allowed, allowed, List.of()
        );

        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = solver.nextGuess();

        // Adjust expected based on your scoring logic.
        assertEquals(new Word("CRANE"), guess);
    }

    @Test
    void nextGuess_singleCandidate_returnsThatWord() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("SLATE")
        );

        List<Word> possible = List.of(
                new Word("SLATE")
        );

        WordRepository repo = new WordRepository(
                allowed, possible, List.of()
        );

        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = solver.nextGuess();

        assertEquals(new Word("SLATE"), guess);
    }

    @Test
    void nextGuess_multipleCandidates_returnsBestScoringWord() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("BRINE"),
                new Word("STONE")
        );

        WordRepository repo = new WordRepository(
                allowed, allowed, List.of()
        );

        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = solver.nextGuess();

        // Adjust expected based on your scoring logic.
        assertEquals(new Word("CRANE"), guess);
    }

    @Test
    void nextGuess_tieBreaker_returnsFirstWord() {
        List<Word> allowed = List.of(
                new Word("ABCDE"),
                new Word("ABCDF")
        );

        WordRepository repo = new WordRepository(
                allowed, allowed, List.of()
        );

        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = solver.nextGuess();

        assertEquals(new Word("ABCDE"), guess);
    }

    @Test
    void nextGuess_noCandidatesFallback_returnsFirstAllowed() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("BRINE")
        );

        WordRepository repo = new WordRepository(
                allowed, allowed, List.of()
        );

        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guessWord = new Word("CRANE");
        Word targetWord = new Word("XXXXX"); // no letters in common

        Feedback fb = Feedback.from(guessWord, targetWord);

        solver.applyFeedback(guessWord, fb);

        Word guess = solver.nextGuess();

        assertEquals(new Word("CRANE"), guess);
    }

    @Test
    void solver_filtersGrayLetterCorrectly() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("BRINE"),
                new Word("STONE")
        );

        WordRepository repo = new WordRepository(allowed, allowed, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = new Word("CRANE");
        Word target = new Word("XXXXX"); // no shared letters → all ABSENT
        Feedback fb = Feedback.from(guess, target);

        solver.applyFeedback(guess, fb);

        List<Word> remaining = solver.remainingCandidates();

        // All words contain at least one of C,R,A,N,E → all eliminated
        assertTrue(remaining.isEmpty());
    }

    @Test
    void solver_filtersGreenCorrectly() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("BRINE"),
                new Word("CROWD")
        );

        WordRepository repo = new WordRepository(allowed, allowed, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = new Word("CXXXX");
        Word target = new Word("CRANE"); // C in position 0 is green
        Feedback fb = Feedback.from(guess, target);

        solver.applyFeedback(guess, fb);

        List<Word> remaining = solver.remainingCandidates();

        assertEquals(
                List.of(new Word("CRANE"), new Word("CROWD")),
                remaining
        );
    }

    @Test
    void solver_filtersYellowCorrectly() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("BRACE"),
                new Word("REACT")
        );

        WordRepository repo = new WordRepository(allowed, allowed, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word guess = new Word("XXNXX");
        Word target = new Word("CRATE"); // R is yellow (wrong position)
        Feedback fb = Feedback.from(guess, target);

        solver.applyFeedback(guess, fb);

        List<Word> remaining = solver.remainingCandidates();

        // Words containing R but not in position 1
        assertEquals(
                List.of(new Word("BRACE"), new Word("REACT")),
                remaining
        );
    }

    @Test
    void solver_combinesMultipleConstraints() {
        List<Word> allowed = List.of(
                new Word("CRANE"),
                new Word("BRACE"),
                new Word("REACT"),
                new Word("TRACE")
        );

        WordRepository repo = new WordRepository(allowed, allowed, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        // First constraint: R is yellow
        Word g1 = new Word("RXXXX");
        Word t1 = new Word("CRANE");
        solver.applyFeedback(g1, Feedback.from(g1, t1));

        // Second constraint: A is green at position 2
        Word g2 = new Word("CXAXX");
        Word t2 = new Word("BRACE");
        solver.applyFeedback(g2, Feedback.from(g2, t2));

        List<Word> remaining = solver.remainingCandidates();

        assertEquals(
                List.of(new Word("BRACE"), new Word("TRACE")),
                remaining
        );
    }

    @Test
    void solver_state_after_one_round_preserves_secret() {
        List<Word> words = List.of(
                new Word("CRANE"),
                new Word("SLATE"),
                new Word("FLAME")
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word secret = new Word("FLAME");
        Word guess1 = new Word("CRANE");
        Feedback fb1 = Feedback.from(guess1, secret);

        solver.applyFeedback(guess1, fb1);

        List<Word> remaining = solver.remainingCandidates();

        assertEquals(
                List.of(new Word("SLATE"), new Word("FLAME")),
                remaining
        );
    }

    @Test
    void solver_multiRoundStateEvolution_convergesOnSecret() {
        List<Word> words = List.of(
                new Word("CRANE"),
                new Word("SLATE"),
                new Word("FLAME")
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word secret = new Word("FLAME");

        // ----- Round 1 -----
        Word g1 = new Word("CRANE");
        Feedback fb1 = Feedback.from(g1, secret);
        solver.applyFeedback(g1, fb1);

        assertEquals(
                List.of(new Word("SLATE"), new Word("FLAME")),
                solver.remainingCandidates()
        );

        // ----- Round 2 -----
        Word g2 = new Word("SLATE");
        Feedback fb2 = Feedback.from(g2, secret);
        solver.applyFeedback(g2, fb2);

        assertEquals(
                List.of(new Word("FLAME")),
                solver.remainingCandidates()
        );

        // ----- Round 3 -----
        Word g3 = solver.nextGuess();
        assertEquals(new Word("FLAME"), g3);
    }

    @Test
    void solver_duplicateLetterGrayDoesNotEliminatePresentLetter() {
        List<Word> words = List.of(
                new Word("BALMY"),
                new Word("MANGO"),
                new Word("CABAL"),
                new Word("MAMMA") // guess only
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word secret = new Word("BALMY");
        Word guess = new Word("MAMMA");

        Feedback fb = Feedback.from(guess, secret);
        solver.applyFeedback(guess, fb);

        List<Word> remaining = solver.remainingCandidates();

        assertEquals(
                List.of(new Word("BALMY")),
                remaining
        );
    }

    @Test
    void solver_duplicateLetterYellowAndGrayBehavesCorrectly() {
        List<Word> words = List.of(
                new Word("CANOE"),  // secret
                new Word("CARGO"),
                new Word("CABIN"),
                new Word("BANAL"),
                new Word("ALARM")   // guess only
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        Word secret = new Word("CANOE");
        Word guess = new Word("ALARM");

        Feedback fb = Feedback.from(guess, secret);
        solver.applyFeedback(guess, fb);

        List<Word> remaining = solver.remainingCandidates();

        assertEquals(
                List.of(
                        new Word("CANOE"),
                        new Word("CABIN")
                ),
                remaining
        );
    }

    @Test
    void rankedGuessesReturnsRequestedCount() {
        List<Word> words = List.of(
                new Word("SLATE"),
                new Word("CRANE"),
                new Word("BRINE"),
                new Word("STONE")
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo, false, Solver.Mode.ALL);

        List<GuessScore> top3 = solver.rankedGuesses(3);

        assertEquals(3, top3.size());
    }

    @Test
    void rankedGuessesClampsToListSize() {
        List<Word> words = List.of(
                new Word("SLATE"),
                new Word("CRANE")
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo);

        List<GuessScore> all = solver.rankedGuesses(10);

        assertEquals(2, all.size());
    }

    @Test
    void rankedGuessesIsSortedAscending() {
        List<Word> words = List.of(
                new Word("SLATE"),
                new Word("CRANE"),
                new Word("BRINE"),
                new Word("STONE")
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo);

        List<GuessScore> ranked = solver.rankedGuesses(4);

        for (int i = 1; i < ranked.size(); i++) {
            assertTrue(
                    ranked.get(i - 1).score() <= ranked.get(i).score(),
                    "List is not sorted in ascending score order"
            );
        }
    }

    @Test
    void rankedGuessesContainOnlyCandidateWords() {
        List<Word> words = List.of(
                new Word("SLATE"),
                new Word("CRANE"),
                new Word("BRINE")
        );

        WordRepository repo = new WordRepository(words, words, List.of());
        Solver solver = new Solver(repo);

        Set<Word> candidates = new HashSet<>(words);

        List<GuessScore> ranked = solver.rankedGuesses(5);

        for (GuessScore gs : ranked) {
            assertTrue(candidates.contains(gs.word()));
        }
    }

}


