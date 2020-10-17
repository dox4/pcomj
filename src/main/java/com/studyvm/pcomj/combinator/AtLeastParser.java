package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class AtLeastParser<R> extends AbstractParserCombinator<List<R>> {
    private final Parser<R> parser;
    private final int atLeast;

    public AtLeastParser(Parser<R> parser, int atLeast) {
        this.parser = parser;
        // many parser == at least 0 parser
        assert atLeast > 0;
        this.atLeast = atLeast;
    }

    @Override
    public Optional<ParseResult<List<R>>> parse(ParserInput s) {
        int i = 0;
        List<R> rl = new LinkedList<>();
        while (i < atLeast) {
            Optional<ParseResult<R>> r = parser.parse(s);
            if (!r.isPresent()) {
                return Optional.empty();
            }
            rl.add(r.get().value());
            s = r.get().input();
            i++;
        }
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
