package com.studyvm.pcomj.base;

import java.util.Objects;

public class Pair<T, U> {
    T first;
    U second;

    private Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public static <T, U> Pair<T, U> with(T t, U u) {
        return new Pair<>(t, u);
    }

    public T first() {
        return first;
    }

    public U second() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(this.first, other.first)
                && Objects.equals(this.second, other.second);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }

    public String concat() {
        return first().toString() + second().toString();
    }
}
