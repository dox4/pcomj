# Change Logs

## version 0.0.2
- add `AltParser` for simplify and optimize `a.or(b).or(c)...`
- rename the member function `convert` to `map`, which will get the `Mapper<T>`
- add `AtLeastParser` for parsing rule like `a+`, `ManyParser` can be replaced with `atLeast(0)`
  but `ManyParser` has existed, so `AtLeast` only accepts a number beyond 0.

## version 0.0.1
the first workable version, and is the first committed version.

this project includes some basic parser combinators.

the type of simple parser is like:
```
type Parser r :: String -> (r, String)
```

which accepts a string and returns the parsing result, which made up with two
parts: a target value, the rest string.

you can see `Parser char`(`CharParser`), `Parser String`(`StringParser`) as the
very simple parsers.

a parser combinator is a parser made up with another parser(s).

in the first edition, this project provides some useful combinators like below:
- `AndParser :: Parser a -> Parser b -> Parser (a, b)`
- `OrParser :: Parser a -> Parser a -> Parser a`
- `Mapper :: Parser a -> (a -> b) -> Parser b`
- `ManyParser :: Parser a -> Parser [a]`
...

for simple usage, `class AbstractParserCombinator` provides various member
functions which can easily combine a parser with another.

for example, we have a `Parser a` named `a` and a `Parser b` named `b`, we can
get the parser `Parser (a, b)` with `new AndParser(a, b)` or `a.and(b)`.

the more complex the parsing rule is, the more advantage the second code style will get.
