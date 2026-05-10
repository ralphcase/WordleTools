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
