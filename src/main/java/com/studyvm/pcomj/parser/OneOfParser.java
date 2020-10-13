package com.studyvm.pcomj.parser;

import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class OneOfParser extends AbstractCharacterParser {
    private final String set;

    public OneOfParser(String set) {
        this.set = set;
    }

    @Override
    public Optional<ParseResult<Character>> parse(ParserInput s) {
        if (!s.empty() && set.indexOf(s.current()) != -1) {
            char ch = s.current();
            s.advance();
            return makeResult(ch, s.rest());
        }
        return Optional.empty();
    }
}
