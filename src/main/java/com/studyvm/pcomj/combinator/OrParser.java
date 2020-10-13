package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

public class OrParser<T> extends AbstractParserCombinator<T> {
    private final Parser<T> a, b;

    public OrParser(Parser<T> a, Parser<T> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Optional<ParseResult<T>> parse(ParserInput s) {
        Optional<ParseResult<T>> r1 = a.parse(s);
        if (r1.isPresent()) {
            return r1;
        }
        return b.parse(s);
    }
}
