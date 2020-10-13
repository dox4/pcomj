package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import com.studyvm.pcomj.base.ParserInput;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class StringAccumulator extends AbstractParserCombinator<String> {

    private final Parser<Character> parser;

    public StringAccumulator(Parser<Character> parser) {
        this.parser = parser;
    }

    @Override
    public Optional<ParseResult<String>> parse(ParserInput s) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            Optional<ParseResult<Character>> r = parser.parse(s);
            if (!r.isPresent()) {
                return sb.length() == 0 ?
                        Optional.empty()
                        : makeResult(sb.toString(), s);
            }
            sb.append(r.get().value());
            s = r.get().input();
        }
    }
}
