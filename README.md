## IdeaVim Internship Task
The task involves creating your own engine for working with regular expressions as described by the specification below. The engine will be focused around the IDE and the work within it. In the repository, you can find the `Editor` and `Caret` interfaces which symbolize the editor and the caret, respectively. It’s worth noting that with regular expressions in the task, the patterns aren't just searching for raw text, but rather taking into account the position of the caret in the IDE editor. Something similar happens with Vim regular expressions.

To complete this task, clone this repository and write your own implementation of the method inside `RegexpServiceImpl`.

**We ask you not to create forks or public repositories. All submissions from public repositories will be ignored.**

### Specification:
- You have to use a backslash (`\`) as an escape character before the characters `\`, `?`, `*`, `+`, `(`, and `)`. For instance, use `\\` when you want a backslash, and `\?` for a question mark, etc.
- All other characters are read literally.
- There's a `\c` character that specifies the caret position. This means that the `w\cord` pattern looks for instances of 'word' where the caret is positioned between `w` and `o`.
- Similar to the majority of existing patterns, any content in brackets is a group. A group supports the quantifiers `?`, `*`, and `+`, where:
  - `?` denotes 0 or 1 repetition.
  - `*` denotes 0 or more repetitions.
  - `+` denotes 1 or more repetitions.
  - Quantifiers can only be placed after the closing bracket of a group.
- A group must have content. It cannot be empty.
- Regular expressions should work using only ASCII characters. Other characters are not allowed.
- If the pattern does not meet specifications, for example, if an escape character is used incorrectly or if the pattern contains an empty group or non-ASCII characters, then the `InvalidPatternException` should be thrown.

#### Examples
The `matchAll(editor: Editor, pattern: String): Set<Pair<Int, Int>>` method inside `RegexpService` takes an editor and pattern and returns all index pairs within the text that correspond to the substring matching the pattern.
Both offsets of the range are included.

| Input text | Caret position | Pattern | Expected result |
|--|--|--|--|
| `Hello world!` | Doesn't matter | `word` | `[[6, 11]]` |
| `Hello world!` | Doesn't matter | `Hello( word\!)?` | `[[0, 5], [0, 12]]` |
| `Ho-ho-ho`| Doesn't matter | `Ho(-ho)*`| `[[0, 2], [0, 5], [0, 8]]` |
| `word word word` | Caret with `offset == 3` | `\cword` | `[]` |
| `word word word` | Caret with `offset == 5` | `\cword` | `[[5, 9]]` |
| `word word word` | Caret with `offset == 0`, caret with `offset == 5` | `\cword` | `[[0, 4], [5, 9]]` |

You can find more examples in the tests (`Tests` class).

### Testing
The `Tests` class contains tests for the minimal verification of your solution.
Tests in `Tests` are exclusively for self-verification; we will test the solution according to our own, larger set of tests.

### Submitting a Solution
Your solution does not necessarily have to pass all tests before it is submitted. Even if your solution only passes most of the tests, and not all of them, don't hesitate to submit it. Should your solution be interesting, we’d still like to see it.
The solution can be shared in the following ways:
- Private repository on GitHub. Please give access to the repository for `https://github.com/AlexPl292` and `https://github.com/lippfi`
- Archive to a ZIP file and send it to `maintainers@ideavim.dev`.

**Once again, we ask you not to create forks or public repositories. All submissions from public repositories will be ignored.**

### Evaluation
There are no specifications for the evaluation, which is based on more than just the number of tests passed.
Solutions are rated higher if they do not use libraries for working with regular expressions.
Java and Kotlin solutions are evaluated equally.
The effectiveness and speed of the solution are not being evaluated. We'll have an internship for finding a good algorithm.
For the amount of time allotted to the solution, we recommend that you focus on passing all tests.
Additional tasks are not mandatory, but completing them will have a positive impact on your evaluation.
We encourage you to write clean and understandable code with explanatory comments where necessary.

### Additional tasks
1. Do you have any ideas about how you might solve this problem in a different way? What makes these ideas better or worse than your solution?
2. Do you have any ideas about optimizing your current solution?
3. What are your thoughts about optimizations, not necessarily those of your own solution, if you know that they will work within the IDE and not when running a straightforward search within a file? For example, since this is an IDE, you can obtain information about which part of the text is currently open to the user. What other information would you be able to use to optimize your search?
