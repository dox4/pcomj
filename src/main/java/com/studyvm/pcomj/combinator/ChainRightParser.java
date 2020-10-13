package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.studyvm.pcomj.utils.CommonUtil.foldr;
import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class ChainRightParser<T1, T2> extends AbstractParserCombinator<T1> {
    final BiFunction<T2, T1, T1> reducer;
    final private Parser<T1> fromParser;
    final private Parser<List<T2>> listParser;

    public ChainRightParser(Parser<T1> fromParser, Parser<List<T2>> listParser, BiFunction<T2, T1, T1> reducer) {
        this.fromParser = fromParser;
        this.listParser = listParser;
        this.reducer = reducer;
    }

    @Override
    public Optional<ParseResult<T1>> parse(ParserInput s) {
        Optional<ParseResult<T1>> from = fromParser.parse(s);
        if (!from.isPresent()) {
            return Optional.empty();
        }
        ParserInput rest = from.get().input();
        Optional<ParseResult<List<T2>>> list = listParser.parse(rest);
        if (!list.isPresent()) {
            return Optional.empty();
        }
        T1 result = foldr(reducer, from.get().value(), list.get().value());
        return makeResult(result, list.get().input());
    }
}
