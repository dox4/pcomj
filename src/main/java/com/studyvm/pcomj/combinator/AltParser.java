package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.List;
import java.util.Optional;

public class AltParser<T> extends AbstractParserCombinator<T> {
    private final List<Parser<T>> alts;

    public AltParser(List<Parser<T>> alts) {
        this.alts = alts;
    }

    @Override
    public Optional<ParseResult<T>> parse(ParserInput s) {
        for (Parser<T> p : alts) {
            Optional<ParseResult<T>> r = p.parse(s);
            if (r.isPresent()) {
                return r;
            }
        }
        return Optional.empty();
    }
}
