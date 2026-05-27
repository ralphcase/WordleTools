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
- Add a priorityQueue to BestStarter to return the top N candidates instead of just the best one. This would allow users to see multiple good starting words and choose based on their preferences.
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

References:
* https://sonorouschocolate.com/notes/index.php?title=The_best_strategies_for_Wordle
* https://www.youtube.com/watch?v=v68zYyaEmEA
* https://www.nytimes.com/interactive/2022/upshot/wordle-bot.html
