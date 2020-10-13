package com.studyvm.pcomj.parser;

import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.combinator.StringAccumulator;

public abstract class AbstractCharacterParser extends AbstractParserCombinator<Character> {
    public StringAccumulator accumulate() {
        return new StringAccumulator(this);
    }
}
