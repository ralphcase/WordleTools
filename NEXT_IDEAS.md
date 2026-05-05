Ideas about where to go next with this project...

# Use different goalWords for different scenarios.
The WordRepository should make this easy.
- all possible words for the base case
- previous solutions for archive problems
- remove known solutions for the (old) wordle pattern of not reusing goals

# Refactor Solver to use ConstraintSet instead of Constraint

# Refactor Solver for better performance
- Keep a list of remaining candidates instead of regenerating it
- When applying a constraint, keep the updated list.
- apply every constrain right away; don't keep a list.

# Implement Hard Mode
- keep a list of candidate guess words, filtered by the same constraints that filter the goal list.

# Add an entry point for BestStarter
- just Solver with no constraints

# clean up garbage files
Some files are left over from prior work and no longer used. Remove them.

# Mark the current state of the main branch
When refactor is merged into main, the old code will be gone from the current branch. It's still in github. Figure out the best way to mark it in case we need to go back.

# Add JavaDoc?

# Examine tests to see what's missing and what could be done better

# Add a UI to input guesses and feedback more easily

 