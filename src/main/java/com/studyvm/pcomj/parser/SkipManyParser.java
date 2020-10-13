package com.studyvm.pcomj.parser;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.succeed;

public class SkipManyParser<T> extends AbstractParserCombinator<Void> {
    private final Parser<T> skipper;

    public SkipManyParser(Parser<T> skipper) {
        this.skipper = skipper;
    }


    @Override
    public Optional<ParseResult<Void>> parse(ParserInput s) {
        Optional<ParseResult<T>> r1 = skipper.parse(s);
        if (!r1.isPresent()) {
            return Optional.empty();
        }
        do {
            s = s.rest();
            Optional<ParseResult<T>> r2 = skipper.parse(s);
            if (!r2.isPresent()) {
                return succeed(s);
            }
        } while (true);
    }
}
