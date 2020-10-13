package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class TakeLeftParser<L, R> extends AbstractParserCombinator<L> {
    private final Parser<L> lParser;
    private final Parser<R> rParser;

    public TakeLeftParser(Parser<L> lParser, Parser<R> rParser) {
        this.lParser = lParser;
        this.rParser = rParser;
    }

    @Override
    public Optional<ParseResult<L>> parse(ParserInput s) {
        Optional<ParseResult<L>> lr = lParser.parse(s);
        if (lr.isPresent()) {
            Optional<ParseResult<R>> rr = rParser.parse(lr.get().input());
            if (rr.isPresent()) {
                return makeResult(lr.get().value(), rr.get().input());
            }
        }
        return Optional.empty();
    }
}
