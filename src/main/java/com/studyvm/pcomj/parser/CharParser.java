package com.studyvm.pcomj.parser;

import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class CharParser extends AbstractCharacterParser {
    private final char ch;

    public CharParser(char ch) {
        this.ch = ch;
    }

    public Optional<ParseResult<Character>> parse(ParserInput s) {
        if (!s.empty() && s.current() == ch) {
            s.advance();
            return makeResult(ch, s.rest());
        }
        return Optional.empty();
    }

}
