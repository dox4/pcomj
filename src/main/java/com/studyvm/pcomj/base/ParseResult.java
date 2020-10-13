package com.studyvm.pcomj.base;

public class ParseResult<R> {
    private final R value;
    private final ParserInput input;

    public ParseResult(R r, ParserInput input) {
        this.value = r;
        this.input = input;
    }

    public R value() {
        return value;
    }

    public ParserInput input() {
        return input;
    }
}
