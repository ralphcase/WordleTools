# Next Ideas

A list of future improvements, organized by category.
Each idea includes: What it is, Why it matters, and Shape of done.

---------------------------------------------------------------------

## Dictionary and WordRepository

## Solver Architecture

### Refactor Solver to use ConstraintSet instead of Constraint
What it is  
Replace the list-of-constraints model with a single accumulator.

Why it matters  
Cleaner invariants, simpler filtering, no nested streams, easier reasoning.

Shape of done  
- Solver holds a ConstraintSet
- applyFeedback updates the set
- remainingCandidates uses a single allows() call


---------------------------------------------------------------------

## Legacy Function that might still be relevant
- Build an example game that picks a random target word and simulates a game using the Solver, showing the guesses and feedback at each step. This would demonstrate how the Solver works in practice and could be used for testing or educational purposes.
- Look more than one guess ahead in the Solver to evaluate the expected information gain of each candidate word, rather than just the immediate reduction in candidates. This would involve simulating possible feedback for each guess and calculating the resulting candidate sets, which could lead to better long-term strategies.

### Add JavaDoc
What it is  
Document public classes and methods.

Why it matters  
Improves readability and onboarding; clarifies invariants.

Shape of done  
- Add JavaDoc to domain classes (Position, Report, ConstraintSet)
- Add JavaDoc to Solver and Repository
- Keep comments invariant-focused, not verbose

---------------------------------------------------------------------

## Testing

### Examine tests to see what is missing or could be improved
What it is  
Audit the test suite for gaps.

Why it matters  
Ensures correctness and protects the refactor.

Shape of done  
- Identify missing edge cases
- Add dictionary invariants tests

### State‑Coverage Model for Solver Testing
The current test suite achieves full branch and line coverage, but solver correctness depends on state evolution, not just code paths. To measure how thoroughly tests exercise solver behavior, introduce a lightweight state‑coverage model.
#### Define an abstract solver state
Represent solver state using a canonical structure:
- Green pattern — fixed letters by position (_ A _ M _)
- Yellow constraints — for each letter, positions it cannot occupy
- Gray letters — letters known to be absent
- Letter count constraints — min/max occurrences inferred so far
- Remaining candidate count — size of the filtered solution set
This captures the shape of the solver’s knowledge without storing full word lists.

#### Add a test‑only snapshotState() helper
Expose a structured view of the solver’s current constraints:
- Called after each applyFeedback()
- Returns the abstract state described above
- Does not affect production behavior
This allows tests to record the solver’s reasoning trajectory.

#### Track state transitions during tests
Each deep‑state test records:
- The initial state
- The state after each feedback application
- The final converged state

Collect these states across the entire test suite to identify:
- Distinct states reached
- Distinct transitions exercised
- Redundant tests (identical state sequences)
- Missing scenarios (unreached state classes)

#### Define “state classes” to ensure broad coverage
Examples:
- No constraints (initial)
- Single green
- Multiple greens
- Single yellow
- Multiple yellows
- Gray elimination
- Duplicate‑letter constraints (green+gray, yellow+gray, overcount gray)
- Contradictory constraints resolved (yellow overridden by later green)
- Hard‑mode restricted guess set
- Single remaining candidate (converged)

The goal is to ensure the test suite reaches at least one state in each class.

#### Compute a simple state‑coverage metric
   Two possible metrics:

- State‑class coverage  
(# of state classes reached) / (total state classes)

- Transition coverage  
(# of distinct transitions observed) / (total possible transitions)

This provides a semantic measure of solver test completeness, independent of code structure.

---------------------------------------------------------------------

## UI and UX

### Add a UI to input guesses and feedback more easily
What it is  
A simple interface for interactive solving.

Why it matters  
Makes the tool usable without editing code or running tests.

Shape of done  
- CLI or minimal Swing or JavaFX UI
- Input guess plus feedback
- Display remaining candidates
- Optional: color-coded output

---------------------------------------------------------------------

## Additional Ideas (Optional)

### ConstraintSet visualization
A debug view showing:
- known greens
- known yellows
- excluded letters
- positional bans

### Performance profiling
Measure:
- filtering cost
- constraint update cost
- dictionary load time

### Dictionary precomputation
Store:
- letter frequencies
- position frequencies
- entropy pre-scores

### Solver strategy plug-ins
Allow:
- entropy scoring
- frequency scoring
- hybrid heuristics

Dictionary options:
ALL: goals.txt
NEW: goals.txt - past_solutions.txt
ARCHIVE: archive_solutions.txt


| Guesses | Prefer Goals | Best words   | Best Words | Prefer Goals | Prefer Goals | Best Words |           
|---------|--------------|--------------|------------|--------------|--------------|------------|
|         | ALL          | ALL          | NEW        | NEW          | ARCHIVE      | ARCHIVE    |
| Starter | **RAISE**    | **ROATE**    | **OATER**  | **SANER**    | **IRATE**    | **ROATE**  |
| 1       | 1            | 0            | 0          | 1            |     1        | 0          | 
| 2       | 109          | 56           | 56         | 112          |   103        | 66         |
| 3       | 1137         | 1224         | 929        | 885          |   696        | 812        |  
| 4       | 1607         | 1729         | 947        | 863          |   379        | 317        |
| 5       | 343          | 200          | 84         | 155          |   16         | 0          |
| 6       | 19           | 7            | 5          | 5            |   0          | 0          |

Histogram of number of guesses:[0, 1, 109, 1137, 1607, 343, 19, 0, 0, 0]
Histogram of number of guesses:[0, 0, 56, 1224, 1729, 200, 7, 0, 0, 0]
Histogram of number of guesses:[0, 0, 56, 929, 947, 84, 5, 0, 0, 0]
Histogram of number of guesses:[0, 1, 112, 885, 863, 155, 5, 0, 0, 0]
Histogram of number of guesses:[0, 1, 103, 696, 379, 16, 0, 0, 0, 0]
Histogram of number of guesses:[0, 0, 66, 812, 317, 0, 0, 0, 0, 0]
NEW SANER [0, 1, 100, 691, 527, 86, 2, 0, 0, 0]

References:
* https://sonorouschocolate.com/notes/index.php?title=The_best_strategies_for_Wordle
* https://www.youtube.com/watch?v=v68zYyaEmEA
* https://www.nytimes.com/interactive/2022/upshot/wordle-bot.html
