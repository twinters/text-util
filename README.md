# text-util
This repository contains several text processing utilities in Java that I tend to use a lot in several other projects.
It is made public mainly due to it being a dependency in other open-sourced software of mine.
I might add some more documentation for this repository when it grows larger.

Currently, it contains several diverse tools, such as:
* `IReplacer`: A class that is able to replace text in other texts. It can handle several given constraints (such as allowing parts of words to be replaced, keeping the casing of the replaced word etc).
* `WordCounter`: A class for counting how many times a word occurs, with normalising capabilities and the possibility for statistical operations.
* `SentenceUtil`: Several static methods for dealing with sentences, punctuations, capitalisation etc.
* Various `IFixer`s and String checkers, that allow to map text to other texts or check their validity under certain conditions.
* `DataLoader`: To easily load texts from files using ClassLoader.
* `DutchDateUtil`: To easily convert date objects into Dutch text.
