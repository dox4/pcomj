package com.studyvm.pcomj.utils;

import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class CommonUtil {
    public static <R> Optional<ParseResult<R>> makeResult(R value, ParserInput s) {
        return Optional.of(new ParseResult<>(value, s));
    }

    public static Optional<ParseResult<Void>> succeed(ParserInput s) {
        return Optional.of(new ParseResult<>(null, s));
    }

    public static <T1, T2> T1 foldl(BiFunction<T1, T2, T1> reducer, T1 from, @NotNull List<T2> list) {
        return foldl(reducer, from, list, 0);
    }

    private static <T1, T2> T1 foldl(BiFunction<T1, T2, T1> reducer, T1 from, @NotNull List<T2> list, int index) {
        if (index == list.size()) {
            return from;
        }
        return foldl(reducer, reducer.apply(from, list.get(index)), list, index + 1);
    }

    public static <T1, T2> T1 foldr(BiFunction<T2, T1, T1> reducer, T1 from, @NotNull List<T2> list) {
        return foldr(reducer, from, list, list.size() - 1);
    }

    private static <T1, T2> T1 foldr(BiFunction<T2, T1, T1> reducer, T1 from, @NotNull List<T2> list, int index) {
        if (index == -1) {
            return from;
        }
        return foldr(reducer, reducer.apply(list.get(index), from), list, index - 1);
    }
}
