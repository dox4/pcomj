package com.studyvm.pcomj.parser;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.succeed;

public class SkipParser<T> extends AbstractParserCombinator<Void> {
    private final Parser<T> parser;

    public SkipParser(Parser<T> parser) {
        this.parser = parser;
    }

    @Override
    public Optional<ParseResult<Void>> parse(ParserInput s) {
        Optional<ParseResult<T>> r1 = parser.parse(s);
        if (r1.isPresent()) {
            return succeed(s.rest());
        }
        return Optional.empty();
    }
}
