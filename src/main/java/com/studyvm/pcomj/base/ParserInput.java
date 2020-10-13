package com.studyvm.pcomj.base;

public class ParserInput {
    private final String source;
    private final Position position;

    public ParserInput(String src) {
        this.source = src;
        this.position = new Position();
    }

    public ParserInput(String source, Position position) {
        this.source = source;
        this.position = new Position(position);
    }

    public ParserInput rest() {
        return new ParserInput(source, position);
    }

    public boolean empty() {
        return position.index == source.length();
    }

    public int restLength() {
        return source.length() - position.index;
    }

    public boolean startsWith(String string) {
        if (restLength() < string.length()) {
            return false;
        }
        int len = string.length();
        for (int i = 0; i < len; i++) {
            if (source.charAt(position.index + i) != string.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public char current() {
        return source.charAt(position.index);
    }

    public void advance() {
        advance(1);
    }

    public void advance(int n) {
        position.index += n;
    }

    public void clear() {
        position.reset();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return equals((String) obj);
        }
        if (!(obj instanceof ParserInput)) {
            return false;
        }
        ParserInput temp = (ParserInput) obj;
        return source.equals(temp.source) && position.equals(temp.position);
    }

    public boolean equals(String rest) {
        return source.substring(position.index).equals(rest);
    }

    @Override
    public String toString() {
        return "ParserInput:\n" +
                "<source: " + source + ">." +
                "at (" + position.toString() + ")";
    }

    static class Position {
        int index;

        public Position() {
            index = 0;
        }

        public Position(Position position) {
            this.index = position.index;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Position) {
                return ((Position) obj).index == index;
            }
            return false;
        }

        public void reset() {
            index = 0;
        }

        @Override
        public String toString() {
            return String.format("index: %d.", index);
        }
    }
}
