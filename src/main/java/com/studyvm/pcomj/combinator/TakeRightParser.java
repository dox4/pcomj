package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class TakeRightParser<L, R> extends AbstractParserCombinator<R> {
    private final Parser<L> lParser;
    private final Parser<R> rParser;

    public TakeRightParser(Parser<L> lParser, Parser<R> rParser) {
        this.lParser = lParser;
        this.rParser = rParser;
    }

    @Override
    public Optional<ParseResult<R>> parse(ParserInput s) {
        Optional<ParseResult<L>> rl = lParser.parse(s);
        if (rl.isPresent()) {
            ParserInput remains = rl.get().input();
            Optional<ParseResult<R>> rr = rParser.parse(remains);
            if (rr.isPresent()) {
                return makeResult(rr.get().value(), rr.get().input());
            }
        }
        return Optional.empty();
    }
}
