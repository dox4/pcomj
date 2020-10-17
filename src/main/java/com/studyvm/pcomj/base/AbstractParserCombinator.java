package com.studyvm.pcomj.base;

import com.studyvm.pcomj.adaptor.Mapper;
import com.studyvm.pcomj.combinator.*;
import com.studyvm.pcomj.parser.SkipManyParser;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.studyvm.pcomj.utils.ParserBuilder.skip;

public abstract class AbstractParserCombinator<T> implements Parser<T> {
    public <U> AndParser<T, U> and(Parser<U> b) {
        return new AndParser<>(this, b);
    }

    public OrParser<T> or(Parser<T> b) {
        return new OrParser<>(this, b);
    }

    public ManyParser<T> many() {
        return new ManyParser<>(this);
    }

    public <R> TakeLeftParser<T, R> takeLeft(Parser<R> right) {
        return new TakeLeftParser<>(this, right);
    }

    public <R> TakeRightParser<T, R> takeRight(Parser<R> right) {
        return new TakeRightParser<>(this, right);
    }

    public <R> Mapper<T, R> map(Function<T, R> convertor) {
        return new Mapper<>(this, convertor);
    }

    public OptionParser<T> option(T defaultValue) {
        return new OptionParser<>(this, defaultValue);
    }

    public SkipManyParser<T> ignore() {
        return skip(this);
    }

    public <U> ChainLeftParser<T, U> chainLeft(Parser<List<U>> listParser, BiFunction<T, U, T> reducer) {
        return new ChainLeftParser<>(this, listParser, reducer);
    }

    public <U> ChainRightParser<T, U> chainRight(Parser<List<U>> listParser, BiFunction<U, T, T> reducer) {
        return new ChainRightParser<>(this, listParser, reducer);
    }

    public AtLeastParser<T> atLeast(int n) {
        return new AtLeastParser<>(this, n);
    }

    public AtLeastParser<T> some() {
        return atLeast(1);
    }
}
