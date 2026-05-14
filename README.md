# WordleTools
 
 Some classes to help in analyzing and solving Wordle (https://www.nytimes.com/games/wordle/index.html).

 ## Dictionaries
 The code can use several dictionaries of words.
 
 `allowed_words.txt`
 : a list of all words that the wordle game will allow to be played.

 `goals.txt`
 : a list of all the words that Wordle might use as a solution. This list can be updated by seeing possible solution words reported by wordlebot and pasting them into `wordlebot.txt`.

 `solutions.txt`
 : a list of words that have been used as solutions. This gets updated daily.

 `archive_solutions.txt`
 : the contents of `solutions.txt` when this project started. Any games played as archive mode should have solutions in this list. 

 ## Pre‑Refactor Code

There was a major refactor in May 2026 that changed almost everything.
The state of the codebase before the 2026 refactor is tagged as `pre-refactor-2026`.
 
