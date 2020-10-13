package com.studyvm.pcomj.utils;

import com.studyvm.pcomj.adaptor.Accumulator;
import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.combinator.SatisfyParser;
import com.studyvm.pcomj.combinator.StringAccumulator;
import com.studyvm.pcomj.combinator.TakeLeftParser;
import com.studyvm.pcomj.parser.CharParser;
import com.studyvm.pcomj.parser.OneOfParser;
import com.studyvm.pcomj.parser.SkipManyParser;
import com.studyvm.pcomj.parser.StringParser;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface ParserBuilder {
    static CharParser symbol(char ch) {
        return new CharParser(ch);
    }

    static StringParser keyword(String kw) {
        return new StringParser(kw);
    }

    static OneOfParser oneOf(String set) {
        return new OneOfParser(set);
    }

    static <T, R> Accumulator<T, R> accumulate(Parser<T> p, Function<T, R> c, BinaryOperator<R> r, R i) {
        return new Accumulator<>(p, c, r, i);
    }

    static <L, M, R> TakeLeftParser<M, R> pack(AbstractParserCombinator<L> l, Parser<M> m, Parser<R> r) {
        return l.takeRight(m).takeLeft(r);
    }

    static StringAccumulator accumulate(Parser<Character> p) {
        return new StringAccumulator(p);
    }

    static SatisfyParser space() {
        return new SatisfyParser(Character::isWhitespace);
    }

    static <T> SkipManyParser<T> skip(Parser<T> skipper) {
        return new SkipManyParser<T>(skipper);
    }
}
