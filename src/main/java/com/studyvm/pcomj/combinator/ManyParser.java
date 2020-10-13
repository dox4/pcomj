package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class ManyParser<R> extends AbstractParserCombinator<List<R>> {
    private final Parser<R> parser;

    public ManyParser(Parser<R> parser) {
        this.parser = parser;
    }

    @Override
    public Optional<ParseResult<List<R>>> parse(ParserInput s) {
        List<R> rl = new LinkedList<>();
        while (true) {
            Optional<ParseResult<R>> r = parser.parse(s);
            if (!r.isPresent()) {
                return makeResult(rl, s);
            }
            rl.add(r.get().value());
            s = r.get().input();
        }
    }
}
