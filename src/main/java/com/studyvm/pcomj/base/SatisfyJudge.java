package com.studyvm.pcomj.base;

@FunctionalInterface
public interface SatisfyJudge<T> {
    boolean judge(T t);
}
