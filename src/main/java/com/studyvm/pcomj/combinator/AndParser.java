package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.adaptor.Mapper;
import com.studyvm.pcomj.base.*;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class AndParser<T, U> extends AbstractParserCombinator<Pair<T, U>> {
    private final Parser<T> a;
    private final Parser<U> b;

    public AndParser(Parser<T> a, Parser<U> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Optional<ParseResult<Pair<T, U>>> parse(ParserInput s) {
        Optional<ParseResult<T>> r1 = a.parse(s);
        if (r1.isPresent()) {
            Optional<ParseResult<U>> r2 = b.parse(r1.get().input());
            if (r2.isPresent()) {
                return makeResult(Pair.with(r1.get().value(), r2.get().value()),
                        r2.get().input());
            }
        }
        return Optional.empty();
    }

    public Mapper<Pair<T, U>, String> concat() {
        return convert(Pair::concat);
    }
}
