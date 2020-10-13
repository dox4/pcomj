package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class OptionParser<T> extends AbstractParserCombinator<T> {
    private final Parser<T> parser;
    private final T defaultValue;

    public OptionParser(Parser<T> parser, T defaultValue) {
        this.parser = parser;
        this.defaultValue = defaultValue;
    }

    @Override
    public Optional<ParseResult<T>> parse(ParserInput s) {
        Optional<ParseResult<T>> optionResult = parser.parse(s);
        return optionResult.map(result ->
                makeResult(result.value(), result.input()))
                .orElseGet(() -> makeResult(defaultValue, s));
    }
}
