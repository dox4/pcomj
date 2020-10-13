package com.studyvm.pcomj.parser;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class StringParser extends AbstractParserCombinator<String> {
    private final String string;

    public StringParser(String string) {
        this.string = string;
    }

    @Override
    public Optional<ParseResult<String>> parse(ParserInput s) {
        if (s.startsWith(string)) {
            s.advance(string.length());
            return makeResult(string, s.rest());
        }
        return Optional.empty();
    }
}
