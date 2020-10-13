package com.studyvm.pcomj.base;

import java.util.Optional;

public interface Parser<T> {
    Optional<ParseResult<T>> parse(ParserInput s);
}
