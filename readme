Pair: Eric Lee and Diana Ding

Extended Usage of EncodingHelper:

"Infer the input type by default. (That is, instead of assuming the input is a 
string, infer from its contents whether to treat it as a string, a sequence of 
codepoints, or a sequence of bytes.)"

The program detects if the data (that is, the argument that is neither -i/-o nor
the input/output) contains indications of an input type of codepoint, utf8, or
string. If it does, it would create the appropriate EncodingHelperChars based on
the inputs it infers. For example:

java EncodingHelper U+0063

will return because it defaults to summary:

--------------------------------
String: c
Code points: U+0063 
Name: LATIN SMALL LETTER C
UTF-8: \x63
--------------------------------

java EncodingHelper -o string U+0063

will return because output is specified to be string:

--------------------------------
c
--------------------------------

This even works for utf8 too. It will detect if the data that's being inputed
is utf8 hex pairs. For example:

java EncodingHelper '\xC3\xAA'

will return because it defaults to summary:

--------------------------------
String: ê
Code points: U+00EA 
Name: LATIN SMALL LETTER E WITH CIRCUMFLEX
UTF-8: \xC3\xAA
--------------------------------

java EncodingHelper -o codepoint '\xC3\xAA'

will return because output is specified to be codepoint:

--------------------------------
U+00EA
--------------------------------