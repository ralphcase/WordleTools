# WordleTools Refactor — Development Rules

These rules govern all work performed on the `refactor` branch.  
The existing `main` branch remains stable and unchanged until the new architecture is ready for production.

---

## 1. Branching Strategy
- All refactor work happens on a dedicated branch named `refactor`.
- `main` remains untouched and fully functional for daily use.
- The `refactor` branch is treated as the working mainline for the redesign.
- Merges into `main` occur only when the new architecture is complete, tested, and ready for production.

---

## 2. Small, Independent Changes
- Each commit must represent one conceptual improvement.
- No large, sweeping changes.
- No multi-purpose commits.
- If a change feels too large, break it into smaller steps.

---

## 3. Complete Unit Tests for Every Change
- Every commit must include tests that fully cover the new or modified behavior.
- Tests must include:
  - positive cases
  - negative cases
  - edge cases
  - repeated-letter scenarios
- No commit is allowed without tests.

---

## 4. The Project Must Always Build
- The codebase must compile successfully after every commit.
- No broken interfaces.
- No commented-out code left behind.
- No half-implemented classes.

---

## 5. “Always Runnable” Will Be Enforced Later
- During early architectural work, the solver may not be fully wired into the CLI.
- Buildability and testability take priority.
- Once the domain stabilizes, the rule “the project must always run end-to-end” will be added.

---

## 6. Commit Discipline
- Each commit must include:
  - the code change
  - the tests
  - comments explaining intent
- Commit messages follow this format:

Examples:
- `[ConstraintSet] Add green-letter constraints and tests`
- `[Solver] Introduce filterCandidates using ConstraintSet`
- `[Report] Add conversion to ConstraintSet delta`

---

## 7. Stable Interfaces Within a Commit
- Public method signatures must not change unless the commit is specifically about that change.
- This prevents cascading breakage and keeps commits conceptually pure.

---

## 8. Temporary Duplication Is Allowed
- When introducing new abstractions (e.g., ConstraintSet), the old logic may remain temporarily.
- This allows incremental migration and safe testing.
- Duplication must be removed once callers are migrated.

---

## 9. No Partially Implemented Classes
- If a class exists, it must be usable, testable, and internally consistent.
- Empty shells or “TODO: implement later” are not allowed in production code.

---

## 10. Test Organization
Use clear, discoverable test naming:

- `ConstraintSetTest` — unit tests for ConstraintSet
- `ConstraintSetIntegrationTest` — ConstraintSet + Solver
- `SolverFilteringTest` — filtering logic
- `SolverScenarioTest` — full game scenarios
- `ReportTest` — feedback logic
- `WordTest` — Position/Word behavior

---

## 11. Architectural Goal
The refactor aims to transition from a game-object model (Position, Report, Guess) to a constraint-driven model:

- `Word` (formerly Position)
- `Feedback` (formerly Report)
- `ConstraintSet` (new core abstraction)
- `Solver` (driven by ConstraintSet)

`Guess` becomes optional and moves to UI/CLI if needed.

---

## 12. Refactor Philosophy
- No “big bang” rewrites.
- No breaking the build.
- No skipping tests.
- Every step moves the architecture forward.
- Every commit is reversible.
- The history should read like a clear narrative of the system’s evolution.



