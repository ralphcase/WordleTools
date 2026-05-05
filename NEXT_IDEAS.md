# Next Ideas

A structured list of future improvements, organized by category.
Each idea includes: What it is, Why it matters, and Shape of done.

---------------------------------------------------------------------

## Dictionary and WordRepository

### Use different goalWords for different scenarios
What it is  
Support multiple goal sets: all words, past solutions, or "no repeats" mode.

Why it matters  
Keeps Solver logic clean and pushes scenario differences into the repository layer.

Shape of done  
- WordRepository exposes goalWordsFor(Scenario)
- Scenarios: base, archive, no-repeat
- Solver receives the correct list at construction time

---------------------------------------------------------------------

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

### Refactor Solver for better performance
What it is  
Stop recomputing candidate lists from scratch.

Why it matters  
Performance and clarity: constraints shrink the candidate set monotonically.

Shape of done  
- Solver stores List<Word> candidates
- Each constraint update filters the existing list
- No list of constraints; only the accumulated ConstraintSet

### Implement Hard Mode
What it is  
Guess list is filtered by the same constraints as the goal list.

Why it matters  
Matches Wordle Hard Mode semantics; simplifies solver behavior.

Shape of done  
- Maintain guessCandidates
- Filter using ConstraintSet
- Solver chooses from this list when Hard Mode is enabled

### Add an entry point for BestStarter
What it is  
A mode that runs Solver with no constraints to compute best first guesses.

Why it matters  
Supports analysis tools and starter word exploration.

Shape of done  
- CLI or method entry point
- Solver initialized with empty ConstraintSet
- Outputs top N scoring words

### Update Dictionaries
What it is
Read data copied from Wordlebot Analysis  

Why it matters
to update the possible goals to better match those in Wordle

Shape of done
- Parse the output of the Wordlebot 
- write out an updated goals dictionary 

---------------------------------------------------------------------

## Codebase Hygiene

### Clean up garbage files
What it is  
Remove unused legacy files from earlier refactors.

Why it matters  
Reduces noise, improves navigability, prevents confusion.

Shape of done  
- Identify unused classes
- Remove or archive
- Ensure tests still pass

### Mark the current state of the main branch
What it is  
Preserve the pre-refactor version in a clear, discoverable way.

Why it matters  
Future debugging or archaeology becomes easier.

Shape of done  
- Tag the commit (for example: pre-refactor-2026)
- Optionally create a legacy branch
- Document this in README

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
- Add tests for ConstraintSet behavior
- Add tests for Solver candidate shrinking
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
