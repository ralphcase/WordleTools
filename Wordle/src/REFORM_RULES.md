
# WordleTools Development Guidelines

These rules govern all work on the `main` branch.  
The codebase uses a constraint-driven architecture with legacy components from the previous design coexisting during transition.

---

## 1. Small, Independent Changes
- Each commit must represent one conceptual improvement.
- No large, sweeping changes.
- No multi-purpose commits.
- If a change feels too large, break it into smaller steps.

---

## 2. Complete Unit Tests for Every Change
- Every commit must include tests that fully cover the new or modified behavior.
- Tests must include:
  - positive cases
  - negative cases
  - edge cases
  - repeated-letter scenarios
- No commit is allowed without tests.

---

## 3. The Project Must Always Build
- The codebase must compile successfully after every commit.
- No broken interfaces.
- No commented-out code left behind.
- No half-implemented classes.

---

## 4. The Project Must Always Run End-to-End
- The solver and CLI must be fully wired and runnable after every commit.
- All integration points must work.
- No orphaned code or incomplete refactoring on `main`.

---

## 5. Commit Discipline
- Each commit must include:
  - the code change
  - the tests
  - comments explaining intent
- Commit messages follow this format:

Examples:
- `[ConstraintSet] Add green-letter constraints and tests`
- `[Solver] Introduce filterCandidates using ConstraintSet`
- `[Feedback] Add conversion to ConstraintSet delta`

---

## 6. Stable Interfaces Within a Commit
- Public method signatures must not change unless the commit is specifically about that change.
- This prevents cascading breakage and keeps commits conceptually pure.

---

## 7. Legacy Code Coexistence
- Legacy classes from the old architecture (located in the default package: e.g., old `Solver`, `Guess`, `Position`, `Report`) may remain in the codebase if they provide functionality not yet re-implemented in the new constraint-driven architecture.
- **All new code must be in appropriately named packages** (e.g., `solver`, `constraints`, `feedback`, `dictionary`, `word`). The default package is reserved for legacy code only.
- Legacy code serves three purposes:
  - **Reference**: Understanding how something worked before.
  - **Fallback**: Providing optimized or complete functionality until the new architecture catches up (e.g., the old Solver was faster; BestStarter performance is still being developed).
  - **Gradual migration**: Allowing incremental transition without blocking new work.
- Legacy classes must be clearly marked as such (in Javadoc or naming convention).
- Legacy code must not be integrated into new components.
- Legacy code must not be expanded or enhanced—if functionality is needed, re-implement it in the new architecture.

---

## 8. No Partially Implemented Classes
- If a class exists in the new architecture, it must be usable, testable, and internally consistent.
- Empty shells or "TODO: implement later" are not allowed.
- (Legacy classes are exempt from this rule while they coexist.)

---

## 9. Test Organization
Use clear, discoverable test naming:

- `ConstraintSetTest` – unit tests for ConstraintSet
- `ConstraintSetIntegrationTest` – ConstraintSet + Solver
- `SolverFilteringTest` – filtering logic
- `SolverScenarioTest` – full game scenarios
- `FeedbackTest` – feedback logic
- `WordTest` – Word/Position behavior

---

## 10. Architecture Overview
The system uses a constraint-driven model:

- `Word` – represents a 5-letter word with position information
- `Feedback` – represents the color feedback from a guess (green/yellow/gray)
- `Constraint` – encodes restrictions derived from feedback
- `ConstraintSet` – aggregates multiple constraints
- `Solver` – filters candidates using constraints

Legacy classes (`Position`, `Report`, `Guess`) may coexist temporarily during migration in the default package.

---

## 11. Development Philosophy
- No "big bang" rewrites on `main`.
- No breaking the build.
- No skipping tests.
- Every step moves the architecture forward.
- Every commit is reversible.
- The history should read like a clear narrative of the system's evolution.
- Legacy functionality is preserved until intentionally replaced, not discarded.

---

## 12. Performance vs. Clarity
- The new architecture prioritizes **clarity, correctness, and testability** over raw performance.
- The old Solver is faster; the new Solver is more maintainable.
- Performance optimization is a future concern, after the architecture stabilizes.
- Benchmarking and optimization should be done deliberately, not ad-hoc.