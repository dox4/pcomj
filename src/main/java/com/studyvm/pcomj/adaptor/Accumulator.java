package com.studyvm.pcomj.adaptor;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class Accumulator<T, R> extends AbstractParserCombinator<R> {
    private final Parser<T> parser;
    private final Function<T, R> mapper;
    private final BinaryOperator<R> reducer;
    private final R identity;

    public Accumulator(Parser<T> parser, Function<T, R> mapper, BinaryOperator<R> reducer, R identity) {
        this.parser = parser;
        this.mapper = mapper;
        this.reducer = reducer;
        this.identity = identity;
    }

    @Override
    public Optional<ParseResult<R>> parse(ParserInput s) {
        R r = identity;
        while (true) {
            Optional<ParseResult<T>> rp = parser.parse(s);
            if (!rp.isPresent()) {
                return makeResult(r, s);
            }
            R temp = mapper.apply(rp.get().value());
            r = reducer.apply(r, temp);
            s = rp.get().input();
        }
    }
}
