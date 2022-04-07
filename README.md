# CSc22100 Assignment 5

In this assignment, we'll explore a Java implementation of [Peter Norvig's Toy Spell Checker](https://norvig.com/spell-correct.html).

## Instructions

1) Run `./gradlew download` to download the data sets. They'll download to a directory called `./data`.
2) Implement the `prepare` method in the `EvalSet` class.
3) In `WordFrequencyCounter.java` implement `countFrequencies`.
4) Write some tests for (2) and (3) (*Hint*: Life will be easier if you make a new test class rather than writing the tests in `SpellCheckerTest`)
5) When you're satisfied  with your implementation, run `./gradlew prepareTask`
6) Next, implement `train` in `LanguageModel` to compute the probability of each token based on the word frequencies. (See Norvig's explanation or [Wikipedia](https://en.wikipedia.org/wiki/Language_model))
7) Write some tests for `LanguageModel` to convince yourself you've done it correctly. (Hint: Use one or two sentences as your 'corpus' and you can compute the expected probabilities by hand easily.)
8) Run `./gradlew train` to train the language model
9) Implement each of the methods in `SpellChecker`.
10) There are some unit tests to check that `SpellChecker` is working correctly. Unfortunately, because they rely on the path to the dataset, you may need to set a system property in your IDE, or run them with gradle (`./gradlew testAll`).
11) Run `./gradlew evaluate` to see how your spell checker does. You can have extra points if you outperform my implementation in either accuracy (on the "hidden" test set--it's on Norvig's site) or in words per second.

### My Results

```
java -jar build/libs/assignment5-all.jar grade --data-dir ./data --working-dir ./data 
[main] INFO csc22100.spelling.SpellCheckerMain - Preprocessing corpus...
[main] INFO csc22100.spelling.SpellCheckerMain - Preprocessing dev examples...
Correct: 200
Incorrect: 55
Accuracy: 0.7843137254901961
Words per second: 127
```

## Grading

| Item                                                                                     | Points |
|------------------------------------------------------------------------------------------|--------|
| Code compiles                                                                            | 1      |
| Tests pass                                                                               | 1      |
| Code cleanliness/readability (are your language model tests in `LanguageModelTest` etc?) | 2      |
| At least two test cases for `WordFrequencyCounter`                                       | 2      |
| At least two test cases for `LanguageModel`                                              | 2      |
| Correctly packaged                                                                       | 1      |


Extra Points:
- 2 for outperforming me on the test set. (Be creative--can you improve on this fairly simple approach?)
- 2 for having an implementation that runs faster than ~85 words per second on my laptop

*Tip* If you're going to work on the extra points, I suggest completing the assignment first, saving a copy (or making a git commit) and 
then start trying to optimize your use of `StringBuilder` or whatever.

## What to submit

As ever, submit two things: a zip file (run `./gradlew packageAssignment`) and a PDF of your code (remember to put `source2pdf.jar` in 
the same directory as `gradlew` and then run `./gradlew source2pdf`)

## Other notes

Right now the `build` gradle task depends on the `testAll` task, which requires that you have trained the language model.
If you want to run `build` *without* running those tests, remember you can skip them with `./gradlew build -xtestAll`.

The project is set up to use the `shadow` plugin, meaning that it builds a runnable jar (complete with all the depedencies packaged together).
If you'd rather run individual commands, you can run the jar itself:
```shell
java -jar build/libs/assignment5-all.jar        
Missing required subcommand
Usage: spell [-hV] [COMMAND]
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  download
  evaluate
  grade
  prepare
  train
```