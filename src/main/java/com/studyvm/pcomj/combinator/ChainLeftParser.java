package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.studyvm.pcomj.utils.CommonUtil.foldl;
import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class ChainLeftParser<T1, T2> extends AbstractParserCombinator<T1> {
    final Parser<T1> fromParser;
    final Parser<List<T2>> listParser;
    final BiFunction<T1, T2, T1> reducer;

    public ChainLeftParser(Parser<T1> fromParser, Parser<List<T2>> listParser, BiFunction<T1, T2, T1> reducer) {
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
        T1 result = foldl(reducer, from.get().value(), list.get().value());
        return makeResult(result, list.get().input());
    }
}
