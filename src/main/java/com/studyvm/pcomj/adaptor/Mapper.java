package com.studyvm.pcomj.adaptor;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;
import java.util.function.Function;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class Mapper<T, R> extends AbstractParserCombinator<R> {
    private final Parser<T> parser;
    private final Function<T, R> mapper;

    public Mapper(Parser<T> parser, Function<T, R> mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }

    @Override
    public Optional<ParseResult<R>> parse(ParserInput s) {
        Optional<ParseResult<T>> optionalResult = parser.parse(s);
        return optionalResult.flatMap(
                result ->
                        makeResult(mapper.apply(result.value()),
                                result.input())
        );
    }
}
