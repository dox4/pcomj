package com.studyvm.pcomj.combinator;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;
import com.studyvm.pcomj.base.SatisfyJudge;

import java.util.Optional;

import static com.studyvm.pcomj.utils.CommonUtil.makeResult;

public class SatisfyParser extends AbstractParserCombinator<Character> {
    private final SatisfyJudge<Character> judge;

    public SatisfyParser(SatisfyJudge<Character> judge) {
        this.judge = judge;
    }

    @Override
    public Optional<ParseResult<Character>> parse(ParserInput s) {
        char ch;
        if (!s.empty() && judge.judge(ch = s.current())) {
            s.advance();
            return makeResult(ch, s.rest());
        }
        return Optional.empty();
    }
}
